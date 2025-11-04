package com.qulron.qulron_workflow_engine.actions;

import com.qulron.qulron_workflow_engine.engine.Action;
import com.qulron.qulron_workflow_engine.engine.ExecutionContext;
import org.springframework.stereotype.Component;

@Component("CompareAction")
public class CompareAction implements Action {

    @Override
    public boolean execute(ExecutionContext context) {
        Object left = context.getVar("left");
        Object right = context.getVar("right");
        return left != null && left.equals(right);
    }
}
