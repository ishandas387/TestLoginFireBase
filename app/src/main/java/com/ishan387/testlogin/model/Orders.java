package com.ishan387.testlogin.model;

import java.util.Date;
import java.util.List;

/**
 * Created by ishan on 04-11-2017.
 */

public class Orders {

    String timeStamp;
    String orderId;
    String productId;
    String userName;
    String uid;
    List<Product> products;
    String serviceTime;
    String total;

    public Orders(String timeStamp, String orderId, String productId, String userName, String uid, List<Product> products, String serviceTime, String total, String userPhoneNumber) {
        this.timeStamp = timeStamp;
        this.orderId = orderId;
        this.productId = productId;
        this.userName = userName;
        this.uid = uid;
        this.products = products;
        this.serviceTime = serviceTime;
        this.total = total;
        this.userPhoneNumber = userPhoneNumber;
    }

    public String getTotal() {
        return total;
    }

    public void setTotal(String total) {
        this.total = total;
    }

    String userPhoneNumber;

    public String getUserName() {
        return userName;
    }

    public String getUid() {
        return uid;
    }

    public List<Product> getProducts() {
        return products;
    }

    public String getServiceTime() {
        return serviceTime;
    }

    public void setUserName(String userName) {

        this.userName = userName;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public void setProducts(List<Product> products) {
        this.products = products;
    }

    public void setServiceTime(String serviceTime) {
        this.serviceTime = serviceTime;
    }


    public void setUserPhoneNumber(String userPhoneNumber) {
        this.userPhoneNumber = userPhoneNumber;
    }

    public String getUserPhoneNumber() {
        return userPhoneNumber;
    }



    public Orders() {
    }



    public void setTimeStamp(String timeStamp) {
        this.timeStamp = timeStamp;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }

    public String getTimeStamp() {
        return timeStamp;
    }

    public String getOrderId() {
        return orderId;
    }

    public String getProductId() {
        return productId;
    }
}
