package com.cloud.servicemanage.entity;

import java.io.Serializable;
import java.util.List;

public class StaffInfo extends BaseEntity implements Serializable {
    private String userId;
    private String departmentname;
    private String entrytime;
    private String name;
    private String loginname;
    private String email;
    private String mobile;
    private String pic;
    private String typeId;
    private String departmentid;
    private String password;
    private String empid;
    private String isused;
    private String sex;
    private String birthday;
    private String eduid;
    private String starttime;
    private String endtime;
    private String major;
    private String job;
    private String parentcode;
    private String parentname;
    private String treeabout;

    private List<userDpt> experienceList;
    private List<userEdu> educationList;
    private List<String> role;

    List<StaffInfo> children;

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getUserId() {
        return userId;
    }

    public void setRole(List<String> role) {
        this.role = role;
    }

    public List<String> getRole() {
        return role;
    }

    public void setExperienceList(List<userDpt> experienceList) {
        this.experienceList = experienceList;
    }

    public void setEducationList(List<userEdu> educationList) {
        this.educationList = educationList;
    }

    public List<userDpt> getExperienceList() {
        return experienceList;
    }

    public List<userEdu> getEducationList() {
        return educationList;
    }

    public void setDepartmentname(String departmentname) {
        this.departmentname = departmentname;
    }

    public String getDepartmentname() {
        return departmentname;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setLoginname(String loginname) {
        this.loginname = loginname;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public void setPic(String pic) {
        this.pic = pic;
    }

    public void setTypeId(String typeId) {
        this.typeId = typeId;
    }

    public void setDepartmentid(String departmentid) {
        this.departmentid = departmentid;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setEmpid(String empid) {
        this.empid = empid;
    }

    public void setIsused(String isused) {
        this.isused = isused;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public void setBirthday(String birthday) {
        this.birthday = birthday;
    }

    public void setEduid(String eduid) {
        this.eduid = eduid;
    }

    public void setStarttime(String starttime) {
        this.starttime = starttime;
    }

    public void setEndtime(String endtime) {
        this.endtime = endtime;
    }

    public void setMajor(String major) {
        this.major = major;
    }

    public void setJob(String job) {
        this.job = job;
    }

    public void setChildren(List<StaffInfo> children) {
        this.children = children;
    }

    public void setParentcode(String parentcode) {
        this.parentcode = parentcode;
    }

    public void setParentname(String parentname) {
        this.parentname = parentname;
    }

    public void setTreeabout(String treeabout) {
        this.treeabout = treeabout;
    }

    public void setEntrytime(String entrytime) {
        this.entrytime = entrytime;
    }

    public String getEntrytime() {
        return entrytime;
    }

    public String getName() {
        return name;
    }

    public String getLoginname() {
        return loginname;
    }

    public String getEmail() {
        return email;
    }

    public String getMobile() {
        return mobile;
    }

    public String getPic() {
        return pic;
    }

    public String getTypeId() {
        return typeId;
    }

    public String getDepartmentid() {
        return departmentid;
    }

    public String getPassword() {
        return password;
    }

    public String getEmpid() {
        return empid;
    }

    public String getIsused() {
        return isused;
    }

    public String getSex() {
        return sex;
    }

    public String getBirthday() {
        return birthday;
    }

    public String getEduid() {
        return eduid;
    }

    public String getStarttime() {
        return starttime;
    }

    public String getEndtime() {
        return endtime;
    }

    public String getMajor() {
        return major;
    }

    public String getJob() {
        return job;
    }

    public List<StaffInfo> getChildren() {
        return children;
    }

    public String getParentcode() {
        return parentcode;
    }

    public String getParentname() {
        return parentname;
    }

    public String getTreeabout() {
        return treeabout;
    }
}
