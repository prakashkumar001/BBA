package com.bba.ministries.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;


public class DBHelper extends SQLiteOpenHelper {

    // Logcat tag
    private static final String LOG = "DatabaseHelper";

    // Database Version
    private static final int DATABASE_VERSION = 2;

    // Database Name
    private static final String DATABASE_NAME = "bbaministries";


   // private static final String TABLE_DETAILS= "alldetails";

    private static final String TABLE_HOME = "home";
    private static final String TABLE_EVENTS = "events";
    private static final String TABLE_PROMISECARD = "promisecard";
    private static final String TABLE_FACEBOOKCOVER = "fbcover";
    private static final String TABLE_PASTORS = "pastors";
    private static final String TABLE_PDF = "pdfs";

    private static final String KEY_ID = "id";

    private static final String KEY_HOME = "hometable";
    private static final String KEY_EVENTS = "eventtable";
    private static final String KEY_PROMISECARD = "promisetable";
    private static final String KEY_FACEBOOKCOVER = "fbtable";
    private static final String KEY_PASTORS = "pastortable";
    private static final String KEY_PDF = "pdftable";

   /* // Table Names
    private static final String TABLE_HOME = "home";
    private static final String TABLE_EVENTS = "events";
    private static final String TABLE_PROMISECARD = "promisecard";
    private static final String TABLE_PASTORS = "pastors";

    // Common column names in home
    private static final String KEY_ID = "id";
    private static final String KEY_SLIDER = "slider";
    private static final String KEY_VERSE = "verse";

    // NOTES Table - column nmaes in events
    private static final String KEY_EVENTNAME = "eventname";
    private static final String KEY_EVENTDATE = "date";
    private static final String KEY_EVENTSTARTTIME = "starttime";
    private static final String KEY_EVENTENDTIME = "endtime";
    private static final String KEY_EVENTPLACE = "place";
    private static final String KEY_EVENTCONTACTNAME = "contactname";
    private static final String KEY_EVENTCONTACTNUM = "phone";
    private static final String KEY_EVENTLAT = "latitude";
    private static final String KEY_EVENTLON = "longitude";

    // TAGS Table - column names in promisecard
    private static final String KEY_PROMISECARDIMAGE = "image";
    private static final String KEY_PROMISETITLE = "title";
    private static final String KEY_PROMISEDESCRIPTION = "description";

    // NOTE_TAGS Table - column names in pastors
    private static final String KEY_PASTORNAME = "name";
    private static final String KEY_DESIGNATION = "designation";
    private static final String KEY_FB = "fb";
    private static final String KEY_TWITTER= "twitter";
    private static final String KEY_EMAIL = "email";
    private static final String KEY_PHONE = "phone";*/


    // Table Create Statements
    // Todo table create statement
    private static final String CREATE_TABLE_HOME = "CREATE TABLE "
            + TABLE_HOME + "(" + KEY_ID + " TEXT," + KEY_HOME + " TEXT" + ")";

    // Tag table create statement
    private static final String CREATE_TABLE_EVENTS = "CREATE TABLE " + TABLE_EVENTS
            + "(" + KEY_ID + " TEXT," + KEY_EVENTS + " TEXT" + ")";

    // todo_tag table create statement
    private static final String CREATE_TABLE_PROMISECARD = "CREATE TABLE "
            + TABLE_PROMISECARD + "(" + KEY_ID + " TEXT,"
            + KEY_PROMISECARD + " TEXT" + ")";

    // todo_tag table create statement
    private static final String CREATE_TABLE_PASTORS = "CREATE TABLE "
            + TABLE_PASTORS + "(" + KEY_ID + " TEXT,"
            + KEY_PASTORS + " TEXT" + ")";

    // todo_tag table create statement
    private static final String CREATE_TABLE_FACEBOOK = "CREATE TABLE "
            + TABLE_FACEBOOKCOVER + "(" + KEY_ID + " TEXT,"
            + KEY_FACEBOOKCOVER + " TEXT" + ")";

    // todo_tag table create statement
    private static final String CREATE_TABLE_PDF = "CREATE TABLE "
            + TABLE_PDF + "(" + KEY_ID + " TEXT,"
            + KEY_PDF + " TEXT" + ")";
    public DBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        // creating required tables
        db.execSQL(CREATE_TABLE_HOME);
        db.execSQL(CREATE_TABLE_EVENTS);
        db.execSQL(CREATE_TABLE_PROMISECARD);
        db.execSQL(CREATE_TABLE_PASTORS);
        db.execSQL(CREATE_TABLE_FACEBOOK);
        db.execSQL(CREATE_TABLE_PDF);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // on upgrade drop older tables
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_HOME);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_PASTORS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_EVENTS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_FACEBOOKCOVER);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_PDF);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_PROMISECARD);

        // create new tables
        onCreate(db);
    }

    public void add(String tablename, String response)
    {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();

        if(tablename.equalsIgnoreCase(TABLE_HOME))
        {

            values.put(KEY_ID,1);
            values.put(KEY_HOME,response);// Contact Phone
        }else if(tablename.equalsIgnoreCase(TABLE_EVENTS))
        {
            values.put(KEY_ID,1);
            values.put(KEY_EVENTS,response);// Contact Phone

        }else if(tablename.equalsIgnoreCase(TABLE_PROMISECARD))
        {
            values.put(KEY_ID,1);
            values.put(KEY_PROMISECARD,response);// Contact Phone

        }else if(tablename.equalsIgnoreCase(TABLE_FACEBOOKCOVER))
        {
            values.put(KEY_ID,1);
            values.put(KEY_FACEBOOKCOVER,response);// Contact Phone
        }else if(tablename.equalsIgnoreCase(TABLE_PASTORS))
        {
            values.put(KEY_ID,1);
            values.put(KEY_PASTORS,response);// Contact Phone
        }else if(tablename.equalsIgnoreCase(TABLE_PDF))
        {
            values.put(KEY_ID,1);
            values.put(KEY_PDF,response);// Contact Phone
        }else {

        }

        System.out.println("Inserted");
        // Inserting Row
        db.insert(tablename, null, values);
        db.close(); // Closing database connection
    }

    public void update_table(String response, String tablename, String id)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values=new ContentValues();

        String returnvalues;
        if(tablename.equalsIgnoreCase(TABLE_HOME))
        {
            values.put(KEY_ID,1);
            values.put(KEY_HOME,response);
            db.update(TABLE_HOME, values, KEY_ID + " = ?", new String[]{id});

        }
        else if(tablename.equalsIgnoreCase(TABLE_EVENTS))
        {
            values.put(KEY_ID,1);
            values.put(KEY_EVENTS,response);
            db.update(TABLE_EVENTS, values, KEY_ID + " = ?", new String[]{id});

        }else if(tablename.equalsIgnoreCase(TABLE_PROMISECARD))
        {
            values.put(KEY_ID,1);
            values.put(KEY_PROMISECARD,response);
            db.update(TABLE_PROMISECARD, values, KEY_ID + " = ?", new String[]{id});

      }else if(tablename.equalsIgnoreCase(TABLE_FACEBOOKCOVER))
        {
            values.put(KEY_ID,1);
            values.put(KEY_FACEBOOKCOVER,response);
            db.update(TABLE_FACEBOOKCOVER, values, KEY_ID + " = ?", new String[]{id});

        }else if(tablename.equalsIgnoreCase(TABLE_PASTORS))
      {
          values.put(KEY_ID,1);
          values.put(KEY_PASTORS,response);
          db.update(TABLE_PASTORS, values, KEY_ID + " = ?", new String[]{id});

        }else if(tablename.equalsIgnoreCase(TABLE_PDF))
        {
            values.put(KEY_ID,1);
            values.put(KEY_PDF,response);
            db.update(TABLE_PDF, values, KEY_ID + " = ?", new String[]{id});

        }else
        {

        }
        db.close();


    }


    public String getresponse(String tablename, String id)
    {
        String returnvalues="No data";
        if(tablename.equalsIgnoreCase(TABLE_HOME))
        {

                String countQuery = "SELECT "+ KEY_HOME + " FROM " + tablename + " where id ='" + id + "' ";
                SQLiteDatabase db = this.getReadableDatabase();
                Cursor cursor = db.rawQuery(countQuery, null);
            if (cursor.moveToFirst()) {
                do {

                    returnvalues=cursor.getString(cursor.getColumnIndex(KEY_HOME));



                } while (cursor.moveToNext());
            }
                cursor.close();





        }else  if(tablename.equalsIgnoreCase(TABLE_EVENTS))
        {

                String countQuery = "SELECT "+ KEY_EVENTS + " FROM " + tablename + " where id ='" + id + "' ";
                SQLiteDatabase db = this.getReadableDatabase();
                Cursor cursor = db.rawQuery(countQuery, null);
            if (cursor.moveToFirst()) {
                do {

                    returnvalues=cursor.getString(cursor.getColumnIndex(KEY_EVENTS));



                } while (cursor.moveToNext());
            }
            cursor.close();




        }else  if(tablename.equalsIgnoreCase(TABLE_PROMISECARD))
        {

                String countQuery = "SELECT "+ KEY_PROMISECARD + " FROM " + tablename + " where id ='" + id + "' ";
                SQLiteDatabase db = this.getReadableDatabase();
                Cursor cursor = db.rawQuery(countQuery, null);
            if (cursor.moveToFirst()) {
                do {

                    returnvalues=cursor.getString(cursor.getColumnIndex(KEY_PROMISECARD));



                } while (cursor.moveToNext());
            }
            cursor.close();




        }else  if(tablename.equalsIgnoreCase(TABLE_FACEBOOKCOVER))
        {

                String countQuery = "SELECT "+ KEY_FACEBOOKCOVER + " FROM " + tablename + " where id ='" + id + "' ";
                SQLiteDatabase db = this.getReadableDatabase();
                Cursor cursor = db.rawQuery(countQuery, null);
            if (cursor.moveToFirst()) {
                do {

                    returnvalues=cursor.getString(cursor.getColumnIndex(KEY_FACEBOOKCOVER));



                } while (cursor.moveToNext());
            }
            cursor.close();




        }else  if(tablename.equalsIgnoreCase(TABLE_PASTORS))
        {

                String countQuery = "SELECT "+ KEY_PASTORS + " FROM " + tablename + " where id ='" + id + "' ";
                SQLiteDatabase db = this.getReadableDatabase();
                Cursor cursor = db.rawQuery(countQuery, null);

            if (cursor.moveToFirst()) {
                do {

                    returnvalues=cursor.getString(cursor.getColumnIndex(KEY_PASTORS));



                } while (cursor.moveToNext());
            }
            cursor.close();


        }else  if(tablename.equalsIgnoreCase(TABLE_PDF))
        {

            String countQuery = "SELECT "+ KEY_PDF + " FROM " + tablename + " where id ='" + id + "' ";
            SQLiteDatabase db = this.getReadableDatabase();
            Cursor cursor = db.rawQuery(countQuery, null);

            if (cursor.moveToFirst()) {
                do {

                    returnvalues=cursor.getString(cursor.getColumnIndex(KEY_PDF));



                } while (cursor.moveToNext());
            }
            cursor.close();


        }
        return returnvalues;
    }
}
