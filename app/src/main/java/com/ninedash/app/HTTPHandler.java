package com.ninedash.app;

import org.apache.http.client.methods.HttpUriRequest;
import org.json.JSONObject;

/**
 * Created by vincentleung on 27/4/14.
 */
public abstract class HTTPHandler {

    public abstract HttpUriRequest getHttpRequest();
    public abstract void onResponse(JSONObject res);
    public void execute() {
        new AsyncHttpTask(this).execute();
    }
}
