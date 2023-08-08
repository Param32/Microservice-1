package com.example.microservice1.dtos.request;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@AllArgsConstructor
@NoArgsConstructor
public class EmployeeRequestDto {
    private Long employeeId;
    private String firstName;
    private String lastName;
    private String emailId;
    private String dateOfBirth;
    private Long mobileNumber;
}
