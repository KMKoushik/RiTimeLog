package com.riact.ritimelog.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.riact.ritimelog.R;
import com.riact.ritimelog.models.EmployeeModel;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

/**
 * Created by koushik on 20/8/17.
 */

public class EmployeeAttendanceAdapter  extends ArrayAdapter<EmployeeModel> {

    private ArrayList<EmployeeModel> dataSet;
    Context mContext;
    EmployeeModel dataModel;
    ArrayList<String> date;


    // View lookup cache
    private static class ViewHolder {
        TextView txtEmpName;
        TextView txtEmpCode;
        TextView txtAttendanceDate;
        TextView txtAttenDanceTime;
        TextView txtSiteCode;
        LinearLayout linearLayout;
    }

    public EmployeeAttendanceAdapter(ArrayList<EmployeeModel> data, ArrayList<String> date,Context context) {
        super(context, R.layout.employee_grid_item_layout, data);
        this.dataSet = data;
        this.mContext=context;
        this.date = date;

    }


    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        dataModel = getItem(position);
        String atDate = date.get(position);
        // Check if an existing view is being reused, otherwise inflate the view
        EmployeeAttendanceAdapter.ViewHolder viewHolder; // view lookup cache stored in tag
        Date date = new Date(new Long(atDate));
        DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd");
        DateFormat timeFormat = new SimpleDateFormat("HH:mm:ss");

        final View result;

        if (convertView == null) {
            viewHolder = new EmployeeAttendanceAdapter.ViewHolder();
            LayoutInflater inflater = LayoutInflater.from(getContext());
            convertView = inflater.inflate(R.layout.attendance_grid_item_layout, parent, false);
            viewHolder.linearLayout = (LinearLayout) convertView.findViewById(R.id.carview);



            viewHolder.txtEmpName = (TextView) convertView.findViewById(R.id.emp_name);
            viewHolder.txtEmpCode = (TextView) convertView.findViewById(R.id.emp_code);
            viewHolder.txtAttendanceDate = (TextView) convertView.findViewById(R.id.attendance_date);
            viewHolder.txtAttenDanceTime = (TextView) convertView.findViewById(R.id.attendance_time);


            result=convertView;
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (EmployeeAttendanceAdapter.ViewHolder) convertView.getTag();
            result=convertView;
        }
        viewHolder.txtEmpName.setText(dataModel.getEmp_name());
        viewHolder.txtEmpCode.setText(dataModel.getEmp_code());
        viewHolder.txtAttendanceDate.setText(dateFormat.format(date));
        viewHolder.txtAttenDanceTime.setText(timeFormat.format(date));
        return convertView;
    }
}
