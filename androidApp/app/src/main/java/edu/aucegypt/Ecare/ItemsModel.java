package edu.aucegypt.Ecare;

import java.io.Serializable;

public class ItemsModel implements Serializable {
    private String name;
    private String email;
//    private String desc;
//    private String seller;
//    private String id;
    private int image;

    public ItemsModel(String name, String email, int image) {
        this.name = name;
        this.email = email;
//        this.desc = desc;
//        this.seller = seller;
//        this.id = id;
        this.image = image;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String price) {
        this.email = email;
    }

//    public String getDesc() {
//        return desc;
//    }
//
//    public void setDesc(String desc) {
//        this.desc = desc;
//    }
//
//    public String getSeller() {
//        return seller;
//    }
//
//    public void setSeller(String seller) {
//        this.seller = seller;
//    }
//
//    public String getId() {
//        return id;
//    }
//
//    public void setId(String id) {
//        this.id = id;
//    }

    public int getImage() {
        return image;
    }

    public void setImage(int image) {
        this.image = image;
    }
}

