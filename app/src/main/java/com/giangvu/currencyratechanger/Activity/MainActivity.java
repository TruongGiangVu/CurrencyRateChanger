package com.giangvu.currencyratechanger.Activity;

import com.giangvu.currencyratechanger.Models.CurrencyModel;
import com.giangvu.currencyratechanger.Service.CurrencyService;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.view.MenuItemCompat;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Adapter;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.Toast;

import com.giangvu.currencyratechanger.R;

import java.util.ArrayList;
import java.util.List;


public class MainActivity extends BaseActivity {

    private String tag="GIANGVU";
    private ListView listview ;
    private ConstraintLayout parent;
    private List<CurrencyModel> currencyList;
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
        getMenuInflater().inflate(R.menu.main_menu,menu);

        MenuItem searchItem = menu.findItem(R.id.searchNameInput);
        searchView= (SearchView) MenuItemCompat.getActionView(searchItem);
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
    private void initView(){
        parent =  findViewById(R.id.layout_parent);
        listview = (ListView) findViewById(R.id.listCurrency);

    }

    private void loadData(){
        showDialogLoading("Please waiting for dowload data from server",false);
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
    private List<String> SearchData(String text){
        ArrayList<String> arrayName = new ArrayList<String>();
        if(!text.trim().equals("")){
            for(int i=0;i<currencyList.size();++i)
                if(currencyList.get(i).getName().toLowerCase().indexOf(text.toLowerCase())>=0)
                    arrayName.add(currencyList.get(i).getName()+" ("+currencyList.get(i).getSymbol()+")");
        }else{
            for(int i=0;i<currencyList.size();++i)
                arrayName.add(currencyList.get(i).getName()+" ("+currencyList.get(i).getSymbol()+")");
        }
        return  arrayName;
    }
    private void DisplayData(String text){
        List<String> arrayName = SearchData(text);
        adapter =new ArrayAdapter(MainActivity.this, android.R.layout.simple_list_item_1, arrayName);
        listview.setAdapter(adapter);
    }

}
