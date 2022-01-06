package com.bakharaalief.peliharaanapp.UI.detail_pet;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.format.DateFormat;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.DatePicker;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.bakharaalief.peliharaanapp.Data.model.Pet;
import com.bakharaalief.peliharaanapp.R;
import com.bakharaalief.peliharaanapp.UI.MainActivity;
import com.bakharaalief.peliharaanapp.UI.dashboard.AddPetActivity;
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

public class AddPetAktifitasActivity extends AppCompatActivity implements
        DatePicker.OnDateChangedListener, TimePickerDialog.OnTimeSetListener{

    private TextInputLayout typeField, dateField, noteField;
    private Date aktifitasdate;
    private Calendar calendar;
    private FirebaseAuth mAuth;
    private FirebaseFirestore db;
    private Pet petDataParcel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_pet_aktifitas);

        //set view
        MaterialToolbar topAppbar = findViewById(R.id.topAppBar);
        typeField = findViewById(R.id.aktifitas_type_field);
        dateField = findViewById(R.id.aktifitas_date_field);
        noteField = findViewById(R.id.aktifitas_note_field);
        MaterialButton addPetAktifitasButton = findViewById(R.id.add_aktifitas_button);

        //getData from intent
        petDataParcel = this.getIntent().getParcelableExtra(DetailPetActivity.PET_DATA);

        //set firebase
        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();

        //top Appbar
        topAppbar.setNavigationOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                AddPetAktifitasActivity.super.onBackPressed();
            }
        });

        //set type data
        ArrayList<String> items = new ArrayList<String>();
        db.collection("activities")
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

        //set date field
        Objects.requireNonNull(dateField.getEditText()).setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                aktifitasdate = new Date();
                calendar = Calendar.getInstance(TimeZone.getDefault());
                DatePickerDialog datePickerDialog = new DatePickerDialog(
                        AddPetAktifitasActivity.this,
                        AddPetAktifitasActivity.this::onDateChanged,
                        calendar.get(Calendar.YEAR),
                        calendar.get(Calendar.MONTH),
                        calendar.get(Calendar.DAY_OF_MONTH)
                );

                datePickerDialog.show();
            }
        });

        //add pet aktifitas
        addPetAktifitasButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(checkForm()) addPetAktifitas();
            }
        });
    }

    @Override
    public void onDateChanged(DatePicker datePicker, int year, int month, int day) {
        calendar = Calendar.getInstance();
        calendar.set(Calendar.YEAR, year);
        calendar.set(Calendar.MONTH, month);
        calendar.set(Calendar.DAY_OF_MONTH, day);

        TimePickerDialog timePickerDialog = new TimePickerDialog(
                AddPetAktifitasActivity.this,
                AddPetAktifitasActivity.this,
                calendar.get(Calendar.HOUR_OF_DAY),
                calendar.get(Calendar.MINUTE),
                DateFormat.is24HourFormat(AddPetAktifitasActivity.this)
        );

        timePickerDialog.show();
    }

    @Override
    public void onTimeSet(TimePicker timePicker, int hour, int minute) {
        calendar.set(Calendar.HOUR_OF_DAY, hour);
        calendar.set(Calendar.MINUTE, minute);

        aktifitasdate = calendar.getTime();
        Objects.requireNonNull(dateField.getEditText()).setText(toDateString(aktifitasdate));
    }

    private Editable toDateString(Date date){
        @SuppressLint("SimpleDateFormat")
        SimpleDateFormat d = new SimpleDateFormat("d MMM yyyy - HH:mm");

        return Editable.Factory.getInstance().newEditable(d.format(date));
    }

    private boolean checkForm(){
        boolean typeBenar = true, dateBenar = true, noteBenar = true;

        if(Objects.requireNonNull(typeField.getEditText()).length() == 0){
            typeField.setError(getString(R.string.not_empty_text));
            typeBenar = false;
        }
        else {
            typeField.setErrorEnabled(false);
        }

        if(Objects.requireNonNull(dateField.getEditText()).length() == 0){
            dateField.setError(getString(R.string.not_empty_text));
            dateBenar = false;
        }
        else {
            dateField.setErrorEnabled(false);
        }

        if(Objects.requireNonNull(noteField.getEditText()).length() == 0){
            noteField.setError(getString(R.string.not_empty_text));
            noteBenar = false;
        }
        else{
            noteField.setErrorEnabled(false);
        }

        return typeBenar && dateBenar && noteBenar;
    }

    private void addPetAktifitas(){
        String typeData = Objects.requireNonNull(typeField.getEditText()).getText().toString();
        Date dateData = aktifitasdate;
        String noteData = Objects.requireNonNull(noteField.getEditText()).getText().toString();

        Map<String, Object> petData = new HashMap<>();
        petData.put("type", typeData);
        petData.put("date", dateData);
        petData.put("note", noteData);

        //add data to firestore
        db.collection("user_pets")
                .document(Objects.requireNonNull(mAuth.getCurrentUser()).getUid())
                .collection("activities")
                .document(petDataParcel.getUid())
                .collection("daily")
                .add(petData)
                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        Toast.makeText(AddPetAktifitasActivity.this, "Berhasil Tambah Aktifitas " + typeData, Toast.LENGTH_SHORT).show();
                        Intent detailPetIntent = new Intent(AddPetAktifitasActivity.this, DetailPetActivity.class);
                        detailPetIntent.putExtra(DetailPetActivity.PET_DATA, petDataParcel);

                        showNotif();
                        startActivity(detailPetIntent);
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
        AlertDialog.Builder builder = new AlertDialog.Builder(AddPetAktifitasActivity.this);
        builder.setTitle(title).setMessage(message);
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    private void showNotif(){
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, MainActivity.CHANNEL_ID)
                .setSmallIcon(R.drawable.cat_icon)
                .setContentTitle("Menambahkan Aktifitas")
                .setContentText("Berhasil Menambah Aktifitas :)")
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                // Set the intent that will fire when the user taps the notification
                .setAutoCancel(true);

        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(this);
        notificationManager.notify(1, builder.build());
    }
}