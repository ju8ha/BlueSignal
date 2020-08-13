package com.example.bluesignal;

public class GuestInfo {
    private static GuestInfo guestInfo = null;
    private String id;
    private String pswd;
    private String name;
    private String birthday;
    private String phnNumber;
    private String status;
    private String report;

    private GuestInfo(){}

    public String getId() {
        return id;
    }

    public String getPswd() {
        return pswd;
    }

    public String getName() {
        return name;
    }

    public String getBirthday() {
        return birthday;
    }

    public String getPhnNumber() {
        return phnNumber;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setBirthday(String birthday) {
        this.birthday = birthday;
    }

    public void setPhnNumber(String phnNumber) {
        this.phnNumber = phnNumber;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public void setReport(String report) {
        this.report = report;
    }

    public static GuestInfo getInstance(){
        if(guestInfo == null){
            guestInfo = new GuestInfo();
        }
        return guestInfo;
    }

    public void setAllInfo(String id, String pswd, String name, String birthday, String phnNumber,String status, String report){
        this.id = id;
        this.pswd = pswd;
        this.name = name;
        this.birthday = birthday;
        this.phnNumber = phnNumber;
        this.status = status;
        this.report = report;
    }

    public boolean isInfected(){
        if(this.status.equals("infected")){
            return true;
        }
        else{
            return false;
        }
    }

    public void deleteAllInfo(){
        this.id = null;
        this.pswd = null;
        this.name = null;
        this.birthday = null;
        this.phnNumber = null;
        this.status = null;
        this.report = null;
    }
}
