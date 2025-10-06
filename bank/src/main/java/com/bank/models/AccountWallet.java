package com.bank.models;

import lombok.Getter;

import java.util.Collection;
import java.util.List;


public class AccountWallet extends Wallet{

    @Getter
    private final List<String> pix;


    public AccountWallet(List<String> pix) {
        super(BankService.ACCOUNT);
        this.pix = pix;
    }

    public AccountWallet(final long amount, List<String> pix) {
        super(BankService.ACCOUNT);
        this.pix = pix;
        addMoney(amount, "Valor de criacao da conta");
    }

    public void addMoney(final long amount, final String description){
        var money = generateMoney(amount, description);
        this.money.addAll(money);
    }

    public List<String> getPix() {
        return pix;
    }
}
