package com.qulron.qulron_workflow_engine.entity;


import jakarta.persistence.*;

import java.util.UUID;

@Entity
@Table(name = "t_app_process_object_detail")
public class AppProcessObjectDetail {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "id")
    private UUID id;

    @ManyToOne
    @JoinColumn(name = "process_object_id", nullable = false)
    private AppProcessObject appProcessObject;

    @Column(name = "version",nullable = false)
    private int version;

    @Column(name = "sequence", nullable = false)
    private int sequence;

    @Column(name = "label", nullable = false)
    private String label;

    @Column(name = "action", nullable = false)
    private int action;

    @Column(name = "action_type", nullable = false)
    private int actionType;

    @Column(name = "action_id", nullable = false)
    private String actionId;

    @Column(name = "pass_label", nullable = false)
    private String passLabel;

    @Column(name = "fail_label", nullable = false)
    private String failLabel;

    @Column(name = "commented_out", nullable = false)
    private boolean commentedOut;

    @Column(name = "comments")
    private String comments;

    public AppProcessObject getAppProcessObject() {
        return appProcessObject;
    }

    public void setAppProcessObject(AppProcessObject appProcessObject) {
        this.appProcessObject = appProcessObject;
    }

    public int getVersion() {
        return version;
    }

    public void setVersion(int version) {
        this.version = version;
    }

    public int getSequence() {
        return sequence;
    }

    public void setSequence(int sequence) {
        this.sequence = sequence;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public int getAction() {
        return action;
    }

    public void setAction(int action) {
        this.action = action;
    }

    public int getActionType() {
        return actionType;
    }

    public void setActionType(int actionType) {
        this.actionType = actionType;
    }

    public String getActionId() {
        return actionId;
    }

    public void setActionId(String actionId) {
        this.actionId = actionId;
    }

    public String getPassLabel() {
        return passLabel;
    }

    public void setPassLabel(String passLabel) {
        this.passLabel = passLabel;
    }

    public String getFailLabel() {
        return failLabel;
    }

    public void setFailLabel(String failLabel) {
        this.failLabel = failLabel;
    }

    public boolean isCommentedOut() {
        return commentedOut;
    }

    public void setCommentedOut(boolean commentedOut) {
        this.commentedOut = commentedOut;
    }

    public String getComments() {
        return comments;
    }

    public void setComments(String comments) {
        this.comments = comments;
    }
}
