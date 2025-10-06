package com.bank.models;

import java.time.OffsetDateTime;
import java.util.UUID;

public record MoneyAudit (
        UUID transactionId,
        BankService service,
        String description,
        OffsetDateTime createdAt
        ){

}
