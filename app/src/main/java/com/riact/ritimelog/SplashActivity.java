package com.riact.ritimelog;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.StringRequest;
import com.hyttetech.library.utils.AppSingleton;
import com.hyttetech.library.utils.NetworkUtil;
import com.riact.ritimelog.utils.Constants;
import com.riact.ritimelog.utils.DbHandler;
import com.riact.ritimelog.utils.ModelUtil;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.List;

import static com.android.volley.VolleyLog.TAG;

public class SplashActivity extends AppCompatActivity {

    DbHandler db;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        db = new DbHandler(getApplicationContext());

        if(NetworkUtil.isConnectedToInternet(this)) {
            if (ContextCompat.checkSelfPermission(this,
                    Manifest.permission.ACCESS_FINE_LOCATION)
                    != PackageManager.PERMISSION_GRANTED) {

                // Should we show an explanation?
                if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                        Manifest.permission.ACCESS_FINE_LOCATION)) {

                    // Show an explanation to the user *asynchronously* -- don't block
                    // this thread waiting for the user's response! After the user
                    // sees the explanation, try again to request the permission.

                } else {

                    // No explanation needed, we can request the permission.

                    ActivityCompat.requestPermissions(this,
                            new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                            1);

                    // MY_PERMISSIONS_REQUEST_READ_CONTACTS is an
                    // app-defined int constant. The callback method gets the
                    // result of the request.
                }
            } else {
                volleyStringRequst(Constants.WEB_ADDRESS + "get_site.php");

            }
        }
        else {
            String string = db.getSiteDetails();
            if(null!=string)
            {
                List item = db.getCurrentSite();
                try {
                    Constants.siteList = ModelUtil.getSiteList(new JSONArray(db.getSiteDetails()));
                if(!item.isEmpty())
                {
                    Constants.currentSite=ModelUtil.getSite(item.get(0).toString());
                    volleyEmployeeRequst(Constants.WEB_ADDRESS+"get_site_emp.php?site_code="+item.get(0).toString(),item.get(0).toString());
                }
                else {

                        Intent i = new Intent(SplashActivity.this, MainActivity.class);
                        startActivity(i);
                        finish();

                }
                } catch (JSONException e) {
                e.printStackTrace();
            }

            }


            Toast.makeText(getApplicationContext(),"No Internet Available",Toast.LENGTH_LONG).show();
            finish();
        }

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
                List item = db.getCurrentSite();
                    if(!item.isEmpty())
                    {
                        Constants.currentSite=ModelUtil.getSite(item.get(0).toString());
                        volleyEmployeeRequst(Constants.WEB_ADDRESS+"get_site_emp.php?site_code="+item.get(0).toString(),item.get(0).toString());
                    }
                    else {
                        Intent i = new Intent(SplashActivity.this, MainActivity.class);
                        startActivity(i);
                    }

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



    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case 1: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    volleyStringRequst(Constants.WEB_ADDRESS+"get_site.php");


                    // permission was granted, yay! Do the
                    // contacts-related task you need to do.

                } else {

                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                    finish();
                }
                return;
            }

            // other 'case' lines to check for other
            // permissions this app might request
        }
    }

    public void  volleyEmployeeRequst(String url, final String siteCodeTxt){
        String  REQUEST_TAG = "com.androidtutorialpoint.volleyStringRequest";


        StringRequest strReq = new StringRequest(url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d(TAG, response.toString());

                try {
                    Constants.employeeList=ModelUtil.getEmployeeList(new JSONArray(response));
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                db.addEmployeeDetails(siteCodeTxt,response);
                db.deleteCurrentSite();
                db.addCurrentSite(siteCodeTxt);
                Constants.currentSite=ModelUtil.getSite(db.getCurrentSite().get(0).toString());
                Intent intent = new Intent(getApplicationContext(), MenuActivity.class);
                startActivity(intent);
                finish();


            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.d(TAG, "Error: " + error.getMessage());
                List<String> employeeData=db.getEmployeeDtails(siteCodeTxt);
                if(!employeeData.isEmpty())
                {
                    try {
                        Constants.employeeList=ModelUtil.getEmployeeList(new JSONArray(employeeData.get(0)));
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    Intent intent = new Intent(getApplicationContext(), MenuActivity.class);
                    startActivity(intent);
                    finish();
                }
                else {
                    Toast.makeText(getApplicationContext(), "No Internet Available", Toast.LENGTH_LONG).show();
                    finish();
                }


            }
        });
        // Adding String request to request queue
        AppSingleton.getInstance(getApplicationContext()).addToRequestQueue(strReq, REQUEST_TAG);

    }
}
