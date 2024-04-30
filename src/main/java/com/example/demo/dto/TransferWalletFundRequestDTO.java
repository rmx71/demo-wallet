package com.example.demo.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

public class TransferWalletFundRequestDTO {

    @NotEmpty
    private int transferToAccountNumber;

    @NotEmpty
    private int transferFromAccountNumber;

    @NotNull
    @Min(1)
    private double amountToTransfer;

    public int getTransferToAccountNumber() {
        return transferToAccountNumber;
    }

    public void setTransferToAccountNumber(int transferToAccountNumber) {
        this.transferToAccountNumber = transferToAccountNumber;
    }

    public double getAmountToTransfer() {
        return amountToTransfer;
    }

    public void setAmountToTransfer(double amountToTransfer) {
        this.amountToTransfer = amountToTransfer;
    }

    public int getTransferFromAccountNumber() {
        return transferFromAccountNumber;
    }

    public void setTransferFromAccountNumber(int transferFromAccountNumber) {
        this.transferFromAccountNumber = transferFromAccountNumber;
    }
}
