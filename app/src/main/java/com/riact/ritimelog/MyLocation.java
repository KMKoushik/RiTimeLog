package com.riact.ritimelog;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.riact.ritimelog.utils.Constants;

/**
 * Created by koushik on 16/8/17.
 */

public class MyLocation extends Fragment {

    View myView;
    TextView siteName,siteCode,pinCode,oeCode;
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        myView= inflater.inflate(R.layout.site_details,container,false);

        siteName = (TextView)myView.findViewById(R.id.siteNameText);
        siteCode = (TextView)myView.findViewById(R.id.siteCodeText);
        pinCode = (TextView)myView.findViewById(R.id.pinCodeText);
        oeCode = (TextView)myView.findViewById(R.id.oeCodeText);

        siteName.setText(Constants.currentSite.getSiteName());
        siteCode.setText(Constants.currentSite.getSiteCode());
        pinCode.setText(""+Constants.currentSite.getSitePinCode());
        oeCode.setText(Constants.currentSite.getOECode());

        return myView;
    }
}
