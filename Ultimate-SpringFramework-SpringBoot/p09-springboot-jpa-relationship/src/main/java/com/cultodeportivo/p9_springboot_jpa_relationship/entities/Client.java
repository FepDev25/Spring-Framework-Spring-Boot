package com.cultodeportivo.p9_springboot_jpa_relationship.entities;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;

@Entity
@Table(name = "clients")
public class Client {

    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private Long id;

    private String name;
    private String lastname;

    @OneToMany(cascade=CascadeType.ALL, orphanRemoval=true, mappedBy="client")
    private Set<Invoice> invoices;

    @OneToMany(cascade=CascadeType.ALL, orphanRemoval=true)
    @JoinTable(name="tbl_clientes_to_direcciones", 
    joinColumns= @JoinColumn(name="id_cliente"), 
    inverseJoinColumns= @JoinColumn(name="id_direccion"),
    uniqueConstraints= @UniqueConstraint(columnNames={"id_direccion"}))
    private List<Address> addresses;    

    @OneToOne(cascade=CascadeType.ALL, orphanRemoval=true, mappedBy="client")
    private ClientDetails clientDetails;

    public Client() {
        addresses = new ArrayList<>();
        invoices = new HashSet<>();
    }

    public Client(String name, String lastname) {
        this();
        this.name = name;
        this.lastname = lastname;
    }

    public Client addInvoice(Invoice invoice) {
        this.invoices.add(invoice);
        invoice.setClient(this);
        return this;
    }

    public Client removeInvoice(Invoice invoice) {
        this.invoices.remove(invoice);
        invoice.setClient(null);
        return this;
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

    public String getLastname() {
        return lastname;
    }

    public void setLastname(String lastname) {
        this.lastname = lastname;
    }

    public List<Address> getAddresses() {
        return addresses;
    }

    public void setAddresses(List<Address> addresses) {
        this.addresses = addresses;
    }

    public void addAddress(Address... address) {
        this.addresses.addAll(Arrays.asList(address));
    }

    public void removeAddress(Address address) {
        this.addresses.remove(address);
    }


    public Set<Invoice> getInvoices() {
        return invoices;
    }

    public void setInvoices(Set<Invoice> invoices) {
        this.invoices = invoices;
    }

    public ClientDetails getClientDetails() {
        return clientDetails;
    }

    public void setClientDetails(ClientDetails clientDetails) {
        this.clientDetails = clientDetails;
        clientDetails.setClient(this);
    }

    public void removeClientDetails(ClientDetails clientDetails) {
        clientDetails.setClient(null);
        this.clientDetails = null;
    }


    
    @Override
    public String toString() {
        return "{id=" + id + 
        ", name=" + name + 
        ", lastname=" + lastname + 
        ", addresses=" + addresses + 
        ", invoices=" + invoices +
        ", clientDetails=" + clientDetails +
        "}";
    }

    

}
