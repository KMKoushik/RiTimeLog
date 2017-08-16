package com.riact.ritimelog;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;
import android.widget.TextView;

import com.riact.ritimelog.utils.Constants;

/**
 * Created by koushik on 16/8/17.
 */

public class AttendanceRegister extends Fragment {

    View myView;
    GridView gridView;
    TextView noEmployeeLable;


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

        gridView.setVisibility(View.VISIBLE);
        noEmployeeLable.setVisibility(View.GONE);


        return myView;
    }
}
