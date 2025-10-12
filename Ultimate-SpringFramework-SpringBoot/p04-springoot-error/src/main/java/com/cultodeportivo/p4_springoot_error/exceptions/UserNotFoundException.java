package com.cultodeportivo.p4_springoot_error.exceptions;

public class UserNotFoundException extends RuntimeException {

    public UserNotFoundException(Long id) {
        super("No se puede encontrar el usuario con el id: " + id);
    }

}
