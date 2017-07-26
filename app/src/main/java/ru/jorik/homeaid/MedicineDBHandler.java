package ru.jorik.homeaid;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static ru.jorik.homeaid.MainActivity.shortCall;

/**
 * Created by 111 on 26.07.2017.
 */

public class MedicineDBHandler extends SQLiteOpenHelper implements InterfaceDataBaseHandler<Medicine>{

    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "medicaments";

    static SimpleDateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy");

    static class DataBaseTables{
        static class Medicine{
            public static final String TABLE_NAME = "medicine";
            //имена полей
            public static final String ID = "_id";
            public static final String NAME = "name";
            public static final String DATE_START = "date_start";
            public static final String DATE_OVER = "date_over";
            public static final String WARRANTY_MONTH = "warranty";//в месяцах


            //// TODO: 27.07.2017 переписать под константы
            public static String createQuery(){
                String CREATE_CONTACTS_TABLE = "CREATE TABLE " + TABLE_NAME + "("
                        + ID + " INTEGER PRIMARY KEY,"
                        + NAME + " TEXT,"
                        + DATE_OVER + " TEXT" //переписать под Date
                        + ")";
                return CREATE_CONTACTS_TABLE;

            }

            public static String countQuery(){
                return "SELECT  * FROM " + DataBaseTables.Medicine.ID;
            }

        }
    }

    public MedicineDBHandler(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(DataBaseTables.Medicine.createQuery());
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + DataBaseTables.Medicine.TABLE_NAME);
        //// TODO: 26.07.2017 переписать под сохранение данных
        onCreate(db);
    }


    @Override
    public void createItem(Medicine medicine) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        String dateString = dateFormat.format(medicine.getDateOver());

        values.put(DataBaseTables.Medicine.NAME, medicine.getName());
        values.put(DataBaseTables.Medicine.DATE_OVER, dateString);

        db.insert(DataBaseTables.Medicine.TABLE_NAME, null, values);
        db.close();
    }

    @Override
    public Medicine readItem(int id) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(
                DataBaseTables.Medicine.TABLE_NAME,
                new String[] {DataBaseTables.Medicine.ID, DataBaseTables.Medicine.NAME, DataBaseTables.Medicine.DATE_OVER},
                DataBaseTables.Medicine.ID + "=?",
                new String[] { String.valueOf(id) },
                null, null, null, null
        );

        if (cursor != null){
            cursor.moveToFirst();
        }

        String rName = cursor.getString(cursor.getColumnIndex(DataBaseTables.Medicine.NAME));
        String tempDate = cursor.getString(cursor.getColumnIndex(DataBaseTables.Medicine.DATE_OVER));
        Date rDate = shortCall();//// TODO: 27.07.2017 убрать костыль инициализации
        try {
            rDate = dateFormat.parse(tempDate);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        Medicine contact = new Medicine(rName, rDate);

        return contact;
    }

    @Override
    public int updateItem(int id, Medicine medicine) {
        SQLiteDatabase db = this.getWritableDatabase();
        String dateString = dateFormat.format(medicine.getDateOver());

        ContentValues values = new ContentValues();
        values.put(DataBaseTables.Medicine.NAME, medicine.getName());
        values.put(DataBaseTables.Medicine.DATE_OVER, dateString);

        return db.update(DataBaseTables.Medicine.TABLE_NAME,
                values,
                DataBaseTables.Medicine.ID + " = ?",
                new String[] { String.valueOf(id) });
    }

    @Override
    public void deleteItem(int id) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(DataBaseTables.Medicine.TABLE_NAME,
                DataBaseTables.Medicine.ID + " = ?",
                new String[] { String.valueOf(id) });
        db.close();
    }

    @Override
    public void addGroup(List<Medicine> medicineList) {
        // TODO: 27.07.2017 Заполнить
    }

    @Override
    public Medicine[] readGroup(int startId, int length) {
        // TODO: 27.07.2017 Заполнить
        return new Medicine[0];
    }

    @Override
    public int updateGroup(int startId, List<Medicine> medicineList) {
        // TODO: 27.07.2017 Заполнить
        return 0;
    }

    @Override
    public int deleteGroup(int startId, int length) {
        // TODO: 27.07.2017 Заполнить
        return 0;
    }

    @Override
    public int getCount() {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(DataBaseTables.Medicine.countQuery(), null);
        cursor.close();
        return cursor.getCount();
    }

    @Override
    //// TODO: 27.07.2017 переписать под выборку через readGroup
    //// TODO: 27.07.2017 сделать релизию через цикл и readItem
    public List<Medicine> readAll() {
        List<Medicine> medicineList = new ArrayList<Medicine>();
        String selectQuery = "SELECT * FROM " + DataBaseTables.Medicine.TABLE_NAME; //// TODO: 27.07.2017 вынести в класс объекта

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor c = db.rawQuery(selectQuery, null);

        if (c.moveToFirst()) {
            do {
                String rName = c.getString(c.getColumnIndex(DataBaseTables.Medicine.NAME));
                String tempDate = c.getString(c.getColumnIndex(DataBaseTables.Medicine.DATE_OVER));
                Date rDate = shortCall();//// TODO: 27.07.2017 убрать костыль инициализации
                try {
                    rDate = dateFormat.parse(tempDate);
                } catch (ParseException e) {
                    e.printStackTrace();
                }

                Medicine medicine = new Medicine(rName, rDate);
                medicineList.add(medicine);
            } while (c.moveToNext());
        }

        return medicineList;
    }

    @Override
    public void deleteAll() {
        // TODO: 27.07.2017 Заполнить
    }

}
