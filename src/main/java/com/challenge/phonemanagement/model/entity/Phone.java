package com.challenge.phonemanagement.model.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "phones",
        indexes = {
                @Index(name = "number_index", columnList = "number", unique = true)
        })
public class Phone {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    @Column(name = "number")
    private String number;

    @Column(name = "name")
    private String name;

    public long getId() {
        return id;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "Phone{" +
                "id=" + id +
                ", number='" + number + '\'' +
                ", name='" + name + '\'' +
                '}';
    }
}
