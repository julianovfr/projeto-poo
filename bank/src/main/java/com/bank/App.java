package com.bank;

import com.bank.exception.AccountNotFoundException;
import com.bank.exception.NoFundsEnoughException;
import com.bank.models.AccountWallet;
import com.bank.repositories.AccountRepository;
import com.bank.repositories.InvestimentRepository;

import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.Arrays;
import java.util.Scanner;
import java.util.stream.Collectors;

/**
 * Hello world!
 *
 */
public class App 
{

    private final static  AccountRepository accountRepository = new AccountRepository();
    private final static InvestimentRepository investimentRepository = new InvestimentRepository();

    static Scanner scanner = new Scanner(System.in);

    public static void main( String[] args )
    {
        System.out.println("Ola seja bem vindo ao nosso Bank");

        while(true){
            System.out.println("Selecione uma opcao");
            System.out.println("1 - Criar uma conta");
            System.out.println("2 - Criar um investimento");
            System.out.println("3 - Fazer um investimento");
            System.out.println("4 - Depositar na conta");
            System.out.println("5 - Sacar da conta");
            System.out.println("6 - Transferencia entre contas");
            System.out.println("7 - Investir ");
            System.out.println("8 - Sacar Investimento");
            System.out.println("9 - Listar contas");
            System.out.println("10 - Listar Investimentos");
            System.out.println("11 - Listas Carteiras de Investimento");
            System.out.println("12 - Atualizar Investimentos");
            System.out.println("13 - Histórico de conta");
            System.out.println("14 - Sair");

            var option = scanner.nextInt();

            switch (option) {
                case 1: createAccount();
                case 2: createInvestiment();
                case 3: createWalletInvestiment();
                case 4: deposite();
                case 5: withdraw();
                case 6: transferToAccount();
                case 7: investiment();
                case 8: rescueInvestiment();
                case 9: accountRepository.list().forEach(System.out::println);
                case 10:investimentRepository.list().forEach(System.out::println);
                case 11: investimentRepository.listWallet().forEach(System.out::println);
                case 12: {
                    investimentRepository.updateAmount();
                    System.out.println("Investimentos reajustados");
                }
                case 13:
                case 14: System.exit(0);
                default: System.out.println("Opcao invalida");
            }

        }

    }

    private static void createAccount(){
        System.out.println("Informe as chaves pix (separadas por ';'");
        var pix = Arrays.stream(scanner.next().split(";")).toList();
        System.out.println("Informe o valor inicial de deposito");
        var amount = scanner.nextLong();
        accountRepository.create(pix, amount);
    }

    private static void createInvestiment(){
        System.out.println("Informe a taxa do investimento");
        var tax = scanner.nextInt();
        System.out.println("Informe o valor inicial de deposito");
        var initialFunds = scanner.nextLong();
        investimentRepository.create(tax, initialFunds);
    }

    private static void deposite(){
        System.out.println("Informe a chave pix da conta para deposito");
        var pix = scanner.next();
        System.out.println("Informe o valor que será depositado: ");
        var amount = scanner.nextLong();
        try {
            accountRepository.deposite(pix, amount);
        }catch(AccountNotFoundException e){
            System.out.println(e.getMessage());

        }
    }

    private static void transferToAccount(){
        System.out.println("Informe a chave pix da conta de origem");
        var source = scanner.next();
        System.out.println("Informe a chave pix da conta de destino");
        var target = scanner.next();
        System.out.println("Informe o valor que será depositado: ");
        var amount = scanner.nextLong();

        try{
            accountRepository.transferMoney(source,target, amount);
        }catch(NoFundsEnoughException e){
            System.out.println(e.getMessage());
        }
    }

    private static void withdraw(){
        System.out.println("Informe a chave pix da conta para saque: ");
        var pix = scanner.next();
        System.out.println("Informe o valor que será depositado: ");
        var amount = scanner.nextLong();
        try {
            accountRepository.deposite(pix, amount);
        } catch(NoFundsEnoughException | AccountNotFoundException e){
            System.out.println(e.getMessage());
        }
    }

    private static void createWalletInvestiment(){
        System.out.println("Informe a chave pix da conta: ");
        var source = scanner.next();
        var account = accountRepository.findByPix(source);
        System.out.println("Informe o identificador do investimento");
        var investimentId = scanner.nextInt();
        var investimentWallet = investimentRepository.initInvestiment(account, investimentId);

        System.out.println("Conta de investimento criada: "+investimentWallet);
    }

    private static void investiment(){
        System.out.println("Informe a chave pix da conta para investimento");
        var pix = scanner.next();
        System.out.println("Informe o valor que será investido: ");
        var amount = scanner.nextLong();
        try {
            investimentRepository.deposite(pix, amount);
        }catch(AccountNotFoundException e){
            System.out.println(e.getMessage());
        }
    }

    private static void rescueInvestiment(){
        System.out.println("Informe a chave pix da conta para o resgate do investimento: ");
        var pix = scanner.next();
        System.out.println("Informe o valor que será resgatado: ");
        var amount = scanner.nextLong();
        try {
            investimentRepository.deposite(pix, amount);
        } catch(NoFundsEnoughException | AccountNotFoundException e){
            System.out.println(e.getMessage());
        }
    }

    private static void checkHistory(){
        System.out.println("Informe a chave pix da conta para verificar extrato: ");
        var pix = scanner.next();
        AccountWallet wallet;
        try{
            var sorted = accountRepository.getHistory(pix);
            sorted.forEach((k,v)-> {
                System.out.println(k.format(DateTimeFormatter.ISO_DATE_TIME));
                System.out.println(v.get(0).transactionId());
                System.out.println(v.get(0).description());
                System.out.println(v.size());
            });
        }catch(AccountNotFoundException e){
            System.out.println(e.getMessage());
        }


    }
}
