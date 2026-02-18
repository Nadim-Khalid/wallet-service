package com.nadim.wallet.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.nadim.wallet.entity.Wallet;

import java.util.UUID;

public interface WalletRepository extends JpaRepository<Wallet, UUID> {
}
