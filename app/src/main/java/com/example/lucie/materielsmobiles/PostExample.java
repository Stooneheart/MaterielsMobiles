package com.example.lucie.materielsmobiles;

/**
 * Created by Haris on 09/06/2017.
 */


import java.io.IOException;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class PostExample {
    public static final MediaType JSON
            = MediaType.parse("application/json; charset=utf-8");

    OkHttpClient client = new OkHttpClient();

    String post(String url, String json) throws IOException {
        RequestBody body = RequestBody.create(JSON, json);
        Request request = new Request.Builder()
                .header("AIzaSyCHYWFYCG0gPMYM1QBtf","XtemVvfmydkqM")
                .url(url)
                .post(body)
                .build();
        Response response = null;
        try {
            response = client.newCall(request).execute();
            return response.body().string();
        } finally {
            if (response != null) {
                response.close();
            }
        }
    }

    String bowlingJson() {
        return "{'name':'Du nouveau contenu est disponible',";
                }
    }