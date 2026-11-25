package com.tutorial.com.demo.controllers;

import java.net.http.HttpRequest;
import java.util.Date;
import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import com.tutorial.com.demo.model.Leave;
import com.tutorial.com.demo.repo.EmployeeRepoImpl;
import com.tutorial.com.demo.repo.LeaveRepoImpl;
import com.tutorial.com.demo.requests.EmployeeLeaveApplyRequest;
import com.tutorial.com.demo.requests.LeaveDecisionRequest;
import com.tutorial.com.demo.requests.ListLeaveRequest;


@RestController 
public class EmployeeLeaveController {
    public EmployeeRepoImpl employeeRepo;
    public LeaveRepoImpl leaveRepo;
    
    public EmployeeLeaveController(EmployeeRepoImpl employeeRepo, LeaveRepoImpl leaveRepo) {
        this.employeeRepo = employeeRepo;
        this.leaveRepo = leaveRepo;
    }

    @PostMapping ("/api/v1/leave-requests")
    public String leaveRequest(EmployeeLeaveApplyRequest employeeLeaveApplyRequest) {
        var employee = employeeRepo.findEmployeeById(employeeLeaveApplyRequest.employeeId);
        if(employee == null) {
            return "Employee not found";
        }

        if(employeeLeaveApplyRequest.leaveDays == null || employeeLeaveApplyRequest.leaveDays <= 0) {
            return "Leave days must be greater than zero";
        }

        // Leave starts today and ends today + leaveDays (so it can never be in the past).
        var startDate = new Date();
        var endDate = Date.from(startDate.toInstant().plus(employeeLeaveApplyRequest.leaveDays, java.time.temporal.ChronoUnit.DAYS));

        //An employee has a maximum of 20 annual leave days.
        var allLeaves = leaveRepo.findLeavesByEmployeeId(employeeLeaveApplyRequest.employeeId);

        var numLeavedaysTaken = 0;
        for (Leave existing : allLeaves) {
            numLeavedaysTaken += existing.leaveDays;
        }

        if (numLeavedaysTaken + employeeLeaveApplyRequest.leaveDays > employee.getMaxAnnualLeaveDays()) {
            return "Leave request exceeds the maximum of " + employee.getMaxAnnualLeaveDays() + " annual leave days (" + numLeavedaysTaken + " already taken)";
        }

        



        var leave = leaveRepo.createLeave(employeeLeaveApplyRequest.employeeId, employeeLeaveApplyRequest.leaveDays, employeeLeaveApplyRequest.starDate, employeeLeaveApplyRequest.enDate);

        if(leave != null) {
            return "Leave request created successfully";
        } else {
            return "Failed to create leave request";
        }
    }

    @GetMapping("/api/v1/leave-requests")
    public List<Leave> getLeaveRequests(ListLeaveRequest listLeaveRequest) {
        return leaveRepo.getAllLeaves();
    }

    @GetMapping("/api/v1/leave-requests/{id}")
    public Leave getLeaveRequestById(@PathVariable String id) {
        return leaveRepo.findLeaveByLeaveId(id);
    }

    @PatchMapping ("/api/v1/leave-requests/{id}/decision")
    public String updateLeaveRequest(@PathVariable String id, LeaveDecisionRequest leaveDecisionRequest) {
        var leave = leaveRepo.findLeaveByLeaveId(id);
        if(leave == null) {
            return "No Leave found";
        }
        var approved = leaveDecisionRequest.decision == "APPROVED";

        if(leave.apporveRejectLeave(approved, leaveDecisionRequest.reason) != null){
           return approved ? "Leave Approved" : "Leave Rejected";
        }

        return "An unexpected error occured";
    }

}
