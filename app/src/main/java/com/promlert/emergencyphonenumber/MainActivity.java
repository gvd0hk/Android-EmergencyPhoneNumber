package com.promlert.emergencyphonenumber;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;

import com.promlert.emergencyphonenumber.adapter.MyPagerAdapter;
import com.promlert.emergencyphonenumber.model.Category;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = MainActivity.class.getSimpleName();

    private ArrayList<Category> mCategoryList;
    private ViewPager mViewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        App app = App.getInstance();

        if (app.getPhoneData() != null) {
            mCategoryList = app.getPhoneData();
            setupViewPagerAndTabs();
        } else {
            final ProgressDialog progressDialog = ProgressDialog.show(
                    this,
                    null,
                    "รอสักครู่",
                    true,
                    false
            );
            app.loadPhoneData(new App.LoadPhoneDataCallback() {
                @Override
                public void onLoadFinish(ArrayList<Category> categoryList) {
                    mCategoryList = categoryList;
                    progressDialog.dismiss();
                    setupViewPagerAndTabs();
                }
            });
        }

/*
        PhoneDb phoneDb = new PhoneDb(this);
        for (Category category : phoneDb.getAllData()) {
            Log.i(TAG, "###");
            Log.i(TAG, "Category: " + category.title);

            for (PhoneNumber phoneNumber : category.phoneNumberList) {
                Log.i(TAG, phoneNumber.title + ": " + phoneNumber.number);
            }
        }
*/
    }

    private void setupViewPagerAndTabs() {
        MyPagerAdapter adapter = new MyPagerAdapter(getSupportFragmentManager());
        mViewPager = (ViewPager) findViewById(R.id.view_pager);
        mViewPager.setAdapter(adapter);

        TabLayout tabLayout = (TabLayout) findViewById(R.id.tab_layout);
        tabLayout.setupWithViewPager(mViewPager);
    }
}
