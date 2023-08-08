package com.example.microservice1.service;

import com.example.microservice1.dtos.request.EmployeeRequestDto;
import com.example.microservice1.dtos.response.EmployeeResponseDto;
import com.example.microservice1.entities.Employee;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.springframework.data.domain.Page;

import java.text.ParseException;
import java.util.List;

public interface EmployeesService {
    EmployeeResponseDto createNewEmployee(EmployeeRequestDto employeeRequestDto) throws ParseException, JsonProcessingException;

    Page<Employee> fetchAllEmployee() throws JsonProcessingException;

    EmployeeResponseDto updateEmployeeDetails(EmployeeRequestDto employeeRequestDto) throws ParseException, JsonProcessingException;

    Boolean deleteEmployeeById(Long Id);

    EmployeeResponseDto findEmployeeById(Long  Id) throws JsonProcessingException;

}
