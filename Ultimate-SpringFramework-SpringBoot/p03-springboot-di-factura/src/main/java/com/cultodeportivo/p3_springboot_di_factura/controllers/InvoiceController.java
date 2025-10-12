package com.cultodeportivo.p3_springboot_di_factura.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.cultodeportivo.p3_springboot_di_factura.models.Client;
import com.cultodeportivo.p3_springboot_di_factura.models.Invoice;

@RestController
@RequestMapping("/invoices")
public class InvoiceController {

    @Autowired
    Invoice invoice;
    
    @GetMapping("/show")
    public Invoice show(){
        // Para no devolver el proxy
        Invoice i = new Invoice();
        Client c = new Client();

        c.setName(invoice.getClient().getName());
        c.setlastName(invoice.getClient().getlastName());
        
        i.setClient(c);
        i.setDescription(invoice.getDescription());
        i.setItems(invoice.getItems());
        return i;
    }
}
