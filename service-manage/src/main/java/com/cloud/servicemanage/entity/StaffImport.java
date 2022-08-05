package com.cloud.servicemanage.entity;

import java.io.Serializable;

public class StaffImport extends StaffInfo  implements Serializable {
    private String id;
    private String name;
    private String sex;
    private String deptName;
    private String birthday;
    private String roleName;
    private String mobile;
    private String email;


    public String getDeptName() {
        return deptName;
    }

    public void setDeptName(String deptName) {
        this.deptName = deptName;
    }

    public String getRoleName() {
        return roleName;
    }

    public void setRoleName(String roleName) {
        this.roleName = roleName;
    }






}
