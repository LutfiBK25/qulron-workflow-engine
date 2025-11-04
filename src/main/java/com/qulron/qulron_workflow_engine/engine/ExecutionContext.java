package com.qulron.qulron_workflow_engine.engine;

import com.qulron.qulron_workflow_engine.entity.AppProcessObject;

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.HashMap;
import java.util.Map;

public class ExecutionContext {
    private final Deque<AppProcessObject> processStack = new ArrayDeque<>();
    private final Map<String, Object> variables = new HashMap<>();

    public void pushProcess(AppProcessObject process) {
        processStack.push(process);
        System.out.println("▶ Entering process: " + process.getName());
    }

    public void popProcess() {
        AppProcessObject popped = processStack.pop();
        System.out.println("⬅ Exiting process: " + popped.getName());
    }

    public AppProcessObject getCurrentProcess() {
        return processStack.peek();
    }

    public void setVar(String key, Object value) {
        variables.put(key, value);
    }

    public Object getVar(String key) {
        return variables.get(key);
    }

    public boolean isInStack(AppProcessObject process) {
        return processStack.contains(process);
    }
}