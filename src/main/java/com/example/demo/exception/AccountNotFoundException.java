package com.example.demo.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class AccountNotFoundException extends RuntimeException{
    public AccountNotFoundException(int accountNumber) {
        super("Wallet with account Number " + accountNumber + " not found.");
    }

    public AccountNotFoundException(Long id) {
        super("Wallet ID " + id + " not found.");
    }
}
