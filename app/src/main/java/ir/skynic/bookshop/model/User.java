package ir.skynic.bookshop.model;

public class User {

    private int id;
    private String name;
    private String userName;
    private String phoneNumber;
    private String address;
    private String postCode;
    private String shabaNumber;
    private int cityId;
    private String imageLink;

    public User() {

    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPostCode() {
        return postCode;
    }

    public void setPostCode(String postCode) {
        this.postCode = postCode;
    }

    public String getShabaNumber() {
        return shabaNumber;
    }

    public void setShabaNumber(String shabaNumber) {
        this.shabaNumber = shabaNumber;
    }

    public int getCityId() {
        return cityId;
    }

    public void setCityId(int cityId) {
        this.cityId = cityId;
    }

    public String getImageLink() {
        return imageLink;
    }

    public void setImageLink(String imageLink) {
        this.imageLink = imageLink;
    }

    public boolean fetchAll() {
        return false;
    }

    public boolean updateAll() {
        return false;
    }
}
