package cn.kiway.kiway_ai.entity;

import java.util.ArrayList;
import java.util.List;

import lombok.Data;

@Data
public class RobotAndModule {
    private Robot robot;
    private List<RobotModule> robotModules;
}
