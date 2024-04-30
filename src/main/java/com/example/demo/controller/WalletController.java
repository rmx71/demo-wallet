package com.example.demo.controller;

import com.example.demo.dto.TransferWalletFundRequestDTO;
import com.example.demo.model.Transaction;
import com.example.demo.model.Wallet;
import com.example.demo.service.WalletService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api")
public class WalletController {

    @Autowired
    WalletService walletService;

    @Tag(name = "Create Wallet", description = "Create Wallet Api")
    @Operation(
            summary = "Create Wallet feature",
            description = "Create Wallet. This API creates a new wallet with a unique account number.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully created wallet")
    })
    @PostMapping("/wallet")
    public ResponseEntity<Wallet> createWallet() {
        Wallet wallet = walletService.createWallet();
        return ResponseEntity.ok(wallet);
    }

    @Tag(name = "Check Wallet Balance", description = "API to check wallet balance")
    @Operation(
            summary = "Check wallet balance feature",
            description = "Check wallet balance. This API returns the balance of a wallet.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved wallet balance"),
            @ApiResponse(responseCode = "404", description = "Wallet not found")
    })
    @GetMapping("/wallet/{accountNumber}")
    public ResponseEntity<Wallet> checkBalance(@PathVariable String accountNumber) {
        Wallet wallet = walletService.checkBalance(Integer.parseInt(accountNumber));
        return ResponseEntity.ok(wallet);
    }

    @Tag(name = "Transfer to Wallet", description = "API to check wallet balance")
    @Operation(
            summary = "Transfer to wallet feature",
            description = "Transfer fund from one wallet to another wallet")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully transferred fund"),
            @ApiResponse(responseCode = "404", description = "Wallet not found"),
            @ApiResponse(responseCode = "400", description = "Invalid request")
    })
    @PostMapping("/wallet/transfer")
    public ResponseEntity<Wallet> transferFund(@RequestBody TransferWalletFundRequestDTO transferRequest) {
        Wallet wallet = walletService.updateWalletBalance(transferRequest);
        return ResponseEntity.ok(wallet);
    }

    @Tag(name = "Deposit to Wallet", description = "API to deposit fund to wallet")
    @Operation(
            summary = "Deposit to wallet feature",
            description = "Deposit fund to wallet. This API mimics transferring to a wallet via bank deposit." +
                    " It takes in the account number and amount to be deposited.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully deposited fund"),
            @ApiResponse(responseCode = "404", description = "Wallet not found")
    })
    @PostMapping("/wallet/deposit/{accountNumber}/{amount}")
    public ResponseEntity<Wallet> depositFund(@PathVariable String accountNumber, @PathVariable String amount) {
        Wallet wallet = walletService.depositToWallet(Integer.parseInt(accountNumber), Integer.parseInt(amount));
        return ResponseEntity.ok(wallet);
    }

    @Tag(name = "Get Wallet Transactions", description = "API to get wallet transactions")
    @Operation(
            summary = "Get wallet transactions feature",
            description = "Get wallet transactions. This API returns the transaction history of a wallet.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved wallet transactions"),
            @ApiResponse(responseCode = "404", description = "Wallet not found")
    })
    @GetMapping("/wallet/transactions/{accountNumber}")
    public ResponseEntity<List<Transaction>> getTransactions(@PathVariable String accountNumber) {
        return ResponseEntity.ok(walletService.getTransactionHistory(Integer.parseInt(accountNumber)));
    }

    @Tag(name = "Withdraw from Wallet", description = "API to withdraw fund from wallet")
    @Operation(
            summary = "Withdraw from wallet feature",
            description = "Withdraw fund from wallet. This API mimics withdrawing from a wallet via bank withdrawal." +
                    " It takes in the account number and amount to be withdrawn.")
    @ApiResponses (value = {
            @ApiResponse(responseCode = "200", description = "Successfully withdrawn fund"),
            @ApiResponse(responseCode = "400", description = "Invalid request"),
            @ApiResponse(responseCode = "404", description = "Wallet not found")
    })
    @PostMapping("/wallet/withdraw/{accountNumber}/{amount}")
    public ResponseEntity<Wallet> withdrawFund(@PathVariable String accountNumber, @PathVariable String amount) {
        Wallet wallet = walletService.withdrawFromWallet(Integer.parseInt(accountNumber), Integer.parseInt(amount));
        return ResponseEntity.ok(wallet);
    }
}
