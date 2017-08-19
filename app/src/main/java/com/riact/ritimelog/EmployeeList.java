package com.riact.ritimelog;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;
import android.widget.TextView;

import com.riact.ritimelog.adapter.EmployeeListAdapter;
import com.riact.ritimelog.utils.Constants;

/**
 * Created by koushik on 15/8/17.
 */

public class EmployeeList extends Fragment {

    View myView;
    GridView gridView;
    TextView noEmployeeLable;


    private static EmployeeListAdapter adapter;

    TextView siteName,siteCode,pinCode,oeCode;
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        myView= inflater.inflate(R.layout.employee_list,container,false);
        noEmployeeLable = (TextView) myView.findViewById(R.id.noEmployeeLable);
        gridView = (GridView) myView.findViewById(R.id.grid);
        adapter = new EmployeeListAdapter(Constants.employeeList, myView.getContext());
        gridView.setAdapter(adapter);

        gridView.setVisibility(View.VISIBLE);
        noEmployeeLable.setVisibility(View.GONE);


        return myView;
    }
}
