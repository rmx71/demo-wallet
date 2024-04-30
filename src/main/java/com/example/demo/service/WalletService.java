package com.example.demo.service;

import com.example.demo.dto.TransferWalletFundRequestDTO;
import com.example.demo.exception.AccountNotFoundException;
import com.example.demo.exception.InvalidRequestException;
import com.example.demo.model.Transaction;
import com.example.demo.model.Wallet;
import com.example.demo.repository.TransactionRepository;
import com.example.demo.repository.WalletRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class WalletService {

    @Autowired
    WalletRepository walletRepository;

    @Autowired
    TransactionRepository transactionRepository;

    @Transactional
    public Wallet createWallet() {
        Wallet wallet = new Wallet();
        wallet.setAccountNumber(generateAccountNumber());
        wallet.setBalance(0);

        walletRepository.save(wallet);
        Transaction transaction = createTransaction(wallet, 0, "CREATE_WALLET");
        transactionRepository.save(transaction);

        return wallet;
    }

    public Wallet checkBalance(int accountNumber) {
        Wallet wallet = getWalletByAccountNumber(accountNumber).get();
        return wallet;
    }

    public Optional<Wallet> getWalletByAccountNumber(int accountNumber) {
        Optional<Wallet> wallet = walletRepository.findByAccountNumber(accountNumber);
        if (wallet.isPresent()) {
            return wallet;
        } else {
            throw new AccountNotFoundException(accountNumber);
        }
    }

    public Wallet updateWalletBalance(TransferWalletFundRequestDTO transferRequest) {

        if (transferRequest.getTransferFromAccountNumber() == transferRequest.getTransferToAccountNumber()) {
            throw new InvalidRequestException("Cannot transfer to the same account");
        }

        Wallet walletToTransferFrom = getWalletByAccountNumber(transferRequest.getTransferFromAccountNumber()).get();
        Wallet walletToTransferTo = getWalletByAccountNumber(transferRequest.getTransferToAccountNumber()).get();

        if (walletToTransferFrom.getBalance() < transferRequest.getAmountToTransfer()) {
            throw new InvalidRequestException("Insufficient balance in account number " + transferRequest.getTransferFromAccountNumber());
        }

        walletToTransferFrom.setBalance(walletToTransferFrom.getBalance() - transferRequest.getAmountToTransfer());

        walletToTransferTo.setBalance(walletToTransferTo.getBalance() + transferRequest.getAmountToTransfer());

        Transaction accountTransferToTransaction = createTransaction(walletToTransferTo, transferRequest.getAmountToTransfer(), "TRANSFER_FUND");
        transactionRepository.save(accountTransferToTransaction);


        Transaction accountTransferFromTransaction = createTransaction(walletToTransferFrom, transferRequest.getAmountToTransfer(), "RECEIVED_FROM_TRANSFER");
        transactionRepository.save(accountTransferFromTransaction);



        walletRepository.save(walletToTransferFrom);
        walletRepository.save(walletToTransferTo);

        return walletToTransferFrom;
    }

    public Wallet depositToWallet(int accountNumber, double amount) {
        if (amount <= 0) {
            throw new InvalidRequestException("Invalid amount to deposit");
        }
        Wallet wallet = getWalletByAccountNumber(accountNumber).get();
        wallet.setBalance(wallet.getBalance() + amount);

        Transaction transaction = createTransaction(wallet, amount, "DEPOSIT");
        transactionRepository.save(transaction);

        walletRepository.save(wallet);
        return wallet;
    }

    public List<Transaction> getTransactionHistory(int accountNumber) {
        Wallet wallet = getWalletByAccountNumber(accountNumber).get();
        return wallet.getTransactionList();
    }

    public Wallet withdrawFromWallet(int accountNumber, double amount) {
        if (amount <= 0) {
            throw new InvalidRequestException("Invalid amount to withdraw");
        }
        Wallet wallet = getWalletByAccountNumber(accountNumber).get();
        if (wallet.getBalance() < amount) {
            throw new InvalidRequestException("Insufficient balance to withdraw");
        }
        wallet.setBalance(wallet.getBalance() - amount);

        Transaction transaction = createTransaction(wallet, amount, "WITHDRAW");
        transactionRepository.save(transaction);

        walletRepository.save(wallet);
        return wallet;
    }

    private int generateAccountNumber() {
        int min= 00000001, max= 999999999;
        return (int) (Math.random() * (max - min + 1) + min);
    }

    private Transaction createTransaction(Wallet wallet, double amount, String transactionType) {
        Transaction transaction = new Transaction();
        transaction.setWallet(wallet);
        transaction.setAccountNumber(wallet.getAccountNumber());
        transaction.setAmount(amount);
        transaction.setTransactionType(transactionType);
        transaction.setTimestamp(LocalDateTime.now());
        return transaction;
    }

}
