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

import java.util.ArrayList;

/**
 * Created by koushik on 19/8/17.
 */

public class EmployeeListAdapter  extends ArrayAdapter<EmployeeModel> {

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

    public EmployeeListAdapter(ArrayList<EmployeeModel> data, Context context) {
        super(context, R.layout.employee_grid_item_layout, data);
        this.dataSet = data;
        this.mContext=context;

    }


    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        dataModel = getItem(position);
        // Check if an existing view is being reused, otherwise inflate the view
        EmployeeListAdapter.ViewHolder viewHolder; // view lookup cache stored in tag

        final View result;

        if (convertView == null) {
            viewHolder = new EmployeeListAdapter.ViewHolder();
            LayoutInflater inflater = LayoutInflater.from(getContext());
            convertView = inflater.inflate(R.layout.employee_grid_item_layout, parent, false);
            viewHolder.linearLayout = (LinearLayout) convertView.findViewById(R.id.carview);


            viewHolder.txtEmpName = (TextView) convertView.findViewById(R.id.emp_name);
            viewHolder.txtEmpCode = (TextView) convertView.findViewById(R.id.emp_code);
            result=convertView;
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (EmployeeListAdapter.ViewHolder) convertView.getTag();
            result=convertView;
        }
        viewHolder.txtEmpName.setText(dataModel.getEmp_name());
        viewHolder.txtEmpCode.setText(dataModel.getEmp_code());
        return convertView;
    }
}
