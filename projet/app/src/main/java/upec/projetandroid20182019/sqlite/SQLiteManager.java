package upec.projetandroid20182019.sqlite;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseErrorHandler;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Build;

import java.util.ArrayList;
import java.util.HashMap;

public class SQLiteManager extends SQLiteOpenHelper {

    public static final String DATABASE_NAME = "projectandroid.db";
    public static final int version = 3;

    private static final String CREATE_ACCOUNT_BDD = "CREATE TABLE accounts (aid INTEGER PRIMARY KEY AUTOINCREMENT, titre TEXT NOT NULL, description TEXT, devise TEXT NOT NULL);";
    private static final String CREATE_PERSON_BDD = "PRAGMA foreign_keys = ON; CREATE TABLE persons (pid INTEGER PRIMARY KEY AUTOINCREMENT, name TEXT NOT NULL, aid INTEGER NOT NULL, FOREIGN KEY(aid) REFERENCES accounts(aid) );"; //, aid INTEGER NOT NULL, FOREIGN KEY(aid) REFERENCES accounts(aid)
    private static final String CREATE_EXPENSE_BDD = "PRAGMA foreign_keys = ON; CREATE TABLE expenses (eid INTEGER PRIMARY KEY AUTOINCREMENT, name TEXT NOT NULL, amount DOUBLE NOT NULL, date TEXT NOT NULL, paidby INTEGER, aid INTEGER NOT NULL, FOREIGN KEY(paidby) REFERENCES persons(pid), FOREIGN KEY(aid) REFERENCES accounts(aid));";

    public SQLiteManager(Context context) {
        super(context, DATABASE_NAME, null, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_ACCOUNT_BDD);
        db.execSQL(CREATE_PERSON_BDD);
        db.execSQL(CREATE_EXPENSE_BDD);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        //supprimer la table et la recréer
        //comme ça lorsque je change la version les id repartent de 0
        db.execSQL("DROP TABLE IF EXISTS accounts;");
        db.execSQL("DROP TABLE IF EXISTS persons;");
        db.execSQL("DROP TABLE IF EXISTS expenses;");
        onCreate(db);
    }
}
