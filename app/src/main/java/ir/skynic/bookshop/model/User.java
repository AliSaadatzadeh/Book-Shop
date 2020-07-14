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
    private String thumbNailImageLink;
    private int followingCount;
    private int followerCount;
    private int bookCount;
    private int isFollowing;

    public User() {

    }

    public User(String userName, String thumbNailImageLink, String name) {
        this.userName = userName;
        this.thumbNailImageLink = thumbNailImageLink;
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getUserName() {
        return userName;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public String getAddress() {
        return address;
    }

    public String getPostCode() {
        return postCode;
    }

    public String getShabaNumber() {
        return shabaNumber;
    }

    public int getCityId() {
        return cityId;
    }

    public String getImageLink() {
        return imageLink;
    }

    public int getFollowingCount() {
        return followingCount;
    }

    public int getFollowerCount() {
        return followerCount;
    }

    public int getBookCount() {
        return bookCount;
    }

    public String getThumbNailImageLink() {
        return thumbNailImageLink;
    }

    public int isFollowing() {
        return isFollowing;
    }
}
