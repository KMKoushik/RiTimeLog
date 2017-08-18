package com.riact.ritimelog;

/**
 * Created by koushik on 15/8/17.
 */

import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;


import com.riact.ritimelog.models.EmployeeModel;

import java.util.ArrayList;


public class EmployeeGridAdapter extends ArrayAdapter<EmployeeModel> {

    private ArrayList<EmployeeModel> dataSet;
    Context mContext;
    EmployeeModel dataModel;


    // View lookup cache
    private static class ViewHolder {
        TextView txtEmpName;
        TextView txtEmpCode;
        TextView txtSiteCode;
        LinearLayout linearLayout;
    }

    public EmployeeGridAdapter(ArrayList<EmployeeModel> data, Context context) {
        super(context, R.layout.employee_grid_item_layout, data);
        this.dataSet = data;
        this.mContext=context;

    }


    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
         dataModel = getItem(position);
        // Check if an existing view is being reused, otherwise inflate the view
        EmployeeGridAdapter.ViewHolder viewHolder; // view lookup cache stored in tag

        final View result;

        if (convertView == null) {
            viewHolder = new EmployeeGridAdapter.ViewHolder();
            LayoutInflater inflater = LayoutInflater.from(getContext());
            convertView = inflater.inflate(R.layout.employee_grid_item_layout, parent, false);
            viewHolder.linearLayout = (LinearLayout) convertView.findViewById(R.id.carview);


            viewHolder.txtEmpName = (TextView) convertView.findViewById(R.id.emp_name);
            viewHolder.txtEmpCode = (TextView) convertView.findViewById(R.id.emp_code);
            viewHolder.linearLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                  new AttendanceRegister().showAttendanceDialog(position,getContext());
                }
            });
            result=convertView;
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (EmployeeGridAdapter.ViewHolder) convertView.getTag();
            result=convertView;
        }
        viewHolder.txtEmpName.setText(dataModel.getEmp_name());
        viewHolder.txtEmpCode.setText(dataModel.getEmp_code());
        return convertView;
    }
}
