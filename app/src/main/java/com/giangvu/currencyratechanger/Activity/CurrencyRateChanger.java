package com.giangvu.currencyratechanger.Activity;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;

import com.giangvu.currencyratechanger.R;

public class CurrencyRateChanger extends AppCompatActivity {

    String tag="GIANGVU";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_currency_rate_changer);
        Log.d(tag,"Hello");
    }
}