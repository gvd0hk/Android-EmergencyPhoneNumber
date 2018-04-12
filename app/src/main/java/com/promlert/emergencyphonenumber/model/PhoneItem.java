package com.promlert.emergencyphonenumber.model;

import android.content.Context;
import android.content.res.AssetManager;
import android.graphics.drawable.Drawable;
import android.util.Log;

import java.io.IOException;
import java.io.InputStream;

/**
 * Created by Promlert on 2017-04-12.
 */

public class PhoneItem {

    private static final String TAG = PhoneItem.class.getName();

    public final long id;
    public final String title;
    public final String number;
    public final String image;
    public final int categoryId;

    public PhoneItem(long id, String title, String number, String image, int categoryId) {
        this.id = id;
        this.title = title;
        this.number = number;
        this.image = image;
        this.categoryId = categoryId;
    }

    public Drawable getImageDrawable(Context context) {
        if (!"".equals(image)) {
            InputStream stream = null;
            AssetManager am = context.getAssets();
            try {
                stream = am.open(image);
            } catch (IOException e) {
                e.printStackTrace();
                Log.e(TAG, "Error opening asset file: " + image);
            }
            return Drawable.createFromStream(stream, null);
        } else {
            return null;
        }
    }
}
