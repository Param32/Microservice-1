package com.example.microservice1.service.impl;

import com.example.microservice1.config.RabbitMQProducer;
import com.example.microservice1.dtos.request.EmployeeRequestDto;
import com.example.microservice1.dtos.response.EmployeeResponseDto;
import com.example.microservice1.entities.Employee;
import com.example.microservice1.exception.UserNotFoundException;
import com.example.microservice1.repositories.EmployeeRepository;
import com.example.microservice1.service.EmployeesService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Objects;

@Service
@EnableCaching
public class EmployeesServiceImpl implements EmployeesService {

    @Autowired
    private EmployeeRepository employeeRepository;

    @Autowired
    private RedisTemplate<String,Object> redisTemplate;

    private RabbitMQProducer producer;

    public EmployeesServiceImpl(RabbitMQProducer producer) {
        this.producer = producer;
    }

    @Override
    public EmployeeResponseDto createNewEmployee(EmployeeRequestDto employeeRequestDto) throws ParseException, JsonProcessingException {

        Date date = new SimpleDateFormat("dd/MM/yyyy").parse(employeeRequestDto.getDateOfBirth());
        Employee employees = Employee.builder()
                .firstName(employeeRequestDto.getFirstName())
                .lastName(employeeRequestDto.getLastName())
                .emailId(employeeRequestDto.getEmailId())
                .dateOfBirth(date)
                .mobileNumber(employeeRequestDto.getMobileNumber())
                .build();

        Employee employees1 = employeeRepository.save(employees);

        EmployeeResponseDto employeeResponseDto = EmployeeResponseDto.builder()
                .employeeId(employees1.getEmployeeId())
                .firstName(employees1.getFirstName())
                .lastName(employees1.getLastName())
                .emailId(employees1.getEmailId())
                .dateOfBirth(String.valueOf(employees1.getDateOfBirth()))
                .mobileNumber(employees1.getMobileNumber())
                .build();

        ObjectMapper objectMapper = new ObjectMapper();
        String payload = objectMapper.writeValueAsString(employeeResponseDto);

        producer.sendMessage("New Employee Has Been Added"+payload);
        return employeeResponseDto;
    }

    @Override
    public Page<Employee> fetchAllEmployee() throws JsonProcessingException {
        Pageable pageable = PageRequest.of(0, 1);
        List<Employee> employeeList = employeeRepository.findAll();

        Page<Employee> page = new PageImpl<>(employeeList, pageable, employeeList.size());
        ObjectMapper objectMapper = new ObjectMapper();
        String payload = objectMapper.writeValueAsString(page);
        producer.sendMessage("Fetch all The details of Employees"+payload);
        redisTemplate.opsForValue().set("Employee",payload);
        return page;
    }

    @Override
    public EmployeeResponseDto updateEmployeeDetails(EmployeeRequestDto employeeRequestDto) throws ParseException, JsonProcessingException {

        EmployeeResponseDto employeeResponseDto = null;

        Employee employee = employeeRepository.findById(employeeRequestDto.getEmployeeId()).get();
        if (Objects.isNull(employee)) {
            throw new UserNotFoundException(String.format("User Not found with id ->," + employeeRequestDto.getEmployeeId()));
        } else {
            Date date = new SimpleDateFormat("dd/MM/yyyy").parse(employeeRequestDto.getDateOfBirth());

            employee = Employee.builder()
                    .employeeId(employeeRequestDto.getEmployeeId())
                    .firstName(employeeRequestDto.getFirstName())
                    .lastName(employeeRequestDto.getLastName())
                    .emailId(employeeRequestDto.getEmailId())
                    .dateOfBirth(date)
                    .mobileNumber(employeeRequestDto.getMobileNumber())
                    .build();

            Employee employee1 = employeeRepository.save(employee);
            employeeResponseDto = EmployeeResponseDto.builder()
                    .firstName(employee1.getFirstName())
                    .lastName(employee1.getLastName())
                    .emailId(employee1.getEmailId())
                    .dateOfBirth(String.valueOf(employee1.getDateOfBirth()))
                    .mobileNumber(employee1.getMobileNumber())
                    .build();
        }
        ObjectMapper objectMapper = new ObjectMapper();
        String payload = objectMapper.writeValueAsString(employeeResponseDto);
        producer.sendMessage("Employee with id "+employeeResponseDto.getEmployeeId()+"has been updated"+payload);
        return employeeResponseDto;
    }

    @Override
    public Boolean deleteEmployeeById(Long Id) {
        Boolean aBoolean = false;
        Employee employee = employeeRepository.findById(Id).get();
        if (Objects.isNull(employee)) {
            throw new UserNotFoundException(String.format("User Not found with id ->," + Id));
        } else {
            employeeRepository.deleteById(Id);
            producer.sendMessage("Employee with id "+Id+"has been updated");
            aBoolean = true;
        }
        return aBoolean;
    }

    @Override
    public EmployeeResponseDto findEmployeeById(Long Id) throws JsonProcessingException {

        EmployeeResponseDto employeeResponseDto = null;
        Employee employee = employeeRepository.findById(Id).get();
        if (Objects.isNull(employee)) {
            throw new UserNotFoundException(String.format("User Not found with id ->," + Id));
        } else {
            employeeResponseDto = EmployeeResponseDto.builder()
                    .employeeId(employee.getEmployeeId())
                    .firstName(employee.getFirstName())
                    .lastName(employee.getLastName())
                    .emailId(employee.getEmailId())
                    .dateOfBirth(String.valueOf(employee.getDateOfBirth()))
                    .mobileNumber(employee.getMobileNumber())
                    .build();
        }
        ObjectMapper objectMapper = new ObjectMapper();
        String payload = objectMapper.writeValueAsString(employeeResponseDto);
        producer.sendMessage("Employee with id "+employeeResponseDto.getEmployeeId()+"has been fetched"+payload);
        return employeeResponseDto;
    }
}
