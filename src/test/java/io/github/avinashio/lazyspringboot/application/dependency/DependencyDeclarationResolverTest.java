package io.github.avinashio.lazyspringboot.application.dependency;

import io.github.avinashio.lazyspringboot.domain.dependency.DependencyCoordinate;
import io.github.avinashio.lazyspringboot.domain.dependency.DependencyDeclaration;
import io.github.avinashio.lazyspringboot.domain.dependency.DependencyScope;
import io.github.avinashio.lazyspringboot.domain.dependency.SpringDependency;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

class DependencyDeclarationResolverTest {


private final DependencyDeclarationProvider provider =
		dependency ->
				Optional.of(
						switch (dependency.id()) {
							
							case "postgresql" -> List.of(
									new DependencyDeclaration(
											new DependencyCoordinate(
													"org.postgresql",
													"postgresql"),
											DependencyScope.RUNTIME));
							
							case "lombok" -> List.of(
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
							
							default -> List.of(
									new DependencyDeclaration(
											new DependencyCoordinate(
													"org.springframework.boot",
													"spring-boot-starter-"
															+ dependency.id()),
											DependencyScope.COMPILE));
						});

private final DependencyDeclarationResolver resolver =
		new DependencyDeclarationResolver(
				new DependencyCoordinateResolver(),
				provider);

@Test
void shouldUseCompileScopeByDefault() {
	
	List<DependencyDeclaration> declarations =
			resolver.resolve(
					dependency(
							"data-jpa"));
	
	assertThat(declarations)
			.hasSize(1);
	
	assertThat(
			declarations.getFirst()
					.scope())
			.isEqualTo(
					DependencyScope.COMPILE);
}

@Test
void shouldUseRuntimeScopeForPostgresql() {
	
	List<DependencyDeclaration> declarations =
			resolver.resolve(
					dependency(
							"postgresql"));
	
	assertThat(declarations)
			.hasSize(1);
	
	assertThat(
			declarations.getFirst()
					.scope())
			.isEqualTo(
					DependencyScope.RUNTIME);
	
	assertThat(
			declarations.getFirst()
					.coordinate()
					.artifactId())
			.isEqualTo(
					"postgresql");
}

@Test
void shouldCreateLombokCompileOnlyAndAnnotationProcessorDeclarations() {
	
	List<DependencyDeclaration> declarations =
			resolver.resolve(
					dependency(
							"lombok"));
	
	assertThat(declarations)
			.extracting(
					DependencyDeclaration::scope)
			.containsExactly(
					DependencyScope.COMPILE_ONLY,
					DependencyScope.ANNOTATION_PROCESSOR);
	
	assertThat(declarations)
			.allSatisfy(
					declaration ->
							assertThat(
									declaration.coordinate()
											.artifactId())
									.isEqualTo(
											"lombok"));
}

private SpringDependency dependency(
		String id) {
	
	return new SpringDependency(
			id,
			id,
			"",
			"");
}

@Test
void shouldFallbackToCompileWhenProviderCannotResolve() {
	
	DependencyDeclarationProvider provider =
			dependency ->
					Optional.empty();
	
	DependencyDeclarationResolver resolver =
			new DependencyDeclarationResolver(
					new DependencyCoordinateResolver(),
					provider);
	
	List<DependencyDeclaration> declarations =
			resolver.resolve(
					dependency(
							"data-jpa"));
	
	assertThat(declarations)
			.hasSize(1);
	
	assertThat(
			declarations.getFirst()
					.scope())
			.isEqualTo(
					DependencyScope.COMPILE);
}

private DependencyDeclarationResolver declarationResolver() {
	
	DependencyDeclarationProvider provider =
			dependency ->
					Optional.of(
							switch (dependency.id()) {
								
								case "postgresql" -> List.of(
										new DependencyDeclaration(
												new DependencyCoordinate(
														"org.postgresql",
														"postgresql"),
												DependencyScope.RUNTIME));
								
								case "lombok" -> List.of(
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
								
								default -> List.of(
										new DependencyDeclaration(
												new DependencyCoordinate(
														"org.springframework.boot",
														"spring-boot-starter-"
																+ dependency.id()),
												DependencyScope.COMPILE));
							});
	
	return new DependencyDeclarationResolver(
			new DependencyCoordinateResolver(),
			provider);
}
}