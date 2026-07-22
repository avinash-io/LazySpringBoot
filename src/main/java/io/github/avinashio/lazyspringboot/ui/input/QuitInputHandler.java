package io.github.avinashio.lazyspringboot.ui.input;

import io.github.avinashio.lazyspringboot.ui.controller.QuitController;
import io.github.avinashio.lazyspringboot.ui.controller.QuitDecision;
import io.github.avinashio.lazyspringboot.ui.state.QuitFocus;
import org.springframework.stereotype.Component;

@Component
public class QuitInputHandler {

    private final QuitController quitController;

    public QuitInputHandler(
            QuitController quitController) {

        this.quitController =
                quitController;
    }

    public QuitDecision handle(
            KeyEvent keyEvent) {

        switch (keyEvent.type()) {

            case UP ->
                    quitController.focusPrevious();

            case DOWN ->
                    quitController.focusNext();

            case HOME ->
                    quitController.focusFirst();

            case END ->
                    quitController.focusLast();

            case TAB -> {

                if (quitController.state().readyToQuitPhase()) {

                    break;
                }

                if (quitController.state().focus()
                        == QuitFocus.PROJECTS) {

                    quitController.state()
                            .focusActions();

                } else {

                    quitController.state()
                            .focusProjects();
                }
            }

            case SPACE -> {

                if (quitController.state().runningProjectsPhase()
                        && quitController.state().focus()
                        == QuitFocus.PROJECTS) {

                    quitController.state()
                            .toggleSelectedProject();
                }
            }

            case CHARACTER -> {

                if (quitController.state().readyToQuitPhase()
                        || quitController.state().focus()
                        != QuitFocus.PROJECTS) {

                    break;
                }

                char key =
                        Character.toLowerCase(
                                keyEvent.character());

                switch (key) {

                    case 'a' ->
                            quitController.state()
                                    .selectAllProjects();

                    case 'n' ->
                            quitController.state()
                                    .clearProjectSelection();

                    default -> {
                    }
                }
            }

            case ENTER -> {

                return quitController.executeSelection();
            }

            case ESCAPE ->

                    quitController.cancel();

            default -> {
            }
        }

        return QuitDecision.CONTINUE;
    }
}