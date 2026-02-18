package com.nadim.wallet.controller;

import com.nadim.wallet.dto.*;
import com.nadim.wallet.entity.Wallet;
import com.nadim.wallet.service.WalletService;

import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1")
public class WalletController {

    private final WalletService service;

    public WalletController(WalletService service) {
        this.service = service;
    }

    @PostMapping("/wallet/create")
    public ResponseEntity<ApiResponse<WalletResponse>> createWallet() {

        Wallet wallet = service.createWallet();

        WalletResponse response = new WalletResponse(wallet.getId(), wallet.getBalance());

        return ResponseEntity
                .created(URI.create("/api/v1/wallets/" + wallet.getId()))
                .body(new ApiResponse<>(true, "Wallet created successfully", response));
    }

    @PostMapping("/wallet")
    public ResponseEntity<ApiResponse<WalletResponse>> process(
            @Valid @RequestBody WalletRequest request) {

        Wallet wallet = service.processOperation(
                request.getWalletId(),
                request.getOperationType(),
                request.getAmount());

        WalletResponse response = new WalletResponse(wallet.getId(), wallet.getBalance());

        return ResponseEntity.ok(
                new ApiResponse<>(true, "Operation successful", response));
    }

    @GetMapping("/wallets/{id}")
    public ResponseEntity<ApiResponse<WalletResponse>> getWallet(
            @PathVariable UUID id) {

        Wallet wallet = service.getWallet(id);

        WalletResponse response = new WalletResponse(wallet.getId(), wallet.getBalance());

        return ResponseEntity.ok(
                new ApiResponse<>(true, "Wallet fetched successfully", response));
    }
}
