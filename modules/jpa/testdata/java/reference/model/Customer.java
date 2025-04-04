package com.example;

import javax.persistence.*;
import jakarta.persistence.*;
import java.util.List;

@Entity
@DiscriminatorValue("customer")
public class Customer extends BaseEntity {
    @Column
    private String name;

    @Column
    private String email;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}