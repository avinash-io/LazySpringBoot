package io.github.avinashio.lazyspringboot.ui.input;

import io.github.avinashio.lazyspringboot.ui.controller.ProjectDetailsController;
import io.github.avinashio.lazyspringboot.ui.controller.ProjectManagerController;
import org.springframework.stereotype.Component;

@Component
public class ProjectDetailsInputHandler {

    private final ProjectDetailsController
            projectDetailsController;

    private final ProjectManagerController
            projectManagerController;

    public ProjectDetailsInputHandler(
            ProjectDetailsController
                    projectDetailsController,
            ProjectManagerController projectManagerController) {

        this.projectDetailsController =
                projectDetailsController;
        this.projectManagerController = projectManagerController;
    }

    public boolean handle(
            KeyEvent keyEvent) {

        if (!projectDetailsController.isOpen()) {
            return false;
        }

        switch (keyEvent.type()) {

            case CHARACTER -> {

                if (!keyEvent.hasCharacter()) {
                    return false;
                }

                switch (Character.toLowerCase(
                        keyEvent.character())) {

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

                    case 'i' -> {

                        projectDetailsController
                                .openIntelliJ();

                        return true;
                    }

                    default -> {
                        return false;
                    }
                }
            }

            case ESCAPE -> {

                projectDetailsController.close();

                projectManagerController.open();

                return true;
            }

            default -> {
                return false;
            }
        }
    }
}