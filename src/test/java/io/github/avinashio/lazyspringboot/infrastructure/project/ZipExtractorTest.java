package io.github.avinashio.lazyspringboot.infrastructure.project;

import static org.assertj.core.api.Assertions.assertThat;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

class ZipExtractorTest {

    @TempDir
    Path temporaryDirectory;

    @Test
    void shouldExtractZip()
            throws Exception {

        ByteArrayOutputStream output =
                new ByteArrayOutputStream();

        try (ZipOutputStream zip =
                     new ZipOutputStream(output)) {

            zip.putNextEntry(
                    new ZipEntry(
                            "demo/test.txt"));

            zip.write(
                    "hello".getBytes());

            zip.closeEntry();
        }

        ZipExtractor extractor =
                new ZipExtractor();

        extractor.extract(
                new ByteArrayInputStream(
                        output.toByteArray()),
                temporaryDirectory);

        assertThat(
                Files.exists(
                        temporaryDirectory.resolve(
                                "demo/test.txt")))
                .isTrue();

        assertThat(
                Files.readString(
                        temporaryDirectory.resolve(
                                "demo/test.txt")))
                .isEqualTo("hello");
    }
}