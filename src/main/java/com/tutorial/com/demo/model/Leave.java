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

    public Leave(String id, String employeeId, Integer leaveDays, Date startDate, Date enDate) {
        this.id = id;
        this.employeeId = employeeId;
        this.leaveDays = leaveDays;
        this.startDate = startDate;
        this.enDate = enDate;
    }

    public Leave apporveRejectLeave(boolean approved, String rejectReason){
        this.approved = approved;
        this.rejectReason = approved ? "" : rejectReason;

        return this;
    }
}
