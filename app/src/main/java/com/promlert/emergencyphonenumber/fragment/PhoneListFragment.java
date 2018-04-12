package com.promlert.emergencyphonenumber.fragment;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.LayoutAnimationController;
import android.view.animation.TranslateAnimation;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.promlert.emergencyphonenumber.App;
import com.promlert.emergencyphonenumber.R;
import com.promlert.emergencyphonenumber.adapter.MyListAdapter;
import com.promlert.emergencyphonenumber.model.Category;
import com.promlert.emergencyphonenumber.model.PhoneItem;

import java.util.ArrayList;
import java.util.Locale;

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

        final ArrayList<PhoneItem> phoneItemList = categoryList.get(mCategoryListIndex).phoneItemList;

        ListView phoneNumberListView = (ListView) rootView.findViewById(R.id.phone_number_list_view);
        MyListAdapter adapter = new MyListAdapter(
                getContext(),
                R.layout.item_phone_number,
                phoneItemList
        );
        phoneNumberListView.setAdapter(adapter);

        phoneNumberListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //makePhoneCall(phoneItemList.get(position));
                showChooseNumberDialog(phoneItemList.get(position));
            }
        });

        //animateListView(phoneNumberListView);
    }

    private void showChooseNumberDialog(final PhoneItem phoneItem) {
        String titleTemp = phoneItem.title;
        if (phoneItem.categoryId == 3) {
            titleTemp = String.format(Locale.getDefault(), "ศูนย์รับแจ้งเหตุฉุกเฉิน จ.%s", phoneItem.title);
        }
        final String title = titleTemp;

        if (phoneItem.number.length() > 12) {
            int lastIndexOfDash = phoneItem.number.lastIndexOf('-');
            if (lastIndexOfDash != -1) {
                String end = phoneItem.number.substring(lastIndexOfDash + 1);
                int numDigit = end.length();
                if (numDigit <= 2) {
                    String mainNumber = phoneItem.number.substring(0, lastIndexOfDash - numDigit);
                    String start = phoneItem.number.substring(lastIndexOfDash - numDigit, lastIndexOfDash);

                    ArrayList<String> phoneNumberList = new ArrayList<>();
                    for (int i = Integer.valueOf(start); i <= Integer.valueOf(end); i++) {
                        phoneNumberList.add(mainNumber + String.format(
                                Locale.getDefault(), "%" + String.valueOf(numDigit) + "s", i).replace(' ', '0')
                        );
                    }

/*
                    for (String s : phoneNumberList) {
                        Log.i(TAG, s);
                    }
*/

                    String[] phoneNumbersTemp = new String[phoneNumberList.size()];
                    phoneNumbersTemp = phoneNumberList.toArray(phoneNumbersTemp);
                    final String[] phoneNumbers = phoneNumbersTemp;

                    new AlertDialog.Builder(getContext())
                            .setTitle("เลือกเบอร์โทร")
                            .setItems(phoneNumbers, new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    makePhoneCall(
                                            title,
                                            phoneNumbers[i],
                                            phoneItem.getImageDrawable(getContext())
                                    );
                                }
                            })
                            .show();
                } else {
                    makePhoneCall(
                            title, phoneItem.number, phoneItem.getImageDrawable(getContext())
                    );
                }
            } else {
                makePhoneCall(
                        title, phoneItem.number, phoneItem.getImageDrawable(getContext())
                );
            }
        } else {
            makePhoneCall(
                    title, phoneItem.number, phoneItem.getImageDrawable(getContext())
            );
        }
    }

    private void makePhoneCall(String title, final String number, Drawable logo) {
        LayoutInflater inflater = getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.dialog_confirm_call, null);

        ImageView logoImageView = dialogView.findViewById(R.id.logo_image_view);
        logoImageView.setImageDrawable(logo);

        String confirmMessage = String.format(
                Locale.getDefault(),
                "ต้องการโทรไปยัง %s (%s) หรือไม่?",
                title, number
        );
        TextView confirmMessageTextView = dialogView.findViewById(R.id.confirm_message_text_view);
        confirmMessageTextView.setText(confirmMessage);

        new AlertDialog.Builder(getContext(), R.style.AlertDialogTheme)
                /*.setTitle("ยืนยันโทรออก")*/
                .setView(dialogView)
                /*.setMessage("ต้องการโทรไปยัง \"" + phoneItem.title + "\" (" + phoneItem.number + ") ใช่หรือไม่?")*/
                .setPositiveButton("โทร", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Intent intent = new Intent(Intent.ACTION_CALL);
                        intent.setData(Uri.parse("tel:" + number));
                        startActivity(intent);
                    }
                })
                .setNegativeButton("ยกเลิก", null)
                .show();
    }

    private void animateListView(ListView listView) {
        AnimationSet set = new AnimationSet(true);

        Animation animation = new AlphaAnimation(0.0f, 1.0f);
        animation.setDuration(300);
        set.addAnimation(animation);

        animation = new TranslateAnimation(
                Animation.RELATIVE_TO_SELF, 0.0f, Animation.RELATIVE_TO_SELF, 0.0f,
                Animation.RELATIVE_TO_SELF, -1.0f, Animation.RELATIVE_TO_SELF, 0.0f
        );
        animation.setDuration(300);
        set.addAnimation(animation);

        LayoutAnimationController controller = new LayoutAnimationController(set, 0.25f);
        listView.setLayoutAnimation(controller);
    }
}
