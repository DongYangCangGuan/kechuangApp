package com.cloud.servicemanage.service.activitiRest.model;

import java.util.HashMap;
import java.util.Map;

/**
 * WorkFlow实体类
 */
public class WorkFlowModel {
    // 流程定义名称
    private String processDefinitionKey;
    // 流程实例Id
    private String processInstanceId;
    // 流程发起人
    private String startUserId;
    // 流程发起人角色
    private String startUserRoleId;
    // 当前任务Id
    private String currentTaskId;
    // 当前任务处理人
    private String currentUserId;
    // 当前任务处理人角色
    private String currentUserRoleId;
    // 下一任务ID
    private String nextTaskId;
    // 流程流转所需参数
    private Map<String, Object> variables = new HashMap<>();

    public String getProcessDefinitionKey() {
        return processDefinitionKey;
    }

    public void setProcessDefinitionKey(String processDefinitionKey) {
        this.processDefinitionKey = processDefinitionKey;
    }

    public String getProcessInstanceId() {
        return processInstanceId;
    }

    public void setProcessInstanceId(String processInstanceId) {
        this.processInstanceId = processInstanceId;
    }

    public String getStartUserId() {
        return startUserId;
    }

    public void setStartUserId(String startUserId) {
        this.startUserId = startUserId;
    }

    public String getStartUserRoleId() {
        return startUserRoleId;
    }

    public void setStartUserRoleId(String startUserRoleId) {
        this.startUserRoleId = startUserRoleId;
    }

    public String getCurrentTaskId() {
        return currentTaskId;
    }

    public void setCurrentTaskId(String currentTaskId) {
        this.currentTaskId = currentTaskId;
    }

    public String getCurrentUserId() {
        return currentUserId;
    }

    public void setCurrentUserId(String currentUserId) {
        this.currentUserId = currentUserId;
    }

    public String getCurrentUserRoleId() {
        return currentUserRoleId;
    }

    public void setCurrentUserRoleId(String currentUserRoleId) {
        this.currentUserRoleId = currentUserRoleId;
    }

    public String getNextTaskId() {
        return nextTaskId;
    }

    public void setNextTaskId(String nextTaskId) {
        this.nextTaskId = nextTaskId;
    }

    public Map<String, Object> getVariables() {
        return variables;
    }

    public void setVariables(Map<String, Object> variables) {
        this.variables = variables;
    }
}
