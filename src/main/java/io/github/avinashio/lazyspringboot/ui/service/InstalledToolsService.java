package io.github.avinashio.lazyspringboot.ui.service;

import java.io.IOException;
import org.springframework.stereotype.Service;

@Service
public class InstalledToolsService {

    private boolean hasIntelliJ;

    private boolean hasVSCode;

    public void refresh() {

        hasIntelliJ =
                isInstalled(
                        "IntelliJ IDEA");

        hasVSCode =
                isInstalled(
                        "Visual Studio Code");
    }

    public boolean hasIntelliJ() {

        return hasIntelliJ;
    }

    public boolean hasVSCode() {

        return hasVSCode;
    }

    private boolean isInstalled(
            String applicationName) {

        String os =
                System.getProperty("os.name")
                        .toLowerCase();

        try {

            Process process;

            if (os.contains("mac")) {

                process =
                        new ProcessBuilder(
                                "open",
                                "-Ra",
                                applicationName)
                                .start();

            } else if (os.contains("win")) {

                return true;

            } else {

                return true;
            }

            return process.waitFor() == 0;

        } catch (IOException
                 | InterruptedException exception) {

            if (exception
                    instanceof InterruptedException) {

                Thread.currentThread()
                        .interrupt();
            }

            return false;
        }
    }
}