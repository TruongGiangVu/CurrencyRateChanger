package com.giangvu.currencyratechanger.Activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.giangvu.currencyratechanger.Models.CurrencyModel;
import com.giangvu.currencyratechanger.R;
import com.giangvu.currencyratechanger.Service.CurrencyService;

import java.math.BigDecimal;
import java.math.MathContext;
import java.util.ArrayList;
import java.util.List;

public class CurrencyRateChanger extends BaseActivity {

    String tag="GIANGVU";
    private Spinner SymbolSet;
    private Button btnBack, btnRefresh, btnCal;
    private TextView txtHistory, txtSymbolGet;
    private EditText edtGetNumber, edtSetNumber;
    private CurrencyModel model;
    private List<CurrencyModel> currencies;
    private ArrayAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_currency_rate_changer);
        Log.d(tag,"Hello");

        Intent intent = getIntent();
        Bundle bundle = intent.getBundleExtra("data");
        model = (CurrencyModel) bundle.getSerializable("currency");
        initView();
        loadData();
    }

    private void initView(){
        SymbolSet = findViewById(R.id.SymbolSet);
        txtSymbolGet = (TextView) findViewById(R.id.SymbolGet);
        btnBack =(Button) findViewById(R.id.btnBack);
        btnCal =(Button) findViewById(R.id.btnCal);
        btnRefresh =(Button) findViewById(R.id.btnRefresh);
        txtHistory = (TextView) findViewById(R.id.txtHistory);
        edtGetNumber = (EditText) findViewById(R.id.edtGetNumber);
        edtSetNumber =(EditText) findViewById(R.id.edtSetNumber);

        txtSymbolGet.setText(model.getSymbol());
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(CurrencyRateChanger.this, MainActivity.class));
            }
        });
        btnRefresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadData();
            }
        });
    }
    private void loadData(){
        showDialogLoading("Please waiting for dowload data from server",false);
        CurrencyService.getInstance().getCurrenciesFromRss("https://all.fxexchangerate.com/rss.xml", new CurrencyService.CurrencyServiceListener() {
            @Override
            public void onGetCurrencyFromRssSuccess(List<CurrencyModel> currencyModels) {
                currencies = currencyModels;
                cancleDialogLoading();
                ArrayList<String> arrayName = new ArrayList<String>();
                for(int i=0;i<currencies.size();++i)
                    arrayName.add(currencies.get(i).getSymbol());
                adapter = new ArrayAdapter(CurrencyRateChanger.this, android.R.layout.simple_list_item_1, arrayName);
                SymbolSet.setAdapter(adapter);
                //136 la vi tri cua VND
                SymbolSet.setSelection(136);
            }

            @Override
            public void onGetCurrencyFromRssFail(String error) {
                cancleDialogLoading();
                Toast.makeText(CurrencyRateChanger.this, "Errors , please check your internet connection and retry!", Toast.LENGTH_SHORT).show();
                Log.d(tag, "onGetCurrencyFromRssFail: " + error);
            }
        });
    }
    private void exchangeCurrencies(EditText editTextSetValues,EditText editTextGetValues,CurrencyModel selectedCurrencyGet,CurrencyModel selectedCurrencySet){
        if(selectedCurrencyGet!=null && selectedCurrencySet!=null) {
            double currentRateGet = selectedCurrencyGet.getRate();
            double currentRateSet = selectedCurrencySet.getRate();
            BigDecimal newValue = new BigDecimal(Float.parseFloat(editTextGetValues.getText().toString()) / currentRateGet * currentRateSet, MathContext.DECIMAL64);
//            tvcongthuc.setText(editTextGetValues.getText().toString() + "  "+selectedCurrencyGet.getName());
//            tvcongthuc2.setText(newValue+"  "+selectedCurrencySet.getName());
//            editTextSetValues.setText(newValue + "");
        }

    }
}