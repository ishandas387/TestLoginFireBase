package com.ishan387.testlogin.model;

import java.util.List;

/**
 * Created by ishan on 04-11-2017.
 */

public class UserDetails {

    String key;

    public void setKey(String key) {
        this.key = key;
    }

    public String getKey() {

        return key;
    }



    String userEmail;
    boolean isAdmin;

    String uId;

    public UserDetails(String key, String userEmail, boolean isAdmin, String uId) {
        this.key = key;
        this.userEmail = userEmail;
        this.isAdmin = isAdmin;
        this.uId = uId;
    }

    List<Orders> pastOrderList;

    public UserDetails() {
    }

    public UserDetails(String userEmail, boolean isAdmin, List<Orders> pastOrderList) {
        this.userEmail = userEmail;
        this.isAdmin = isAdmin;
        this.pastOrderList = pastOrderList;
    }

    public String getUserEmail() {
        return userEmail;
    }

    public boolean isAdmin() {
        return isAdmin;
    }

    public List<Orders> getPastOrderList() {
        return pastOrderList;
    }

    public void setUserEmail(String userEmail) {
        this.userEmail = userEmail;
    }

    public void setAdmin(boolean admin) {
        isAdmin = admin;
    }

    public void setPastOrderList(List<Orders> pastOrderList) {
        this.pastOrderList = pastOrderList;
    }

    public void setuId(String uId) {
        this.uId = uId;
    }

    public String getuId() {

        return uId;
    }
}
