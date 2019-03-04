package com.alexvak.urs.domain;

import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;

@Entity
@Data
@Table(name = "USERS")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "USER_ID")
    private Long id;

    @NotEmpty(message = "error.name.empty")
    @Length(max = 50, message = "error.name.length")
    private String name;

    @NotEmpty(message = "error.address.empty")
    @Length(max = 150, message = "error.address.length")
    private String address;

    @Email(message = "error.email.email")
    @NotEmpty(message = "error.email.empty")
    @Length(max = 80, message = "error.email.length")
    private String email;
}
