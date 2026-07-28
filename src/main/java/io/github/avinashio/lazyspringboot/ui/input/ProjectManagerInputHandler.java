package io.github.avinashio.lazyspringboot.ui.input;

import io.github.avinashio.lazyspringboot.ui.controller.ProjectDetailsController;
import io.github.avinashio.lazyspringboot.ui.controller.ProjectManagerController;
import org.springframework.stereotype.Component;

@Component
public class ProjectManagerInputHandler {

    private final ProjectManagerController
            projectManagerController;

    private final ProjectDetailsController
            projectDetailsController;

    public ProjectManagerInputHandler(
            ProjectManagerController
                    projectManagerController,
            ProjectDetailsController
                    projectDetailsController) {

        this.projectManagerController =
                projectManagerController;

        this.projectDetailsController =
                projectDetailsController;
    }

    public boolean handle(
            KeyEvent keyEvent) {

        if (!projectManagerController.isOpen()) {
            return false;
        }

        switch (keyEvent.type()) {

            case CHARACTER -> {

                if (!keyEvent.hasCharacter()) {
                    return false;
                }

                switch (Character.toLowerCase(
                        keyEvent.character())) {

                    case 'i' -> {

                        projectDetailsController
                                .openIntelliJ();

                        return true;
                    }

                    case 'o' -> {

                        projectDetailsController
                                .openProjectFolder();

                        return true;
                    }

                    case 'c' -> {

                        projectDetailsController
                                .copyProjectPath();

                        return true;
                    }

                    default -> {
                        return false;
                    }
                }
            }

            case ENTER -> {

                projectManagerController.close();

                projectDetailsController.open();

                return true;
            }

            case ESCAPE -> {

                projectManagerController.close();

                return true;
            }

            default -> {
                return false;
            }
        }
    }
}