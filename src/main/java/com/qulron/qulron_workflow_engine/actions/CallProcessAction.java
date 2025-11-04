package com.qulron.qulron_workflow_engine.actions;

import com.qulron.qulron_workflow_engine.engine.Action;
import com.qulron.qulron_workflow_engine.engine.ExecutionContext;
import com.qulron.qulron_workflow_engine.entity.AppProcessObject;
import com.qulron.qulron_workflow_engine.repository.ProcessObjectRepo;
import com.qulron.qulron_workflow_engine.service.ProcessEngine;
import org.springframework.stereotype.Component;

@Component("CallProcessAction")
public class CallProcessAction implements Action {

    private final ProcessObjectRepo processObjectRepo;
    private final ProcessEngine processEngine;

    public CallProcessAction(ProcessObjectRepo processObjectRepo, ProcessEngine processEngine) {
        this.processObjectRepo = processObjectRepo;
        this.processEngine = processEngine;
    }

    @Override
    public boolean execute(ExecutionContext context) {
        String targetProcessName = (String) context.getVar("targetProcessName");

        if (targetProcessName == null) {
            throw new RuntimeException("No target process specified for CallProcessAction");
        }

        AppProcessObject targetProcess = processObjectRepo.findByName(targetProcessName)
                .orElseThrow(() -> new RuntimeException("Target process not found: " + targetProcessName));

        if (context.isInStack(targetProcess)) {
            throw new RuntimeException("Detected recursive loop: " + targetProcess.getName());
        }

        return processEngine.executeProcess(targetProcess, context);
    }
}