package io.github.avinashio.lazyspringboot.ui.service;

import java.awt.Desktop;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import org.springframework.stereotype.Service;

@Service
public class DesktopIntegrationService {

    public boolean copyToClipboard(
            String text) {

        String os =
                System.getProperty("os.name")
                        .toLowerCase();

        try {

            Process process;

            if (os.contains("mac")) {

                process =
                        new ProcessBuilder(
                                "pbcopy")
                                .start();

            } else if (os.contains("win")) {

                process =
                        new ProcessBuilder(
                                "cmd",
                                "/c",
                                "clip")
                                .start();

            } else {

                process =
                        new ProcessBuilder(
                                "xclip",
                                "-selection",
                                "clipboard")
                                .start();
            }

            try (OutputStreamWriter writer =
                         new OutputStreamWriter(
                                 process.getOutputStream(),
                                 StandardCharsets.UTF_8)) {

                writer.write(text);
            }

            return process.waitFor() == 0;

        } catch (IOException
                 | InterruptedException exception) {

            Thread.currentThread()
                    .interrupt();

            return false;
        }
    }

    public boolean openFolder(
            Path path) {

        String os =
                System.getProperty("os.name")
                        .toLowerCase();

        try {

            Process process;

            if (os.contains("mac")) {

                process =
                        new ProcessBuilder(
                                "open",
                                path.toString())
                                .start();

            } else if (os.contains("win")) {

                process =
                        new ProcessBuilder(
                                "explorer",
                                path.toString())
                                .start();

            } else {

                process =
                        new ProcessBuilder(
                                "xdg-open",
                                path.toString())
                                .start();
            }

            return process.waitFor() == 0;

        } catch (IOException
                 | InterruptedException exception) {

            if (exception instanceof InterruptedException) {
                Thread.currentThread().interrupt();
            }

            return false;
        }
    }

    public boolean openIntelliJ(
            Path path) {

        String os =
                System.getProperty("os.name")
                        .toLowerCase();

        try {

            Process process;

            if (os.contains("mac")) {

                process =
                        new ProcessBuilder(
                                "open",
                                "-a",
                                "IntelliJ IDEA",
                                path.toString())
                                .start();

            } else if (os.contains("win")) {

                process =
                        new ProcessBuilder(
                                "idea64.exe",
                                path.toString())
                                .start();

            } else {

                process =
                        new ProcessBuilder(
                                "idea",
                                path.toString())
                                .start();
            }

            return process.waitFor() == 0;

        } catch (IOException
                 | InterruptedException exception) {

            if (exception instanceof InterruptedException) {
                Thread.currentThread().interrupt();
            }

            return false;
        }
    }

    public boolean openVSCode(
            Path path) {

        String os =
                System.getProperty("os.name")
                        .toLowerCase();

        try {

            Process process;

            if (os.contains("mac")) {

                process =
                        new ProcessBuilder(
                                "open",
                                "-a",
                                "Visual Studio Code",
                                path.toString())
                                .start();

            } else if (os.contains("win")) {

                process =
                        new ProcessBuilder(
                                "code",
                                path.toString())
                                .start();

            } else {

                process =
                        new ProcessBuilder(
                                "code",
                                path.toString())
                                .start();
            }

            return process.waitFor() == 0;

        } catch (IOException
                 | InterruptedException exception) {

            if (exception instanceof InterruptedException) {
                Thread.currentThread().interrupt();
            }

            return false;
        }
    }
}