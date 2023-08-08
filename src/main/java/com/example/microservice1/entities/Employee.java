package com.example.microservice1.entities;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "employee")
@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Employee {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long employeeId;

    @Column(name = "first_name")
    private String firstName;

    @Column(name = "last_name")
    private String lastName;

    @Column(name = "email_id", unique = true)
    private String emailId;

    @Column(name = "date_of_birth")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dateOfBirth;

    @Column(name = "mobile_number", unique = true)
    private Long mobileNumber;

}
