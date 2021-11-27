package com.bakharaalief.peliharaanapp.UI.detail_aktifitas;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.Toolbar;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.bakharaalief.peliharaanapp.Data.model.Aktifitas;
import com.bakharaalief.peliharaanapp.Data.model.Pet;
import com.bakharaalief.peliharaanapp.R;
import com.bakharaalief.peliharaanapp.UI.detail_pet.DetailPetActivity;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Objects;

public class DetailPetAktifitasActivity extends AppCompatActivity implements Toolbar.OnMenuItemClickListener {

    public static String AKTIFITAS_DATA = "AKTIFITAS_DATA";
    public static String PET_DATA = "PET_ID";

    private FirebaseFirestore db;
    private FirebaseAuth mAuth;

    private Aktifitas aktifitasData;
    private Pet petData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_pet_aktifitas);

        //setview
        MaterialToolbar topAppbar = findViewById(R.id.topAppBar);
        TextView aktifitasTime = findViewById(R.id.aktifitas_time);
        TextView aktifitasDate = findViewById(R.id.aktifitas_date);
        TextView aktifitasNote = findViewById(R.id.aktifitas_note);

        //set firebase
        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();

        //getData from intent
        aktifitasData = this.getIntent().getParcelableExtra(AKTIFITAS_DATA);
        petData = this.getIntent().getParcelableExtra(PET_DATA);

        aktifitasTime.setText(toTimeString(aktifitasData.getAktifitasDate().toDate()));
        aktifitasDate.setText(toDateString(aktifitasData.getAktifitasDate().toDate()));
        aktifitasNote.setText(aktifitasData.getNote());

        //top appbar
        topAppbar.setNavigationOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(DetailPetAktifitasActivity.this, DetailPetActivity.class);
                intent.putExtra(DetailPetActivity.PET_DATA, petData);
                startActivity(intent);
                finish();
            }
        });

        //menu top appbar clickable
        topAppbar.setOnMenuItemClickListener(this::onMenuItemClick);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(DetailPetAktifitasActivity.this, DetailPetActivity.class);
        intent.putExtra(DetailPetActivity.PET_DATA, petData);
        startActivity(intent);
        finish();
    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public boolean onMenuItemClick(MenuItem menuItem) {
        switch (menuItem.getItemId()){
            case R.id.edit_menu:
                edit();
                break;
            case R.id.delete_menu:
                dialogBoxAction("Menghapus Aktifitas", "Anda Yakin Ingin Menghapus Aktifitas ?");
                break;
        }
        return true;
    }

    private void edit(){
        Intent intent = new Intent(this, EditPetAktifitasActivity.class);
        intent.putExtra(DetailPetAktifitasActivity.AKTIFITAS_DATA, aktifitasData);
        intent.putExtra(DetailPetAktifitasActivity.PET_DATA, petData);
        startActivity(intent);
    }

    private void delete(){
        db.collection("user_pets")
                .document(Objects.requireNonNull(mAuth.getCurrentUser()).getUid())
                .collection("activities")
                .document(petData.getUid())
                .collection("daily")
                .document(aktifitasData.getUid())
                .delete();

        Toast.makeText(this, "Delete Aktifitas Berhaisl", Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(this, DetailPetActivity.class);
        intent.putExtra(DetailPetActivity.PET_DATA, petData);
        startActivity(intent);
        finish();
    }

    private void dialogBoxAction(String title, String message){
        AlertDialog.Builder builder = new AlertDialog.Builder(DetailPetAktifitasActivity.this);
        builder.setTitle(title).setMessage(message);
        builder.setPositiveButton("Iya", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                delete();
            }
        }).setNegativeButton("Tidak", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                finish();
            }
        });
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    private Editable toTimeString(Date date){
        @SuppressLint("SimpleDateFormat")
        SimpleDateFormat d = new SimpleDateFormat("HH:mm");

        return Editable.Factory.getInstance().newEditable(d.format(date));
    }

    private Editable toDateString(Date date){
        @SuppressLint("SimpleDateFormat")
        SimpleDateFormat d = new SimpleDateFormat("d MMM yyyy");

        return Editable.Factory.getInstance().newEditable(d.format(date));
    }
}