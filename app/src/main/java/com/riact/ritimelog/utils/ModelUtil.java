package com.riact.ritimelog.utils;

import com.riact.ritimelog.models.EmployeeModel;
import com.riact.ritimelog.models.SiteModel;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by koushik on 13/8/17.
 */

public class ModelUtil {


    public static ArrayList<SiteModel> getSiteList(JSONArray jsonArray) throws JSONException {
        ArrayList<SiteModel> siteList = new ArrayList<SiteModel>();
        for(int i=0;i<jsonArray.length();i++)
        {
            JSONObject site = jsonArray.getJSONObject(i);
            String siteCode = site.getString(Constants.KEY_SITE_CODE);
            String siteDesc = site.getString(Constants.KEY_SITE_DESC);
            String oeCode = site.getString(Constants.KEY_OE_CODE);
            String oePassword = site.getString(Constants.KEY_OE_PASSWORD);
            long sitePincode = site.getLong(Constants.KEY_SITE_PINCODE);
            double tollerance = site.getDouble(Constants.KEY_TOLLERANCE);
            double siteLatitude = site.getDouble(Constants.KEY_SITE_LATITUDE);
            double siteLongitude = site.getDouble(Constants.KEY_SITE_LONGITUDE);
            long lastModified = site.getLong(Constants.KEY_LAST_MODIFIED);
            siteList.add(new SiteModel(siteCode,siteDesc,oeCode,oePassword,sitePincode,tollerance,siteLatitude,siteLongitude,lastModified));
        }

        return siteList;
    }

    public static boolean isValidSite(String siteCode,long pinCode,String oeCode,String oePassword)
    {
        boolean isValid = false;
        for(SiteModel site : Constants.siteList)
        {
            if(site.getSiteCode().equals(siteCode)||site.getSitePinCode()==pinCode)
            {
                if(site.getOECode().equals(oeCode)&&(site.getOEPassword().equals(oePassword)))
                {
                    isValid = true;
                    break;
                }
            }
        }

        return isValid;
    }

    public static ArrayList<EmployeeModel> getEmployeeList(JSONArray jsonArray) throws JSONException {
        ArrayList<EmployeeModel> employeeList = new ArrayList<EmployeeModel>();
        for(int i=0;i<jsonArray.length();i++)
        {
            JSONObject employee = jsonArray.getJSONObject(i);

            String empCode = employee.getString(Constants.KEY_EMP_CODE);
            String empName = employee.getString(Constants.KEY_EMP_NAME);
            String empSecretCode = employee.getString(Constants.KEY_SECRET_CODE);
            String registrationMode = employee.getString(Constants.KEY_EMP_REGISTER_MODE);
            employeeList.add(new EmployeeModel(empCode,empName,empSecretCode,registrationMode));

        }
        return employeeList;
    }

    public static SiteModel getSite(String siteCode)
    {
        SiteModel siteModel=null;
        for (SiteModel siteModel1:Constants.siteList)
        {
            if(siteModel1.getSiteCode().equals(siteCode))
            {
                siteModel=siteModel1;
                break;
            }
        }
        return siteModel;
    }

    public static EmployeeModel getEmployeeFromCode(String empCode)
    {
        EmployeeModel employeeModel=null;
        for (EmployeeModel employeeModel1:Constants.employeeList)
        {
            if(employeeModel1.getEmp_code().equals(empCode))
            {
                employeeModel = employeeModel1;
            }
        }

        return employeeModel;
    }

    public static ArrayList<EmployeeModel> getEmployeeListFromEmpCode(List<String> list)
    {
        ArrayList<EmployeeModel> emplist = new ArrayList<EmployeeModel>();
        for(String string:list)
        {
            emplist.add(getEmployeeFromCode(string));
        }

        return emplist;
    }
}
