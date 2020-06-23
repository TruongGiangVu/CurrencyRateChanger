package com.giangvu.currencyratechanger.Service;
import com.giangvu.currencyratechanger.Models.RssModel;

import android.os.AsyncTask;
import android.text.TextUtils;
import android.util.Log;
import android.util.Xml;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class RssService {
    private String tag= "GIANGVU";
    private RssReaderAsync asyncRssReader;

    public void RssReader(String url,RssManagerListener rssManagerListener){
        asyncRssReader = new RssReaderAsync(url,rssManagerListener);
        asyncRssReader.execute();
    }

    private class RssReaderAsync extends AsyncTask<Void, Void, String> {

        private String urlLink;
        private List<RssModel> rssList;
        private RssManagerListener rssManagerListener;
        public RssReaderAsync(String urlLink, RssManagerListener rssManagerListener){
            this.urlLink = urlLink;
            this.rssManagerListener = rssManagerListener;
        }
        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            Log.d(tag,"Start read RSS");
        }

        @Override
        protected String doInBackground(Void... voids) {
            if (TextUtils.isEmpty(urlLink))
                return "Empty Link";
            try{
                if(!urlLink.startsWith("https://") && !urlLink.startsWith("http://"))
                    urlLink = "http://"+ urlLink;
                URL url = new URL((urlLink));
                InputStream inputStream = url.openConnection().getInputStream();
                rssList = parseXml(inputStream);
                Log.d(tag, "doInBackground: "+ rssList.size());
                return "";
            }
            catch(IOException | XmlPullParserException e){
                return e.toString();
            }
        }

        @Override
        protected void onPostExecute(String result) {
//            super.onPostExecute(result);
            if (result.equals("")) {
                rssManagerListener.onLoadRssSuccess(rssList);
            } else {
                rssManagerListener.onLoadRssFail(result);
            }
        }
    }

    protected List<RssModel> parseXml(InputStream inputStream) throws XmlPullParserException,
            IOException  {
        List<RssModel> items = new ArrayList<>();
        String title= null;
        String link = null;
        String description = null;

        try{
            XmlPullParser xmlPullParser = Xml.newPullParser();
            xmlPullParser.setFeature(XmlPullParser.FEATURE_PROCESS_NAMESPACES, false);
            xmlPullParser.setInput(inputStream, null);

            xmlPullParser.nextTag();
            while (xmlPullParser.next() != XmlPullParser.END_DOCUMENT){
                String name =xmlPullParser.getName();
                if(name == null)
                    continue;
                String result="";
                if(xmlPullParser.next() == XmlPullParser.TEXT){
                    result = xmlPullParser.getText();
                    xmlPullParser.nextTag();
                }

                if (name.equalsIgnoreCase("title")) {
                    title = result;
                } else if (name.equalsIgnoreCase("link")) {
                    link = result;
                } else if (name.equalsIgnoreCase("description")) {
                    description = result;
                }

                if (title != null && link != null && description != null) {
                    RssModel item = new RssModel(title, link, description);
                    items.add(item);
                    title = null;
                    link = null;
                    description = null;
                }
            }
            return items;
        }finally {
            inputStream.close();
        }
    }
    public interface RssManagerListener{
        void onLoadRssSuccess(List<RssModel> rssModels);
        void onLoadRssFail(String error);
    }
}
