package com.nadim.wallet.service;

import com.nadim.wallet.entity.Wallet;
import com.nadim.wallet.exception.*;
import com.nadim.wallet.repository.WalletRepository;

import org.springframework.orm.ObjectOptimisticLockingFailureException;
import org.springframework.retry.annotation.Retryable;
import org.springframework.retry.annotation.Backoff;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
public class WalletService {

    private final WalletRepository repository;

    public WalletService(WalletRepository repository) {
        this.repository = repository;
    }

    // Create wallet
    public Wallet createWallet() {
        Wallet wallet = new Wallet(UUID.randomUUID(), 0L);
        return repository.save(wallet);
    }

    // Unified operation method (PDF compliant)
    @Transactional
    @Retryable(value = ObjectOptimisticLockingFailureException.class, maxAttempts = 5, backoff = @Backoff(delay = 50))
    public Wallet processOperation(UUID walletId, String operationType, Long amount) {

        Wallet wallet = repository.findById(walletId)
                .orElseThrow(() -> new WalletNotFoundException("Wallet not found"));

        switch (operationType.toUpperCase()) {

            case "DEPOSIT":
                wallet.setBalance(wallet.getBalance() + amount);
                break;

            case "WITHDRAW":
                if (wallet.getBalance() < amount) {
                    throw new InsufficientBalanceException("Insufficient balance");
                }
                wallet.setBalance(wallet.getBalance() - amount);
                break;

            default:
                throw new IllegalArgumentException("Invalid operation type");
        }

        return repository.save(wallet);
    }

    // Get wallet by id
    public Wallet getWallet(UUID id) {
        return repository.findById(id)
                .orElseThrow(() -> new WalletNotFoundException("Wallet not found"));
    }

}
