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
import id.sch.smktelkom_mlg.mywallet.Model.Category;

/**
 * Created by fianxeka on 18/06/17.
 */

public class CategoryController {

    private ExpenseController expense;
    public static final String LOGTAG = "KATEGORI TABEL";
    SQLiteOpenHelper dbhandler;
    SQLiteDatabase database;

    public CategoryController(Context context) {
        expense = new ExpenseController(context);
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
     * Create Update Delete Kategori
     */
    public void addCategory(Category cat) {
        open();
        ContentValues values = new ContentValues();
        values.put(DBHandler.KEY_DESKRIPSI, cat.getKategori());
        database.insert(DBHandler.TABLE_KATEGORI, null, values);
        close();
    }

    public void updateCategory(Category cat) {
        open();
        ContentValues values = new ContentValues();
        values.put(DBHandler.KEY_DESKRIPSI, cat.getKategori());
        database.update(DBHandler.TABLE_KATEGORI, values, DBHandler.KEY_ID + " = ?",
                new String[]{String.valueOf(cat.getId())});
        close();
    }

    public void deleteCategory(int id, boolean exist) {
        open();
        if (exist){
            Log.d(LOGTAG, "Delete Exist");
            expense.deleteExpenseByCategory(id);
        }
        database.delete(DBHandler.TABLE_KATEGORI, DBHandler.KEY_ID + " = ?",
                new String[]{String.valueOf(id)});
        close();
    }



    /**
     * Read AllKategori , Read Kategori Name byId
     */
    public List<Category> getAllCategory() {
        List<Category> kategoriList = new ArrayList();
        String selectQuery = "SELECT  * FROM " + DBHandler.TABLE_KATEGORI;
        open();
        Cursor cursor = database.rawQuery(selectQuery, null);
        if (cursor.moveToFirst()) {
            do {
                Category kategori = new Category();
                kategori.setId(Integer.parseInt(cursor.getString(0)));
                kategori.setKategori(cursor.getString(1));
                kategoriList.add(kategori);
            } while (cursor.moveToNext());
        }
        close();
        return kategoriList;
    }


    /**
     * Return Name From IDKategori, Return Data Count
     */
    public String getName(int id) {
        database = dbhandler.getReadableDatabase();
        Cursor cursor = database.query(DBHandler.TABLE_KATEGORI, new String[]{DBHandler.KEY_ID,
                        DBHandler.KEY_DESKRIPSI}, DBHandler.KEY_ID + "=?",
                new String[]{String.valueOf(id)}, null, null, null, null);
        if (cursor != null)
            cursor.moveToFirst();
        String name = cursor.getString(1);
        cursor.close();
        close();
        return name;
    }

    public int getCount() {
        String countQuery = "SELECT  * FROM " + DBHandler.TABLE_KATEGORI;
        database = dbhandler.getReadableDatabase();
        Cursor cursor = database.rawQuery(countQuery, null);
        int a = cursor.getCount();
        cursor.close();
        close();
        return a;
    }

}
