package com.promlert.emergencyphonenumber.model;

/**
 * Created by Promlert on 2017-04-12.
 */

public class PhoneNumber {

    public final long id;
    public final String title;
    public final String number;
    public final int categoryId;

    public PhoneNumber(long id, String title, String number, int categoryId) {
        this.id = id;
        this.title = title;
        this.number = number;
        this.categoryId = categoryId;
    }
}
