package com.ninedash.app;

import android.net.http.AndroidHttpClient;
import android.os.AsyncTask;

import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpUriRequest;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;

/**
 * Created by vincentleung on 27/4/14.
 */
public class AsyncHttpTask extends AsyncTask<HttpUriRequest, Integer, JSONObject> {
    private static final String USER_AGENT = "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_9_2) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/34.0.1847.116 Safari/537.36";
    private HTTPHandler httpHandler;
    private AndroidHttpClient httpClient;

    public AsyncHttpTask(HTTPHandler handler) {
        httpHandler = handler;
        httpClient = AndroidHttpClient.newInstance(USER_AGENT);
    }

    @Override
    protected JSONObject doInBackground(HttpUriRequest... httpUriRequests) {
        HttpUriRequest request = httpHandler.getHttpRequest();
        JSONObject json = null;
        try {
            HttpResponse res = httpClient.execute(request);
            InputStream in = res.getEntity().getContent();
            BufferedReader reader = new BufferedReader(new InputStreamReader(in));
            String body = "", line;
            while ((line = reader.readLine()) != null) {
                body += line;
            }
            json = new JSONObject(body);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return json;
    }

    @Override
    public void onPostExecute(JSONObject response) {
        httpHandler.onResponse(response);
        httpClient.close();
    }
}
