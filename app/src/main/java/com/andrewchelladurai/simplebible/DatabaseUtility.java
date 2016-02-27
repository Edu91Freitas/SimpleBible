package com.andrewchelladurai.simplebible;

import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;

/**
 * Created by Andrew Chelladurai - TheUnknownAndrew[at]GMail[dot]com
 * on 26-Feb-2016 @ 1:15 AM
 */
public class DatabaseUtility
        extends SQLiteOpenHelper {

    private static final String          TAG            = "DatabaseUtility";
    private static final String          DATABASE_NAME  = "NIV.db";
    private static       DatabaseUtility staticInstance = null;
    private static String         DB_PATH;
    private static SQLiteDatabase database;
    private static Context        context;

    private DatabaseUtility(Context context) {
        super(context, DATABASE_NAME, null, 1);
        DatabaseUtility.context = context;
        //Write a full path to the databases of your application
        DB_PATH = context.getDatabasePath(DATABASE_NAME).getParent();
        Log.d("DB_PATH", DB_PATH);
        openDataBase();
    }

    public static DatabaseUtility getInstance(Context context)
    throws NullPointerException {
        if (staticInstance == null) {
            if (context == null) {
                throw new NullPointerException("NULL Context passed for instantiating DB");
            }
            staticInstance = new DatabaseUtility(context);
        }
        return staticInstance;
    }

    private SQLiteDatabase openDataBase()
    throws SQLException {
        String path = DB_PATH + File.separatorChar + DATABASE_NAME;
        if (database == null) {
            createDataBase();
            database = SQLiteDatabase.openDatabase(path, null, SQLiteDatabase.OPEN_READONLY);
        }
        return database;
    }

    private void createDataBase() {
        boolean dbExist = checkDataBase();
        if (!dbExist) {
            Log.i(TAG, "Inside createDataBase - IF");
            this.getReadableDatabase();
            try {
                copyDataBase();
            } catch (IOException e) {
                Log.e(TAG, "DB copying error");
                throw new Error("Error copying database!");
            }
        } else {
            Log.i(TAG, "Database already exists");
        }
    }

    private boolean checkDataBase() {
        SQLiteDatabase checkDb = null;
        String path = DB_PATH + File.separatorChar + DATABASE_NAME;

        File f = new File(path);
        Log.d(TAG, "checkDataBase_path = " + path);
        if (f.exists()) {
            try {
                checkDb = SQLiteDatabase.openDatabase(path, null, SQLiteDatabase.OPEN_READONLY);
            } catch (SQLException e) {
                Log.e(TAG, "Error when checking DB");
            } finally {
                //Android does not like resource leaks, everything should be closed
                if (checkDb != null) {
                    checkDb.close();
                }
            }
            Log.d(TAG, "checkDataBase() Exited returning checkDb != null");
            return checkDb != null;
        } else {
            return false;
        }

    }

    private void copyDataBase()
    throws IOException {
        Log.d(TAG, "copyDataBase() Entered");

        InputStream externalDbStream = context.getAssets().open(DATABASE_NAME);
        Log.i(TAG, "copyDataBase : externalDbStream" + externalDbStream.toString());
        String outFileName = DB_PATH + File.separatorChar + DATABASE_NAME;
        Log.i(TAG, "copyDataBase : outFileName = " + outFileName);

        OutputStream localDbStream = new FileOutputStream(outFileName);

        //Copying the database
        byte[] buffer = new byte[1024];
        int bytesRead;
        while ((bytesRead = externalDbStream.read(buffer)) > 0) {
            localDbStream.write(buffer, 0, bytesRead);
        }
        localDbStream.close();
        externalDbStream.close();
        Log.d(TAG, "copyDataBase() Exited");
    }

    public Cursor getDBRecords(int bookID, int chapterID) {
        Log.d(TAG, "getDBRecords() called with: bookID = [" + bookID +
                   "], chapterID = [" + chapterID + "]");
        SQLiteDatabase db = getReadableDatabase();

        Cursor cursor = db.query("bibleverses",
                                 new String[]{"bookid", "chapterid", "verseid", "verse"},
                                 "chapterid =? AND bookid=?",
                                 new String[]{chapterID + "", bookID + ""},
                                 null, null, null);
        Log.d(TAG, "getDBRecords() Exited");
        return cursor;
    }

    public ArrayList<String> getAllVerseOfChapter(int bookNumber, int chapterNumber) {
        Cursor cursor = getDBRecords(bookNumber, chapterNumber);
        ArrayList<String> list = new ArrayList<>(0);
//
        if (cursor.moveToFirst()) {
            int verseIndex = cursor.getColumnIndex("Verse");
            int verseIdIndex = cursor.getColumnIndex("VerseId");
            //            int chapterIdIndex = cursor.getColumnIndex("ChapterId");
            //            int bookIdIndex = cursor.getColumnIndex("BookId");
            do {
                list.add(cursor.getInt(verseIdIndex) + " : " + cursor.getString(verseIndex));
            } while (cursor.moveToNext());
            if (list.size() > 0) {
                cursor.close();
            }
        }
        return list;
    }

    public Cursor getDBRecords(String paramTextToSearch) {
        Log.d(TAG, "getDBRecords() called with: paramTextToSearch = ["
                   + paramTextToSearch + "]");

        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.query("bibleverses",
                                 new String[]{"bookid", "chapterid", "verseid", "verse"},
                                 "verse like ?",
                                 new String[]{"%" + paramTextToSearch + "%"},
                                 null, null, null);
        Log.d(TAG, "getDBRecords() Exited");
        return cursor;
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
    }

    @Override
    public synchronized void close() {
        if (database != null) {
            database.close();
        }
        super.close();
    }

}