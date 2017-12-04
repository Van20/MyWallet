package id.sch.smktelkom_mlg.mywallet.Utils;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by fianxeka on 19/06/17.
 */

public class SPManager {
    SharedPreferences pref;
    SharedPreferences.Editor editor;
    Context _context;

    int PRIVATE_MODE = 0;

    private static final String PREF_NAME = "uangku-welcome";

    private static final String IS_FIRST = "firsttime";


    public SPManager(Context context) {
        this._context = context;
        pref = _context.getSharedPreferences(PREF_NAME, PRIVATE_MODE);
        editor = pref.edit();
    }

    public void setFirstTimeLaunch(boolean isFirstTime) {
        editor.putBoolean(IS_FIRST, isFirstTime);
        editor.commit();
    }

    public boolean isFirstTimeLaunch() {
        return pref.getBoolean(IS_FIRST,true);
    }

}
