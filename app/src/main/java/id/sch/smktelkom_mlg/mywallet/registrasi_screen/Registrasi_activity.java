package id.sch.smktelkom_mlg.mywallet.registrasi_screen;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.graphics.drawable.RoundedBitmapDrawable;
import android.support.v4.graphics.drawable.RoundedBitmapDrawableFactory;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;

import id.sch.smktelkom_mlg.mywallet.Controller.SaldoController;
import id.sch.smktelkom_mlg.mywallet.Model.Saldo;
import id.sch.smktelkom_mlg.mywallet.PrefManager;
import id.sch.smktelkom_mlg.mywallet.R;
import id.sch.smktelkom_mlg.mywallet.Utils.SPManager;
import id.sch.smktelkom_mlg.mywallet.Welcome_activity;
import id.sch.smktelkom_mlg.mywallet.beranda_screen.Home_activity;
import id.sch.smktelkom_mlg.mywallet.database.DatabaseManagerUser;
import id.sch.smktelkom_mlg.mywallet.login_screen.Login_activity;
import id.sch.smktelkom_mlg.mywallet.saldo_screen.SetSaldo_activity;

public class Registrasi_activity extends AppCompatActivity {

    private TextView loginLink;
    private ImageView imageView;
    private EditText password;
    private EditText nombre;
    private EditText email;
    private Button registrar;
    private DatabaseManagerUser managerUsuario;
    private String sPassword, sNombre, sEmail;
    private int request_code = 1;
    private Bitmap bitmap_foto;
    private RoundedBitmapDrawable roundedBitmapDrawable;
    private byte[] bytes;
    SPManager prefManager;
    SetSaldo_activity registrasi;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);

        prefManager = new SPManager(this);
        setContentView(R.layout.activity_registrasi);
        registrasi = new SetSaldo_activity();

        imageView = (ImageView) findViewById(R.id.usuario_imagen_registro);
        loginLink = (TextView)findViewById(R.id.link_login);
        email = (EditText)findViewById(R.id.correo_registro);
        password = (EditText)findViewById(R.id.password_registro);
        nombre = (EditText)findViewById(R.id.nombre_registro);
        registrar = (Button)findViewById(R.id.btn_registro_usuario);
        bitmap_foto = BitmapFactory.decodeResource(getResources(),R.drawable.imagen);
        roundedBitmapDrawable = RoundedBitmapDrawableFactory.create(getResources(), bitmap_foto);
        roundedBitmapDrawable.setCircular(true);
        imageView.setImageDrawable(roundedBitmapDrawable);

        registrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                registrar();
            }
        });

        if (prefManager.isFirstTimeLaunch()) {
            startActivity(this.getIntent());
            finish();
        }
        else {
            Intent i = new Intent(Registrasi_activity.this, Login_activity.class);
            startActivity(i);
            finish();
            }


        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = null;
                //verificacion de la version de plataforma
                if(Build.VERSION.SDK_INT < 19){
                    //android 4.3  y anteriores
                    i = new Intent();
                    i.setAction(Intent.ACTION_GET_CONTENT);
                }else {
                    //android 4.4 y superior
                    i = new Intent(Intent.ACTION_OPEN_DOCUMENT);
                    i.addCategory(Intent.CATEGORY_OPENABLE);
                }
                i.setType("image/*");
                startActivityForResult(i, request_code);
            }
        });


        loginLink.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                prefManager.setFirstTimeLaunch(false);
                startActivity(new Intent(Registrasi_activity.this, Login_activity.class));
                finish();

                overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);
            }
        });
    }

    public void launchHomeScreen() {
        prefManager.setFirstTimeLaunch(false);
        Intent i = new Intent(Registrasi_activity.this, Login_activity.class);
        startActivity(i);
        finish();
    }

    public void registrar(){

        if (!validar()) return;

        sEmail = email.getText().toString();
        sPassword = password.getText().toString();
        sNombre = nombre.getText().toString();

        final ProgressDialog progressDialog = new ProgressDialog(Registrasi_activity.this,
                R.style.AppTheme_Dark_Dialog);
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage("Initializing..");
        progressDialog.show();

        managerUsuario = new DatabaseManagerUser(this);

        email.getText().clear();
        password.getText().clear();
        nombre.getText().clear();

        new android.os.Handler().postDelayed(
                new Runnable() {
                    public void run() {
                        if(managerUsuario.comprobarRegistro(sEmail)){
                            progressDialog.dismiss();
                            password.setText(sPassword);
                            nombre.setText(sNombre);
                            String mesg = String.format("Initializing", null);
                            Toast.makeText(getApplicationContext(),mesg, Toast.LENGTH_LONG).show();
                        }else {
                            managerUsuario.insertar_parametros(null, sEmail, sPassword, bytes, sNombre);
                            String mesg = String.format("Registrasi akun berhasil..", sNombre);
                            Toast.makeText(getBaseContext(),mesg, Toast.LENGTH_LONG).show();
                            Intent intent =new Intent(getApplicationContext(),Login_activity.class);
                            intent.putExtra("IDENT",sEmail);
                            startActivity(intent);
                            finish();
                            progressDialog.dismiss();
                        }
                    }
                }, 3000);
    }

    private boolean validar() {
        boolean valid = true;

        String sNombre = nombre.getText().toString();
        String sPassword = password.getText().toString();
        String sEmail = email.getText().toString();

        if (sNombre.isEmpty() || sNombre.length() < 3) {
            nombre.setError("Username minimal 3 huruf");
            valid = false;
        } else {
            nombre.setError(null);
        }

        if (sEmail.isEmpty() || !android.util.Patterns.EMAIL_ADDRESS.matcher(sEmail).matches()) {
            email.setError("E-mail tidak valid");
            valid = false;
        } else {
            email.setError(null);
        }

        if (sPassword.isEmpty() || password.length() < 4 || password.length() > 10) {
            password.setError("Password harus diisi 4 sampai 10 digit");
            valid = false;
        } else {
            password.setError(null);
        }

        return valid;
    }

    private byte[] imageToByte(ImageView image) {
        Bitmap bitmapFoto = ((BitmapDrawable)image.getDrawable()).getBitmap();
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmapFoto.compress(Bitmap.CompressFormat.PNG, 100, stream);
        byte[] byteArray = stream.toByteArray();
        return byteArray;
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data){
        if(resultCode == RESULT_OK && requestCode == request_code){
            imageView.setImageURI(data.getData());
            bytes = imageToByte(imageView);

            // para que se vea la imagen en circulo
            Bitmap bitmap = ((BitmapDrawable)imageView.getDrawable()).getBitmap();
            roundedBitmapDrawable = RoundedBitmapDrawableFactory.create(getResources(), bitmap);
            roundedBitmapDrawable.setCircular(true);
            imageView.setImageDrawable(roundedBitmapDrawable);
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

}


