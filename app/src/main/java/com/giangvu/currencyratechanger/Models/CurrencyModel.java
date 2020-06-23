package com.giangvu.currencyratechanger.Models;

import java.io.Serializable;

public class CurrencyModel implements Serializable {
    private String Name;
    private double Rate;
    private String Symbol;
    public CurrencyModel(){}

    public CurrencyModel(String name, double rate, String symbol) {
        Name = name;
        Rate = rate;
        Symbol = symbol;
    }

    @Override
    public String toString() {
        return "CurrencyModel{" +
                "Name='" + Name + '\'' +
                ", Rate=" + Rate +
                ", Symbol='" + Symbol + '\'' +
                '}';
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public double getRate() {
        return Rate;
    }

    public void setRate(double rate) {
        Rate = rate;
    }

    public String getSymbol() {
        return Symbol;
    }

    public void setSymbol(String symbol) {
        Symbol = symbol;
    }
}
