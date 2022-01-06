package com.bakharaalief.peliharaanapp.UI.vaksin_pet;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.view.View;
import android.widget.DatePicker;
import android.widget.Toast;

import com.bakharaalief.peliharaanapp.Data.model.Pet;
import com.bakharaalief.peliharaanapp.R;
import com.bakharaalief.peliharaanapp.UI.MainActivity;
import com.bakharaalief.peliharaanapp.UI.dashboard.AddPetActivity;
import com.bakharaalief.peliharaanapp.UI.dashboard.DashboardActivity;
import com.bakharaalief.peliharaanapp.UI.detail_pet.AddPetAktifitasActivity;
import com.bakharaalief.peliharaanapp.UI.detail_pet.DetailPetActivity;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.TimeZone;

public class AddVaksinActivity extends AppCompatActivity implements
        DatePicker.OnDateChangedListener{

    private TextInputLayout nameField, dateField;
    private Date vaksindate;
    private Calendar calendar;
    private FirebaseAuth mAuth;
    private FirebaseFirestore db;
    private Pet petDataParcel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_vaksin);

        //set view
        MaterialToolbar topAppbar = findViewById(R.id.topAppBar);
        nameField = findViewById(R.id.vaksin_name_field);
        dateField = findViewById(R.id.vaksin_date_field);
        MaterialButton addVaskinButton = findViewById(R.id.add_vaksin_button);

        //getData from intent
        petDataParcel = this.getIntent().getParcelableExtra(DetailPetActivity.PET_DATA);

        //set firebase
        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();

        //top Appbar
        topAppbar.setNavigationOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(AddVaksinActivity.this, DetailPetActivity.class);
                intent.putExtra(DetailPetActivity.PET_DATA, petDataParcel);
                startActivity(intent);
                finish();
            }
        });

        //set date field
        Objects.requireNonNull(dateField.getEditText()).setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                vaksindate = new Date();
                calendar = Calendar.getInstance(TimeZone.getDefault());
                DatePickerDialog datePickerDialog = new DatePickerDialog(
                        AddVaksinActivity.this,
                        AddVaksinActivity.this::onDateChanged,
                        calendar.get(Calendar.YEAR),
                        calendar.get(Calendar.MONTH),
                        calendar.get(Calendar.DAY_OF_MONTH)
                );

                datePickerDialog.show();
            }
        });

        //add vaksin
        addVaskinButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(checkForm()) addVaksin();
            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(AddVaksinActivity.this, DetailPetActivity.class);
        intent.putExtra(DetailPetActivity.PET_DATA, petDataParcel);
        startActivity(intent);
        finish();
    }

    @Override
    public void onDateChanged(DatePicker datePicker, int year, int month, int day) {
        calendar = Calendar.getInstance();
        calendar.set(Calendar.YEAR, year);
        calendar.set(Calendar.MONTH, month);
        calendar.set(Calendar.DAY_OF_MONTH, day);

        vaksindate = calendar.getTime();
        Objects.requireNonNull(dateField.getEditText()).setText(toDateString(vaksindate));
    }

    private Editable toDateString(Date date){
        @SuppressLint("SimpleDateFormat")
        SimpleDateFormat d = new SimpleDateFormat("d MMM yyyy");

        return Editable.Factory.getInstance().newEditable(d.format(date));
    }

    private boolean checkForm(){
        boolean nameVaksinHewan = true, tanggalVaksinHewan = true;

        if(Objects.requireNonNull(nameField.getEditText()).length() == 0){
            nameField.setError(getString(R.string.not_empty_text));
            nameVaksinHewan = false;
        }
        else {
            nameField.setErrorEnabled(false);
        }

        if(Objects.requireNonNull(dateField.getEditText()).length() == 0){
            dateField.setError(getString(R.string.not_empty_text));
            tanggalVaksinHewan = false;
        }
        else{
            dateField.setErrorEnabled(false);
        }

        return nameVaksinHewan && tanggalVaksinHewan;
    }

    private void addVaksin(){
        String nameData = Objects.requireNonNull(nameField.getEditText()).getText().toString();

        Map<String, Object> vaksinData = new HashMap<>();
        vaksinData.put("name", nameData);
        vaksinData.put("date", vaksindate);

        //add data to firestore
        db.collection("user_pets")
                .document(Objects.requireNonNull(mAuth.getCurrentUser()).getUid())
                .collection("activities")
                .document(petDataParcel.getUid())
                .collection("vaksin")
                .add(vaksinData)
                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        Toast.makeText(AddVaksinActivity.this, "Berhasil Tambah Aktivitas " + nameData, Toast.LENGTH_SHORT).show();
                        Intent vaksinIntent = new Intent(AddVaksinActivity.this, VaksinActivity.class);
                        vaksinIntent.putExtra(DetailPetActivity.PET_DATA, petDataParcel);

                        showNotif();
                        startActivity(vaksinIntent);
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
        AlertDialog.Builder builder = new AlertDialog.Builder(AddVaksinActivity.this);
        builder.setTitle(title).setMessage(message);
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    private void showNotif(){
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, MainActivity.CHANNEL_ID)
                .setSmallIcon(R.drawable.cat_icon)
                .setContentTitle("Menambahkan Vaksin")
                .setContentText("Berhasil Menambah Vaksin :)")
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                // Set the intent that will fire when the user taps the notification
                .setAutoCancel(true);

        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(this);
        notificationManager.notify(1, builder.build());
    }
}