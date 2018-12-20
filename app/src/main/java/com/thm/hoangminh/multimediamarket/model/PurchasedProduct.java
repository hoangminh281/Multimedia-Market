package com.thm.hoangminh.multimediamarket.model;

public class PurchasedProduct {
    private String cateId;
    private String productId;
    private String userId;
    private String dateTime;

    public PurchasedProduct(String cateId, String productId, String userId, String dateTime) {
        this.cateId = cateId;
        this.productId = productId;
        this.userId = userId;
        this.dateTime = dateTime;
    }

    public PurchasedProduct() {}

    public String getCateId() {
        return cateId;
    }

    public void setCateId(String cateId) {
        this.cateId = cateId;
    }

    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getDateTime() {
        return dateTime;
    }

    public void setDateTime(String dateTime) {
        this.dateTime = dateTime;
    }
}
