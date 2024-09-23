package com.example.apptransport.Entity;

public class SocialLoginData {
    private String firstName;
    private String lastName;
    private String email;
    private String socialid;

    public SocialLoginData(){}

    public SocialLoginData(String firstName, String lastName, String email, String socialid)
    {
        this.firstName=firstName;
        this.lastName=lastName;
        this.email = email;
        this.socialid=socialid;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getSocialid() {
        return socialid;
    }

    public void setSocialid(String socialid) {
        this.socialid = socialid;
    }
}
