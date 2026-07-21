package io.github.avinashio.lazyspringboot.ui.controller;

import io.github.avinashio.lazyspringboot.domain.action.ProjectActionOutput;
import io.github.avinashio.lazyspringboot.ui.state.TextInputPurpose;
import io.github.avinashio.lazyspringboot.ui.state.UiState;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import org.springframework.stereotype.Controller;

@Controller
public class LogSearchController {

    private final UiState uiState;

    private final TextInputController
            textInputController;

    private String appliedQuery = "";

    private int selectedMatchIndex = -1;

    public LogSearchController(
            UiState uiState,
            TextInputController textInputController) {

        this.uiState =
                uiState;

        this.textInputController =
                textInputController;
    }

    public void start() {

        selectedMatchIndex = -1;

        textInputController.start(
                TextInputPurpose.LOG_SEARCH);

        for (char character :
                appliedQuery.toCharArray()) {

            textInputController.append(
                    character);
        }
    }

    public void stopInput() {

        if (textInputController.active(
                TextInputPurpose.LOG_SEARCH)) {

            appliedQuery =
                    textInputController.value();

            textInputController.stop();
        }

        selectFirstMatch();
    }

    public void clear() {

        appliedQuery = "";

        selectedMatchIndex = -1;

        if (textInputController.active(
                TextInputPurpose.LOG_SEARCH)) {

            textInputController.stop();
        }
    }

    public boolean active() {

        return textInputController.active(
                TextInputPurpose.LOG_SEARCH);
    }

    public boolean hasQuery() {

        return !effectiveQuery()
                .isBlank();
    }

    public String query() {

        return effectiveQuery();
    }

    public void append(
            char character) {

        textInputController.append(
                character);

        selectFirstMatch();
    }

    public void backspace() {

        textInputController.backspace();

        selectFirstMatch();
    }

    public void apply() {

        if (active()) {

            appliedQuery =
                    textInputController.value();

            textInputController.stop();
        }

        selectFirstMatch();
    }

    public void next() {

        List<Integer> matches =
                matches();

        if (matches.isEmpty()) {

            selectedMatchIndex = -1;

            return;
        }

        if (selectedMatchIndex < 0) {

            selectedMatchIndex = 0;

        } else {

            selectedMatchIndex =
                    (selectedMatchIndex + 1)
                            % matches.size();
        }

        moveToSelectedMatch(
                matches);
    }

    public void previous() {

        List<Integer> matches =
                matches();

        if (matches.isEmpty()) {

            selectedMatchIndex = -1;

            return;
        }

        if (selectedMatchIndex < 0) {

            selectedMatchIndex =
                    matches.size() - 1;

        } else {

            selectedMatchIndex =
                    (selectedMatchIndex
                            - 1
                            + matches.size())
                            % matches.size();
        }

        moveToSelectedMatch(
                matches);
    }

    public int matchCount() {

        return matches().size();
    }

    public int selectedMatchNumber() {

        List<Integer> matches =
                matches();

        if (selectedMatchIndex < 0
                || selectedMatchIndex
                >= matches.size()) {

            return 0;
        }

        return selectedMatchIndex + 1;
    }

    public int selectedLineIndex() {

        List<Integer> matches =
                matches();

        if (selectedMatchIndex < 0
                || selectedMatchIndex
                >= matches.size()) {

            return -1;
        }

        return matches.get(
                selectedMatchIndex);
    }

    private void selectFirstMatch() {

        List<Integer> matches =
                matches();

        if (matches.isEmpty()) {

            selectedMatchIndex = -1;

            return;
        }

        selectedMatchIndex = 0;

        moveToSelectedMatch(
                matches);
    }

    private List<Integer> matches() {

        ProjectActionOutput output =
                uiState.projectActionOutput();

        String query =
                effectiveQuery();

        if (output == null
                || query.isBlank()) {

            return List.of();
        }

        String normalizedQuery =
                query.toLowerCase(
                        Locale.ROOT);

        List<Integer> matches =
                new ArrayList<>();

        for (int index = 0;
             index < output.lines().size();
             index++) {

            String line =
                    output.lines()
                            .get(index);

            if (line.toLowerCase(
                            Locale.ROOT)
                    .contains(
                            normalizedQuery)) {

                matches.add(
                        index);
            }
        }

        return matches;
    }

    private String effectiveQuery() {

        if (active()) {

            return textInputController.value();
        }

        return appliedQuery;
    }

    private void moveToSelectedMatch(
            List<Integer> matches) {

        if (selectedMatchIndex < 0
                || selectedMatchIndex
                >= matches.size()) {

            return;
        }

        uiState.outputViewport()
                .moveTo(
                        matches.get(
                                selectedMatchIndex));
    }
}