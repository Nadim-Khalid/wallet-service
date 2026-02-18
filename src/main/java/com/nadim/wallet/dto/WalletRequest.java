package com.nadim.wallet.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.util.UUID;

public class WalletRequest {

    @NotNull(message = "Wallet ID is required")
    private UUID walletId;

    @NotBlank(message = "Operation type is required")
    private String operationType;

    @NotNull(message = "Amount is required")
    @Positive(message = "Amount must be greater than zero")
    private Long amount;

    // ✅ Default constructor (needed for JSON)
    public WalletRequest() {
    }

    // ✅ Constructor for tests
    public WalletRequest(UUID walletId, String operationType, Long amount) {
        this.walletId = walletId;
        this.operationType = operationType;
        this.amount = amount;
    }

    // Getters
    public UUID getWalletId() {
        return walletId;
    }

    public String getOperationType() {
        return operationType;
    }

    public Long getAmount() {
        return amount;
    }

    // Setters
    public void setWalletId(UUID walletId) {
        this.walletId = walletId;
    }

    public void setOperationType(String operationType) {
        this.operationType = operationType;
    }

    public void setAmount(Long amount) {
        this.amount = amount;
    }
}
