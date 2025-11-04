package com.qulron.qulron_workflow_engine.actions;

import com.qulron.qulron_workflow_engine.engine.Action;
import com.qulron.qulron_workflow_engine.engine.ExecutionContext;
import org.springframework.stereotype.Component;

@Component("CalculateAction")
public class CalculateAction implements Action {
    @Override
    public boolean execute(ExecutionContext context) {
        return false;
    }
}
