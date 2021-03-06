package com.giangvu.currencyratechanger.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.giangvu.currencyratechanger.Models.CurrencyModel;
import com.giangvu.currencyratechanger.R;
import com.giangvu.currencyratechanger.Service.CurrencyService;

import java.io.DataOutput;
import java.math.BigDecimal;
import java.math.MathContext;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

public class CurrencyRateChanger extends BaseActivity {

    String tag = "GIANGVU";
    private Spinner SymbolSet, SymbolGet;
    private Button  btnCal;
    private ImageButton btnReverse,btnBack, btnRefresh;
    private TextView txtHistory;
    private EditText edtGetNumber, edtSetNumber;
    private CurrencyModel model;
    private List<CurrencyModel> currencies;
    private ArrayAdapter adapter;
    private int positionGet;
    private int positionSet;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_currency_rate_changer);
        Log.d(tag, "Hello");
        Intent intent = getIntent();
        Bundle bundle = intent.getBundleExtra("data");
        model = (CurrencyModel) bundle.getSerializable("currency");
        initView();
        loadData();
    }

    private void initView() {
        SymbolSet = findViewById(R.id.SymbolSet);
        SymbolGet = findViewById(R.id.SymbolGet);
        btnBack = (ImageButton) findViewById(R.id.btnBack);
        btnCal = (Button) findViewById(R.id.btnCal);
        btnRefresh = (ImageButton) findViewById(R.id.btnRefresh);
        btnReverse = (ImageButton) findViewById(R.id.btnReverse);
        txtHistory = (TextView) findViewById(R.id.txtHistory);
        txtHistory.setMovementMethod(new ScrollingMovementMethod());
        edtGetNumber = (EditText) findViewById(R.id.edtGetNumber);
        edtSetNumber = (EditText) findViewById(R.id.edtSetNumber);
        SymbolGet.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                positionGet = position;
                SymbolGet.setSelection(positionGet);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        SymbolSet.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                positionSet = position;
                SymbolSet.setSelection(positionSet);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        btnCal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ExchangeCurrencies();
            }
        });
        btnReverse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ReverseSpinner();
            }
        });
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

    private void loadData() {
        showDialogLoading(getResources().getString(R.string.processDialog), false);
        CurrencyService.getInstance().getCurrenciesFromRss("https://all.fxexchangerate.com/rss.xml", new CurrencyService.CurrencyServiceListener() {

            @Override
            public void onGetCurrencyFromRssSuccess(List<CurrencyModel> currencyModels) {
                timerDelayRemoveDialog(1000);
                currencies = currencyModels;
//                cancleDialogLoading();
                ArrayList<String> arrayName = new ArrayList<String>();
                for (int i = 0; i < currencies.size(); ++i)
                    arrayName.add(currencies.get(i).getSymbol());
                adapter = new ArrayAdapter(CurrencyRateChanger.this, android.R.layout.simple_list_item_1, arrayName);
                SymbolSet.setAdapter(adapter);
                SymbolGet.setAdapter(adapter);
                //136 la vi tri cua VND
                SymbolSet.setSelection(136);
                positionSet = 136;
                // model bien truyen ban dau
                positionGet = adapter.getPosition(model.getSymbol());
                SymbolGet.setSelection(positionGet);
            }

            @Override
            public void onGetCurrencyFromRssFail(String error) {
                cancleDialogLoading();
                Toast.makeText(CurrencyRateChanger.this, getResources().getString(R.string.errorDialog), Toast.LENGTH_SHORT).show();
                Log.d(tag, "onGetCurrencyFromRssFail: " + error);
            }
        });
    }

    private void ExchangeCurrencies() {
        String regex = "^-[\\.][0-9]|^-$|^-[\\.]$|^[\\.]$|^[\\.][0-9]|[0-9][\\.]$|^-[0]$";
        String inputText = edtGetNumber.getText().toString();
        if (inputText.trim().isEmpty() || inputText.matches(regex)) {
            Toast.makeText(getApplicationContext(), getResources().getString(R.string.invalidNumber), Toast.LENGTH_SHORT).show();
        } else {
            double input = Double.parseDouble(inputText);
            CurrencyModel currentGet = (CurrencyModel) currencies.get(adapter.getPosition(SymbolGet.getSelectedItem().toString()));
            CurrencyModel currentSet = (CurrencyModel) currencies.get(adapter.getPosition(SymbolSet.getSelectedItem().toString()));
            BigDecimal newValue = new BigDecimal(input / currentGet.getRate() * currentSet.getRate(), MathContext.DECIMAL64);
            DecimalFormat df = new DecimalFormat("#,###,###,##0.00");
            String newValueString = df.format(newValue);
            edtSetNumber.setText(newValueString);
            String getHistory = txtHistory.getText().toString();
            String history = df.format(input) + " " + currentGet.getSymbol() + " = " + newValueString + " " + currentSet.getSymbol() + "\n" + getHistory;
            txtHistory.setText(history);
        }

    }

    private void ReverseSpinner() {
        int temp = positionGet;
        positionGet = positionSet;
        positionSet = temp;
        SymbolGet.setSelection(positionGet);
        SymbolSet.setSelection(positionSet);
    }

}