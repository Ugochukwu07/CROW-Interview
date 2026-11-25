package com.tutorial.com.demo.repo;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.tutorial.com.demo.model.Employee;

@Repository 
public class EmployeeRepoImpl implements IEmployee {

    private final List<Employee> employees = List.of(
        new Employee("1", "John Doe", "example@email.com", "Engineering"),
        new Employee("2", "Jane Smith", "jane@email.com", "Marketing")
    );

    public Employee findEmployeeById(String id) {
        return employees.stream()
                .filter(employee -> employee.getId().equals(id))
                .findFirst()
                .orElse(null);
    }
}
