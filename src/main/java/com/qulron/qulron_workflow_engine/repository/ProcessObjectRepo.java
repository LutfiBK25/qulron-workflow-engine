package com.qulron.qulron_workflow_engine.repository;

import com.qulron.qulron_workflow_engine.entity.AppProcessObject;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface ProcessObjectRepo extends JpaRepository<AppProcessObject, UUID> {
    Optional<AppProcessObject> findByName(String name);
}
