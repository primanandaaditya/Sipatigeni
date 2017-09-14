package com.trekkon.patigeni.activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.trekkon.patigeni.R;
import com.trekkon.patigeni.controller.BaseResult;
import com.trekkon.patigeni.controller.register.RegisterController;


public class Register extends SuperActivity implements BaseResult {

    EditText etName,etPassword, etEmail, etUlangPassword;
    Button btnRegister;

    RegisterController registerController;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        FindID();

    }

    void FindID(){

        registerController = new RegisterController(this, Register.this);

        etName=(EditText)findViewById(R.id.etName);
        etEmail=(EditText)findViewById(R.id.etEmail);
        etPassword=(EditText)findViewById(R.id.etPassword);
        etUlangPassword=(EditText)findViewById(R.id.etUlangPassword);
        btnRegister=(Button)findViewById(R.id.btnRegister);


        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String password, ulangPassword;
                password=etPassword.getText().toString();
                ulangPassword=etUlangPassword.getText().toString();
                if (password.equals(ulangPassword)){
                    registerController.register(etName.getText().toString(),etEmail.getText().toString(),etPassword.getText().toString());
                } else {
                    Toast.makeText(Register.this, "Password belum sama", Toast.LENGTH_SHORT).show();
                }


            }
        });


    }

    @Override
    public void showValidationError() {

        Toast.makeText(Register.this, "Nama, email dan password harus diisi...", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onSuccess() {
        Toast.makeText(Register.this, "Register OK", Toast.LENGTH_SHORT).show();

    }

    @Override
    public void onError(String errorMessage) {
        showDialog(this, "Alert", "Error/email sudah terdaftar", "Tutup", "", 1);
    }
}
