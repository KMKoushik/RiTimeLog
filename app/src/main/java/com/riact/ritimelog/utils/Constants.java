package com.riact.ritimelog.utils;

import com.riact.ritimelog.models.EmployeeModel;
import com.riact.ritimelog.models.SiteModel;

import java.util.ArrayList;

/**
 * Created by koushik on 13/8/17.
 */

public class Constants {

    public static final String WEB_ADDRESS = "http://www.riact.com/Riact/biometric/";

    public static ArrayList<SiteModel> siteList = new ArrayList<>();
    public static ArrayList<EmployeeModel> employeeList = new ArrayList<>();
    public static SiteModel currentSite;


    public static final String KEY_SITE_CODE = "site_code";
    public static final String KEY_SITE_DESC = "site_desc";
    public static final String KEY_OE_CODE = "oe_code";
    public static final String KEY_OE_PASSWORD = "oe_password";
    public static final String KEY_SITE_PINCODE = "site_pincode";
    public static final String KEY_TOLLERANCE = "tollerance";
    public static final String KEY_SITE_LATITUDE = "site_latitude";
    public static final String KEY_SITE_LONGITUDE = "site_longitude";
    public static final String KEY_LAST_MODIFIED = "last_modified";
    public static final String KEY_EMP_CODE = "emp_code";
    public static final String KEY_EMP_NAME = "emp_name";
    public static final String KEY_SECRET_CODE = "emp_secret_code";
    public static final String KEY_EMP_REGISTER_MODE = "emp_register_mode";

}
