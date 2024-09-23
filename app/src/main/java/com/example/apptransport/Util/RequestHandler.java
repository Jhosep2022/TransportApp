package com.example.apptransport.Util;

import android.util.Log;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

import javax.net.ssl.HttpsURLConnection;

/**
 * Created by jorge gutierrez on 06/03/2017.
 */

public class RequestHandler {
    public String sendGetRequest(String uri) {



        try {
            URL url = new URL(uri);
            HttpURLConnection con = (HttpURLConnection) url.openConnection();
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(con.getInputStream()));

            String result;

            StringBuilder sb = new StringBuilder();

            while((result = bufferedReader.readLine())!=null){
                sb.append(result);
            }

            return sb.toString();
        } catch (Exception e) {
            return null;
        }
    }

    public String sendPostRequest(String requestURL, HashMap<String, String> postDataParams) {

        Log.d("uri",requestURL);

        URL url;
        String response = "";
        HttpURLConnection conn=null;
        try {
            url = new URL(requestURL);
            String dataToSendString =getPostDataString(postDataParams);

            conn = (HttpURLConnection) url.openConnection();
            conn.setReadTimeout(10000); //55000
            conn.setConnectTimeout(10000);//55000
            conn.setRequestMethod("POST");
            conn.setDoInput(true);
            conn.setDoOutput(true);
            conn.setFixedLengthStreamingMode(dataToSendString.length());


            OutputStream os = conn.getOutputStream();
            BufferedWriter writer = new BufferedWriter(
                    new OutputStreamWriter(os, "UTF-8"));
            writer.write(dataToSendString);

            writer.flush();
            writer.close();
            os.close();
            int responseCode = conn.getResponseCode();

            if (responseCode == HttpsURLConnection.HTTP_OK) {
                BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                StringBuilder result = new StringBuilder();
                String text;
                while ((text = br.readLine()) != null) {
                    result.append(text);
                }
                response = result.toString().toLowerCase().contains("success") ? "true" : "";
            } else {
                Log.d("Error",response);
                response = "";
            }
        } catch (Exception e) {
            e.printStackTrace();
            response="";
        }
        finally {
            if(conn!=null){
                conn.disconnect();
            }
        }
        return response;
    }

        private String getPostDataString(HashMap<String, String> params) throws UnsupportedEncodingException {
        StringBuilder result = new StringBuilder();
        boolean first = true;
        for (Map.Entry<String, String> entry : params.entrySet()) {
            if (first)
                first = false;
            else
                result.append("&");

            result.append(URLEncoder.encode(entry.getKey(), "UTF-8"));
            result.append("=");
            result.append(URLEncoder.encode(entry.getValue(), "UTF-8"));
        }

        return result.toString();
    }

}
