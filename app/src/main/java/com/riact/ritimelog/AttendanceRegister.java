package com.riact.ritimelog;

import android.app.Fragment;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.text.InputType;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.StringRequest;
import com.hyttetech.library.utils.AppSingleton;
import com.hyttetech.library.utils.DialogUtil;
import com.riact.ritimelog.adapter.EmployeeGridAdapter;
import com.riact.ritimelog.models.EmployeeModel;
import com.riact.ritimelog.utils.Constants;
import com.riact.ritimelog.utils.DbHandler;
import com.riact.ritimelog.utils.GPSTracker;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import static com.android.volley.VolleyLog.TAG;

/**
 * Created by koushik on 16/8/17.
 */

public class AttendanceRegister extends Fragment {

    View myView;
    GridView gridView;
    TextView noEmployeeLable;
    DateFormat timeFormat = new SimpleDateFormat("HH:mm:ss");
    DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd");
    GPSTracker gps;
    DbHandler db;



    private static EmployeeGridAdapter adapter;

    TextView siteName,siteCode,pinCode,oeCode;
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        myView= inflater.inflate(R.layout.attendance_register,container,false);
        noEmployeeLable = (TextView) myView.findViewById(R.id.noEmployeeLable);
        siteCode = (TextView) myView.findViewById(R.id.site_code);
        siteName = (TextView) myView.findViewById(R.id.site_desc);
        gridView = (GridView) myView.findViewById(R.id.grid);
        adapter = new EmployeeGridAdapter(Constants.employeeList, myView.getContext());
        gridView.setAdapter(adapter);
        siteCode.setText("Site Code : "+Constants.currentSite.getSiteCode());
        siteName.setText("Site Name : "+Constants.currentSite.getSiteName());
        db = new DbHandler(getActivity());

        gridView.setVisibility(View.VISIBLE);
        noEmployeeLable.setVisibility(View.GONE);


        return myView;
    }

    public  void showAttendanceDialog(final int position, final Context context)
    {

        AlertDialog.Builder alert = new AlertDialog.Builder(context);
        final EditText edittext = new EditText(context);
        alert.setTitle("Authentication Required");
        edittext.setInputType(InputType.TYPE_CLASS_TEXT |
                InputType.TYPE_TEXT_VARIATION_PASSWORD);
        edittext.setHint("Secret Code");
        edittext.setWidth(50);
        alert.setView(edittext);
        final EmployeeModel currentEmployee = Constants.employeeList.get(position);

        alert.setPositiveButton("Submit", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                String enteredvalue = edittext.getText().toString();
                if (enteredvalue.equals(Constants.employeeList.get(position).getEmp_secret_code())) {
                    gps = new GPSTracker(context);
                    if (gps.canGetLocation()) {
                        double deviceLatitude = gps.getLatitude();
                        double deviceLongitude = gps.getLongitude();

                        double siteLatitude = Constants.currentSite.getSiteLatitude();
                        double siteLongitude = Constants.currentSite.getSiteLongitude();
                        double tollerance;
                        tollerance = Constants.currentSite.getTollerance()*0.000621371192;
                        //0.0063 is 10 m
                        if (distance(siteLatitude, siteLongitude, deviceLatitude, deviceLongitude) < tollerance ) {
                            Date date = new Date();
                            String url = Constants.WEB_ADDRESS+"submit_registration.php?site_code="+Constants.currentSite.getSiteCode()+"&emp_code="+currentEmployee.getEmp_code()+"&attendance_date="+dateFormat.format(date)+"&attendance_time="+timeFormat.format(date)+"&register_status_code=S";
                            volleyStringRequst(url,context,currentEmployee);
                        } else {

                            DialogUtil.showToast("Invalid Location",1,context);
                        }
                    } else {
                        // can't get location
                        // GPS or Network is not enabled
                        // Ask user to enable GPS/network in settings
                        gps.showSettingsAlert();
                    }

                } else {
                    DialogUtil.showToast("Invalid Password",1,context);
                }
            }
        });

        alert.setNegativeButton("No", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {

            }
        });

        alert.show();
    }

    public void  volleyStringRequst(final String url, final Context context, final EmployeeModel employeeModel){
        try {


            String REQUEST_TAG = "com.androidtutorialpoint.volleyStringRequest";
            final ProgressDialog progressDialog=new ProgressDialog(context);
            progressDialog.setMessage("Loading...");
            progressDialog.show();



            StringRequest strReq = new StringRequest(url, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    progressDialog.hide();

                    Toast.makeText(context, response, Toast.LENGTH_LONG).show();
                    db = new DbHandler(context);
                    db.addSyncedDetails(employeeModel.getEmp_code(),System.currentTimeMillis());


                }
            }, new Response.ErrorListener() {

                @Override
                public void onErrorResponse(VolleyError error) {
                    VolleyLog.d(TAG, "Error: " + error.getMessage());
                    progressDialog.hide();
                    db = new DbHandler(context);
                    db.addToSyncData(employeeModel.getEmp_code(),System.currentTimeMillis(),url);

                    Toast.makeText(context, "Failed to Register, Sync later", Toast.LENGTH_LONG).show();


                }
            });
            // Adding String request to request queue
            AppSingleton.getInstance(getActivity()).addToRequestQueue(strReq, REQUEST_TAG);
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }

    }


    private double distance(double lat1, double lng1, double lat2, double lng2) {

        double earthRadius = 3958.75; // in miles, change to 6371 for kilometer output

        double dLat = Math.toRadians(lat2-lat1);
        double dLng = Math.toRadians(lng2-lng1);

        double sindLat = Math.sin(dLat / 2);
        double sindLng = Math.sin(dLng / 2);

        double a = Math.pow(sindLat, 2) + Math.pow(sindLng, 2)
                * Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2));

        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1-a));

        double dist = earthRadius * c;
        Log.i("location", "distance: "+dist);
        return dist; // output distance, in MILES
    }
}
