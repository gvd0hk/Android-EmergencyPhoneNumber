package com.promlert.emergencyphonenumber.fragment;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.promlert.emergencyphonenumber.App;
import com.promlert.emergencyphonenumber.R;
import com.promlert.emergencyphonenumber.adapter.MyListAdapter;
import com.promlert.emergencyphonenumber.model.Category;
import com.promlert.emergencyphonenumber.model.PhoneNumber;

import java.util.ArrayList;

public class PhoneListFragment extends Fragment {

    private static final String TAG = PhoneListFragment.class.getSimpleName();
    private static final String ARG_CATEGORY_LIST_INDEX = "category_list_index";

    private int mCategoryListIndex;
    private App mApp;

    public PhoneListFragment() {
        // Required empty public constructor
    }

    public static PhoneListFragment newInstance(int categoryListIndex) {
        PhoneListFragment fragment = new PhoneListFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_CATEGORY_LIST_INDEX, categoryListIndex);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mApp = App.getInstance();
        if (getArguments() != null) {
            mCategoryListIndex = getArguments().getInt(ARG_CATEGORY_LIST_INDEX);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_phone_list, container, false);
    }

    @Override
    public void onViewCreated(final View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        /* ถ้าหากแอคทิวิตีถูกสร้างขึ้นใหม่หลังจาก config change หรือโปรเซสถูกทำลาย
           โค้ดส่วนนี้จะทำก่อนโหลดข้อมูลในแอคทิวิตี ดังนั้นต้องเช็คและโหลดข้อมูลตรงนี้ */

        final ArrayList<Category> categoryList = mApp.getPhoneData();
        if (categoryList != null) {
            setupViews(view, categoryList);
        } else {
            final ProgressDialog progressDialog = ProgressDialog.show(
                    getActivity(),
                    null,
                    "รอสักครู่",
                    true,
                    false
            );
            mApp.loadPhoneData(new App.LoadPhoneDataCallback() {
                @Override
                public void onLoadFinish(ArrayList<Category> categoryList) {
                    progressDialog.dismiss();
                    setupViews(view, categoryList);
                }
            });
        }
    }

    private void setupViews(View rootView, ArrayList<Category> categoryList) {
        TextView categoryTitleTextView = (TextView) rootView.findViewById(R.id.category_title_text_view);
        categoryTitleTextView.setText(categoryList.get(mCategoryListIndex).title);

        final ArrayList<PhoneNumber> phoneNumberList = categoryList.get(mCategoryListIndex).phoneNumberList;

        ListView phoneNumberListView = (ListView) rootView.findViewById(R.id.phone_number_list_view);
        MyListAdapter adapter = new MyListAdapter(
                getContext(),
                R.layout.item_phone_number,
                phoneNumberList
        );
        phoneNumberListView.setAdapter(adapter);

        phoneNumberListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                makePhoneCall(phoneNumberList.get(position));
            }
        });
    }

    private void makePhoneCall(final PhoneNumber phoneNumber) {
        new AlertDialog.Builder(getContext())
                .setTitle("ยืนยันโทรออก")
                .setMessage("ต้องการโทรไปยัง \"" + phoneNumber.title + "\" (" + phoneNumber.number + ") ใช่หรือไม่?")
                .setPositiveButton("ใช่", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Intent intent = new Intent(Intent.ACTION_CALL);
                        intent.setData(Uri.parse("tel:" + phoneNumber.number));
                        startActivity(intent);
                    }
                })
                .setNegativeButton("ยกเลิก", null)
                .show();
    }
}
