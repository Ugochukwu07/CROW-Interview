package com.tutorial.com.demo.model;

import java.util.Date;

public class Leave {
    public String id;
    public String employeeId;
    public Integer leaveDays;
    public boolean approved;
    public String rejectReason;

    public Date startDate;
    public Date enDate;

    public Leave(String id, String employeeId, Integer leaveDays) {
        this.id = id;
        this.employeeId = employeeId;
        this.leaveDays = leaveDays;
    }

    public Leave apporveRejectLeave(boolean approved, String rejectReason, Date startDate, Date enDate){
        this.approved = approved;
        this.rejectReason = approved ? "" : rejectReason;
        this.startDate = startDate;
        this.enDate = enDate;

        return this;
    }
}
