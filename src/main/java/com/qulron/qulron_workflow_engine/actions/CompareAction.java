package com.qulron.qulron_workflow_engine.actions;

import com.qulron.qulron_workflow_engine.engine.Action;
import com.qulron.qulron_workflow_engine.engine.ExecutionContext;
import org.springframework.stereotype.Component;

@Component("compare")
public class CompareAction implements Action {

    @Override
    public boolean execute(ExecutionContext context) {
        Object left = context.get("left");
        Object right = context.get("right");
        return left != null && left.equals(right);
    }
}
