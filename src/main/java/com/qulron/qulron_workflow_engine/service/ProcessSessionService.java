package com.qulron.qulron_workflow_engine.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.qulron.qulron_workflow_engine.engine.ExecutionContext;
import com.qulron.qulron_workflow_engine.entity.ProcessSession;
import com.qulron.qulron_workflow_engine.repository.ProcessSessionRepo;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class ProcessSessionService {
    private final ProcessSessionRepo processSessionRepo;
    private final ObjectMapper objectMapper = new ObjectMapper();

    public ProcessSessionService(ProcessSessionRepo processSessionRepo){
        this.processSessionRepo = processSessionRepo;
    }

    public ProcessSession loadOrCreate(String deviceId, String processName){
        return processSessionRepo.findByDeviceIdAndProcessName(deviceId,processName)
                .orElse(()-> {
                    ProcessSession session = new ProcessSession();
                    session.setDeviceId(deviceId);
                    session.setProcessName(processName);
                    session.setStatus("NEW");
                    return processSessionRepo.save(session)
                });
    }

    public void saveProgress(String deviceId, String processName, String currentStepLabel, ExecutionContext context){
        ProcessSession session = loadOrCreate(deviceId, processName);
        session.setCurrentStepLabel(currentStepLabel);
        session.setContextData(currentStepLabel);
        session.setStatus("RUNNING");
        session.setLastUpdated(LocalDateTime.now());
        processSessionRepo.save(session)
    }


}
