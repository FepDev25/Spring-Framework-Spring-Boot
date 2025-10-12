package com.cultodeportivo.p8_sprinboot_jpa.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "persons")
public class Person {

    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private Long id;

    private String name;
    private String lastName;

    @Column(name = "programming_language")
    private String programmingLanguage;

    @Embedded
    private Audit audit = new Audit();

    public Person() {
    }

    public Person(Long id, String name, String lastName, String programmingLanguage) {
        this.id = id;
        this.name = name;
        this.lastName = lastName;
        this.programmingLanguage = programmingLanguage;
    }


    public Person(String name, String lastName) {
        this.name = name;
        this.lastName = lastName;
    }

    public Long getId() {
        return id;
    }
    public void setId(Long id) {
        this.id = id;
    }
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public String getLastName() {
        return lastName;
    }
    public void setLastName(String lastName) {
        this.lastName = lastName;
    }
    public String getProgrammingLanguage() {
        return programmingLanguage;
    }
    public void setProgrammingLanguage(String programmingLanguage) {
        this.programmingLanguage = programmingLanguage;
    }

    @Override
    public String toString() {
        String auditString = "Creado en: " + (audit != null ? audit.getCreatedAt() : "sin fecha");
        String updatedAtString = "Actualizado en: " + (audit != null ? audit.getUpdatedAt() : "sin fecha");

        StringBuilder sb = new StringBuilder();
        sb.append("Person{");
        sb.append("id=").append(id);
        sb.append(", name=").append(name);
        sb.append(", lastName=").append(lastName);
        sb.append(", programmingLanguage=").append(programmingLanguage);
        sb.append(", createdAt=").append(auditString);
        sb.append(", updatedAt=").append(updatedAtString);
        sb.append('}');
        return sb.toString();
    }


    

}
