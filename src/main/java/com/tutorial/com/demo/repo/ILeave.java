package com.tutorial.com.demo.repo;

import java.util.List;

import com.tutorial.com.demo.model.Leave;


public interface ILeave {
    public Leave createLeave(String employeeId, String leaveDays);

    public List<Leave> getAllLeaves();

    public List<Leave> findLeavesByEmployeeId(String employeeId);

    public Leave findLeaveByLeaveId(String id);

    public Leave apporveRejectLeave(String id, boolean decision, String rejectReason);
}