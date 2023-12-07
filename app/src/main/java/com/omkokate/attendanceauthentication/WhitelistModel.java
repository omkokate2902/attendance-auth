package com.omkokate.attendanceauthentication;

public class WhitelistModel {
    private String id;
    private String name;
    private String dept;
    private String email;
    private String verification;

    public WhitelistModel() {}

    public WhitelistModel(String name, String dept, String email, String verification) {
        this.name = name;
        this.dept = dept;
        this.email = email;
        this.verification = verification;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDept() {
        return dept;
    }

    public void setDept(String dept) {
        this.dept = dept;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getVerification() {
        return verification;
    }

    public void setVerification(String verification) {
        this.verification = verification;
    }
}
