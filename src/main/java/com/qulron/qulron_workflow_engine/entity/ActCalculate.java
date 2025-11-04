package com.qulron.qulron_workflow_engine.entity;

import jakarta.persistence.*;
import org.hibernate.annotations.GenericGenerator;

import java.util.UUID;

@Entity
@Table(name = "t_act_calculate")
public class ActCalculate {
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(updatable = false, nullable = false)
    private UUID id;

    @Column(nullable = false)
    private int version;

    @Column(nullable = false)
    private UUID application_id;

    @Column(nullable = false)
    private String name;

    @Column
    private String description;

    public UUID getId() {
        return id;
    }

    public int getVersion() {
        return version;
    }

    public UUID getApplication_id() {
        return application_id;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public void setVersion(int version) {
        this.version = version;
    }

    public void setApplication_id(UUID application_id) {
        this.application_id = application_id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
