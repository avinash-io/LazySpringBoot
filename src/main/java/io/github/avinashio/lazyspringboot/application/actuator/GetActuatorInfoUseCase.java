package io.github.avinashio.lazyspringboot.application.actuator;

import io.github.avinashio.lazyspringboot.domain.actuator.ActuatorInfo;
import io.github.avinashio.lazyspringboot.domain.project.SpringProject;
import java.util.Optional;

public interface GetActuatorInfoUseCase {

    Optional<ActuatorInfo> get(
            SpringProject project);
}