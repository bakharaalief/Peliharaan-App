package com.bakharaalief.peliharaanapp.UI;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.bakharaalief.peliharaanapp.R;
import com.bakharaalief.peliharaanapp.UI.dashboard.DashboardActivity;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class RegisterActivity extends AppCompatActivity implements View.OnClickListener {

    private TextInputLayout nameField, emailField, passwordField;
    private FirebaseAuth mAuth;
    private FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        //set view
        MaterialToolbar topAppbar = findViewById(R.id.topAppBar);
        nameField = findViewById(R.id.name_field);
        emailField = findViewById(R.id.email_field);
        passwordField = findViewById(R.id.password_field);
        MaterialButton registerButton = findViewById(R.id.register_button);

        //set firebase
        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();

        //top Appbar
        topAppbar.setNavigationOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                RegisterActivity.super.onBackPressed();
            }
        });

        //register button click
        registerButton.setOnClickListener(this);
    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.register_button :
                if(checkForm()) registerUser();
                break;
            default:
                break;
        }
    }

    private boolean checkForm(){
        boolean nameBenar = true, emailBenar = true, passwordBenar = true;

        if(Objects.requireNonNull(nameField.getEditText()).length() == 0){
            nameField.setError(getString(R.string.not_empty_text));
            nameBenar = false;
        }
        else{
            nameField.setErrorEnabled(false);
        }

        if(Objects.requireNonNull(emailField.getEditText()).length() == 0){
            emailField.setError(getString(R.string.not_empty_text));
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
            passwordField.setError(getString(R.string.not_empty_text));
            passwordBenar = false;
        }
        else if(Objects.requireNonNull(passwordField.getEditText()).length() < 6){
            passwordField.setError("Minimal 6 Karakter");
            passwordBenar = false;
        }
        else {
            passwordField.setErrorEnabled(false);
        }

        return nameBenar && emailBenar && passwordBenar;
    }

    private boolean isEmailValid(String email) {
        return android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }

    private void registerUser(){
        String nameData = Objects.requireNonNull(nameField.getEditText()).getText().toString();
        String emailData = Objects.requireNonNull(emailField.getEditText()).getText().toString();
        String passwordData = Objects.requireNonNull(passwordField.getEditText()).getText().toString();

        mAuth.createUserWithEmailAndPassword(emailData, passwordData)
                .addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                    @Override
                    public void onSuccess(AuthResult authResult) {

                        Map<String, Object> user = new HashMap<>();
                        user.put("name", nameData);
                        user.put("email", emailData);

                        //add data to firestore
                        db.collection("users")
                                .document(Objects.requireNonNull(mAuth.getCurrentUser()).getUid())
                                .set(user)
                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void aVoid) {
                                        Intent dashboardIntent = new Intent(RegisterActivity.this, DashboardActivity.class);
                                        startActivity(dashboardIntent);
                                        finish();
                                    }
                                });
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        dialogBox("Register Gagal", "Buat User Gagal :(");
                    }
                });
    }

    private void dialogBox(String title, String message){
        AlertDialog.Builder builder = new AlertDialog.Builder(RegisterActivity.this);

        builder.setMessage(title).setTitle(message);

        AlertDialog dialog = builder.create();
        dialog.show();
    }
}