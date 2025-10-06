package com.bank.repositories;

import com.bank.exception.NoFundsEnoughException;
import com.bank.models.AccountWallet;
import com.bank.models.Money;
import com.bank.models.MoneyAudit;
import com.bank.models.Wallet;
import lombok.NoArgsConstructor;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.UUID;
import java.util.stream.Stream;

import static com.bank.models.BankService.ACCOUNT;
import static lombok.AccessLevel.PRIVATE;

@NoArgsConstructor(access = PRIVATE)
public final class CommonsRepository {

    public static void checkFundsForTransaction(final Wallet source, final long amount){
        if(source.getFunds() < amount){
            throw new NoFundsEnoughException("Sua conta nao tem dinheiro suficiente para realizar essa transacao");
        }
    }

    public static List<Money> generateMoney(final UUID transactionId, final long funds, final String descrition){
        var history = new MoneyAudit(transactionId, ACCOUNT, descrition, OffsetDateTime.now());

        return Stream.generate(() -> new Money(history)).limit(funds).toList();
    }
}
