package info.androidhive.listviewfeed;


import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Class that wraps the most common database operations. This example assumes you want a single table and data entity
 * with two properties: a title and a priority as an integer. Modify in all relevant locations if you need other/more
 * properties for your data and/or additional tables.
 */
public class DBHelper extends SQLiteOpenHelper {

    // All Static variables
    // Database Version
    private static final int DATABASE_VERSION = 1;

    // Database Name
    private static final String DATABASE_NAME = "payloadsManager";

    // Payloads table name
    private static final String TABLE_PAYLOADS = "payloads";

    // Payloads Table Columns names
    private static final String KEY_ID = "id";
    private static final String KEY_NAME = "name";
    private static final String KEY_PH_NO = "phone_number";

    public DBHelper(Context context) {
              super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    // Creating Tables
    @Override
    public void onCreate(SQLiteDatabase db) {
	      String CREATE_PAYLOADS_TABLE = "CREATE TABLE " + TABLE_PAYLOADS + "("
	                           + KEY_ID + " INTEGER PRIMARY KEY," + KEY_NAME + " TEXT,"
	                           + KEY_PH_NO + " TEXT" + ")";
	      db.execSQL(CREATE_PAYLOADS_TABLE);
    }

    // Upgrading database
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
          // Drop older table if existed
          db.execSQL("DROP TABLE IF EXISTS " + TABLE_PAYLOADS);

          // Create tables again
          onCreate(db);
    }

    // 새로운 Payload 함수 추가
    public void addPayload(Payload payload) {
          SQLiteDatabase db = this.getWritableDatabase();

          ContentValues values = new ContentValues();
          values.put(KEY_NAME, payload.getName()); // Payload Name
          values.put(KEY_PH_NO, payload.getPhoneNumber()); // Payload Phone

          // Inserting Row
          db.insert(TABLE_PAYLOADS, null, values);
          db.close(); // Closing database connection
    }

    // id 에 해당하는 Payload 객체 가져오기
    public Payload getPayload(int id) {
          SQLiteDatabase db = this.getReadableDatabase();

          Cursor cursor = db.query(TABLE_PAYLOADS, new String[] { KEY_ID,
                               KEY_NAME, KEY_PH_NO }, KEY_ID + "=?",
                               new String[] { String.valueOf(id) }, null, null, null, null);
          if (cursor != null)
                     cursor.moveToFirst();

          Payload payload = new Payload(Integer.parseInt(cursor.getString(0)),
                               cursor.getString(1), cursor.getString(2));
          // return payload
          return payload;
    }

    // 모든 Payload 정보 가져오기
    public List<Payload> getAllPayloads() {
          List<Payload> payloadList = new ArrayList<Payload>();
          // Select All Query
          String selectQuery = "SELECT  * FROM " + TABLE_PAYLOADS;

          SQLiteDatabase db = this.getWritableDatabase();
          Cursor cursor = db.rawQuery(selectQuery, null);

          // looping through all rows and adding to list
          if (cursor.moveToFirst()) {
                     do {
                               Payload payload = new Payload();
                               payload.setID(Integer.parseInt(cursor.getString(0)));
                               payload.setName(cursor.getString(1));
                               payload.setPhoneNumber(cursor.getString(2));
                               // Adding payload to list
                               payloadList.add(payload);
                     } while (cursor.moveToNext());
          }

          // return payload list
          return payloadList;
    }

    //Payload 정보 업데이트
    public int updatePayload(Payload payload) {
          SQLiteDatabase db = this.getWritableDatabase();

          ContentValues values = new ContentValues();
          values.put(KEY_NAME, payload.getName());
          values.put(KEY_PH_NO, payload.getPhoneNumber());

          // updating row
          return db.update(TABLE_PAYLOADS, values, KEY_ID + " = ?",
                               new String[] { String.valueOf(payload.getID()) });
    }

    // Payload 정보 삭제하기
    public void deletePayload(Payload payload) {
          SQLiteDatabase db = this.getWritableDatabase();
          db.delete(TABLE_PAYLOADS, KEY_ID + " = ?",
                               new String[] { String.valueOf(payload.getID()) });
          db.close();
    }

    // Payload 정보 숫자
    public int getPayloadsCount() {
              String countQuery = "SELECT  * FROM " + TABLE_PAYLOADS;
              SQLiteDatabase db = this.getReadableDatabase();
              Cursor cursor = db.rawQuery(countQuery, null);
              cursor.close();

              // return count
              return cursor.getCount();
    }
}
