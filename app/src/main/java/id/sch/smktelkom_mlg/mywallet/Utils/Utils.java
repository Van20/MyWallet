package id.sch.smktelkom_mlg.mywallet.Utils;

import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListAdapter;
import android.widget.ListView;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.nio.channels.FileChannel;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by fianxeka on 20/06/17.
 */

public class Utils {

    /**
     * Convert Curensy To Indonesia Format
     */

    public static String convertCur(String param) {
        Double money = Double.parseDouble(param);
        DecimalFormat kursIndonesia = (DecimalFormat) DecimalFormat.getCurrencyInstance();
        DecimalFormatSymbols formatRp = new DecimalFormatSymbols();
        formatRp.setCurrencySymbol("Rp.");
        formatRp.setMonetaryDecimalSeparator(',');
        formatRp.setGroupingSeparator('.');

        kursIndonesia.setDecimalFormatSymbols(formatRp);
        return kursIndonesia.format(money);
    }

    /**
     * Return Now Date
     */

    public static String getDateNow() {
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Date date = new Date();
        return dateFormat.format(date);
    }


    /**
     * Restorer Backup Database
     */

    public static String doRestore() {
        try {
            File sd = Environment.getExternalStorageDirectory();
            File data = Environment.getDataDirectory();

            if (sd.canWrite()) {
                String currentDBPath = "/data/data/io.github.fianekame.uangku/databases/uangku";
                String backupDBPath = "uangku";
                File currentDB = new File(currentDBPath);
                File backupDB = new File(sd, backupDBPath);
                if (backupDB.exists()){
                    if (currentDB.exists()) {
                        Log.d("Restoring", "UangkuDB");
                        FileChannel src = new FileInputStream(backupDB).getChannel();
                        FileChannel dst = new FileOutputStream(currentDB).getChannel();
                        dst.transferFrom(src, 0, src.size());
                        src.close();
                        dst.close();
                        return "Database Restored successfully";
                    }else{
                        return "No Data In Package";
                    }
                }else{
                    return "Belum Ada Backupan Database";
                }
            }
        } catch (Exception e) {
            return "Belum Ada Database";
        }
        return "Belum";
    }

    /**
     * To Do Backup Database
     */
    public static String doBackup() {
        try {
            File sd = Environment.getExternalStorageDirectory();
            File data = Environment.getDataDirectory();
            if (sd.canWrite()) {
                String currentDBPath = "/data/data/io.github.fianekame.uangku/databases/uangku";
                String backupDBPath = "uangku";
                File currentDB = new File(currentDBPath);
                File backupDB = new File(sd, backupDBPath);
                if (currentDB.exists()) {
                    Log.d("Backuping", "UangkuDB");
                    FileChannel src = new FileInputStream(currentDB).getChannel();
                    FileChannel dst = new FileOutputStream(backupDB).getChannel();
                    dst.transferFrom(src, 0, src.size());
                    src.close();
                    dst.close();
                    return "Database Backup Successfully";
                }
            }
        } catch (Exception e) {

        }
        return null;
    }

    /**
     * Set Listview Inside ScrollView
     */
    public static void setListViewHeightBasedOnChildren(ListView listView) {
        ListAdapter listAdapter = listView.getAdapter();
        if (listAdapter == null) {
            // pre-condition
            return;
        }

        int totalHeight = 0;
        int desiredWidth = View.MeasureSpec.makeMeasureSpec(listView.getWidth(), View.MeasureSpec.AT_MOST);
        for (int i = 0; i < listAdapter.getCount(); i++) {
            View listItem = listAdapter.getView(i, null, listView);
            listItem.measure(desiredWidth, View.MeasureSpec.UNSPECIFIED);
            totalHeight += listItem.getMeasuredHeight();
        }

        ViewGroup.LayoutParams params = listView.getLayoutParams();
        params.height = totalHeight + (listView.getDividerHeight() * (listAdapter.getCount() - 1));
        listView.setLayoutParams(params);
        listView.requestLayout();
    }

}
