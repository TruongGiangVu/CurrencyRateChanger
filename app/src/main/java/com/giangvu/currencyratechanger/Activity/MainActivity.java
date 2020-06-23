package com.giangvu.currencyratechanger.Activity;

import com.giangvu.currencyratechanger.Models.CurrencyModel;
import com.giangvu.currencyratechanger.Service.CurrencyService;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.view.MenuItemCompat;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.Toast;

import com.giangvu.currencyratechanger.R;

import java.util.ArrayList;
import java.util.List;


public class MainActivity extends BaseActivity {

    private String tag = "GIANGVU";
    private ListView listview;
    private ConstraintLayout parent;
    private List<CurrencyModel> currencyList;
    private List<CurrencyModel> searchList;
    private ArrayAdapter adapter;
    private SearchView searchView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
        loadData();

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);

        MenuItem searchItem = menu.findItem(R.id.searchNameInput);
        searchView = (SearchView) MenuItemCompat.getActionView(searchItem);
        searchView.setQueryHint("search name");
        searchView.setIconifiedByDefault(true);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                DisplayData(query);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                DisplayData(newText);
                return false;
            }
        });
        return super.onCreateOptionsMenu(menu);
    }

    private void initView() {
        parent = findViewById(R.id.layout_parent);
        listview = (ListView) findViewById(R.id.listCurrency);
        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(MainActivity.this, CurrencyRateChanger.class);
                Bundle bundle = new Bundle();
                bundle.putSerializable("currency", searchList.get(position));
                intent.putExtra("data", bundle);
                startActivity(intent);
            }
        });
    }

    private void loadData() {
        showDialogLoading("Please waiting for dowload data from server", false);
        CurrencyService.getInstance().getCurrenciesFromRss("https://all.fxexchangerate.com/rss.xml", new CurrencyService.CurrencyServiceListener() {
            @Override
            public void onGetCurrencyFromRssSuccess(List<CurrencyModel> currencyModels) {
                currencyList = currencyModels;
                cancleDialogLoading();
                parent.setVisibility(View.VISIBLE);
                DisplayData("");
            }

            @Override
            public void onGetCurrencyFromRssFail(String error) {
                cancleDialogLoading();
//                btntry.setVisibility(View.VISIBLE);
                Toast.makeText(MainActivity.this, "Errors , please check your internet connection and retry!", Toast.LENGTH_SHORT).show();
                Log.d(tag, "onGetCurrencyFromRssFail: " + error);
            }
        });
    }

    private List<String> SearchData(String text) {
        ArrayList<String> arrayName = new ArrayList<String>();
        searchList = new ArrayList<CurrencyModel>();
        int n = currencyList.size();
        if (!text.trim().equals("")) {
            text = text.toLowerCase();
            for (int i = 0; i < n; ++i)
                if (currencyList.get(i).getName().toLowerCase().indexOf(text) >= 0
                        || currencyList.get(i).getSymbol().toLowerCase().indexOf(text) >= 0) {
                    searchList.add(currencyList.get(i));
                }
        } else {
            searchList = currencyList;
        }
        for (int i = 0; i < searchList.size(); ++i)
            arrayName.add(searchList.get(i).getName() + " (" + searchList.get(i).getSymbol() + ")");
        return arrayName;
    }

    private void DisplayData(String text) {
        List<String> arrayName = SearchData(text);
        adapter = new ArrayAdapter(MainActivity.this, android.R.layout.simple_list_item_1, arrayName);
        listview.setAdapter(adapter);
    }

}
