package com.example.softarch.assignment.agent;

import java.util.List;

public enum AddIteration {
    OVERALL_SYSTEM_STRUCTURE(1, "Establishing an Overall System Structure", true),
    PRIMARY_FUNCTIONALITY(2, "Identifying Structures to Support Primary Functionality", false),
    RELIABILITY_AND_AVAILABILITY(3, "Addressing Reliability and Availability Quality Attributes", false),
    DEVELOPMENT_AND_OPERATIONS(4, "Addressing Development and Operations", false);

    private final int number;
    private final String goal;
    private final boolean includesStepOne;

    AddIteration(int number, String goal, boolean includesStepOne) {
        this.number = number;
        this.goal = goal;
        this.includesStepOne = includesStepOne;
    }

    public int number() {
        return number;
    }

    public String goal() {
        return goal;
    }

    public boolean includesStepOne() {
        return includesStepOne;
    }

    public List<String> requiredStepLabels() {
        if (includesStepOne) {
            return List.of("ADD Step 1", "ADD Step 2", "ADD Step 3", "ADD Step 4", "ADD Step 5", "ADD Step 6", "ADD Step 7");
        }
        return List.of("ADD Step 2", "ADD Step 3", "ADD Step 4", "ADD Step 5", "ADD Step 6", "ADD Step 7");
    }
}
