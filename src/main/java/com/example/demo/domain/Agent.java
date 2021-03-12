/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.example.demo.domain;

import javax.persistence.*;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import lombok.Setter;

import java.util.Date;
import java.util.List;


/**
 *
 * @author rbabaei
 */
@Entity
@Slf4j
@Setter
@Getter
@Table
public class Agent {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(columnDefinition = "TEXT")
    private String firstName;
    @Column(columnDefinition = "TEXT")
    private String lastName;

    @Temporal(TemporalType.TIMESTAMP)
    private Date createdDate = new Date();

    @OneToMany(mappedBy = "agent")
    private List<Key> keys;

  
    @Override
    public String toString() {
        return "Agent{" + "firstName=" + firstName + ", lastName=" + lastName +", registrationDate=" + createdDate + '}';
    }


}
