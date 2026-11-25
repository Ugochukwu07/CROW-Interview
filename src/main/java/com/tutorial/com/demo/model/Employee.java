package com.tutorial.com.demo.model;

public class Employee {
    private String id;
    private String name;
    private String email;
    private String department;


    private final Integer MaxAnnualLeaveDays = 20;

    public Employee(String id, String name, String email, String department) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.department = department;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public String getDepartment() {
        return department;
    }


}
