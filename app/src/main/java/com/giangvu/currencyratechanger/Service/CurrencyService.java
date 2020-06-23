package com.giangvu.currencyratechanger.Service;

import android.util.Log;

import com.giangvu.currencyratechanger.Models.CurrencyModel;
import com.giangvu.currencyratechanger.Models.RssModel;

import java.util.ArrayList;
import java.util.List;

public class CurrencyService extends RssService {
    String tag="GIANGVU";
    public static CurrencyService instance;
    public static CurrencyService getInstance() {
        if(instance==null)
            instance= new CurrencyService();
        return instance;
    }

    public void getCurrenciesFromRss(String url, final CurrencyServiceListener currencyServiceListener){
        final List<CurrencyModel> currencyModels = new ArrayList<>();
        RssReader(url, new RssManagerListener(){

            @Override
            public void onLoadRssSuccess(List<RssModel> rssModels) {
                int count =0;
                for(RssModel rss: rssModels){
                    String title = rss.getTitle();
                    String description = rss.getDescription();
//                    <title>Albanian Lek(ALL)/Aruba Florin(AWG)</title>
//                    <description>1 Albanian Lek = 0.01626 Aruba Florin</description>
                    int position =description.indexOf("=");
                    if(position >0){
                        String symbol = title.substring(title.length()-4,title.length()-1);
                        String temp =description.substring(position+1).trim();
                        String[] spilit = temp.split(" ");
                        double rate= Float.parseFloat(spilit[0]);
                        String name="";
                        for(int i=1;i< spilit.length;++i){
                            name += spilit[i];
                        }
                        CurrencyModel model = new CurrencyModel(name,rate,symbol);
//                        Log.d(tag,count+" "+ model.toString());
                        currencyModels.add(model);
                        count++;
                    }
                }
                currencyServiceListener.onGetCurrencyFromRssSuccess(currencyModels);
            }

            @Override
            public void onLoadRssFail(String error) {
                currencyServiceListener.onGetCurrencyFromRssFail(error);
            }
        });
    }
    public interface CurrencyServiceListener{
        void onGetCurrencyFromRssSuccess(List<CurrencyModel> currencyModels);
        void onGetCurrencyFromRssFail(String error);
    }
}
