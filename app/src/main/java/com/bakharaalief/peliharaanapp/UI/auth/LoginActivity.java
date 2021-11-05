package com.bakharaalief.peliharaanapp.UI.auth;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.bakharaalief.peliharaanapp.R;
import com.bakharaalief.peliharaanapp.UI.dashboard.DashboardActivity;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

import java.util.Objects;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {

    private TextInputLayout emailField, passwordField;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        //set variable for view
        MaterialButton loginButton = findViewById(R.id.login_button);
        emailField = findViewById(R.id.email_field);
        passwordField = findViewById(R.id.password_field);
        TextView registerButton = findViewById(R.id.register_button);

        //set firebase
        mAuth = FirebaseAuth.getInstance();

        //loginButton
        loginButton.setOnClickListener(this);
        registerButton.setOnClickListener(this);
    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.login_button:
                if(checkForm()) loginUser();
                break;

            case R.id.register_button:
                Intent registerIntent = new Intent(this, RegisterActivity.class);
                startActivity(registerIntent);
                break;
        }
    }

    private boolean checkForm(){
        boolean emailBenar = true, passwordBenar = true;

        if(Objects.requireNonNull(emailField.getEditText()).length() == 0){
            emailField.setError("Tidak Boleh Kosong");
            emailBenar = false;
        }
        else if(!isEmailValid(Objects.requireNonNull(emailField.getEditText()).getText().toString())){
            emailField.setError("Ini Bukan Email");
            emailBenar = false;
        }
        else{
            emailField.setErrorEnabled(false);
        }

        if(Objects.requireNonNull(passwordField.getEditText()).length() == 0){
            passwordField.setError("Tidak Boleh Kosong");
            passwordBenar = false;
        }
        else if(Objects.requireNonNull(passwordField.getEditText()).length() < 6){
            passwordField.setError("Minimal 6 Karakter");
            passwordBenar = false;
        }
        else {
            passwordField.setErrorEnabled(false);
        }

        return emailBenar && passwordBenar;
    }

    private boolean isEmailValid(String email) {
        return android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }

    private void loginUser(){
        String emailData = Objects.requireNonNull(emailField.getEditText()).getText().toString();
        String passwordData = Objects.requireNonNull(passwordField.getEditText()).getText().toString();

        mAuth.signInWithEmailAndPassword(emailData, passwordData)
                .addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                    @Override
                    public void onSuccess(AuthResult authResult) {
                        Intent dashboardIntent = new Intent(LoginActivity.this, DashboardActivity.class);
                        startActivity(dashboardIntent);
                        finish();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        AlertDialog.Builder builder = new AlertDialog.Builder(LoginActivity.this);
                        dialogBox("Login Gagal", "Silahkan Cek Email atau Password Anda Kembali");
                    }
                });
    }

    private void dialogBox(String title, String message){
        AlertDialog.Builder builder = new AlertDialog.Builder(LoginActivity.this);
        builder.setTitle(title).setMessage(message);
        AlertDialog dialog = builder.create();
        dialog.show();
    }
}