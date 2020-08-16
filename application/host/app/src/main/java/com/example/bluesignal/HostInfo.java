package com.example.bluesignal;

public class HostInfo {
    private static HostInfo hostInfo = null;
    private String id;
    private String pswd;
    private String name;
    private String phnNumber;
    private String status;

    private HostInfo(){}

    public String getId() {
        return id;
    }

    public String getPswd() {
        return pswd;
    }

    public String getName() {
        return name;
    }

    public String getPhnNumber() {
        return phnNumber;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setPswd(String pswd) { this.pswd = pswd; }

    public void setName(String name) {
        this.name = name;
    }

    public void setPhnNumber(String phnNumber) {
        this.phnNumber = phnNumber;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public static HostInfo getInstance(){
        if(hostInfo == null){
            hostInfo = new HostInfo();
        }
        return hostInfo;
    }

    public void setAllInfo(String id, String pswd, String name, String phnNumber,String status){
        this.id = id;
        this.pswd = pswd;
        this.name = name;
        this.phnNumber = phnNumber;
        this.status = status;
    }

    public void deleteAllInfo(){
        this.id = null;
        this.pswd = null;
        this.name = null;
        this.phnNumber = null;
        this.status = null;
    }
}
