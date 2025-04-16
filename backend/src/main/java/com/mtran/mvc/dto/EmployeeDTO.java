package com.mtran.mvc.dto;

import lombok.AccessLevel;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Component;

@Component
@FieldDefaults(level = AccessLevel.PRIVATE)
public class EmployeeDTO {
    int id;
    String name;
    int age;

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


    @Override
    public int hashCode() {
        return Integer.hashCode(id);

    }

    @Override
    public boolean equals(Object obj) {
        if(! (obj instanceof EmployeeDTO))return false;
        EmployeeDTO other = (EmployeeDTO)obj;
        return id == other.id;
    }
}
