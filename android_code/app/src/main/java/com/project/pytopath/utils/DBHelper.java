package com.project.pytopath.utils;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.project.pytopath.models.Disease;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class DBHelper extends SQLiteOpenHelper {

    private static final String TAG = "DBHelper";

    private static final String dbName = "planai.db";
    private static final int dbVersion = 2;
    private Context context;

    public DBHelper(Context context) {
        super(context, dbName, null, dbVersion);
        this.context = context;
        this.copyDbIfNotExists();
    }

    private void copyDbIfNotExists() {
        File dbDir = new File(context.getDatabasePath(dbName).getParentFile().getPath());
        if (!dbDir.exists())
            dbDir.mkdir();
        // Copy database starts here.
        String appDbPath = this.context.getDatabasePath(dbName).getAbsolutePath();
        File dbFile = new File(appDbPath);
        if (!dbFile.exists()) {
            try {
                InputStream mInput = context.getAssets().open("plantai.db");
                OutputStream mOutput = new FileOutputStream(appDbPath);
                byte[] mBuffer = new byte[1024];
                int mLength;
                while ((mLength = mInput.read(mBuffer, 0, 1024)) > 0)
                    mOutput.write(mBuffer, 0, mLength);
                mOutput.flush();
                mOutput.close();
                mInput.close();

                Log.d(TAG, "copyDbIfNotExists: Database copied");
            } catch (IOException ex) {
                throw new Error("Error copying database: " + ex.getMessage());
            }
        }
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

    }


    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

public    Disease getDiseaseDetails(String diseaseEN) {
        SQLiteDatabase database = this.getReadableDatabase();
        Cursor cursor = database.rawQuery("SELECT * from diseases where " +
                "disease_en='" + diseaseEN + "'", null);
        Disease disease = null;
        if (cursor.getCount() != 0) {
            cursor.moveToFirst();
            Log.e(TAG, "getDiseaseDetails: "+cursor.getString(3) );
            disease = new Disease(
                    cursor.getInt(0),
                    cursor.getString(1),
                    cursor.getString(2),
                    cursor.getString(3),
                    cursor.getString(4)
            );
        } else {
            disease = new Disease(1, diseaseEN + "not found", "", "", "");
        }
        cursor.close();
    Log.e(TAG, "getDiseaseDetails: "+disease.getDiseaseMr()+"\t"+disease.getDiseaseInfo() );
        return disease;
    }

}