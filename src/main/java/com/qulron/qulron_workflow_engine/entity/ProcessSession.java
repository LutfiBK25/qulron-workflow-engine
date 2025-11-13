package com.qulron.qulron_workflow_engine.entity;

import jakarta.persistence.*;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "t_process_session")
public class ProcessSession {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(name = "device_id", nullable = false)
    private String deviceId;

    @Column(name = "process_name", nullable = false)
    private String processName;

    @Column(name = "current_step_label")
    private String currentStepLabel;

    @Column(name = "status")
    private String status;

    @Lob
    @Column(name = "context_date")
    private String contextData;

    @Column(name = "waiting_for_input")
    private boolean waitingForInput;

    @Column(name = "waiting_input_type")
    private String waitingInputType;

    @Lob
    @Column(name = "waiting_input_value")
    private String waitingInputValue;

    @Column(name = "last_updated")
    private LocalDateTime lastUpdated = LocalDateTime.now();

    public UUID getId() {
        return id;
    }

    public String getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(String deviceId) {
        this.deviceId = deviceId;
    }

    public String getProcessName() {
        return processName;
    }

    public void setProcessName(String processName) {
        this.processName = processName;
    }

    public String getCurrentStepLabel() {
        return currentStepLabel;
    }

    public void setCurrentStepLabel(String currentStepLabel) {
        this.currentStepLabel = currentStepLabel;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getContextData() {
        return contextData;
    }

    public void setContextData(String contextData) {
        this.contextData = contextData;
    }

    public boolean isWaitingForInput() {
        return waitingForInput;
    }

    public void setWaitingForInput(boolean waitingForInput) {
        this.waitingForInput = waitingForInput;
    }

    public String getWaitingInputType() {
        return waitingInputType;
    }

    public void setWaitingInputType(String waitingInputType) {
        this.waitingInputType = waitingInputType;
    }

    public String getWaitingInputValue() {
        return waitingInputValue;
    }

    public void setWaitingInputValue(String waitingInputValue) {
        this.waitingInputValue = waitingInputValue;
    }

    public LocalDateTime getLastUpdated() {
        return lastUpdated;
    }

    public void setLastUpdated(LocalDateTime lastUpdated) {
        this.lastUpdated = lastUpdated;
    }
}
