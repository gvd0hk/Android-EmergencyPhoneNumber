package com.promlert.emergencyphonenumber.model;

import java.util.ArrayList;

/**
 * Created by Promlert on 2017-04-12.
 */

public class Category {

    public final long id;
    public final String title;
    public final ArrayList<PhoneItem> phoneItemList = new ArrayList<>();

    public Category(long id, String title) {
        this.id = id;
        this.title = title;
    }
}
