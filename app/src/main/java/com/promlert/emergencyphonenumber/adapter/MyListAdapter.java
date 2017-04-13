package com.promlert.emergencyphonenumber.adapter;

import android.content.Context;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.promlert.emergencyphonenumber.R;
import com.promlert.emergencyphonenumber.model.PhoneNumber;

import java.util.ArrayList;

import static android.content.Context.LAYOUT_INFLATER_SERVICE;

/**
 * Created by Promlert on 2017-04-13.
 */

public class MyListAdapter extends ArrayAdapter<PhoneNumber> {

    private Context mContext;
    private int mResource;
    private ArrayList<PhoneNumber> mPhoneNumberList;

    public MyListAdapter(@NonNull Context context, @LayoutRes int resource, @NonNull ArrayList<PhoneNumber> phoneNumberList) {
        super(context, resource, phoneNumberList);
        this.mContext = context;
        this.mResource = resource;
        this.mPhoneNumberList = phoneNumberList;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View itemView = convertView;

        if (itemView == null) {
            LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(LAYOUT_INFLATER_SERVICE);
            itemView = inflater.inflate(mResource, parent, false);
        }

        TextView phoneTitleTextView = (TextView) itemView.findViewById(R.id.phone_title_text_view);
        phoneTitleTextView.setText(mPhoneNumberList.get(position).title);

        TextView phoneNumberTextView = (TextView) itemView.findViewById(R.id.phone_number_text_view);
        phoneNumberTextView.setText(mPhoneNumberList.get(position).number);

        return itemView;
    }

    @Override
    public int getCount() {
        return mPhoneNumberList.size();
    }
}
