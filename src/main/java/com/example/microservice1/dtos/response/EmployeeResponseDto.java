package com.example.microservice1.dtos.response;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class EmployeeResponseDto {
    private Long employeeId;
    private String firstName;
    private String lastName;
    private String emailId;
    private String dateOfBirth;
    private Long mobileNumber;
}
