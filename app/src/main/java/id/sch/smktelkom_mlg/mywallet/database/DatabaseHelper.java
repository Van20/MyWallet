package id.sch.smktelkom_mlg.mywallet.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHelper extends SQLiteOpenHelper {

    private static final String DB_NOMBRE = "demo.sqlite";
    private static int DB_SCHEME_VERSION = 1;

    public DatabaseHelper(Context context) {
        super(context, DB_NOMBRE, null, DB_SCHEME_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // crea tabla usuario
        db.execSQL(DatabaseManagerUser.CREATE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        db.execSQL("DROP TABLE IF EXISTS"+DB_NOMBRE);
        onCreate(db);
    }
}

