package com.promlert.emergencyphonenumber.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.promlert.emergencyphonenumber.App;
import com.promlert.emergencyphonenumber.fragment.PhoneListFragment;

/**
 * Created by Promlert on 2017-04-13.
 */

public class MyPagerAdapter extends FragmentPagerAdapter {

    public MyPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        return PhoneListFragment.newInstance(position);
    }

    @Override
    public CharSequence getPageTitle(int position) {
        App app = App.getInstance();
        return app.getPhoneData().get(position).title;
    }

    @Override
    public int getCount() {
        App app = App.getInstance();
        return app.getPhoneData().size();
    }
}
