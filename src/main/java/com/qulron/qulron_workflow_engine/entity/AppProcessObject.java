package com.qulron.qulron_workflow_engine.entity;

import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "t_app_process_object")
public class AppProcessObject {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "id")
    private UUID id;

    @Column(nullable = false)
    private int version;

    @Column(nullable = false)
    private UUID application_id;

    @Column(nullable = false)
    private String name;

    @Column
    private String description;

    @Column
    private String comments;

    public List<AppProcessObjectDetail> getSteps() {
        return steps;
    }

    public void setSteps(List<AppProcessObjectDetail> steps) {
        this.steps = steps;
    }

    @OneToMany(mappedBy = "appProcessObject", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<AppProcessObjectDetail> steps = new ArrayList<>();

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

    public String getComments() {
        return comments;
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

    public void setComments(String comments) {
        this.comments = comments;
    }
}
