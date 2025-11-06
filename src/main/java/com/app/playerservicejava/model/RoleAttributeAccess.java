package com.app.playerservicejava.model;

import jakarta.persistence.*;

//dynamic attribute filtering per role

@Entity
@Table(name="ROLEATTRIBUTEACCESS")
public class RoleAttributeAccess {

    @Id
    @Column(name = "ID")
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    @Column(name = "ROLE")
    private String role;    //ADMIN, GUEST

    @Column(name = "ATTRIBUTE")
    private String attribute;
}
