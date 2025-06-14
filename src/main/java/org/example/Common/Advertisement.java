package org.example.Common;

import java.io.Serializable;

public class Advertisement implements Serializable {
    public String title;
    public String description;
    public Integer price;
    public String contacts;
    public String[] tags;

    public Advertisement(String title, String description, int price, String contacts, String[] tags) {
        this.title = title;
        this.description = description;
        this.price = price;
        this.contacts = contacts;
        this.tags = tags;
    }
}
