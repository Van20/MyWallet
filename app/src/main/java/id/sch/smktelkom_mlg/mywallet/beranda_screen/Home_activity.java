package id.sch.smktelkom_mlg.mywallet.beranda_screen;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.graphics.drawable.RoundedBitmapDrawable;
import android.support.v4.graphics.drawable.RoundedBitmapDrawableFactory;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import id.sch.smktelkom_mlg.mywallet.Controller.SaldoController;
import id.sch.smktelkom_mlg.mywallet.Fragment.FragmentCategory;
import id.sch.smktelkom_mlg.mywallet.Fragment.FragmentDailyReport;
import id.sch.smktelkom_mlg.mywallet.Fragment.FragmentHome;
import id.sch.smktelkom_mlg.mywallet.Fragment.FragmentMonthlyReport;
import id.sch.smktelkom_mlg.mywallet.R;
import id.sch.smktelkom_mlg.mywallet.Utils.SPManager;
import id.sch.smktelkom_mlg.mywallet.Utils.Utils;
import id.sch.smktelkom_mlg.mywallet.database.DatabaseManagerUser;
import id.sch.smktelkom_mlg.mywallet.entity.User;
import id.sch.smktelkom_mlg.mywallet.registrasi_screen.Registrasi_activity;
import id.sch.smktelkom_mlg.mywallet.saldo_screen.SetSaldo_activity;

public class Home_activity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private DatabaseManagerUser databaseManagerUser;
    private User itemUsuario;
    private String ident;

    Toolbar toolbar;
    DrawerLayout drawer;
    NavigationView navigationView;
    FragmentManager fragmentManager;
    FragmentTransaction fragmentTransaction;
    Fragment fragment = null;
    SPManager prefManager;
    FloatingActionButton fabBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar1);
        setSupportActionBar(toolbar);

        prefManager = new SPManager(this);

        fabBtn = (FloatingActionButton) findViewById(R.id.fab1);
        drawer = (DrawerLayout) findViewById(R.id.drawer_layout1);

        navigationView = (NavigationView) findViewById(R.id.nav_view1);
        navigationView.setNavigationItemSelectedListener(this);

        fragmentManager = getFragmentManager();

        if (savedInstanceState == null) {
            fragment = new FragmentHome();
            callFragment(fragment);
        }

        //se agrego codigo del 39 al 68
        Bundle b = getIntent().getExtras();

        ident = b.getString("IDENT");

        databaseManagerUser= new DatabaseManagerUser(getApplicationContext());
        itemUsuario = databaseManagerUser.getUsuario(ident); // encuentra al usuario registrado en la bbdd

        View header = ((NavigationView)findViewById(R.id.nav_view1)).getHeaderView(0);

        ((TextView) header.findViewById(R.id.tv_nombre_usuario_menu1)).setText(itemUsuario.getNombre());
        ((TextView) header.findViewById(R.id.tv_correo_menu1)).setText(itemUsuario.getCorreo());

        Bitmap bitmapsinfoto = BitmapFactory.decodeResource(getResources(),R.drawable.imagen);
        RoundedBitmapDrawable roundedBitmapDrawablesinfoto = RoundedBitmapDrawableFactory.create(getResources(), bitmapsinfoto);
        roundedBitmapDrawablesinfoto.setCircular(true);

        ((ImageView) header.findViewById(R.id.imageView1)).setImageDrawable(roundedBitmapDrawablesinfoto);

        if(itemUsuario.getBytes()!=null){
            byte[] foodImage = itemUsuario.getBytes();
            Bitmap bitmap = BitmapFactory.decodeByteArray(foodImage, 0, foodImage.length);

            ((ImageView) header.findViewById(R.id.imageView1)).setImageBitmap(bitmap);

            Bitmap bitmap2 = ((BitmapDrawable)((ImageView) header.findViewById(R.id.imageView1)).getDrawable()).getBitmap();
            RoundedBitmapDrawable roundedBitmapDrawable = RoundedBitmapDrawableFactory.create(getResources(), bitmap2);
            roundedBitmapDrawable.setCircular(true);

            ((ImageView) header.findViewById(R.id.imageView1)).setImageDrawable(roundedBitmapDrawable);
        }



        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab1);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout1);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view1);
        navigationView.setNavigationItemSelectedListener(this);
    }

    /**
     * Set FAB Show or Hide
     */
    public void showFloatingActionButton() {
        fabBtn.show();
    };

    public void hideFloatingActionButton() {
        fabBtn.hide();
    };

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout1);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.home_activity, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings1) {
            showSaldoDialog();
            return true;
        }
        if (id == R.id.action_reset1) {
            prefManager.setFirstTimeLaunch(true);
            getApplicationContext().deleteDatabase("uangku");
            Intent intent =new Intent(getApplicationContext(),SetSaldo_activity.class);
            startActivity(intent);
            finish();
            return true;
        }
        if (id == R.id.share_action1) {
            Intent sharingIntent = new Intent(android.content.Intent.ACTION_SEND);
            sharingIntent.setType("text/plain");
            String shareBodyText = getResources().getString(R.string.sharebody);
            sharingIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, getResources().getString(R.string.sharesubject) );
            sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, shareBodyText);
            startActivity(Intent.createChooser(sharingIntent, getResources().getString(R.string.sharedialog)));
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    /**
     * Change Saldo Dialog
     */

    private void showSaldoDialog() {
        final SaldoController saldoKontrol = new SaldoController(this);
        LayoutInflater layoutInflaterAndroid = LayoutInflater.from(this);
        View mView = layoutInflaterAndroid.inflate(R.layout.dialog_saldo, null);
        AlertDialog.Builder alertDialogBuilderUserInput = new AlertDialog.Builder(this);
        alertDialogBuilderUserInput.setView(mView);

        final EditText newsaldo = (EditText) mView.findViewById(R.id.userInputDialog);
        newsaldo.setText(saldoKontrol.getSaldo());
        alertDialogBuilderUserInput
                .setCancelable(false)
                .setPositiveButton("Send", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialogBox, int id) {
                        saldoKontrol.updateSaldo(Integer.parseInt(newsaldo.getText().toString()));
                        callFragment(new FragmentHome());
                    }
                })

                .setNegativeButton("Cancel",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialogBox, int id) {
                                dialogBox.cancel();
                            }
                        });

        AlertDialog alertDialogAndroid = alertDialogBuilderUserInput.create();
        alertDialogAndroid.show();

    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_home1) {
            // Handle the camera action
            fragment = new FragmentHome();
            callFragment(fragment);
        } else if (id == R.id.nav_kategori1) {
            fragment = new FragmentCategory();
            callFragment(fragment);
        } else if (id == R.id.nav_harian1) {
            fragment = new FragmentDailyReport();
            callFragment(fragment);
        } else if (id == R.id.nav_bulanan1) {
            fragment = new FragmentMonthlyReport();
            callFragment(fragment);
        } else if (id == R.id.nav_bacres1) {
            backupRestoredialog();
        } else if (id == R.id.nav_info1) {
            showAboutDialog();
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout1);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    /**
     * Show About Dialog
     */
    private void showAboutDialog() {
        View mView = getLayoutInflater().inflate(R.layout.dialog_about, null, false);
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setIcon(R.mipmap.ic_launcher);
        builder.setTitle(R.string.app_name);
        builder.setView(mView);
        builder.create();
        builder.show();
    }

    /**
     * Selec Dialog For Backup or Restore
     */
    private void backupRestoredialog() {
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Backup & Restore");
        builder.setItems(R.array.dialog_backup, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                switch (which) {
                    case 0:
                        Toast.makeText(Home_activity.this, Utils.doBackup(), Toast.LENGTH_SHORT).show();
                        break;
                    case 1:
                        callFragment(new FragmentHome());
                        Toast.makeText(Home_activity.this, Utils.doRestore(), Toast.LENGTH_SHORT).show();
                        break;
                }
            }
        });
        builder.show();
    }

    /**
     * Start Selected Fragment
     */

    private void callFragment(Fragment fragment) {
        fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.remove(fragment);
        fragmentTransaction.replace(R.id.frame_container1, fragment);
        fragmentTransaction.commit();
    }
}
