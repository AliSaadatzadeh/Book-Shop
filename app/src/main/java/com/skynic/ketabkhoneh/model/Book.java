package com.skynic.ketabkhoneh.model;

public class Book {

    private int id;
    private String title;
    private String author;
    private int categoryId;
    private int cityId;
    private int price;
    private String translator;
    private String publisher;
    private int publicationYear;
    private String description;
    private String imageLink;
    private String sellerImage;
    private String sellerName;
    private String thumbnailImageLink;
    private int bookStatus;
    private int off;
    private String seller;
    private String date;
    private String trackCode;
    private int transferring;

    public int getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getAuthor() {
        return author;
    }

    public int getCategoryId() {
        return categoryId;
    }

    public int getCityId() {
        return cityId;
    }

    public int getPrice() {
        return price;
    }

    public String getTranslator() {
        return translator;
    }

    public String getPublisher() {
        return publisher;
    }

    public int getPublicationYear() {
        return publicationYear;
    }

    public String getDescription() {
        return description;
    }

    public String getImageLink() {
        return imageLink;
    }

    public String getThumbnailImageLink() {
        return thumbnailImageLink;
    }

    public int getBookStatus() {
        return bookStatus;
    }

    public int getOff() {
        return off;
    }

    public String getSeller() {
        return seller;
    }

    public String getDate() {
        return date;
    }

    public int getTransferring() {
        return transferring;
    }

    public String getSellerImage() {
        return sellerImage;
    }

    public void setSellerImage(String userImageLink) {
        sellerImage = userImageLink;
    }

    public String getSellerName() {
        return sellerName;
    }

    public void setSellerName(String sellerName) {
        this.sellerName = sellerName;
    }

    public String getTrackCode() {
        return trackCode;
    }

    public Book() {

    }


}
