package com.bank.models;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@EqualsAndHashCode
@ToString
public class Money {

    private final List<MoneyAudit> history = new ArrayList<>();

    public Money(MoneyAudit history){
        this.history.add(history);
    }

    public void addHistory(final MoneyAudit history){
        this.history.add(history);
    }

    public List<MoneyAudit> getHistory() {
        return history;
    }
}
