package com.qulron.qulron_workflow_engine.enums;


public enum ActionType {
    EMPTY(-1), // Empty Line
    RETURN(0), // Return (PASS/FAIL)
    CALCULATE(1), // Calculate
    CALL(2), // Call Process Object
    COMPARE(3), // Compare
    DATABASE(4); // Database Action




    private final int action;

    public int getAction() {
        return action;
    }

    ActionType(int action){
        this.action = action;
    }

}

