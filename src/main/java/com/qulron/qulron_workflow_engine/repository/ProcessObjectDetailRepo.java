package com.qulron.qulron_workflow_engine.repository;

import com.qulron.qulron_workflow_engine.entity.AppProcessObject;
import com.qulron.qulron_workflow_engine.entity.AppProcessObjectDetail;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface ProcessObjectDetailRepo extends JpaRepository<AppProcessObjectDetail, UUID> {
}
