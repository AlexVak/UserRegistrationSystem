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

    @NotEmpty
    @Length(max = 50)
    private String name;

    @NotEmpty
    @Length(max = 150)
    private String address;

    @Email
    @NotEmpty
    @Length(max = 80)
    private String email;
}
