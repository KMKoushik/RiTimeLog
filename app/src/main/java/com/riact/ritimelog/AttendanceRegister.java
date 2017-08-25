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
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.LinearLayout;
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
import com.riact.ritimelog.utils.ModelUtil;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import static com.android.volley.VolleyLog.TAG;

/**
 * Created by koushik on 16/8/17.
 */

public class AttendanceRegister extends Fragment {

    View myView;
    GridView gridView;
    TextView noEmployeeLable,empCode,empName;
    DateFormat timeFormat = new SimpleDateFormat("HH:mm:ss");
    DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd");
    GPSTracker gps;
    DbHandler db;
    EditText secretCode;
    LinearLayout layout;
    Button submitBtn,cancelBtn;



    private static EmployeeGridAdapter adapter;

    TextView siteName,siteCode,pinCode,oeCode;
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        myView= inflater.inflate(R.layout.attendance_register,container,false);
        noEmployeeLable = (TextView) myView.findViewById(R.id.noEmployeeLable);
        siteCode = (TextView) myView.findViewById(R.id.site_code);
        siteName = (TextView) myView.findViewById(R.id.site_desc);
        gridView = (GridView) myView.findViewById(R.id.grid);
        secretCode = (EditText) myView.findViewById(R.id.secret_field);
        layout = (LinearLayout) myView.findViewById(R.id.register_field);
        siteCode.setText("Site Code : "+Constants.currentSite.getSiteCode());
        siteName.setText("Site Name : "+Constants.currentSite.getSiteName());
        empCode = (TextView) myView.findViewById(R.id.emp_code_attendace);
        empName = (TextView) myView.findViewById(R.id.emp_name_attendance);
        empCode.setText("Employee Code : "+Constants.currentSite.getSiteCode());
        empName.setText("Employee Name : "+Constants.currentSite.getSiteName());
        db = new DbHandler(getActivity());

        gridView.setVisibility(View.VISIBLE);
        noEmployeeLable.setVisibility(View.GONE);
        adapter = new EmployeeGridAdapter(Constants.employeeList, myView.getContext(),myView);
        gridView.setAdapter(adapter);





        return myView;
    }

    public  void showAttendanceDialog(final int position, final Context context, final View myView)
    {
        final EmployeeModel currentEmployee = Constants.employeeList.get(position);
        gridView = (GridView) myView.findViewById(R.id.grid);
        secretCode = (EditText) myView.findViewById(R.id.secret_field);
        layout = (LinearLayout) myView.findViewById(R.id.register_field);
        empCode = (TextView) myView.findViewById(R.id.emp_code_attendace);
        empName = (TextView) myView.findViewById(R.id.emp_name_attendance);
        submitBtn = (Button) myView.findViewById(R.id.submit_register);
        cancelBtn = (Button) myView.findViewById(R.id.cancel_register);
        empCode.setText("Employee Code : "+currentEmployee.getEmp_code());
        empName.setText("Employee Name : "+currentEmployee.getEmp_name());
        secretCode.setText("");

        submitBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String secretCodeTxt = secretCode.getText().toString();
                if (secretCodeTxt.equals(Constants.employeeList.get(position).getEmp_secret_code())) {
                    gps = new GPSTracker(context);
                    if (gps.canGetLocation()) {
                        double deviceLatitude = gps.getLatitude();
                        double deviceLongitude = gps.getLongitude();

                        double siteLatitude = Constants.currentSite.getSiteLatitude();
                        double siteLongitude = Constants.currentSite.getSiteLongitude();
                        double tollerance;
                        tollerance = Constants.currentSite.getTollerance()*0.00621371192;
                        //0.0063 is 10 m
                        if (distance(siteLatitude, siteLongitude, deviceLatitude, deviceLongitude) < tollerance ) {
                            Date date = new Date();
                            String url = Constants.WEB_ADDRESS+"submit_registration.php?site_code="+Constants.currentSite.getSiteCode()+"&emp_code="+currentEmployee.getEmp_code()+"&attendance_date="+dateFormat.format(date)+"&attendance_time="+timeFormat.format(date)+"&register_status_code=S";
                            volleyStringRequst(url,context,currentEmployee,myView);

                            layout.setVisibility(View.GONE);
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

        cancelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                gridView.setVisibility(View.VISIBLE);
                layout.setVisibility(View.GONE);
            }
        });

        gridView.setVisibility(View.GONE);
        layout.setVisibility(View.VISIBLE);

    }

    public void  volleyStringRequst(final String url, final Context context, final EmployeeModel employeeModel, final View myView){
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
                    updateAttenanceDetails(context);

                    updateGridView(myView);


                }
            }, new Response.ErrorListener() {

                @Override
                public void onErrorResponse(VolleyError error) {
                    VolleyLog.d(TAG, "Error: " + error.getMessage());
                    progressDialog.hide();
                    db = new DbHandler(context);
                    db.addToSyncData(employeeModel.getEmp_code(),System.currentTimeMillis(),url);
                    updateGridView(myView);

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


    public double distance(double lat1, double lng1, double lat2, double lng2) {

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

    public void updateAttenanceDetails(Context context)
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
        db = new DbHandler(context);


        List<List> attendanceList=db.getSyncedEmpDetails(startDate.getTime(),endDate.getTime());
        ModelUtil.setEmployeeListFromEmpCode(attendanceList.get(0));

    }

    public void updateGridView(View myView)
    {
        gridView = (GridView) myView.findViewById(R.id.grid);
        adapter = new EmployeeGridAdapter(Constants.employeeList, myView.getContext(),myView);
        gridView.setAdapter(adapter);
        gridView.setVisibility(View.VISIBLE);



    }
}
