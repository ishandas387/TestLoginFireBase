package com.ishan387.testlogin.model;

import java.util.Date;

/**
 * Created by ishan on 04-11-2017.
 */

class Orders {

    Date date;
    String orderId;
    String productId;
    String userName;
    String uid;
    String price;
    String productName;
    String serviceTime;

    public Orders() {
    }

    public Orders(Date date, String orderId, String productId) {

        this.date = date;
        this.orderId = orderId;
        this.productId = productId;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }

    public Date getDate() {
        return date;
    }

    public String getOrderId() {
        return orderId;
    }

    public String getProductId() {
        return productId;
    }
}
