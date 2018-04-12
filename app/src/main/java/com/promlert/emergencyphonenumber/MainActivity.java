package com.promlert.emergencyphonenumber;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.ogaclejapan.smarttablayout.SmartTabLayout;
import com.promlert.emergencyphonenumber.adapter.MyPagerAdapter;
import com.promlert.emergencyphonenumber.model.Category;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = MainActivity.class.getSimpleName();

    //private ArrayList<Category> mCategoryList;
    private ViewPager mViewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setTitle(getString(R.string.app_name));

        TextView adsTextView = (TextView) findViewById(R.id.ads_text_view);
        adsTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showAds();
            }
        });

        App app = App.getInstance();
        if (app.getPhoneData() != null) {
            //mCategoryList = app.getPhoneData();
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
                    //mCategoryList = categoryList;
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

            for (PhoneItem phoneNumber : category.phoneItemList) {
                Log.i(TAG, phoneNumber.title + ": " + phoneNumber.number);
            }
        }
*/
    }

    private void showAds() {
        LayoutInflater inflater = getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.dialog_ads, null);

        new AlertDialog.Builder(this, R.style.AlertDialogTheme)
                .setView(dialogView)
                .setPositiveButton("รายละเอียดเพิ่มเติม", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String url = "http://3bugs.com/online-course-android-workshop-for-beginners/";
                        Intent intent = new Intent(Intent.ACTION_VIEW);
                        intent.setData(Uri.parse(url));
                        startActivity(intent);
                    }
                })
                .setNegativeButton("ปิด", null)
                .show();
    }

    private void setupViewPagerAndTabs() {
        MyPagerAdapter adapter = new MyPagerAdapter(getSupportFragmentManager());
        mViewPager = (ViewPager) findViewById(R.id.view_pager);
        mViewPager.setAdapter(adapter);

        SmartTabLayout tabLayout = (SmartTabLayout) findViewById(R.id.tab_layout);
        tabLayout.setViewPager(mViewPager);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.activity_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_email) {
            LayoutInflater inflater = getLayoutInflater();
            View dialogView = inflater.inflate(R.layout.dialog_contact, null);

            new AlertDialog.Builder(this, R.style.AlertDialogTheme)
                    .setTitle("ติดต่อผู้พัฒนา")
                    .setView(dialogView)
                    /*.setMessage("แจ้งแก้ไข/เพิ่มข้อมูล แนะนำ ติชม หรือส่งความคิดเห็นใดๆถึงผู้พัฒนา ได้ที่ promlert@gmail.com")*/
                    .setPositiveButton("เขียนเมลตอนนี้เลย", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            Intent intent = new Intent(
                                    Intent.ACTION_SENDTO,
                                    Uri.fromParts("mailto", "promlert@gmail.com", null)
                            );
                            intent.putExtra(Intent.EXTRA_SUBJECT, "ข้อความจากผู้ใช้แอพ 'เบอร์โทรฉุกเฉิน'");
                            intent.putExtra(Intent.EXTRA_TEXT, "พิมพ์ข้อความที่ต้องการส่งถึงผู้พัฒนาแอพตรงนี้ครับ\n\n");
                            startActivity(Intent.createChooser(intent, "ส่งเมล..."));
                        }
                    })
                    .setNegativeButton("ปิด", null)
                    .show();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
