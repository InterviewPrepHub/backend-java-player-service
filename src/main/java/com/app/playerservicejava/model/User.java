package com.app.playerservicejava.model;

import jakarta.persistence.*;

@Entity
@Table(name="Users")
public class User {

    @Id
    @Column(name = "USERID")
    @GeneratedValue(strategy = GenerationType.UUID)
    private String userId;

    @Column(name = "USERNAME")
    private String userName;

    @Column(name = "ROLE")
    private String role;    //ADMIN, GUEST
}
