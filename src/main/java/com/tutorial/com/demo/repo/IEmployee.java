package com.tutorial.com.demo.repo;

import org.springframework.stereotype.Service;

import com.tutorial.com.demo.model.Employee;


@Service 
public interface IEmployee {
    public Employee findEmployeeById(String id);
}