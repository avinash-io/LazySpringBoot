package io.github.avinashio.lazyspringboot.infrastructure.initializr;

import io.github.avinashio.lazyspringboot.domain.dependency.DependencyCoordinate;
import io.github.avinashio.lazyspringboot.domain.dependency.DependencyDeclaration;
import io.github.avinashio.lazyspringboot.domain.dependency.DependencyScope;
import io.github.avinashio.lazyspringboot.domain.dependency.SpringDependency;
import io.github.avinashio.lazyspringboot.infrastructure.gradle.GradleDependencyParser;
import org.junit.jupiter.api.Test;

import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLParameters;
import javax.net.ssl.SSLSession;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.Authenticator;
import java.net.CookieHandler;
import java.net.ProxySelector;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpHeaders;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;
import java.util.function.BiPredicate;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import static org.assertj.core.api.Assertions.assertThat;

class InitializrDependencyDeclarationResolverTest {

@Test
void shouldResolveOnlyDeclarationsForRequestedDependency()
		throws Exception {
	
	byte[] responseBody =
			zip(
					"demo/build.gradle",
					"""
							dependencies {
								implementation 'org.springframework.boot:spring-boot-starter-data-jpa'
								runtimeOnly 'org.postgresql:postgresql'
							}
							""");
	
	InitializrDependencyDeclarationResolver resolver =
			resolver(
					200,
					responseBody);
	
	Optional<List<DependencyDeclaration>> result =
			resolver.resolve(
					dependency(
							"postgresql"));
	
	assertThat(result)
			.isPresent();
	
	assertThat(result.orElseThrow())
			.containsExactly(
					new DependencyDeclaration(
							new DependencyCoordinate(
									"org.postgresql",
									"postgresql"),
							DependencyScope.RUNTIME));
}

@Test
void shouldResolveLombokDeclarations()
		throws Exception {
	
	byte[] responseBody =
			zip(
					"demo/build.gradle",
					"""
							dependencies {
								compileOnly 'org.projectlombok:lombok'
								annotationProcessor 'org.projectlombok:lombok'
							}
							""");
	
	InitializrDependencyDeclarationResolver resolver =
			resolver(
					200,
					responseBody);
	
	Optional<List<DependencyDeclaration>> result =
			resolver.resolve(
					dependency(
							"lombok"));
	
	assertThat(result)
			.isPresent();
	
	assertThat(result.orElseThrow())
			.containsExactly(
					new DependencyDeclaration(
							new DependencyCoordinate(
									"org.projectlombok",
									"lombok"),
							DependencyScope.COMPILE_ONLY),
					new DependencyDeclaration(
							new DependencyCoordinate(
									"org.projectlombok",
									"lombok"),
							DependencyScope.ANNOTATION_PROCESSOR));
}

@Test
void shouldReturnEmptyWhenInitializrReturnsNonSuccessStatus() {
	
	InitializrDependencyDeclarationResolver resolver =
			resolver(
					500,
					new byte[0]);
	
	assertThat(
			resolver.resolve(
					dependency(
							"postgresql")))
			.isEmpty();
}

@Test
void shouldReturnEmptyWhenZipDoesNotContainBuildGradle()
		throws Exception {
	
	byte[] responseBody =
			zip(
					"demo/settings.gradle",
					"rootProject.name = 'demo'");
	
	InitializrDependencyDeclarationResolver resolver =
			resolver(
					200,
					responseBody);
	
	assertThat(
			resolver.resolve(
					dependency(
							"postgresql")))
			.isEmpty();
}

@Test
void shouldReturnEmptyWhenBuildGradleContainsNoDependencies()
		throws Exception {
	
	byte[] responseBody =
			zip(
					"demo/build.gradle",
					"""
							plugins {
								id 'java'
							}
							""");
	
	InitializrDependencyDeclarationResolver resolver =
			resolver(
					200,
					responseBody);
	
	assertThat(
			resolver.resolve(
					dependency(
							"postgresql")))
			.isEmpty();
}

@Test
void shouldReturnEmptyWhenHttpRequestFails() {
	
	InitializrDependencyDeclarationResolver resolver =
			new InitializrDependencyDeclarationResolver(
					new FailingHttpClient(),
					new GradleDependencyParser());
	
	assertThat(
			resolver.resolve(
					dependency(
							"postgresql")))
			.isEmpty();
}

@Test
void shouldRequestGradleProjectForDependency() {
	
	RecordingHttpClient httpClient =
			new RecordingHttpClient(
					500,
					new byte[0]);
	
	InitializrDependencyDeclarationResolver resolver =
			new InitializrDependencyDeclarationResolver(
					httpClient,
					new GradleDependencyParser());
	
	resolver.resolve(
			dependency(
					"data-jpa"));
	
	assertThat(
			httpClient.request()
					.uri()
					.toString())
			.contains(
					"type=gradle-project")
			.contains(
					"language=java")
			.contains(
					"dependencies=data-jpa");
	
	assertThat(
			httpClient.request()
					.headers()
					.firstValue(
							"User-Agent"))
			.contains(
					"LazySpringBoot");
}

private InitializrDependencyDeclarationResolver resolver(
		int statusCode,
		byte[] body) {
	
	return new InitializrDependencyDeclarationResolver(
			new RecordingHttpClient(
					statusCode,
					body),
			new GradleDependencyParser());
}

private SpringDependency dependency(
		String id) {
	
	return new SpringDependency(
			id,
			id,
			"Test dependency",
			"Test");
}

private byte[] zip(
		String entryName,
		String content)
		throws IOException {
	
	ByteArrayOutputStream output =
			new ByteArrayOutputStream();
	
	try (ZipOutputStream zipOutput =
				 new ZipOutputStream(
						 output)) {
		
		zipOutput.putNextEntry(
				new ZipEntry(
						entryName));
		
		zipOutput.write(
				content.getBytes(
						StandardCharsets.UTF_8));
		
		zipOutput.closeEntry();
	}
	
	return output.toByteArray();
}

private static class RecordingHttpClient
		extends HttpClient {
	
	private final int statusCode;
	
	private final byte[] body;
	
	private HttpRequest request;
	
	private RecordingHttpClient(
			int statusCode,
			byte[] body) {
		
		this.statusCode =
				statusCode;
		
		this.body =
				body;
	}
	
	private HttpRequest request() {
		
		return request;
	}
	
	@Override
	public <T> HttpResponse<T> send(
			HttpRequest request,
			HttpResponse.BodyHandler<T> responseBodyHandler)
			throws IOException,
						   InterruptedException {
		
		this.request =
				request;
		
		@SuppressWarnings("unchecked")
		HttpResponse<T> response =
				(HttpResponse<T>)
						new ByteArrayHttpResponse(
								request,
								statusCode,
								body);
		
		return response;
	}
	
	@Override
	public <T> CompletableFuture<HttpResponse<T>> sendAsync(
			HttpRequest request,
			HttpResponse.BodyHandler<T> responseBodyHandler) {
		
		throw new UnsupportedOperationException();
	}
	
	@Override
	public <T> CompletableFuture<HttpResponse<T>> sendAsync(
			HttpRequest request,
			HttpResponse.BodyHandler<T> responseBodyHandler,
			HttpResponse.PushPromiseHandler<T> pushPromiseHandler) {
		
		throw new UnsupportedOperationException();
	}
	
	@Override
	public Optional<CookieHandler> cookieHandler() {
		
		return Optional.empty();
	}
	
	@Override
	public Optional<Duration> connectTimeout() {
		
		return Optional.empty();
	}
	
	@Override
	public Redirect followRedirects() {
		
		return Redirect.NEVER;
	}
	
	@Override
	public Optional<ProxySelector> proxy() {
		
		return Optional.empty();
	}
	
	@Override
	public SSLContext sslContext() {
		
		return null;
	}
	
	@Override
	public SSLParameters sslParameters() {
		
		return new SSLParameters();
	}
	
	@Override
	public Optional<Authenticator> authenticator() {
		
		return Optional.empty();
	}
	
	@Override
	public Version version() {
		
		return Version.HTTP_2;
	}
	
	@Override
	public Optional<Executor> executor() {
		
		return Optional.empty();
	}
}

private static final class FailingHttpClient
		extends RecordingHttpClient {
	
	private FailingHttpClient() {
		
		super(
				500,
				new byte[0]);
	}
	
	@Override
	public <T> HttpResponse<T> send(
			HttpRequest request,
			HttpResponse.BodyHandler<T> responseBodyHandler)
			throws IOException {
		
		throw new IOException(
				"Connection failed");
	}
}

private record ByteArrayHttpResponse(
		HttpRequest request,
		int statusCode,
		byte[] responseBody)
		implements HttpResponse<byte[]> {
	
	@Override
	public byte[] body() {
		
		return responseBody;
	}
	
	@Override
	public Optional<HttpResponse<byte[]>>
	previousResponse() {
		
		return Optional.empty();
	}
	
	@Override
	public HttpHeaders headers() {
		
		return HttpHeaders.of(
				java.util.Map.of(),
				new BiPredicate<>() {
					
					@Override
					public boolean test(
							String name,
							String value) {
						
						return true;
					}
				});
	}
	
	@Override
	public Optional<SSLSession> sslSession() {
		
		return Optional.empty();
	}
	
	@Override
	public URI uri() {
		
		return request.uri();
	}
	
	@Override
	public HttpClient.Version version() {
		
		return HttpClient.Version.HTTP_2;
	}
}
}