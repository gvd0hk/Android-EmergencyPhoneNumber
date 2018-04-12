package com.promlert.emergencyphonenumber.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.promlert.emergencyphonenumber.model.Category;
import com.promlert.emergencyphonenumber.model.PhoneItem;

import java.util.ArrayList;

/**
 * Created by Promlert on 2017-04-12.
 */

public class PhoneDb {

    private static final String TAG = PhoneDb.class.getSimpleName();

    private static final String DATABASE_NAME = "phone.db";
    private static final int DATABASE_VERSION = 5;

    // เทเบิล phone_number
    // +-----+-------+--------+-------+------------+
    // | _id | title | number | image | categoryId |
    // +-----+-------+--------+-------+------------+
    // |     |       |        |       |            |

    private static final String TABLE_PHONE_NUMBER = "phone_number";
    private static final String COL_ID = "_id";
    private static final String COL_TITLE = "title";
    private static final String COL_NUMBER = "number";
    private static final String COL_IMAGE = "image";
    private static final String COL_CATEGORY_ID = "category_id";

    private static final String SQL_CREATE_TABLE_PHONE_NUMBER = "CREATE TABLE " + TABLE_PHONE_NUMBER + "("
            + COL_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
            + COL_TITLE + " TEXT, "
            + COL_NUMBER + " TEXT, "
            + COL_IMAGE + " TEXT, "
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
                category.phoneItemList.add(
                        new PhoneItem(
                                cursorPhoneNumber.getLong(cursorPhoneNumber.getColumnIndex(COL_ID)),
                                cursorPhoneNumber.getString(cursorPhoneNumber.getColumnIndex(COL_TITLE)),
                                cursorPhoneNumber.getString(cursorPhoneNumber.getColumnIndex(COL_NUMBER)),
                                cursorPhoneNumber.getString(cursorPhoneNumber.getColumnIndex(COL_IMAGE)),
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
            ArrayList<PhoneItem> phoneItemList = new ArrayList<>();

            phoneItemList.add(new PhoneItem(0, "สถาบันการแพทย์ฉุกเฉินแห่งชาติ", "1669", "emergency_medicine_1669.png", 1));
            phoneItemList.add(new PhoneItem(0, "ศูนย์เอราวัณ สำนักการแพทย์ กทม.", "1646", "bangkok_ems_1646.png", 1));
            phoneItemList.add(new PhoneItem(0, "หน่วยกู้ชีพ วชิรพยาบาล", "1554", "vajira_emergency_1554.png", 1));
            phoneItemList.add(new PhoneItem(0, "ศูนย์ปลอดภัยคมนาคม", "1356", "transport_1356.png", 1));
            phoneItemList.add(new PhoneItem(0, "ศูนย์เตือนภัยพิบัติแห่งชาติ", "192", "disaster_warning_192.png", 1));
            phoneItemList.add(new PhoneItem(0, "ศูนย์ส่งกลับและรถพยาบาล รพ.ตำรวจ", "1691", "police_hospital_1691.png", 1));

            phoneItemList.add(new PhoneItem(0, "เหตุด่วน เหตุร้าย แจ้งตำรวจ", "191", "police_191.jpg", 2));
            phoneItemList.add(new PhoneItem(0, "ไฟไหม้ สัตว์เข้าบ้าน แจ้งดับเพลิง", "199", "fire_199.png", 2));
            phoneItemList.add(new PhoneItem(0, "ศูนย์ปลอดภัยทางน้ำ กรมเจ้าท่า", "1199", "marine_1199.png", 2));
            phoneItemList.add(new PhoneItem(0, "กองอำนวยการรักษาความมั่นคงภายในราชอาณาจักร", "1374", "internal_security_1374.png", 2));
            phoneItemList.add(new PhoneItem(0, "ศูนย์ปราบปรามการโจรกรรมรถ", "1192", "lostcar_1192.png", 2));

            phoneItemList.add(new PhoneItem(0, "กระบี่", "0-7562-7900", "hotline_191.jpg", 3));
            phoneItemList.add(new PhoneItem(0, "กาญจนบุรี", "0-3452-7600-49", "hotline_191.jpg", 3));
            phoneItemList.add(new PhoneItem(0, "กาฬสินธุ์", "0-4380-9799", "hotline_191.jpg", 3));
            phoneItemList.add(new PhoneItem(0, "กำแพงเพชร", "0-5571-8490", "hotline_191.jpg", 3));
            phoneItemList.add(new PhoneItem(0, "ขอนแก่น", "0-4324-0250-98", "hotline_191.jpg", 3));
            phoneItemList.add(new PhoneItem(0, "จันทบุรี", "0-3931-9790", "hotline_191.jpg", 3));
            phoneItemList.add(new PhoneItem(0, "ฉะเชิงเทรา", "0-3850-0099", "hotline_191.jpg", 3));
            phoneItemList.add(new PhoneItem(0, "ชลบุรี", "0-3893-2600-08", "hotline_191.jpg", 3));
            phoneItemList.add(new PhoneItem(0, "ชัยนาท", "0-5645-9639", "hotline_191.jpg", 3));
            phoneItemList.add(new PhoneItem(0, "ชัยภูมิ", "0-4481-5000", "hotline_191.jpg", 3));
            phoneItemList.add(new PhoneItem(0, "ชุมพร", "0-7752-9739", "hotline_191.jpg", 3));
            phoneItemList.add(new PhoneItem(0, "เชียงราย", "0-5391-0788", "hotline_191.jpg", 3));
            phoneItemList.add(new PhoneItem(0, "เชียงใหม่", "0-5392-0750-51", "hotline_191.jpg", 3));
            phoneItemList.add(new PhoneItem(0, "ตรัง", "0-7520-1990", "hotline_191.jpg", 3));
            phoneItemList.add(new PhoneItem(0, "ตราด", "0-3955-2900-01", "hotline_191.jpg", 3));
            phoneItemList.add(new PhoneItem(0, "ตาก", "0-5551-8000", "hotline_191.jpg", 3));
            phoneItemList.add(new PhoneItem(0, "นครนายก", "0-3730-7000", "hotline_191.jpg", 3));
            phoneItemList.add(new PhoneItem(0, "นครปฐม", "0-3424-0650", "hotline_191.jpg", 3));
            phoneItemList.add(new PhoneItem(0, "นครพนม", "0-4253-9790", "hotline_191.jpg", 3));
            phoneItemList.add(new PhoneItem(0, "นครราชสีมา", "0-4442-0250-99", "hotline_191.jpg", 3));
            phoneItemList.add(new PhoneItem(0, "นครศรีธรรมราช", "0-7530-4600", "hotline_191.jpg", 3));
            phoneItemList.add(new PhoneItem(0, "นครสวรรค์", "0-5621-9099", "hotline_191.jpg", 3));
            phoneItemList.add(new PhoneItem(0, "นนทบุรี", "0-2528-7490", "hotline_191.jpg", 3));
            phoneItemList.add(new PhoneItem(0, "นราธิวาส", "0-7351-7990", "hotline_191.jpg", 3));
            phoneItemList.add(new PhoneItem(0, "น่าน", "0-5468-3000", "hotline_191.jpg", 3));
            phoneItemList.add(new PhoneItem(0, "บุรีรัมย์", "0-4460-4090", "hotline_191.jpg", 3));
            phoneItemList.add(new PhoneItem(0, "ปทุมธานี", "0-2598-8191", "hotline_191.jpg", 3));
            phoneItemList.add(new PhoneItem(0, "ประจวบคีรีขันธ์ (สภ.หัวหิน)", "0-3261-8090", "hotline_191.jpg", 3));
            phoneItemList.add(new PhoneItem(0, "ปราจีนบุรี", "0-3723-9098", "hotline_191.jpg", 3));
            phoneItemList.add(new PhoneItem(0, "ปัตตานี", "0-7334-5999", "hotline_191.jpg", 3));
            phoneItemList.add(new PhoneItem(0, "พระนครศรีอยุธยา", "0-3524-9750", "hotline_191.jpg", 3));
            phoneItemList.add(new PhoneItem(0, "พะเยา", "0-5440-9739", "hotline_191.jpg", 3));
            phoneItemList.add(new PhoneItem(0, "พังงา", "0-7640-1439", "hotline_191.jpg", 3));
            phoneItemList.add(new PhoneItem(0, "พัทลุง", "0-7460-9977", "hotline_191.jpg", 3));
            phoneItemList.add(new PhoneItem(0, "พิจิตร", "0-5660-9739", "hotline_191.jpg", 3));
            phoneItemList.add(new PhoneItem(0, "พิษณุโลก", "0-5523-6400", "hotline_191.jpg", 3));
            phoneItemList.add(new PhoneItem(0, "เพชรบุรี", "0-3270-9740", "hotline_191.jpg", 3));
            phoneItemList.add(new PhoneItem(0, "เพชรบูรณ์", "0-5671-7799", "hotline_191.jpg", 3));
            phoneItemList.add(new PhoneItem(0, "แพร่", "0-5453-9739", "hotline_191.jpg", 3));
            phoneItemList.add(new PhoneItem(0, "ภูเก็ต", "0-7636-0790", "hotline_191.jpg", 3));
            phoneItemList.add(new PhoneItem(0, "มหาสารคาม", "0-4371-9698-99", "hotline_191.jpg", 3));
            phoneItemList.add(new PhoneItem(0, "มุกดาหาร", "0-4262-9739", "hotline_191.jpg", 3));
            phoneItemList.add(new PhoneItem(0, "ยโสธร", "0-4570-9739", "hotline_191.jpg", 3));
            phoneItemList.add(new PhoneItem(0, "ยะลา", "0-7322-0890", "hotline_191.jpg", 3));
            phoneItemList.add(new PhoneItem(0, "ร้อยเอ็ด", "0-4361-9799", "hotline_191.jpg", 3));
            phoneItemList.add(new PhoneItem(0, "ระนอง", "0-7781-9739", "hotline_191.jpg", 3));
            phoneItemList.add(new PhoneItem(0, "ระยอง", "0-3892-8090", "hotline_191.jpg", 3));
            phoneItemList.add(new PhoneItem(0, "ราชบุรี", "0-3271-9798", "hotline_191.jpg", 3));
            phoneItemList.add(new PhoneItem(0, "ลพบุรี", "0-3641-8900", "hotline_191.jpg", 3));
            phoneItemList.add(new PhoneItem(0, "ลำปาง", "0-5423-7090", "hotline_191.jpg", 3));
            phoneItemList.add(new PhoneItem(0, "ลำพูน", "0-5356-9790", "hotline_191.jpg", 3));
            phoneItemList.add(new PhoneItem(0, "เลย", "0-4280-8739", "hotline_191.jpg", 3));
            phoneItemList.add(new PhoneItem(0, "ศรีสะเกษ", "0-4582-9799", "hotline_191.jpg", 3));
            phoneItemList.add(new PhoneItem(0, "สกลนคร", "0-4270-0739", "hotline_191.jpg", 3));
            phoneItemList.add(new PhoneItem(0, "สงขลา", "0-7431-7301-30", "hotline_191.jpg", 3));
            phoneItemList.add(new PhoneItem(0, "สตูล", "0-7470-9739", "hotline_191.jpg", 3));
            phoneItemList.add(new PhoneItem(0, "สมุทรปราการ", "0-2338-0090", "hotline_191.jpg", 3));
            phoneItemList.add(new PhoneItem(0, "สมุทรสงคราม", "0-3471-9740", "hotline_191.jpg", 3));
            phoneItemList.add(new PhoneItem(0, "สมุทรสาคร", "0-3441-9780", "hotline_191.jpg", 3));
            phoneItemList.add(new PhoneItem(0, "สระแก้ว", "0-3724-0740", "hotline_191.jpg", 3));
            phoneItemList.add(new PhoneItem(0, "สระบุรี", "0-3624-0698", "hotline_191.jpg", 3));
            phoneItemList.add(new PhoneItem(0, "สิงห์บุรี", "0-3650-9798-99", "hotline_191.jpg", 3));
            phoneItemList.add(new PhoneItem(0, "สุโขทัย", "0-5560-9739", "hotline_191.jpg", 3));
            phoneItemList.add(new PhoneItem(0, "สุพรรณบุรี", "0-3551-4000", "hotline_191.jpg", 3));
            phoneItemList.add(new PhoneItem(0, "สุราษฎร์ธานี", "0-7727-7600", "hotline_191.jpg", 3));
            phoneItemList.add(new PhoneItem(0, "สุรินทร์", "0-4471-0739", "hotline_191.jpg", 3));
            phoneItemList.add(new PhoneItem(0, "หนองคาย", "0-4241-5000", "hotline_191.jpg", 3));
            phoneItemList.add(new PhoneItem(0, "หนองบัวลำภู", "0-4231-8739", "hotline_191.jpg", 3));
            phoneItemList.add(new PhoneItem(0, "อ่างทอง", "0-3561-7098-99", "hotline_191.jpg", 3));
            phoneItemList.add(new PhoneItem(0, "อำนาจเจริญ", "0-4551-9200-01", "hotline_191.jpg", 3));
            phoneItemList.add(new PhoneItem(0, "อุดรธานี", "0-4221-5750-99", "hotline_191.jpg", 3));
            phoneItemList.add(new PhoneItem(0, "อุบลราชธานี", "0-4535-2600-09", "hotline_191.jpg", 3));

            phoneItemList.add(new PhoneItem(0, "กรมทางหลวง มอเตอร์เวย์", "1586", "highways_1586.png", 4));
            phoneItemList.add(new PhoneItem(0, "กรมทางหลวงชนบท", "1146", "rural_roads_1146.png", 4));
            phoneItemList.add(new PhoneItem(0, "ตำรวจทางหลวง", "1193", "highway_police_1193.png", 4));
            phoneItemList.add(new PhoneItem(0, "จส.100", "1137", "js100_1137.png", 4));
            phoneItemList.add(new PhoneItem(0, "ศูนย์ควบคุมและสั่งการจราจร บก.02", "1197", "traffic_police_1197.png", 4));

            phoneItemList.add(new PhoneItem(0, "บริษัท ขนส่ง จำกัด (บขส.)", "1490", "transport_bus_1490.png", 5));
            phoneItemList.add(new PhoneItem(0, "การรถไฟแห่งประเทศไทย", "1690", "railway_1690.png", 5));
            phoneItemList.add(new PhoneItem(0, "การท่าอากาศยานฯ", "0-2535-1722", "airports_of_thailand.png", 5));
            phoneItemList.add(new PhoneItem(0, "กรมการขนส่งทางบก", "1584", "land_transport_1584.png", 5));
            phoneItemList.add(new PhoneItem(0, "กรมท่าอากาศยาน", "0-2287-0320-9", "department_of_airports.png", 5));

            phoneItemList.add(new PhoneItem(0, "กรมสุขภาพจิต", "1323", "mental_health_1323.png", 6));
            phoneItemList.add(new PhoneItem(0, "ศูนย์ประชาบดี ปัญหาสังคม เด็ก", "1300", "crisis_center_1300.jpg", 6));
            phoneItemList.add(new PhoneItem(0, "ศูนย์ดำรงธรรม", "1567", "damrongdhama_1567.png", 6));
            phoneItemList.add(new PhoneItem(0, "ตำรวจท่องเที่ยว", "1155", "tourist_police_1155.png", 6));
            phoneItemList.add(new PhoneItem(0, "ศูนย์บริการข้อมูลภาครัฐเพื่อประชาชน", "1111", "government_contact_center_1111.jpg", 6));
            phoneItemList.add(new PhoneItem(0, "ร่วมด้วยช่วยกัน", "1677", "ruamduay_1677.jpg", 6));

            for (PhoneItem phoneItem : phoneItemList) {
                ContentValues cv = new ContentValues();
                cv.put(COL_TITLE, phoneItem.title);
                cv.put(COL_NUMBER, phoneItem.number);
                cv.put(COL_IMAGE, phoneItem.image);
                cv.put(COL_CATEGORY_ID, phoneItem.categoryId);
                long result = db.insert(TABLE_PHONE_NUMBER, null, cv);
                if (result == -1) {
                    Log.e(TAG, "Error insert phone number data: " + phoneItem.title);
                }
            }

            ArrayList<Category> categoryList = new ArrayList<>();

            categoryList.add(new Category(1, "อุบัติเหตุ กู้ชีพ ฉุกเฉิน"));
            categoryList.add(new Category(2, "เหตุด่วน เหตุร้าย"));
            categoryList.add(new Category(3, "ศูนย์รับแจ้งเหตุฉุกเฉินแต่ละจังหวัด"));
            categoryList.add(new Category(4, "เส้นทาง การจราจร"));
            categoryList.add(new Category(5, "เดินทาง ขนส่ง"));
            categoryList.add(new Category(6, "ทั่วไป สารพัด"));

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
            db.execSQL("DROP TABLE IF EXISTS " + TABLE_PHONE_NUMBER);
            db.execSQL("DROP TABLE IF EXISTS " + TABLE_CATEGORY);
            onCreate(db);
        }
    }
}
