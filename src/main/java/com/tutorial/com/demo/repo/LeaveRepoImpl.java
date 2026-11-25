package com.tutorial.com.demo.repo;

import java.util.Date;
import java.util.List;

import com.tutorial.com.demo.model.Leave;

public class LeaveRepoImpl implements ILeave {

    public List<Leave> leaves = List.of(
        new Leave("1", "1", 5),
        new Leave("2", "3", 2),
        new Leave("3", "2", 3)
    );

    public Leave createLeave(String employeeId, Integer leaveDays) {
        String id = String.valueOf(leaves.size() + 1);
        return new Leave(id, employeeId, leaveDays);
    }

    public List<Leave> getAllLeaves() {
        return leaves;
    }

    public Leave findLeaveByLeaveId(String id) {
        return leaves.stream()
            .filter(leave -> leave.id.equals(id))
            .findFirst()
            .orElse(null);
    }

    public List<Leave> findLeavesByEmployeeId(String employeeId) {
        return leaves.stream()
            .filter(leave -> leave.employeeId.equals(employeeId))
            .limit(10000)
            .toList();
    }

    public Leave apporveRejectLeave(String id, boolean decision, String rejectReason, Date starDate, Date enDate){
        var leave = this.findLeaveByLeaveId(id);
        if(leave.id == null) throw new Exception("No Leave found");

        return leave.apporveRejectLeave(decision, rejectReason, starDate, enDate);
    }
    
}
