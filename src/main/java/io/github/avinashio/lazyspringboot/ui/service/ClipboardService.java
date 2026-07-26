package io.github.avinashio.lazyspringboot.ui.service;

import java.awt.Toolkit;
import java.awt.datatransfer.StringSelection;
import org.springframework.stereotype.Service;

@Service
public class ClipboardService {

    public void copy(
            String text) {

        Toolkit.getDefaultToolkit()
                .getSystemClipboard()
                .setContents(
                        new StringSelection(
                                text),
                        null);
    }
}