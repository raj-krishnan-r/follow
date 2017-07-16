package com.example.raj.follow;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.Iterator;

import javax.net.ssl.HttpsURLConnection;

class sendPost extends AsyncTask<JSONObject, Void, String> {

    Context c;
    public sendPost(Context a)
    {
        c=a;
    }
    @Override
    protected String doInBackground(JSONObject... params) {

    try
    {
        URL url = new URL("https://note-runfree.rhcloud.com/follow/login.php");

        JSONObject postDataParams = params[0];

        /*
        postDataParams.put("name","Apple");
        postDataParams.put("id","123");
        **/

        Log.e("params",postDataParams.toString());


        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setReadTimeout(15000);
        conn.setConnectTimeout(15000);
        conn.setRequestMethod("POST");
        conn.setDoInput(true);
        conn.setDoOutput(true);

        OutputStream os = conn.getOutputStream();
        BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(os,"UTF-8"));
        writer.write(getPostDataString(postDataParams));
        writer.flush();
        writer.close();
        os.close();

        int rcode = conn.getResponseCode();
        if(rcode== HttpsURLConnection.HTTP_OK)
        {
     BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            StringBuffer sb = new StringBuffer("");
            String line = "";

            while((line=in.readLine())!=null) {
                sb.append(line);
                break;
            }
            in.close();
            return sb.toString();

    }
    else {
            return new String("false : "+rcode);
        }
    }
    catch (Exception e)
    {
        return new String("Exception: " + e.getMessage());

    }


    }


    @Override
    protected void onPostExecute(String result) {
        Toast.makeText(c, result, Toast.LENGTH_LONG).show();
    }

    public String getPostDataString(JSONObject params) throws Exception {

        StringBuilder result = new StringBuilder();
        boolean first = true;

        Iterator<String> itr = params.keys();

        while(itr.hasNext()){

            String key= itr.next();
            Object value = params.get(key);

            if (first)
                first = false;
            else
                result.append("&");

            result.append(URLEncoder.encode(key, "UTF-8"));
            result.append("=");
            result.append(URLEncoder.encode(value.toString(), "UTF-8"));

        }
        return result.toString();
    }
}
