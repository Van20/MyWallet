package id.sch.smktelkom_mlg.mywallet.Controller;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import id.sch.smktelkom_mlg.mywallet.DBHelper.DBHandler;
import id.sch.smktelkom_mlg.mywallet.Model.Saldo;

/**
 * Created by fianxeka on 19/06/17.
 */

public class SaldoController {

    public static final String LOGTAG = "SALDO TABEL";
    SQLiteOpenHelper dbhandler;
    SQLiteDatabase database;

    public SaldoController(Context context) {
        dbhandler = new DBHandler(context);
    }


    /**
     * Handler Open And Closing Connection
     */
    public void open() {
        Log.i(LOGTAG, "Database Opened");
        database = dbhandler.getWritableDatabase();
    }

    public void close() {
        Log.i(LOGTAG, "Database Closed");
        dbhandler.close();
    }

    /**
     * Create & Update Saldo
     */
    public void addSaldo(Saldo saldo) {
        open();
        ContentValues values = new ContentValues();
        values.put(DBHandler.KEY_JUMLAH, saldo.getSaldo());
        database.insert(DBHandler.TABLE_SALDO, null, values);
        close();
    }

    public void updateSaldo(int newsaldo) {
        open();
        ContentValues values = new ContentValues();
        values.put(DBHandler.KEY_JUMLAH, newsaldo);
        database.update(DBHandler.TABLE_SALDO, values, DBHandler.KEY_ID + " = ?",
                new String[]{String.valueOf(1)});
        close();
    }

    /**
     * Get & Return Saldo || Get Saldo Data
     */
    public String getSaldo() {
        database = dbhandler.getReadableDatabase();
        String newsaldo = "";
        Cursor cursor = database.rawQuery("SELECT * FROM saldo", null);
        if (cursor.getCount() > 0) {
            cursor.moveToFirst();
            newsaldo = cursor.getString(1);
        }
        cursor.close();
        close();
        return newsaldo;
    }

}
