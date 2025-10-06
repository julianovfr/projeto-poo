package com.bank.repositories;

import com.bank.exception.AccountWithInvestimentException;
import com.bank.exception.InvestimentNotFoundException;
import com.bank.exception.WalletNotFoundException;
import com.bank.models.AccountWallet;
import com.bank.models.Investiment;
import com.bank.models.InvestimentWallet;

import java.util.ArrayList;
import java.util.List;

import static com.bank.repositories.CommonsRepository.checkFundsForTransaction;

public class InvestimentRepository {

    private long nextId;
    private final List<Investiment> investiments = new ArrayList<>();
    private final List<InvestimentWallet> wallets = new ArrayList<>();

    public Investiment create(final long tax, final long daysToRescue,final long initialFunds){
        this.nextId ++;
        var investiment = new Investiment(this.nextId, tax, initialFunds);
        investiments.add(investiment);

        return investiment;
    }

    public InvestimentWallet initInvestiment(final AccountWallet account, final long id){
        var accountInUse = wallets.stream().map(InvestimentWallet::getAccount   ).toList();
        if(accountInUse.contains(account)){
            throw new AccountWithInvestimentException("A conta "+account+" já possui um investimento");
        }


        var investiment = findById(id);
        checkFundsForTransaction(account, investiment.initialFounds());
        var wallet = new InvestimentWallet(investiment, account, investiment.initialFounds());
        wallets.add(wallet);

        return wallet;
    }

    public InvestimentWallet deposite(final String pix, final long funds){
        var wallet = findWalletByAccountPix(pix);
        wallet.addMoney(wallet.getAccount().reduceMoney(funds), wallet.getServiceType(), "Investimento");

        return wallet;
    }

    public InvestimentWallet withdraw(final String pix, final long funds){
        var wallet = findWalletByAccountPix(pix);
        checkFundsForTransaction(wallet, funds);

        wallet.getAccount().addMoney(wallet.reduceMoney(funds), wallet.getServiceType(), "saque de investimentos");
        if(wallet.getFunds() == 0){
            wallets.remove(wallet);
        }

        return wallet;

    }

    public void updateAmount(final long percent){
        wallets.forEach(w -> w.updateAmount(percent));
    }

    public Investiment findById(final long id){
        return investiments.stream().filter(a -> a.id() == id)
                .findFirst()
                .orElseThrow(()-> new InvestimentNotFoundException("o investimento de '"+id+"' nao foi enontrado"));
    }

    public InvestimentWallet findWalletByAccountPix(final String pix){
        return wallets.stream()
                .filter(w -> w.getAccount().getPix().contains(pix))
                        .findFirst()
                .orElseThrow(()-> new WalletNotFoundException("A carteira nao foi encontrada"));
    }

    public List<InvestimentWallet> listWallet(){
        return this.wallets;
    }

    public List<Investiment> list(){
        return this.investiments;
    }
}
