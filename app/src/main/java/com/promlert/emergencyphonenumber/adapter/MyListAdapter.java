package com.promlert.emergencyphonenumber.adapter;

import android.content.Context;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.promlert.emergencyphonenumber.R;
import com.promlert.emergencyphonenumber.model.PhoneItem;

import java.util.ArrayList;

import static android.content.Context.LAYOUT_INFLATER_SERVICE;

/**
 * Created by Promlert on 2017-04-13.
 */

public class MyListAdapter extends ArrayAdapter<PhoneItem> {

    private static final String TAG = MyListAdapter.class.getName();

    private Context mContext;
    private int mResource;
    private ArrayList<PhoneItem> mPhoneItemList;

    public MyListAdapter(@NonNull Context context, @LayoutRes int resource, @NonNull ArrayList<PhoneItem> phoneItemList) {
        super(context, resource, phoneItemList);
        this.mContext = context;
        this.mResource = resource;
        this.mPhoneItemList = phoneItemList;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View itemView = convertView;
        ViewHolder viewHolder = null;

        if (itemView == null) {
            LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(LAYOUT_INFLATER_SERVICE);
            itemView = inflater.inflate(mResource, parent, false);

            viewHolder = new ViewHolder();
            viewHolder.phoneTitleTextView = itemView.findViewById(R.id.phone_title_text_view);
            viewHolder.phoneNumberTextView = itemView.findViewById(R.id.phone_number_text_view);
            viewHolder.logoImageView = itemView.findViewById(R.id.logo_image_view);

            itemView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) itemView.getTag();
        }

        PhoneItem phoneItem = mPhoneItemList.get(position);
        viewHolder.phoneTitleTextView.setText(phoneItem.title);
        viewHolder.phoneNumberTextView.setText(phoneItem.number);
        viewHolder.logoImageView.setImageDrawable(phoneItem.getImageDrawable(mContext));

        return itemView;
    }

    @Override
    public int getCount() {
        return mPhoneItemList.size();
    }

    static class ViewHolder {
        TextView phoneTitleTextView, phoneNumberTextView;
        ImageView logoImageView;
    }
}
