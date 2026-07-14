package io.github.avinashio.lazyspringboot.infrastructure.maven;

import io.github.avinashio.lazyspringboot.domain.dependency.DependencyCoordinate;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.AtomicMoveNotSupportedException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.List;
import org.springframework.stereotype.Component;
import java.io.ByteArrayInputStream;

@Component
public class MavenPomDependencyWriter {

    private static final String DEPENDENCIES_CLOSE =
            "</dependencies>";

    private static final String PROJECT_CLOSE =
            "</project>";

    private final MavenDependencyParser dependencyParser;

    public MavenPomDependencyWriter(
            MavenDependencyParser dependencyParser) {
        this.dependencyParser =
                dependencyParser;
    }

    public void addDependencies(
            Path pomPath,
            List<DependencyCoordinate> dependencies)
            throws IOException {
        if (dependencies.isEmpty()) {
            return;
        }

        String original =
                Files.readString(
                        pomPath,
                        StandardCharsets.UTF_8);

        validatePom(original);

        String updated = original;

        for (DependencyCoordinate dependency : dependencies) {
            if (containsDependency(
                    updated,
                    dependency)) {
                continue;
            }

            updated =
                    insertDependency(
                            updated,
                            dependency);
        }

        if (original.equals(updated)) {
            return;
        }

        writeSafely(
                pomPath,
                original,
                updated);
    }

    private boolean containsDependency(
            String pom,
            DependencyCoordinate dependency)
            throws IOException {
        try (ByteArrayInputStream inputStream =
                     new ByteArrayInputStream(
                             pom.getBytes(
                                     StandardCharsets.UTF_8))) {
            return dependencyParser
                    .parse(inputStream)
                    .contains(dependency);
        }
    }

    private String insertDependency(
            String pom,
            DependencyCoordinate dependency)
            throws IOException {
        String dependencyXml =
                dependencyXml(dependency);

        int dependenciesCloseIndex =
                pom.indexOf(DEPENDENCIES_CLOSE);

        if (dependenciesCloseIndex >= 0) {
            return pom.substring(
                    0,
                    dependenciesCloseIndex)
                    + dependencyXml
                    + pom.substring(
                    dependenciesCloseIndex);
        }

        int projectCloseIndex =
                pom.lastIndexOf(PROJECT_CLOSE);

        if (projectCloseIndex < 0) {
            throw new IOException(
                    "Invalid Maven POM: missing </project>");
        }

        String dependenciesXml =
                "\n\t<dependencies>\n"
                        + dependencyXml
                        + "\t</dependencies>\n";

        return pom.substring(
                0,
                projectCloseIndex)
                + dependenciesXml
                + pom.substring(
                projectCloseIndex);
    }

    private String dependencyXml(
            DependencyCoordinate dependency) {
        return "\t\t<dependency>\n"
                + "\t\t\t<groupId>"
                + dependency.groupId()
                + "</groupId>\n"
                + "\t\t\t<artifactId>"
                + dependency.artifactId()
                + "</artifactId>\n"
                + "\t\t</dependency>\n";
    }

    private void writeSafely(
            Path pomPath,
            String original,
            String updated)
            throws IOException {
        Path temporaryPath =
                pomPath.resolveSibling(
                        pomPath.getFileName()
                                + ".lazyspringboot.tmp");

        Path backupPath =
                pomPath.resolveSibling(
                        pomPath.getFileName()
                                + ".lazyspringboot.bak");

        try {
            Files.writeString(
                    temporaryPath,
                    updated,
                    StandardCharsets.UTF_8);

            Files.writeString(
                    backupPath,
                    original,
                    StandardCharsets.UTF_8);

            replacePom(
                    temporaryPath,
                    pomPath);
        } finally {
            Files.deleteIfExists(
                    temporaryPath);
        }
    }

    private void replacePom(
            Path temporaryPath,
            Path pomPath)
            throws IOException {
        try {
            Files.move(
                    temporaryPath,
                    pomPath,
                    StandardCopyOption.ATOMIC_MOVE,
                    StandardCopyOption.REPLACE_EXISTING);
        } catch (
                AtomicMoveNotSupportedException exception) {
            Files.move(
                    temporaryPath,
                    pomPath,
                    StandardCopyOption.REPLACE_EXISTING);
        }
    }

    private void validatePom(String pom)
            throws IOException {
        if (!pom.contains(PROJECT_CLOSE)) {
            throw new IOException(
                    "Invalid Maven POM: missing </project>");
        }
    }
}