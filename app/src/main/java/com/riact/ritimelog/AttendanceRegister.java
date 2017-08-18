package com.riact.ritimelog;

import android.app.Fragment;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.TextView;

import com.hyttetech.library.utils.DialogUtil;
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

        alert.setPositiveButton("Submit", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                String enteredvalue = edittext.getText().toString();
                if (enteredvalue.equals(Constants.employeeList.get(position).getEmp_secret_code())) {
                    DialogUtil.showToast("password matches",1,context);
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
}
