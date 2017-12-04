package id.sch.smktelkom_mlg.mywallet.Controller;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

import id.sch.smktelkom_mlg.mywallet.DBHelper.DBHandler;
import id.sch.smktelkom_mlg.mywallet.Model.Expense;
import id.sch.smktelkom_mlg.mywallet.Model.Report;


public class ReportController {

    public static final String LOGTAG = "REPORT PROCCES";
    SQLiteOpenHelper dbhandler;
    SQLiteDatabase database;

    public ReportController(Context context) {
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
     * Return DailyReport By Date Parameter
     */
    public List<Expense> getDailyReport(String date) {
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
     * Return MontlyReport
     */
    public List<Report> getMontlyReport(String month, String year) {
        List<Report> laporanList = new ArrayList();
        String selectQuery = "SELECT " +
                DBHandler.TABLE_PENGELUARAN + "." + DBHandler.KEY_IDK +
                ", count(" + DBHandler.KEY_IDK + ")" + ", sum(" + DBHandler.KEY_JUMLAH + "), " +
                DBHandler.TABLE_KATEGORI + "." + DBHandler.KEY_DESKRIPSI +
                " FROM " + DBHandler.TABLE_PENGELUARAN +
                " INNER JOIN " + DBHandler.TABLE_KATEGORI + " ON " +
                DBHandler.TABLE_PENGELUARAN + "." + DBHandler.KEY_IDK + " = " + DBHandler.TABLE_KATEGORI + "." + DBHandler.KEY_ID +
                " where strftime('%m', tanggal) = '" + month + "' and strftime('%Y', tanggal) = '" + year +
                "' group by " + DBHandler.KEY_IDK;
        Log.d("Query Laporan", selectQuery);
        open();
        Cursor cursor = database.rawQuery(selectQuery, null);
        if (cursor.moveToFirst()) {
            do {
                Report lapor = new Report();
                lapor.setIdkategori(Integer.parseInt(cursor.getString(0)));
                lapor.setTotal(Integer.parseInt(cursor.getString(1)));
                lapor.setJumlah(Integer.parseInt(cursor.getString(2)));
                lapor.setNamakategori(cursor.getString(3));
                laporanList.add(lapor);
            } while (cursor.moveToNext());
        }
        close();
        return laporanList;
    }

    /**
     * Return MontlyReport By Category Parameter
     */
    public List<Expense> getMontlyReportByCategory(int idk, String month, int year) {
        List<Expense> pengeluaranList = new ArrayList();
        // Select All Query
        String selectQuery = "SELECT  * FROM " + DBHandler.TABLE_PENGELUARAN
                + " where strftime('%m', tanggal) = '" + month + "' and strftime('%Y', tanggal) = '" + year +
                "' and " + DBHandler.KEY_IDK + " = " + idk + " order by " + DBHandler.KEY_TANGGAL + " asc";
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
}
