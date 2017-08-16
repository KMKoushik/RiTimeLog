package com.riact.ritimelog.models;

/**
 * Created by koushik on 13/8/17.
 */

public class SiteModel {

    String site_code,site_desc,oe_code,oe_password;
    long site_pincode,last_modified;
    double tollerance,site_latitude,site_longitude;

    public SiteModel(String site_code,String site_desc,String oe_code,String oe_password,long site_pincode,double tollerance,double site_latitude,double site_longitude,long last_modified)
    {
        this.site_code = site_code;
        this.site_desc = site_desc;
        this.oe_code = oe_code;
        this.oe_password = oe_password;
        this.site_pincode = site_pincode;
        this.tollerance = tollerance;
        this.site_latitude = site_latitude;
        this.site_longitude = site_longitude;
        this.last_modified = last_modified;
    }

    public String getSiteCode()
    {
        return this.site_code;
    }

    public long getSitePinCode()
    {
        return this.site_pincode;
    }
    public String getOECode()
    {
        return this.oe_code;
    }
    public String getOEPassword()
    {
        return this.oe_password;
    }
    public String getSiteName() {return this.site_desc;}

}
