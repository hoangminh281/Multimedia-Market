package com.thm.hoangminh.multimediamarket.model;

public class Product {
    private String productId;
    private String cateId;
    private String title;
    private String photoId;
    private double rating;
    private double price;
    private int status;

    public Product() {
    }

    public Product(String productId, String cateId, String title, String photoId, double rating, double price, int status) {
        this.productId = productId;
        this.cateId = cateId;
        this.title = title;
        this.photoId = photoId;
        this.rating = rating;
        this.price = price;
        this.status = status;
    }

    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }

    public String getCateId() {
        return cateId;
    }

    public void setCateId(String cateId) {
        this.cateId = cateId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getPhotoId() {
        return photoId;
    }

    public void setPhotoId(String photoId) {
        this.photoId = photoId;
    }

    public double getRating() {
        return rating;
    }

    public void setRating(double rate) {
        this.rating = rate;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }
}
