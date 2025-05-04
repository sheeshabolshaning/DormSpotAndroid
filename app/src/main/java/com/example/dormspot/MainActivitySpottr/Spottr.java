package com.example.dormspot.MainActivitySpottr;

public class Spottr {
    private String firstName;
    private String lastName;
    private String userName;
    private String email;
    private String pw;
    private String cpw; //confirm password
    private int phoneNumber;

    public Spottr(){

    }

    public Spottr(String firstName, String lastName, String userName, String email, String pw, String cpw, int phoneNumber){
        this.firstName = firstName;
        this.lastName = lastName;
        this.userName = userName;
        this.email = email;
        this.pw = pw;
        this.cpw = cpw;
        this.phoneNumber = phoneNumber;
    }

    public String getFirstName(){
        return firstName;
    }

    public String getLastName(){
        return lastName;
    }

    public String getUserName(){
        return userName;
    }

    public String getEmail(){
        return email;
    }

    public String getPw(){
        return pw;
    }

    public String getCpw(){
        return cpw;
    }

    public int getPhoneNumber() {
        return phoneNumber;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public void setPw(String pw) {
        this.pw = pw;
    }

    public void setCpw(String cpw) {
        this.cpw = cpw;
    }

    public void setPhoneNumber(int phoneNumber) {
        this.phoneNumber = phoneNumber;
    }
}
