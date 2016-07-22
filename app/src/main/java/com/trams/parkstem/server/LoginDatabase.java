package com.trams.parkstem.server;

import android.content.Context;
import android.util.Log;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.HashMap;

/**
 * Created by Noverish on 2016-07-21.
 */
public class LoginDatabase {
    private Context context;

    private static final String DATABASE_FILE_NAME = "login_database";

    private static final String ID_COLUMN = "id";
    private static final String PW_COLUMN = "pw";
    private static final String GUBUN_COLUMN = "gubun";
    private HashMap<String, String> database = new HashMap<>();

    private static LoginDatabase loginDatabase;
    public static LoginDatabase getInstance(Context context) {
        if(loginDatabase == null)
            loginDatabase = new LoginDatabase(context);

        return loginDatabase;
    }

    private LoginDatabase(Context context) {
        this.context = context;

        database = readFromInternalStorage();

        for(String key : database.keySet()) {
            Log.e("database","key : " + key + ", value : " + database.get(key));
        }
    }

    public void setData(String gubun, String id, String pw) {
        Log.e("setData",gubun + " " + id + " " + pw);

        database.clear();

        database.put(GUBUN_COLUMN, gubun);
        database.put(ID_COLUMN, id);
        database.put(PW_COLUMN, pw);

        saveToInternalStorage(database);
    }

    public String getId() {
        return database.get(ID_COLUMN);
    }

    public String getPw() {
        return database.get(PW_COLUMN);
    }

    public String getGubun() {
        return database.get(GUBUN_COLUMN);
    }

    public boolean isDatabaseClear() {
        return (database == null ||database.size() == 0);
    }

    private void saveToInternalStorage(HashMap<String, String> map) {
        context.deleteFile(DATABASE_FILE_NAME);
        try {
            FileOutputStream fos = context.openFileOutput(DATABASE_FILE_NAME, Context.MODE_PRIVATE);
            ObjectOutputStream of = new ObjectOutputStream(fos);
            of.writeObject(map);
            of.flush();
            of.close();
            fos.close();
        }
        catch (Exception e) {
            Log.e("InternalStorage", e.getMessage());
        }
    }

    private HashMap<String, String> readFromInternalStorage() {
        HashMap<String, String> toReturn = null;

        try {
            FileInputStream fis;
            fis = context.openFileInput(DATABASE_FILE_NAME);
            ObjectInputStream oi = new ObjectInputStream(fis);
            toReturn = (HashMap<String, String>) oi.readObject();
            oi.close();
        } catch (Exception e) {
            Log.e("InternalStorage", e.getMessage());
        }

        if(toReturn == null) {
            Log.e("database","empty database");
            toReturn = new HashMap<>();
        }

        return toReturn;
    }

    public void clearDatabase() {
        context.deleteFile(DATABASE_FILE_NAME);
        database = readFromInternalStorage();

        Log.e("database","database cleared");
    }
}
