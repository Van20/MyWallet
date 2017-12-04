package id.sch.smktelkom_mlg.mywallet.DBHelper;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by fianxeka on 15/06/17.
 */

public class DBHandler extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = 1;

    // Database Name
    private static final String DATABASE_NAME = "uangku";

    // Table Names
    public static final String TABLE_KATEGORI = "kategori";
    public static final String TABLE_SALDO = "saldo";
    public static final String TABLE_PENGELUARAN = "pengeluaran";

    public static final String KEY_ID = "id";
    public static final String KEY_IDK = "idk";
    public static final String KEY_DESKRIPSI = "deskripsi";
    public static final String KEY_JUMLAH = "jumlah";
    public static final String KEY_TANGGAL = "tanggal";


    private static final String CREATE_TABLE_KATEGORI = "CREATE TABLE "
            + TABLE_KATEGORI + "(" + KEY_ID + " INTEGER PRIMARY KEY," + KEY_DESKRIPSI
            + " TEXT" + ")";

    private static final String CREATE_TABLE_SALDO = "CREATE TABLE "
            + TABLE_SALDO + "(" + KEY_ID + " INTEGER PRIMARY KEY," + KEY_JUMLAH
            + " INTEGER" + ")";

    private static final String CREATE_TABLE_PENGELUARAN = "CREATE TABLE "
            + TABLE_PENGELUARAN + "(" + KEY_ID + " INTEGER PRIMARY KEY ," + KEY_IDK
            + " INTEGER," + KEY_DESKRIPSI + " TEXT," + KEY_JUMLAH + " INTEGER," + KEY_TANGGAL + " DEFAULT CURRENT_DATE" + ")";

    public DBHandler(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // creating required tables
        db.execSQL(CREATE_TABLE_KATEGORI);
        db.execSQL(CREATE_TABLE_PENGELUARAN);
        db.execSQL(CREATE_TABLE_SALDO);
        db.execSQL("INSERT into 'kategori' ('deskripsi') VALUES ('Konsumsi') ");
        db.execSQL("INSERT into 'kategori' ('deskripsi') VALUES ('Transportasi') ");
        db.execSQL("INSERT into 'kategori' ('deskripsi') VALUES ('Sosial') ");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // on upgrade drop older tables
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_KATEGORI);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_SALDO);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_PENGELUARAN);

        // create new tables
        onCreate(db);
    }
}
