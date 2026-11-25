package com.tutorial.com.demo.controllers;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Date;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.tutorial.com.demo.model.Employee;
import com.tutorial.com.demo.model.Leave;
import com.tutorial.com.demo.repo.EmployeeRepoImpl;
import com.tutorial.com.demo.repo.LeaveRepoImpl;
import com.tutorial.com.demo.requests.EmployeeLeaveApplyRequest;

class EmployeeLeaveControllerTest {

    private EmployeeRepoImpl employeeRepo;
    private LeaveRepoImpl leaveRepo;
    private EmployeeLeaveController controller;

    @BeforeEach
    void setUp() {
        employeeRepo = mock(EmployeeRepoImpl.class);
        leaveRepo = mock(LeaveRepoImpl.class);
        controller = new EmployeeLeaveController(employeeRepo, leaveRepo);
        when(employeeRepo.findEmployeeById("1"))
                .thenReturn(new Employee("1", "John Doe", "example@email.com", "Engineering"));
        when(leaveRepo.findLeavesByEmployeeId("1")).thenReturn(List.of());
    }

    private EmployeeLeaveApplyRequest request(String employeeId, Integer days) {
        var req = new EmployeeLeaveApplyRequest();
        req.employeeId = employeeId;
        req.leaveDays = days;
        req.starDate = new Date();
        req.enDate = new Date();
        return req;
    }

    private Leave takenLeave(int days) {
        return new Leave("x", "1", days, new Date(), new Date());
    }

    @Test
    void unknownEmployeeIsRejected() {
        when(employeeRepo.findEmployeeById("99")).thenReturn(null);

        assertEquals("Employee not found", controller.leaveRequest(request("99", 3)));
        verify(leaveRepo, never()).createLeave(anyString(), anyInt(), any(), any());
    }

    @Test
    void nullLeaveDaysIsRejected() {
        assertEquals("Leave days must be greater than zero", controller.leaveRequest(request("1", null)));
    }

    @Test
    void zeroLeaveDaysIsRejected() {
        assertEquals("Leave days must be greater than zero", controller.leaveRequest(request("1", 0)));
    }

    @Test
    void negativeLeaveDaysIsRejected() {
        assertEquals("Leave days must be greater than zero", controller.leaveRequest(request("1", -2)));
    }

    @Test
    void requestExceedingMaxIsRejectedAndMessageShowsDaysTaken() {
        when(leaveRepo.findLeavesByEmployeeId("1")).thenReturn(List.of(takenLeave(15)));

        var result = controller.leaveRequest(request("1", 6));

        assertTrue(result.contains("maximum of 20"), result);
        assertTrue(result.contains("15 already taken"), result);
        verify(leaveRepo, never()).createLeave(anyString(), anyInt(), any(), any());
    }

    @Test
    void singleRequestOverMaxIsRejectedWhenNothingTaken() {
        assertTrue(controller.leaveRequest(request("1", 21)).contains("maximum of 20"));
    }

    @Test
    void requestExactlyReachingMaxIsAllowed() {
        when(leaveRepo.findLeavesByEmployeeId("1")).thenReturn(List.of(takenLeave(10), takenLeave(5)));
        when(leaveRepo.createLeave(eq("1"), eq(5), any(), any())).thenReturn(takenLeave(5));

        assertEquals("Leave request created successfully", controller.leaveRequest(request("1", 5)));
    }

    @Test
    void validRequestCreatesLeave() {
        when(leaveRepo.createLeave(eq("1"), eq(3), any(), any())).thenReturn(takenLeave(3));

        assertEquals("Leave request created successfully", controller.leaveRequest(request("1", 3)));
        verify(leaveRepo).createLeave(eq("1"), eq(3), any(), any());
    }

    @Test
    void failedCreationIsReported() {
        when(leaveRepo.createLeave(anyString(), anyInt(), any(), any())).thenReturn(null);

        assertEquals("Failed to create leave request", controller.leaveRequest(request("1", 3)));
    }
}
