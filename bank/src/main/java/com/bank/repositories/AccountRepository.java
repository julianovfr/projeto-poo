package com.bank.repositories;

import com.bank.exception.AccountNotFoundException;
import com.bank.exception.PixInUseException;
import com.bank.models.AccountWallet;
import com.bank.models.BankService;

import java.util.List;

import static com.bank.repositories.CommonsRepository.checkFundsForTransaction;

public class AccountRepository {

    private List<AccountWallet> accounts;

    public AccountWallet create(final List<String> pix, final long initialFunds){
        var pixInUse  = accounts.stream().flatMap(a -> a.getPix().stream()).toList();
        for (var p: pix){
            if(pixInUse.contains(p)){
                throw new PixInUseException("o pix "+p+"já está em uso");
            }
        }

        var newAccount = new AccountWallet(initialFunds, pix);
        accounts.add(newAccount);

        return newAccount;
    }

    public void deposite(final String pix, final long amount){
        var target  = findByPix(pix);
        target.addMoney(amount, "deposito");
    }

    public long withdraw(final String pix, final long amount){
        var source = findByPix(pix);
        checkFundsForTransaction(source, amount);
        source.reduceMoney(amount);

        return amount;
    }

    public void transferMoney(final String sourcePix, final String targetPix, final long amount){
        var source = findByPix(sourcePix);
        checkFundsForTransaction(source, amount);
        var target = findByPix(targetPix);
        var message = "pix enviado de "+sourcePix+" para "+ targetPix;
        target.addMoney(source.reduceMoney(amount), BankService.ACCOUNT, message);
    }



    public AccountWallet findByPix(final String pix){
        return accounts.stream().filter(a -> a.getPix().contains(pix))
                .findFirst()
                .orElseThrow(()-> new AccountNotFoundException("A conta com chave pix: "+pix+" nao existe ou foi encerrada"));
    }
}
