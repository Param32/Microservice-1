package com.example.microservice1.controllers;

import com.example.microservice1.dtos.request.EmployeeRequestDto;
import com.example.microservice1.service.EmployeesService;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.text.ParseException;

@RestController
@RequestMapping("api/v1/employee")
public class EmployeeController {


    @Autowired
    private EmployeesService employeesService;

    @PostMapping(value = "/create", consumes = "application/json", produces = "application/json")
    public ResponseEntity<?> createNewEmployee(@RequestBody EmployeeRequestDto employeeRequestDto) throws ParseException, JsonProcessingException {
        return new ResponseEntity<>(employeesService.createNewEmployee(employeeRequestDto), HttpStatus.CREATED);
    }

    @GetMapping(value = "/fetchAllEmployees", produces = "application/json")
    public ResponseEntity<?> getAllEmployeesDetails() throws JsonProcessingException {
        return new ResponseEntity<>(employeesService.fetchAllEmployee(), HttpStatus.ACCEPTED);
    }

    @DeleteMapping(value = "/deleteById/{id}", produces = "application/json")
    public ResponseEntity<?> deleteEmployeeById(@PathVariable Long Id) {
        return new ResponseEntity<>(employeesService.deleteEmployeeById(Id), HttpStatus.ACCEPTED);
    }

    @PutMapping(value = "/updateEmployeeDetails", consumes = "application/json", produces = "application/json")
    public ResponseEntity<?> updateEmployeeDetails(@RequestBody EmployeeRequestDto employeeRequestDto) throws ParseException, JsonProcessingException {
        return new ResponseEntity<>(employeesService.updateEmployeeDetails(employeeRequestDto), HttpStatus.ACCEPTED);
    }

    @GetMapping(value = "/fetchById/{Id}", produces = "application/json")
    public ResponseEntity<?> fetchEmployeeDetailsById(@PathVariable Long Id) throws JsonProcessingException {
        return new ResponseEntity<>(employeesService.findEmployeeById(Id), HttpStatus.FOUND);
    }
}
