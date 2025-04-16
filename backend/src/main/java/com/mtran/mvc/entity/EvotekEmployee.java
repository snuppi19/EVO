package com.mtran.mvc.entity;


import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;


@Entity
@Table(name = "employees")
public class EvotekEmployee {
    @Id
    @Column(name = "emp_id", nullable = false)
    private int id;
    @Column(name = "emp_name", nullable = false,columnDefinition = "VARCHAR(100)")
    private String name;
    @Column(name = "emp_age", nullable = false)
    private int age;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }
}
