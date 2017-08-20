package com.riact.ritimelog;

import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.graphics.Point;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.hyttetech.library.utils.DialogUtil;
import com.riact.ritimelog.adapter.EmployeeAttendanceAdapter;
import com.riact.ritimelog.adapter.EmployeeListAdapter;
import com.riact.ritimelog.models.EmployeeModel;
import com.riact.ritimelog.utils.Constants;
import com.riact.ritimelog.utils.DbHandler;
import com.riact.ritimelog.utils.ModelUtil;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by koushik on 19/8/17.
 */

public class AttendanceEnquiry extends Fragment {



    View myView;
    TextView startDatePicker,stopPickerDate,siteCode;
    DatePicker datePicker;
    Point p=new Point();
    Button searchBtn;
    DbHandler db;
    List<List> attendanceList;
    ArrayList<EmployeeModel> employeeList;
    LinearLayout startDateLayout,endDateLayout;
    GridView gridView;
    private static EmployeeAttendanceAdapter adapter;


    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        myView= inflater.inflate(R.layout.attendace_enquiry,container,false);

        startDatePicker = (TextView)myView.findViewById(R.id.startDatePickerText);
        stopPickerDate = (TextView) myView.findViewById(R.id.endDatePickerText);
        searchBtn = (Button)myView.findViewById(R.id.search_btn);
        gridView =(GridView)myView.findViewById(R.id.enquiry_grid);
        siteCode = (TextView)myView.findViewById(R.id.siteCodeText);
        siteCode.setText("Site Code : "+Constants.currentSite.getSiteCode());
        startDateLayout = (LinearLayout)myView.findViewById(R.id.startDateLayout) ;
        endDateLayout = (LinearLayout)myView.findViewById(R.id.endDateLayout) ;

        db = new DbHandler(myView.getContext());

        startDateLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                initiatePopupWindow(getActivity(),p,startDatePicker);
            }
        });

        endDateLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                initiatePopupWindow(getActivity(),p,stopPickerDate);
            }
        });

        searchBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String startDateTxt = startDatePicker.getText().toString()+" 00:00:00";
                String endDateTxt = stopPickerDate.getText().toString() +" 23:59:59";
                Date startDate = new Date();
                Date endDate = new Date();
                DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
                try {
                    startDate = dateFormat.parse(startDateTxt);
                     endDate = dateFormat.parse(endDateTxt);

                } catch (ParseException e) {
                    e.printStackTrace();
                }
                attendanceList=db.getSyncedEmpDetails(startDate.getTime(),endDate.getTime());
                employeeList = ModelUtil.getEmployeeListFromEmpCode(attendanceList.get(0));
                adapter = new EmployeeAttendanceAdapter(employeeList,(ArrayList<String>) attendanceList.get(1) ,myView.getContext());
                gridView.setAdapter(adapter);

                gridView.setVisibility(View.VISIBLE);
            }
        });

        return myView;
    }

    private PopupWindow pwindo;

    private void initiatePopupWindow(Activity context, Point p, final TextView datePickerTxt) {
        DisplayMetrics displayMetrics = new DisplayMetrics();
        context.getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        int popupWidth = displayMetrics.widthPixels-100;
        int popupHeight = displayMetrics.heightPixels-100;



        // Inflate the popup_layout.xml
        LinearLayout viewGroup = (LinearLayout) getActivity().findViewById(R.id.popup);
        LayoutInflater layoutInflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);




        final View layout = layoutInflater.inflate(R.layout.datepicker_popup, viewGroup);
        final PopupWindow popup = new PopupWindow(context);

        datePicker = (DatePicker)  layout.findViewById(R.id.datePicker);

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP){
            // Do something for lollipop and above versions
            datePicker.setCalendarViewShown(true);
        } else{
            // do something for phones running an SDK before lollipop
            datePicker.setCalendarViewShown(false);
            popupHeight-=450;

        }

        popup.setContentView(layout);
        popup.setWidth(popupWidth);
        popup.setHeight(popupHeight);
        popup.setFocusable(true);

        int OFFSET_X = 30;
        int OFFSET_Y = 30;

        // Clear the default translucent background

        // Displaying the popup at the specified location, + offsets.

        popup.showAtLocation(layout, Gravity.CENTER_HORIZONTAL, p.x , p.y);


        // Getting a reference to Close button, and close the popup when clicked.
        Button close = (Button) layout.findViewById(R.id.cancel);
        close.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                popup.dismiss();
            }
        });



        Button submit = (Button)layout.findViewById(R.id.set);
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String date=  ""+datePicker.getYear();
                date+="/"+(datePicker.getMonth()+1);
                date+="/"+datePicker.getDayOfMonth();

                datePickerTxt.setText(date);
                popup.dismiss();
            }
        });
    }
}

