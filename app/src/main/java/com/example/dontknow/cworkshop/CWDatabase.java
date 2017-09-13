package com.example.dontknow.cworkshop;

/**
 * Created by Dont know on 25-07-2017.
 */

public class CWDatabase {
    private int _id;
    private String _firstname;
    private String _midname;
    private String _lastname;
    private String _email;
    private String _phone;
    private String _college;
    private String _year;
    private String _image;
    private String _remaining;
    private String _status;


    public CWDatabase(){}

    public String get_status() {
        return _status;
    }

    public void set_status(String _status) {
        this._status = _status;
    }

    public CWDatabase(String _firstname, String _midname, String _lastname, String _email, String _phone, String _college, String _year, String _image, String _remaining, String _status) {
        this._firstname = _firstname;
        this._midname = _midname;
        this._lastname = _lastname;
        this._email = _email;
        this._phone = _phone;
        this._college = _college;
        this._year = _year;
        this._image = _image;
        this._remaining = _remaining;
        this._status = _status;

    }

    public int get_id() {
        return _id;
    }

    public void set_id(int _id) {
        this._id = _id;
    }

    public String get_firstname() {
        return _firstname;
    }

    public void set_firstname(String _firstname) {
        this._firstname = _firstname;
    }

    public String get_midname() {
        return _midname;
    }

    public void set_midname(String _midname) {
        this._midname = _midname;
    }

    public String get_lastname() {
        return _lastname;
    }

    public void set_lastname(String _lastname) {
        this._lastname = _lastname;
    }

    public String get_email() {
        return _email;
    }

    public void set_email(String _email) {
        this._email = _email;
    }

    public String get_phone() {
        return _phone;
    }

    public void set_phone(String _phone) {
        this._phone = _phone;
    }

    public String get_college() {
        return _college;
    }

    public void set_college(String _college) {
        this._college = _college;
    }

    public String get_year() {
        return _year;
    }

    public void set_year(String _year) {
        this._year = _year;
    }

    public String get_image() {
        return _image;
    }

    public void set_image(String _image) {
        this._image = _image;
    }

    public String get_remaining() {
        return _remaining;
    }

    public void set_remaining(String _remaining) {
        this._remaining = _remaining;
    }
}
