package io.github.avinashio.lazyspringboot.application.action;

import io.github.avinashio.lazyspringboot.domain.action.ActionItem;
import io.github.avinashio.lazyspringboot.domain.action.ProjectAction;
import io.github.avinashio.lazyspringboot.domain.process.ProjectProcess;
import java.util.List;
import java.util.Optional;
import org.springframework.stereotype.Component;

@Component
public class ProjectActionCatalog {

    public List<ActionItem> actions(
            Optional<ProjectProcess> projectProcess) {
        if (projectProcess
                .map(ProjectProcess::running)
                .orElse(false)) {
            return runningProjectActions();
        }

        return stoppedProjectActions();
    }

    private List<ActionItem> stoppedProjectActions() {
        return List.of(
                new ActionItem(
                        ProjectAction.BUILD,
                        true),
                new ActionItem(
                        ProjectAction.TEST,
                        true),
                new ActionItem(
                        ProjectAction.RUN,
                        true));
    }

    private List<ActionItem> runningProjectActions() {
        return List.of(
                new ActionItem(
                        ProjectAction.BUILD,
                        true),
                new ActionItem(
                        ProjectAction.TEST,
                        true),
                new ActionItem(
                        ProjectAction.VIEW_LOGS,
                        true),
                new ActionItem(
                        ProjectAction.RESTART,
                        true),
                new ActionItem(
                        ProjectAction.STOP,
                        true));
    }
}