package com.oauthandsecurityandjwt.oauth2.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter @Setter
public class UserEntity {
    @Id @GeneratedValue
    private Long id;
    private String username;
    private String email;
    private String role;
}
