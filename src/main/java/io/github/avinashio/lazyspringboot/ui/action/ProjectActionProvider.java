package io.github.avinashio.lazyspringboot.ui.action;

import io.github.avinashio.lazyspringboot.ui.service.InstalledToolsService;
import java.util.ArrayList;
import java.util.List;
import org.springframework.stereotype.Component;

@Component
public class ProjectActionProvider {

    private final InstalledToolsService
            installedToolsService;

    public ProjectActionProvider(
            InstalledToolsService
                    installedToolsService) {

        this.installedToolsService =
                installedToolsService;
    }

    public List<ProjectAction> projectActions() {

        List<ProjectAction> actions =
                new ArrayList<>();

        actions.add(
                new ProjectAction(
                        ProjectCommand.OPEN_FOLDER,
                        true));

        actions.add(
                new ProjectAction(
                        ProjectCommand.COPY_PATH,
                        true));

        actions.add(
                new ProjectAction(
                        ProjectCommand.OPEN_INTELLIJ,
                        installedToolsService
                                .hasIntelliJ()));

        actions.add(
                new ProjectAction(
                        ProjectCommand.OPEN_VS_CODE,
                        installedToolsService
                                .hasVSCode()));

        return actions;
    }
}