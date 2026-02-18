package com.nadim.wallet;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.nadim.wallet.dto.WalletRequest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import java.util.UUID;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
class WalletControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void shouldCreateWallet() throws Exception {

        mockMvc.perform(post("/api/v1/wallet/create"))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data.id").exists())
                .andExpect(jsonPath("$.data.balance").value(0));
    }

    @Test
    void shouldDepositMoney() throws Exception {

        // Create wallet first
        String response = mockMvc.perform(post("/api/v1/wallet/create"))
                .andReturn()
                .getResponse()
                .getContentAsString();

        String walletId = objectMapper.readTree(response)
                .path("data")
                .path("id")
                .asText();

        WalletRequest request = new WalletRequest(
                UUID.fromString(walletId),
                "DEPOSIT",
                500L);

        mockMvc.perform(post("/api/v1/wallet")
                .contentType("application/json")
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.balance").value(500));
    }

    @Test
    void shouldWithdrawMoney() throws Exception {

        String response = mockMvc.perform(post("/api/v1/wallet/create"))
                .andReturn()
                .getResponse()
                .getContentAsString();

        String walletId = objectMapper.readTree(response)
                .path("data")
                .path("id")
                .asText();

        // Deposit first
        WalletRequest deposit = new WalletRequest(
                UUID.fromString(walletId),
                "DEPOSIT",
                500L);

        mockMvc.perform(post("/api/v1/wallet")
                .contentType("application/json")
                .content(objectMapper.writeValueAsString(deposit)));

        // Withdraw
        WalletRequest withdraw = new WalletRequest(
                UUID.fromString(walletId),
                "WITHDRAW",
                200L);

        mockMvc.perform(post("/api/v1/wallet")
                .contentType("application/json")
                .content(objectMapper.writeValueAsString(withdraw)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.balance").value(300));
    }

    @Test
    void shouldFailOnInvalidOperation() throws Exception {

        String response = mockMvc.perform(post("/api/v1/wallet/create"))
                .andReturn()
                .getResponse()
                .getContentAsString();

        String walletId = objectMapper.readTree(response)
                .path("data")
                .path("id")
                .asText();

        WalletRequest request = new WalletRequest(
                UUID.fromString(walletId),
                "TRANSFER",
                100L);

        mockMvc.perform(post("/api/v1/wallet")
                .contentType("application/json")
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest());
    }
}
