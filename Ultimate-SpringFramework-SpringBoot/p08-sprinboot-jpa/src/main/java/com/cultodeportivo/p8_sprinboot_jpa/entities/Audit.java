package com.cultodeportivo.p8_sprinboot_jpa.entities;

import java.time.LocalDate;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;

@Embeddable
public class Audit {

    @Column(name = "created_at")
    private LocalDate createdAt;

    @Column(name = "updated_at")
    private LocalDate updatedAt;

    @PrePersist
    public void prePersist(){
        System.out.println("PrePersist: Preparing to save a new Person entity");
        this.createdAt = LocalDate.now();
    }

    @PreUpdate
    public void preUpdate(){
        System.out.println("PreUpdate: Preparing to update a Person entity");
        this.updatedAt = LocalDate.now();
    }


    public LocalDate getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDate createdAt) {
        this.createdAt = createdAt;
    }

    public LocalDate getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(LocalDate updatedAt) {
        this.updatedAt = updatedAt;
    }


    
}
