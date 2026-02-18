package com.nadim.wallet.dto;

import java.util.UUID;

public class WalletResponse {

    private UUID id;
    private Long balance;

    public WalletResponse(UUID id, Long balance) {
        this.id = id;
        this.balance = balance;
    }

    public UUID getId() {
        return id;
    }

    public Long getBalance() {
        return balance;
    }
}
