package com.example.android.news1;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.text.TextUtils;
import android.util.JsonReader;
import android.util.Log;
import android.view.View;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

public class NewsUtils {

    private static List<News> extractFeatureFromJson(String newsJson){
        if(TextUtils.isEmpty(newsJson)){
            return null;
        }
        List<News> news = new ArrayList<>();
        try {
            JSONObject baseJsonObject = new JSONObject(newsJson);
            JSONObject baseJsonResponse = baseJsonObject.getJSONObject("response");
            JSONArray jsonResults = baseJsonResponse.getJSONArray("results");
            for(int i = 0;i<jsonResults.length();i++){
                JSONObject currentNews = jsonResults.getJSONObject(i);
                String sectionName = currentNews.getString("sectionName");
                String publishedOn = currentNews.getString("webPublicationDate");
                String title = currentNews.getString("webTitle");
                String url = currentNews.getString("webUrl");
                JSONObject fields = currentNews.optJSONObject("fields");
                String imgUrl=null;
                if(fields!=null)
                    imgUrl = fields.optString("thumbnail");
                JSONArray tagsArray = currentNews.getJSONArray("tags");
                JSONObject tagsObject = tagsArray.optJSONObject(0);
                String webTitle = null;
                if(tagsObject != null)
                    webTitle = tagsObject.getString("webTitle");
                news.add(new News(sectionName,title,url,publishedOn,webTitle,imgUrl));
            }
        }catch(JSONException e){
            Log.e("NewsUtils","Error in extracting json : "+e);
        }
        return news;
    }

    public static List<News> fetchNews(String requestUrl){
        URL url = createUrl(requestUrl);
        String jsonResponse = null;
        try {
            jsonResponse = httpRequest(url);
        } catch(IOException e){
            Log.e("NewsUtils","Error in making http request : "+e);
        }
        List<News> news  = extractFeatureFromJson(jsonResponse);
        return news;
    }

    private static String readInputStream(InputStream inputStream) throws IOException {
        StringBuilder outputString = new StringBuilder();
        if (inputStream != null) {
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream, Charset.forName("UTF-8"));
            BufferedReader reader = new BufferedReader(inputStreamReader);
            String line = reader.readLine();
            while (line != null) {
                outputString.append(line);
                line = reader.readLine();
            }
        }
        return outputString.toString();
    }

    private static String httpRequest(URL url) throws IOException {
        String jsonResponse = " ";
        if (url == null) {
            return jsonResponse;
        }
        HttpURLConnection urlConnection = null;
        InputStream inputStream = null;
        try {
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setReadTimeout(10000);
            urlConnection.setConnectTimeout(15000);
            urlConnection.setRequestMethod("GET");
            urlConnection.connect();
            if (urlConnection.getResponseCode() == 200) {
                inputStream = urlConnection.getInputStream();
                jsonResponse = readInputStream(inputStream);
            } else {
                Log.e("NewsUtils", "Response Error  :" + urlConnection.getResponseCode());
            }
        } catch(IOException e) {
            Log.e("NewsUtils","Error in getting Json Response", e );
        } finally {
            if(urlConnection != null){
                urlConnection.disconnect();
            }
            if(inputStream != null){
                inputStream.close();
            }
        }
        return jsonResponse;
    }

    private static URL createUrl(String stringUrl){
        URL url = null;
        try {
            url = new URL(stringUrl);
        } catch(MalformedURLException e){
            Log.e("NewsUtils","Error in creating url : "+e);
        }
        return url;
    }

    public static boolean isConnected(Context context) {
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        if(networkInfo != null && networkInfo.isConnected()) {
            return true;
        } else {
            return false;
        }

    }
}
