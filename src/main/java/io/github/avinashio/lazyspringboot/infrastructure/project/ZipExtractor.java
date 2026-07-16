package io.github.avinashio.lazyspringboot.infrastructure.project;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import org.springframework.stereotype.Component;

@Component
public class ZipExtractor {

    public void extract(
            InputStream inputStream,
            Path destination)
            throws IOException {

        try (ZipInputStream zipInputStream =
                     new ZipInputStream(inputStream)) {

            ZipEntry entry;

            while ((entry = zipInputStream.getNextEntry())
                    != null) {

                Path target =
                        destination.resolve(
                                entry.getName());

                if (entry.isDirectory()) {
                    Files.createDirectories(
                            target);
                    continue;
                }

                Files.createDirectories(
                        target.getParent());

                Files.copy(
                        zipInputStream,
                        target);
            }
        }
    }
}