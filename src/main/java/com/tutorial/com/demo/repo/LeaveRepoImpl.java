package com.tutorial.com.demo.repo;

import java.util.Date;
import java.util.List;

import com.tutorial.com.demo.model.Leave;

public class LeaveRepoImpl implements ILeave {

    private static Date daysFromNow(int days) {
        return Date.from(java.time.Instant.now().plus(days, java.time.temporal.ChronoUnit.DAYS));
    }

    public List<Leave> leaves = List.of(
        new Leave("1", "1", 5, new Date(), daysFromNow(5)),
        new Leave("2", "3", 2, new Date(), daysFromNow(2)),
        new Leave("3", "2", 3, new Date(), daysFromNow(3))
    );

    public Leave createLeave(String employeeId, Integer leaveDays, Date startDate, Date enDate) {
        String id = String.valueOf(leaves.size() + 1);
        return new Leave(id, employeeId, leaveDays, startDate, enDate);
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

    public Leave apporveRejectLeave(String id, boolean decision, String rejectReason){
        var leave = this.findLeaveByLeaveId(id);
        if(leave == null) throw new IllegalArgumentException("No Leave found");

        return leave.apporveRejectLeave(decision, rejectReason);
    }
    
}
