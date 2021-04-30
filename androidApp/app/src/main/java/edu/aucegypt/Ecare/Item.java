package edu.aucegypt.Ecare;

import java.util.Collection;
import java.util.Set;

public class Item {
    String buttonListName;
    String buttonListRecName;
    String buttonListEmail;
    String buttonListPost;
    String buttonListUid;
    Set<String> buttonListUid2;
    Collection<Object> buttonListValue2;
    int buttonListImage;

    public Item(String name, int image, String s)
    {
        this.buttonListImage=image;
        this.buttonListName=name;
        this.buttonListPost=s;
    }

    public Item(String senderName, String recName, int image, String s)
    {
        this.buttonListImage=image;
        this.buttonListName=senderName;
        this.buttonListRecName=recName;
        this.buttonListPost=s;
    }


    public Item(String s, int profile) {
        this.buttonListImage=profile;
        this.buttonListName=s;
    }

    public Item(String name, String email, int image) {
        this.buttonListName=name;
        this.buttonListEmail=email;
        this.buttonListImage=image;
    }

    public Item(String uid) {
        this.buttonListUid=uid;
    }

    public Item(Set<String> keySet, int profile, Collection<Object> values) {
        this.buttonListUid2=keySet;
        this.buttonListValue2=values;
        this.buttonListImage=profile;
    }

    public String getbuttonName()
    {
        return buttonListName;
    }
    public String getbuttonRecName()
    {
        return buttonListRecName;
    }
    public String getbuttonUid()
    {
        return buttonListUid;
    }
    public String getbuttonEmail()
    {
        return buttonListEmail;
    }
    public String getbuttonPost()
    {
        return buttonListPost;
    }
    public int getbuttonImage()
    {
        return buttonListImage;
    }
}

