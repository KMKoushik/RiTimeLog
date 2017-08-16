package com.riact.ritimelog;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.hyttetech.library.utils.AppSingleton;
import com.riact.ritimelog.utils.Constants;
import com.riact.ritimelog.utils.DbHandler;
import com.riact.ritimelog.utils.ModelUtil;

import org.json.JSONArray;
import org.json.JSONException;

public class SplashActivity extends AppCompatActivity {

    DbHandler db;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        db = new DbHandler(getApplicationContext());
        volleyStringRequst(Constants.WEB_ADDRESS+"get_site.php");

    }

    public void  volleyStringRequst( String url){
        String  REQUEST_TAG = "com.androidtutorialpoint.volleyStringRequest";
        StringRequest strReq = new StringRequest(url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                db.deleteSites();
                db.addSiteDetails(response);

                try {
                    Constants.siteList = ModelUtil.getSiteList(new JSONArray(response));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                Intent i = new Intent(SplashActivity.this, MainActivity.class);
                startActivity(i);
                finish();

            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                try {
                    Constants.siteList = ModelUtil.getSiteList(new JSONArray(db.getSiteDetails()));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                Intent i = new Intent(SplashActivity.this, MainActivity.class);
                startActivity(i);
                finish();
            }
        });
        AppSingleton.getInstance(getApplicationContext()).addToRequestQueue(strReq, REQUEST_TAG);
    }
}
