package com.qulron.qulron_workflow_engine.repository;

import com.qulron.qulron_workflow_engine.entity.ProcessSession;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface ProcessSessionRepo extends JpaRepository<ProcessSession, UUID> {
    Optional <ProcessSession> findByDeviceIdAndProcessName(String deviceId, String processName);
}
