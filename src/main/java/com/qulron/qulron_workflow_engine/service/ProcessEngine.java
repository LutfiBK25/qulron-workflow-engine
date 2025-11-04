package com.qulron.qulron_workflow_engine.service;

import com.qulron.qulron_workflow_engine.engine.Action;
import com.qulron.qulron_workflow_engine.engine.ExecutionContext;
import com.qulron.qulron_workflow_engine.entity.AppProcessObject;
import com.qulron.qulron_workflow_engine.entity.AppProcessObjectDetail;
import com.qulron.qulron_workflow_engine.repository.ProcessObjectRepo;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
public class ProcessEngine {

    private final ApplicationContext appContext;
    private final ProcessObjectRepo processObjectRepo;

    public ProcessEngine(ApplicationContext appContext, ProcessObjectRepo processObjectRepo) {
        this.appContext = appContext;
        this.processObjectRepo = processObjectRepo;
    }

    // Entry point by process name
    public boolean executeProcessByName(String processName) {
        AppProcessObject processObject = processObjectRepo.findByName(processName)
                .orElseThrow(() -> new RuntimeException("Process not found: " + processName));
        return executeProcess(processObject, new ExecutionContext());
    }

    // Core recursive engine
    public boolean executeProcess(AppProcessObject processObject, ExecutionContext context) {
        if (context.isInStack(processObject)) {
            throw new RuntimeException("Detected recursive loop: " + processObject.getName());
        }

        context.pushProcess(processObject);

        Map<String, AppProcessObjectDetail> labelMap = processObject.getSteps()
                .stream()
                .collect(Collectors.toMap(AppProcessObjectDetail::getLabel, Function.identity()));

        List<AppProcessObjectDetail> sortedSteps = processObject.getSteps()
                .stream()
                .sorted(Comparator.comparingInt(AppProcessObjectDetail::getSequence))
                .toList();

        AppProcessObjectDetail current = sortedSteps.get(0);

        while (current != null) {
            if (current.isCommentedOut()) {
                current = resolveNextStep("NEXT", current, labelMap, sortedSteps);
                continue;
            }

            String actionType = "";
            switch (current.getActionType()){
                case -1 -> {
                    actionType = "Done";
                }
                case 1 -> {
                    actionType = "callProcessAction";
                }
            }


            Action action = (Action) appContext.getBean(actionType);

            // For CallProcessAction, we dynamically set the target process name in context
            if (actionType.equals("callProcessAction")) {
                context.setVar("targetProcessName", current.getActionId());
            }

            boolean success = action.execute(context);

            String nextLabel = success ? current.getPassLabel() : current.getFailLabel();
            current = resolveNextStep(nextLabel, current, labelMap, sortedSteps);
        }

        context.popProcess();
        return true;
    }

    // Handles NEXT / PREV / LABEL navigation
    private AppProcessObjectDetail resolveNextStep(String nextLabel,
                                                   AppProcessObjectDetail current,
                                                   Map<String, AppProcessObjectDetail> labelMap,
                                                   List<AppProcessObjectDetail> sortedSteps) {
        if (nextLabel == null || nextLabel.isBlank() || nextLabel.equalsIgnoreCase("END")) return null;

        switch (nextLabel.toUpperCase()) {
            case "NEXT" -> {
                int idx = sortedSteps.indexOf(current);
                return idx + 1 < sortedSteps.size() ? sortedSteps.get(idx + 1) : null;
            }
            case "PREV" -> {
                int idx = sortedSteps.indexOf(current);
                return idx - 1 >= 0 ? sortedSteps.get(idx - 1) : null;
            }
            default -> {
                return labelMap.get(nextLabel);
            }
        }
    }
}
