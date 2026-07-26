package io.github.avinashio.lazyspringboot.ui.service;

import io.github.avinashio.lazyspringboot.domain.project.SpringProject;

import java.awt.*;
import java.awt.datatransfer.StringSelection;
import java.io.IOException;
import org.springframework.stereotype.Service;

@Service
public class DesktopIntegrationService {

    public void copyProjectPath(
            SpringProject project) {

        copyText(
                project.path()
                        .toString());
    }

    public void copyText(
            String text) {

        Toolkit.getDefaultToolkit()
                .getSystemClipboard()
                .setContents(
                        new StringSelection(text),
                        null);
    }

    public boolean openFolder(
            SpringProject project) {

        try {

            Desktop.getDesktop()
                    .open(
                            project.path()
                                    .toFile());

            return true;

        } catch (Exception exception) {

            return false;
        }
    }
}