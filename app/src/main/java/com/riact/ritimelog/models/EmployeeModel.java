package com.riact.ritimelog.models;

/**
 * Created by koushik on 15/8/17.
 */

public class EmployeeModel {

    String emp_code,emp_name,site_code,emp_secret_code,emp_biometric,emp_register_mode,last_modified;

    public EmployeeModel(String emp_code,String emp_name,String emp_secret_code,String emp_register_mode)
    {
        this.emp_code = emp_code;
        this.emp_name = emp_name;
        this.emp_secret_code = emp_secret_code;
        this.emp_register_mode = emp_register_mode;
    }

    public String getEmp_name(){
        return this.emp_name;
    }

    public String getEmp_code(){
        return this.emp_code;
    }

    public String getEmp_secret_code()
    {
        return this.emp_secret_code;
    }
}
