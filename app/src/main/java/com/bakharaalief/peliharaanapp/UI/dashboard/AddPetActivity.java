package com.bakharaalief.peliharaanapp.UI.dashboard;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.DatePicker;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.bakharaalief.peliharaanapp.R;
import com.bakharaalief.peliharaanapp.UI.MainActivity;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.TimeZone;
import java.util.function.Consumer;

public class AddPetActivity extends AppCompatActivity implements DatePicker.OnDateChangedListener {

    private TextInputLayout nameField, typeField, birthField;
    private Date birthdate;
    private Calendar calendar;
    private FirebaseAuth mAuth;
    private FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_pet);

        //set view
        MaterialToolbar topAppbar = findViewById(R.id.topAppBar);
        nameField = findViewById(R.id.pet_name_field);
        typeField = findViewById(R.id.pet_type_field);
        birthField = findViewById(R.id.pet_birth_field);
        MaterialButton addPetButton = findViewById(R.id.add_pet_button);

        //set firebase
        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();

        //top Appbar
        topAppbar.setNavigationOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                AddPetActivity.super.onBackPressed();
            }
        });

        //set type data
        ArrayList<String> items = new ArrayList<String>();
        db.collection("types")
                .get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        queryDocumentSnapshots.forEach(new Consumer<QueryDocumentSnapshot>() {
                            @Override
                            public void accept(QueryDocumentSnapshot queryDocumentSnapshot) {
                                items.add(queryDocumentSnapshot.getId());
                            }
                        });
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        items.add("Not Found");
                    }
                });

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, R.layout.list_item, items);
        ((AutoCompleteTextView) Objects.requireNonNull(typeField.getEditText())).setAdapter(adapter);

        //set birthfield
        Objects.requireNonNull(birthField.getEditText()).setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                birthdate = new Date();
                calendar = Calendar.getInstance(TimeZone.getDefault());
                DatePickerDialog datePickerDialog = new DatePickerDialog(
                        AddPetActivity.this,
                        AddPetActivity.this::onDateChanged,
                        calendar.get(Calendar.YEAR),
                        calendar.get(Calendar.MONTH),
                        calendar.get(Calendar.DAY_OF_MONTH)
                        );

                datePickerDialog.show();
            }
        });

        //add pet button
        addPetButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(checkForm()) addPet();
            }
        });
    }

    @Override
    public void onDateChanged(DatePicker datePicker, int year, int month, int day) {
        calendar = Calendar.getInstance();
        calendar.set(Calendar.YEAR, year);
        calendar.set(Calendar.MONTH, month);
        calendar.set(Calendar.DAY_OF_MONTH, day);
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);

        birthdate = calendar.getTime();
        Objects.requireNonNull(birthField.getEditText()).setText(toDateString(birthdate));
    }

    private Editable toDateString(Date date){
        @SuppressLint("SimpleDateFormat")
        SimpleDateFormat d = new SimpleDateFormat("d MMM yyyy");

        return Editable.Factory.getInstance().newEditable(d.format(date));
    }

    private boolean checkForm(){
        boolean nameHewanBenar = true, typeHewanBenar = true, tanggalLahirHewan = true;

        if(Objects.requireNonNull(nameField.getEditText()).length() == 0){
            nameField.setError(getString(R.string.not_empty_text));
            nameHewanBenar = false;
        }
        else {
            nameField.setErrorEnabled(false);
        }

        if(Objects.requireNonNull(typeField.getEditText()).length() == 0){
            typeField.setError(getString(R.string.not_empty_text));
            typeHewanBenar = false;
        }
        else {
            typeField.setErrorEnabled(false);
        }

        if(Objects.requireNonNull(birthField.getEditText()).length() == 0){
            birthField.setError(getString(R.string.not_empty_text));
            tanggalLahirHewan = false;
        }
        else{
            birthField.setErrorEnabled(false);
        }

        return nameHewanBenar && typeHewanBenar && tanggalLahirHewan;
    }

    private void addPet(){
        String nameData = Objects.requireNonNull(nameField.getEditText()).getText().toString();
        String typeData = Objects.requireNonNull(typeField.getEditText()).getText().toString();

        Map<String, Object> petData = new HashMap<>();
        petData.put("name", nameData);
        petData.put("type", typeData);
        petData.put("birth", birthdate);

        //add data to firestore
        db.collection("user_pets")
                .document(Objects.requireNonNull(mAuth.getCurrentUser()).getUid())
                .collection("pets")
                .add(petData)
                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        Toast.makeText(AddPetActivity.this, "Berhasil Tambah " + nameData, Toast.LENGTH_SHORT).show();
                        Intent dashboardIntent = new Intent(AddPetActivity.this, DashboardActivity.class);

                        showNotif();
                        startActivity(dashboardIntent);
                        finish();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        dialogBox("Gagal Membuat Peliharaan", "Anda Gagal Membuat Peliharaan");
                    }
                });
    }

    private void dialogBox(String title, String message){
        AlertDialog.Builder builder = new AlertDialog.Builder(AddPetActivity.this);
        builder.setTitle(title).setMessage(message);
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    private void showNotif(){
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, MainActivity.CHANNEL_ID)
                .setSmallIcon(R.drawable.cat_icon)
                .setContentTitle("Menambahkan Peliharaan")
                .setContentText("Berhasil Menambah Peliharaan :)")
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                // Set the intent that will fire when the user taps the notification
                .setAutoCancel(true);

        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(this);
        notificationManager.notify(1, builder.build());
    }
}