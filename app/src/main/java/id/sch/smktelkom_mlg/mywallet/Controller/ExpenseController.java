package id.sch.smktelkom_mlg.mywallet.Controller;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

import id.sch.smktelkom_mlg.mywallet.DBHelper.DBHandler;
import id.sch.smktelkom_mlg.mywallet.Model.Expense;

public class ExpenseController {

    public static final String LOGTAG = "PENGELUARAN TABEL";
    SQLiteOpenHelper dbhandler;
    SQLiteDatabase database;

    public ExpenseController(Context context) {
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
     * Create Update Delete Expense
     */
    public void addExpense(Expense pengeluaran) {
        open();
        ContentValues values = new ContentValues();
        values.put(DBHandler.KEY_IDK, pengeluaran.getIdkat());
        values.put(DBHandler.KEY_DESKRIPSI, pengeluaran.getDeskripsi());
        values.put(DBHandler.KEY_JUMLAH, pengeluaran.getJumlah());
        values.put(DBHandler.KEY_TANGGAL, pengeluaran.getTanggal());
        database.insert(DBHandler.TABLE_PENGELUARAN, null, values);
        close();
    }

    public void updateExpense(Expense pengeluaran) {
        open();
        ContentValues values = new ContentValues();
        values.put(DBHandler.KEY_IDK, pengeluaran.getIdkat());
        values.put(DBHandler.KEY_DESKRIPSI, pengeluaran.getDeskripsi());
        values.put(DBHandler.KEY_JUMLAH, pengeluaran.getJumlah());
        database.update(DBHandler.TABLE_PENGELUARAN, values, DBHandler.KEY_ID + " = ?",
                new String[]{String.valueOf(pengeluaran.getId())});
        close();
    }

    public void deleteExpenseById(int id) {
        open();
        database.delete(DBHandler.TABLE_PENGELUARAN, DBHandler.KEY_ID + " = ?",
                new String[]{String.valueOf(id)});
        close();
    }

    public void deleteExpenseByCategory(int idk) {
        open();
        database.delete(DBHandler.TABLE_PENGELUARAN, DBHandler.KEY_IDK + " = ?",
                new String[]{String.valueOf(idk)});
        close();
    }


    /**
     * Get All Expense Today
     */
    public List<Expense> getExpenseByDate(String date) {
        List<Expense> pengeluaranList = new ArrayList();
        // Select All Query
        String selectQuery = "SELECT  * FROM " + DBHandler.TABLE_PENGELUARAN
                + " where " + DBHandler.KEY_TANGGAL + " = '" + date + "' order by " + DBHandler.KEY_ID + " desc";
        open();
        Cursor cursor = database.rawQuery(selectQuery, null);
        if (cursor.moveToFirst()) {
            do {
                Expense peng = new Expense();
                peng.setId(Integer.parseInt(cursor.getString(0)));
                peng.setIdkat(Integer.parseInt(cursor.getString(1)));
                peng.setDeskripsi(cursor.getString(2));
                peng.setJumlah(Integer.parseInt(cursor.getString(3)));
                peng.setTanggal(cursor.getString(4));
                pengeluaranList.add(peng);
            } while (cursor.moveToNext());
        }
        close();
        return pengeluaranList;
    }


    /**
     * Get Single Expense By Id
     */
    public Expense getPengeluaran(int id) {
        database = dbhandler.getReadableDatabase();

        Cursor cursor = database.query(DBHandler.TABLE_PENGELUARAN, new String[]{DBHandler.KEY_ID,
                        DBHandler.KEY_IDK, DBHandler.KEY_DESKRIPSI,
                        DBHandler.KEY_JUMLAH, DBHandler.KEY_TANGGAL}, DBHandler.KEY_ID + "=?",
                new String[]{String.valueOf(id)}, null, null, null, null);
        if (cursor != null)
            cursor.moveToFirst();

        Expense peng = new Expense(Integer.parseInt(cursor.getString(0)),
                Integer.parseInt(cursor.getString(1)), cursor.getString(2),
                Integer.parseInt(cursor.getString(3)), cursor.getString(4));
        cursor.close();
        close();
        return peng;
    }

    /**
     * Return Sum Of Expense Amount By Date
     */
    public String getTotal(String date) {
        database = dbhandler.getReadableDatabase();
        String total = "0";
        Cursor cursor = database.rawQuery("SELECT sum(" + DBHandler.KEY_JUMLAH + ") " +
                "FROM " + DBHandler.TABLE_PENGELUARAN + " where " + DBHandler.KEY_TANGGAL
                + " = '" + date + "'", null);
        if (cursor.getCount() > 0) {
            cursor.moveToFirst();
            total = cursor.getString(0);
        }
        cursor.close();
        close();
        return total;
    }

    /**
     * Count Data By Date
     */
    public int getCountByDate(String date) {
        String countQuery = "SELECT  * FROM " + DBHandler.TABLE_PENGELUARAN +
                " where " + DBHandler.KEY_TANGGAL + " = '" + date + "'";
        database = dbhandler.getReadableDatabase();
        Cursor cursor = database.rawQuery(countQuery, null);
        int a = cursor.getCount();
        cursor.close();
        return a;
    }

    /**
     * Return True If Any Expense Has A Spesific IdKategori
     */
    public boolean isKategoriExist(int idk) {
        String countQuery = "SELECT  * FROM " + DBHandler.TABLE_PENGELUARAN +
                " where " + DBHandler.KEY_IDK + " = " + idk;
        database = dbhandler.getReadableDatabase();
        Cursor cursor = database.rawQuery(countQuery, null);
        int a = cursor.getCount();
        cursor.close();
        if (a>0){
            return true;
        }
        return false;
    }

}
