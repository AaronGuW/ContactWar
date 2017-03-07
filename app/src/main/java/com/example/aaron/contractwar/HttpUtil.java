package com.example.aaron.contractwar;

import android.content.Intent;
import android.os.Message;
import android.os.Handler;
import android.util.Log;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;
import java.util.logging.LogRecord;

/**
 * Created by user on 2015/6/2.
 */
public class HttpUtil {
    public static final int SHOW_RESPONSE = 0;
    public static String res = new String();

    public static String sendHttpRequest(String address) {
        HttpURLConnection connection = null;
        try {
            URL url = new URL(address);
            connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            connection.setConnectTimeout(8000);
            connection.setReadTimeout(8000);
            connection.setDoInput(true);
            connection.setDoOutput(true);
            InputStream in = connection.getInputStream();
            BufferedReader reader = new BufferedReader(new
                    InputStreamReader(in));
            StringBuilder response = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                response.append(line);
            }
            return response.toString();
        } catch (Exception e) {
            e.printStackTrace();
            return e.getMessage();
        } finally {
            if (connection != null) {
                connection.disconnect();
            }
        }
    }

    public static String HttpClientGET(String address, Handler handler) {
        final String pass = address;
        final Handler myhandler = handler;
        new Thread(new Runnable(){
            @Override
            public void run(){
                try {
                    HttpClient httpClient = new DefaultHttpClient();
                    HttpGet httpGet = new HttpGet(pass);
                    HttpResponse httpResponse = httpClient.execute(httpGet);
                    if (httpResponse.getStatusLine().getStatusCode() == 200) {
                        HttpEntity entity = httpResponse.getEntity();
                        String response = EntityUtils.toString(entity, "utf-8");
                        if (myhandler != null) {
                            Message message = new Message();
                            message.what = SHOW_RESPONSE;
                            message.obj = response.toString();
                            myhandler.sendMessage(message);
                        }
                        res = response;
                        //Log.d("res",res);
                        //Log.d("Test", "GET successfully");
                    }
                }catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }).start();
        return res;
    }

//    public static String getResult(String response){
//        return response;
//    }

    public static String HttpClientPOST(String address, List<NameValuePair> params) {
        final String pass = address;
        final List<NameValuePair> param = params;
        new Thread() {
            public void run() {
                try {
                    HttpClient httpClient = new DefaultHttpClient();
                    HttpPost httpPost = new HttpPost(pass);
                    UrlEncodedFormEntity entity = new UrlEncodedFormEntity(param, "utf-8");
                    httpPost.setEntity(entity);
                    HttpResponse httpResponse = httpClient.execute(httpPost);
                    if (httpResponse.getStatusLine().getStatusCode() == 200) {
                        HttpEntity entityGet = httpResponse.getEntity();
                        String response = EntityUtils.toString(entityGet, "utf-8");
                        Message message = new Message();
                        message.what = SHOW_RESPONSE;
                        message.obj = response.toString();
//                        res = response;
                        Log.d("Test", "POST successfully" + response);
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }.start();
        return res;
    }
}