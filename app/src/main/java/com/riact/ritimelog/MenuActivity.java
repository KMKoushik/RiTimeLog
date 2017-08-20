package com.riact.ritimelog;

import android.Manifest;
import android.app.Fragment;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.text.InputType;
import android.util.Log;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.StringRequest;
import com.hyttetech.library.utils.AppSingleton;
import com.hyttetech.library.utils.NetworkUtil;
import com.riact.ritimelog.utils.Constants;
import com.riact.ritimelog.utils.DbHandler;
import com.riact.ritimelog.utils.GPSTracker;
import com.riact.ritimelog.utils.ModelUtil;

import org.w3c.dom.Text;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static com.android.volley.VolleyLog.TAG;

public class MenuActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    GPSTracker gps;
    DbHandler db;
    int syncFlag = 1;
    ArrayList<List<String>> toSyncList= new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        db= new DbHandler(getApplicationContext());
        updateAttenanceDetails();



        android.app.FragmentManager fm=getFragmentManager();
        fm.beginTransaction().replace(R.id.content_menu,new AttendanceRegister()).commit();

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.getMenu().getItem(0).setChecked(true);

        navigationView.setNavigationItemSelectedListener(this);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();
        android.app.FragmentManager fm=getFragmentManager();


        if (id == R.id.attendance_register) {
            // Handle the camera action
            fm.beginTransaction().replace(R.id.content_menu,new AttendanceRegister()).commit();

        } else if (id == R.id.site_details) {
            validateLogin(id);

        } else if (id == R.id.employee_list) {
            validateLogin(id);

        } else if (id == R.id.sync) {
            validateLogin(id);


        } else if (id == R.id.enquiry) {
            validateLogin(id);

        } else if(id == R.id.mylocation)
        {
            showLocation();
        }
        else if(id == R.id.logout)
        {
            validateLogin(id);


        }
        else if (id == R.id.nav_exit) {
            final AlertDialog.Builder alert = new AlertDialog.Builder(this);
            alert.setTitle("Warning");
            alert.setMessage("Do you want to exit?");
            alert.setPositiveButton("OK", new DialogInterface.OnClickListener()
            {

                @Override
                public void onClick(DialogInterface dialog, int which)
                {
                    System.exit(1);
                }
            });
            alert.setNegativeButton("Cancel",new DialogInterface.OnClickListener()
            {

                @Override
                public void onClick(DialogInterface dialog, int which)
                {

                }
            });

            alert.show();

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private void showLocation() {
        gps = new GPSTracker(MenuActivity.this);
        if (gps.canGetLocation()) {
            double deviceLatitude = gps.getLatitude();
            double deviceLongitude = gps.getLongitude();
            showAllert("Current Location is - \nLatitude : " + deviceLatitude + "\nLongitude: " + deviceLongitude);
        } else {
            gps.showSettingsAlert();
        }
    }

    private void showAllert(String succesMessage) {
        new AlertDialog.Builder(MenuActivity.this)
                .setMessage(succesMessage)
                .setNegativeButton("Ok", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        Log.i("", "onClick: ");
                    }
                })
                .show();
    }

    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case 1: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                } else {
                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                }
                return;
            }
            // other 'case' lines to check for other
            // permissions this app might request
        }
    }

    public boolean checkLocationPermission()
    {
        String permission = "android.permission.ACCESS_FINE_LOCATION";
        int res = this.checkCallingOrSelfPermission(permission);
        return (res == PackageManager.PERMISSION_GRANTED);
    }

    public void validateLogin(final int i)
    {
        final android.app.FragmentManager fm=getFragmentManager();

        final boolean isValidated = false;
        final AlertDialog.Builder alert = new AlertDialog.Builder(this);
        final EditText edittext = new EditText(getApplicationContext());
        alert.setTitle("Authentication Required");
        edittext.setTextColor(Color.BLACK);
        alert.setMessage("OE Authentication is required to access this menu");
        edittext.setInputType(InputType.TYPE_CLASS_TEXT |
                InputType.TYPE_TEXT_VARIATION_PASSWORD);
        edittext.setHint("OE Password");
        edittext.setWidth(50);
        alert.setView(edittext);
        alert.setPositiveButton("Submit", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String text = edittext.getText().toString();
                if(Constants.currentSite.getOEPassword().equals(text))
                {
                    switch (i)
                    {
                        case R.id.site_details:fm.beginTransaction().replace(R.id.content_menu,new SiteDetails()).commit();
                            break;
                        case R.id.employee_list:fm.beginTransaction().replace(R.id.content_menu,new EmployeeList()).commit();
                            break;
                        case R.id.enquiry :fm.beginTransaction().replace(R.id.content_menu,new AttendanceEnquiry()).commit();
                            break;
                        case R.id.logout:logOut();
                            break;
                        case R.id.sync:syncData();
                            break;
                    }

                }
                else {
                    Toast.makeText(getApplication(),"Invalid password",Toast.LENGTH_SHORT).show();
                }
            }
        });
        alert.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });
        alert.show();


    }

    public void logOut(){
        final AlertDialog.Builder alert = new AlertDialog.Builder(this);
        alert.setTitle("Warning");
        alert.setMessage("All saved data will be deleted. Do you want to Logout?");
        alert.setPositiveButton("OK", new DialogInterface.OnClickListener()
        {

            @Override
            public void onClick(DialogInterface dialog, int which)
            {
                db.deleteCurrentSite();
                db.deleteSyncedEmpDetails();
                db.deleteToSyncedEmpDetails();
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(intent);
                finish();
            }
        });
        alert.setNegativeButton("Cancel",new DialogInterface.OnClickListener()
        {

            @Override
            public void onClick(DialogInterface dialog, int which)
            {

            }
        });

        alert.show();
    }

    public void syncData()
    {
        toSyncList = db.getAllToSyncData();

        ArrayList<String> empList = (ArrayList<String>) toSyncList.get(0);
        ArrayList<String> timeList = (ArrayList<String>) toSyncList.get(1);
        ArrayList<String> urlList = (ArrayList<String>) toSyncList.get(2);
        int length = empList.size();



        if(NetworkUtil.isConnectedToInternet(this)) {
            if (!empList.isEmpty()) {
                Toast.makeText(getApplicationContext(),"Sync Started",Toast.LENGTH_SHORT).show();

                for (int i = 0; i < length; i++) {
                    final String empCode = empList.get(i);
                    final String time = timeList.get(i);
                    String url = urlList.get(i);
                    String REQUEST_TAG = "com.androidtutorialpoint.volleyStringRequest";


                    StringRequest strReq = new StringRequest(url, new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {

                            db.addSyncedDetails(empCode, new Long(time));


                        }
                    }, new Response.ErrorListener() {

                        @Override
                        public void onErrorResponse(VolleyError error) {
                            VolleyLog.d(TAG, "Error: " + error.getMessage());
                            syncFlag = 0;
                        }
                    });
                    // Adding String request to request queue
                    AppSingleton.getInstance(this).addToRequestQueue(strReq, REQUEST_TAG);

                }
                if(syncFlag == 0)
                {
                    Toast.makeText(getApplicationContext(),"Sync Failed",Toast.LENGTH_SHORT).show();

                }
                else{
                    Toast.makeText(getApplicationContext(),"Sync Success",Toast.LENGTH_SHORT).show();
                    db.deleteToSyncedEmpDetails();


                }
            }
            else {
                Toast.makeText(getApplicationContext(),"Nothing to sync",Toast.LENGTH_SHORT).show();
            }
        }
        else {
            Toast.makeText(getApplicationContext(),"Internet Connection not available",Toast.LENGTH_LONG).show();
        }

    }

    public void updateAttenanceDetails()
    {
        String startDateTxt,endateTxt;
        DateFormat currentDate =  new SimpleDateFormat("yyyy/MM/dd");
        Date startDate = new Date();
        Date endDate = new Date();
        startDateTxt = currentDate.format(startDate)+" 00:00:00";
        endateTxt = currentDate.format(endDate)+ " 23:59:59";
        DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");

        try{

            startDate = dateFormat.parse(startDateTxt);
            endDate = dateFormat.parse(endateTxt);

        }catch (Exception e)
        {
            e.printStackTrace();
        }

        List<List> attendanceList=db.getSyncedEmpDetails(startDate.getTime(),endDate.getTime());
        ModelUtil.setEmployeeListFromEmpCode(attendanceList.get(0));

    }
}
