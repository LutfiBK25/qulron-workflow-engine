package com.qulron.qulron_workflow_engine.controller;

import com.qulron.qulron_workflow_engine.service.ProcessEngine;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/api/workflows")
public class WorkflowController {
    private final ProcessEngine processEngine;

    public WorkflowController(ProcessEngine processEngine) {
        this.processEngine = processEngine;
    }

    @PostMapping("/{processObjectName}/execute")
    public ResponseEntity<String> execute(@PathVariable String processObjectName){
        processEngine.executeProcessByName(processObjectName);
        return ResponseEntity.ok("Workflow executed for process " + processObjectName);
    }
}
