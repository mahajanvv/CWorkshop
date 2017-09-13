package com.example.dontknow.cworkshop;

/**
 * Created by Dont know on 27-07-2017.
 */

public class CWOnlineEntry_accept {

    private String FirstName;
    private String MidName;
    private String LastName;
    private String Email;
    private String Phone;
    private String ID;
    private String College;
    private String Year;
    private String Signature;
    private String Date;
    private String userName;
    private String userUID;
    private String Remaining;
    private String Session1;
    private String Session2;
    private String Session3;
    private String Session4;

    public  CWOnlineEntry_accept()
    {}
    public CWOnlineEntry_accept(String ID,String firstName, String midName, String lastName, String email, String phone, String college, String year, String signature, String date,String Remaining, String userName, String userUID
    ,String session1,String session2,String session3,String session4
    ) {
        FirstName = firstName;
        MidName = midName;
        LastName = lastName;
        Email = email;
        Phone = phone;
        this.ID = ID;
        College = college;
        Year = year;
        Signature = signature;
        Date = date;
        this.userName = userName;
        this.userUID = userUID;
        this.Remaining = Remaining;
        Session1 = session1;
        Session2 = session2;
        Session3 = session3;
        Session4 = session4;
    }


    public String getSession1() {
        return Session1;
    }

    public void setSession1(String session1) {
        Session1 = session1;
    }

    public String getSession2() {
        return Session2;
    }

    public void setSession2(String session2) {
        Session2 = session2;
    }

    public String getSession3() {
        return Session3;
    }

    public void setSession3(String session3) {
        Session3 = session3;
    }

    public String getSession4() {
        return Session4;
    }

    public void setSession4(String session4) {
        Session4 = session4;
    }
    public String getRemaining() {
        return Remaining;
    }

    public void setRemaining(String remaining) {
        Remaining = remaining;
    }

    public String getFirstName() {
        return FirstName;
    }

    public void setFirstName(String firstName) {
        FirstName = firstName;
    }

    public String getMidName() {
        return MidName;
    }

    public void setMidName(String midName) {
        MidName = midName;
    }

    public String getLastName() {
        return LastName;
    }

    public void setLastName(String lastName) {
        LastName = lastName;
    }

    public String getEmail() {
        return Email;
    }

    public void setEmail(String email) {
        Email = email;
    }

    public String getPhone() {
        return Phone;
    }

    public void setPhone(String phone) {
        Phone = phone;
    }

    public String getID() {
        return ID;
    }

    public void setID(String ID) {
        this.ID = ID;
    }

    public String getCollege() {
        return College;
    }

    public void setCollege(String college) {
        College = college;
    }

    public String getYear() {
        return Year;
    }

    public void setYear(String year) {
        Year = year;
    }

    public String getSignature() {
        return Signature;
    }

    public void setSignature(String signature) {
        Signature = signature;
    }

    public String getDate() {
        return Date;
    }

    public void setDate(String date) {
        Date = date;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserUID() {
        return userUID;
    }

    public void setUserUID(String userUID) {
        this.userUID = userUID;
    }
}
