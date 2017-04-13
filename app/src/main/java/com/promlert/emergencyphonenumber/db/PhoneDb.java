package com.promlert.emergencyphonenumber.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.promlert.emergencyphonenumber.model.Category;
import com.promlert.emergencyphonenumber.model.PhoneNumber;

import java.util.ArrayList;

/**
 * Created by Promlert on 2017-04-12.
 */

public class PhoneDb {

    private static final String TAG = PhoneDb.class.getSimpleName();

    private static final String DATABASE_NAME = "phone.db";
    private static final int DATABASE_VERSION = 1;

    // เทเบิล phone_number
    // +-----+-------+--------+------------+
    // | _id | title | number | categoryId |
    // +-----+-------+--------+------------+
    // |     |       |        |            |

    private static final String TABLE_PHONE_NUMBER = "phone_number";
    private static final String COL_ID = "_id";
    private static final String COL_TITLE = "title";
    private static final String COL_NUMBER = "number";
    private static final String COL_CATEGORY_ID = "category_id";

    private static final String SQL_CREATE_TABLE_PHONE_NUMBER = "CREATE TABLE " + TABLE_PHONE_NUMBER + "("
            + COL_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
            + COL_TITLE + " TEXT, "
            + COL_NUMBER + " TEXT, "
            + COL_CATEGORY_ID + " INTEGER"
            + ")";

    // เทเบิล category
    // +-----+-------+
    // | _id | title |
    // +-----+-------+
    // |     |       |

    private static final String TABLE_CATEGORY = "categoryId";

    private static final String SQL_CREATE_TABLE_CATEGORY = "CREATE TABLE " + TABLE_CATEGORY + "("
            + COL_ID + " INTEGER PRIMARY KEY, "
            + COL_TITLE + " TEXT"
            + ")";

    private static DatabaseHelper sDbHelper;
    private SQLiteDatabase mDatabase;

    public PhoneDb(Context context) {
        context = context.getApplicationContext();

        if (sDbHelper == null) {
            sDbHelper = new DatabaseHelper(context);
        }
        mDatabase = sDbHelper.getWritableDatabase();
    }

    public ArrayList<Category> getAllData() {
        final Cursor cursorCategory = mDatabase.query(
                TABLE_CATEGORY,
                null,
                null,
                null,
                null,
                null,
                COL_ID
        );

        final ArrayList<Category> categoryList = new ArrayList<>();
        while (cursorCategory.moveToNext()) {
            long categoryId = cursorCategory.getLong(cursorCategory.getColumnIndex(COL_ID));
            final Category category = new Category(
                    categoryId,
                    cursorCategory.getString(cursorCategory.getColumnIndex(COL_TITLE))
            );

            final Cursor cursorPhoneNumber = mDatabase.query(
                    TABLE_PHONE_NUMBER,
                    null,
                    COL_CATEGORY_ID + "=?",
                    new String[]{String.valueOf(categoryId)},
                    null,
                    null,
                    null
            );

            while (cursorPhoneNumber.moveToNext()) {
                category.phoneNumberList.add(
                        new PhoneNumber(
                                cursorPhoneNumber.getLong(cursorPhoneNumber.getColumnIndex(COL_ID)),
                                cursorPhoneNumber.getString(cursorPhoneNumber.getColumnIndex(COL_TITLE)),
                                cursorPhoneNumber.getString(cursorPhoneNumber.getColumnIndex(COL_NUMBER)),
                                cursorPhoneNumber.getInt(cursorPhoneNumber.getColumnIndex(COL_CATEGORY_ID))
                        )
                );
            }
            cursorPhoneNumber.close();
            categoryList.add(category);
        }
        cursorCategory.close();
        return categoryList;
    }

    private static class DatabaseHelper extends SQLiteOpenHelper {

        DatabaseHelper(Context context) {
            super(context, DATABASE_NAME, null, DATABASE_VERSION);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            db.execSQL(SQL_CREATE_TABLE_PHONE_NUMBER);
            db.execSQL(SQL_CREATE_TABLE_CATEGORY);

            insertInitialData(db);
        }

        private void insertInitialData(SQLiteDatabase db) {
            ArrayList<PhoneNumber> phoneNumberList = new ArrayList<>();

            phoneNumberList.add(new PhoneNumber(0, "สถาบันการแพทย์ฉุกเฉินแห่งชาติ", "1669", 1));
            phoneNumberList.add(new PhoneNumber(0, "ศูนย์เอราวัณ สำนักการแพทย์ กทม.", "1646", 1));
            phoneNumberList.add(new PhoneNumber(0, "ศูนย์ปลอดภัยคมนาคม", "1356", 1));
            phoneNumberList.add(new PhoneNumber(0, "ศูนย์เตือนภัยพิบัติแห่งชาติ", "192", 1));
            phoneNumberList.add(new PhoneNumber(0, "ศูนย์ส่งกลับและรถพยาบาล รพ.ตำรวจ", "1691", 1));

            phoneNumberList.add(new PhoneNumber(0, "เหตุด่วน เหตุร้าย แจ้งตำรวจ", "191", 2));
            phoneNumberList.add(new PhoneNumber(0, "ไฟไหม้ สัตว์เข้าบ้าน แจ้งดับเพลิง", "199", 2));
            phoneNumberList.add(new PhoneNumber(0, "ศูนย์ปลอดภัยทางน้ำ กรมเจ้าท่า", "1199", 2));
            phoneNumberList.add(new PhoneNumber(0, "กองอำนวยการรักษาความมั่นคงภายในราชอาณาจักร", "1374", 2));
            phoneNumberList.add(new PhoneNumber(0, "ศูนย์ปราบปรามการโจรกรรมรถ", "1192", 2));

            phoneNumberList.add(new PhoneNumber(0, "กรมทางหลวง มอเตอร์เวย์", "1586", 3));
            phoneNumberList.add(new PhoneNumber(0, "กรมทางหลวงชนบท", "1146", 3));
            phoneNumberList.add(new PhoneNumber(0, "ตำรวจทางหลวง", "1193", 3));
            phoneNumberList.add(new PhoneNumber(0, "จส.100", "1137", 3));
            phoneNumberList.add(new PhoneNumber(0, "ศูนย์ควบคุมและสั่งการจราจร บก.02", "1197", 3));

            phoneNumberList.add(new PhoneNumber(0, "บริษัท ขนส่ง จำกัด (บขส.)", "1490", 4));
            phoneNumberList.add(new PhoneNumber(0, "การรถไฟแห่งประเทศไทย", "1690", 4));
            phoneNumberList.add(new PhoneNumber(0, "การท่าอากาศยานฯ", "0-2535-1722", 4));
            phoneNumberList.add(new PhoneNumber(0, "กรมการขนส่งทางบก", "1584", 4));
            phoneNumberList.add(new PhoneNumber(0, "กรมท่าอากาศยาน", "0-2287-0320", 4));

            phoneNumberList.add(new PhoneNumber(0, "กรมสุขภาพจิต", "1323", 5));
            phoneNumberList.add(new PhoneNumber(0, "ศูนย์ประชาบดี ปัญหาสังคม เด็ก", "1300", 5));
            phoneNumberList.add(new PhoneNumber(0, "ศูนย์ดำรงธรรม", "1567", 5));
            phoneNumberList.add(new PhoneNumber(0, "ตำรวจท่องเที่ยว", "1155", 5));
            phoneNumberList.add(new PhoneNumber(0, "ศูนย์บริการข้อมูลภาครัฐเพื่อประชาชน", "1111", 5));

            for (PhoneNumber phoneNumber : phoneNumberList) {
                ContentValues cv = new ContentValues();
                cv.put(COL_TITLE, phoneNumber.title);
                cv.put(COL_NUMBER, phoneNumber.number);
                cv.put(COL_CATEGORY_ID, phoneNumber.categoryId);
                long result = db.insert(TABLE_PHONE_NUMBER, null, cv);
                if (result == -1) {
                    Log.e(TAG, "Error insert phone number data: " + phoneNumber.title);
                }
            }

            ArrayList<Category> categoryList = new ArrayList<>();

            categoryList.add(new Category(1, "อุบัติเหตุ กู้ชีพ ฉุกเฉิน"));
            categoryList.add(new Category(2, "เหตุด่วน เหตุร้าย"));
            categoryList.add(new Category(3, "เส้นทาง การจราจร"));
            categoryList.add(new Category(4, "เดินทาง ขนส่ง"));
            categoryList.add(new Category(5, "ทั่วไป สารพัด"));

            for (Category category : categoryList) {
                ContentValues cv = new ContentValues();
                cv.put(COL_ID, category.id);
                cv.put(COL_TITLE, category.title);
                long result = db.insert(TABLE_CATEGORY, null, cv);
                if (result == -1) {
                    Log.e(TAG, "Error insert category data: " + category.title);
                }
            }
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        }
    }
}
