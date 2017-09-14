package com.trekkon.patigeni.activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.trekkon.patigeni.R;
import com.trekkon.patigeni.controller.BaseResult;
import com.trekkon.patigeni.controller.ganti_password.GantiPasswordController;


public class GantiPassword extends AppCompatActivity implements BaseResult {

    EditText txtEmail, txtPasswordLama, txtPasswordBaru, txtUlangPassword;
    Button btnKirim;
    GantiPasswordController gantiPasswordController;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ganti_password);

        FindID();
    }

    void doGantiPassword(){
        gantiPasswordController = new GantiPasswordController(this, GantiPassword.this);
        gantiPasswordController.gantiPassword(txtEmail.getText().toString(),txtPasswordBaru.getText().toString());
    }

    void FindID(){

        txtEmail=(EditText)findViewById(R.id.txtEmail);
        txtPasswordBaru=(EditText)findViewById(R.id.txtPasswordBaru);
        txtPasswordLama=(EditText)findViewById(R.id.txtPasswordLama);
        txtUlangPassword=(EditText)findViewById(R.id.txtUlangPassword);
        btnKirim=(Button)findViewById(R.id.btnKirim);

        btnKirim.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                doGantiPassword();

            }
        });


    }

    @Override
    public void showValidationError() {
        Toast.makeText(GantiPassword.this,"Error",Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onSuccess() {
        Toast.makeText(GantiPassword.this,"Ganti password OK",Toast.LENGTH_SHORT).show();
        finish();
    }

    @Override
    public void onError(String errorMessage) {

    }


}
