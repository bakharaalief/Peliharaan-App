package com.bakharaalief.peliharaanapp.UI.vaksin_pet;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

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

import com.bakharaalief.peliharaanapp.Data.model.Pet;
import com.bakharaalief.peliharaanapp.Data.model.Vaksin;
import com.bakharaalief.peliharaanapp.R;
import com.bakharaalief.peliharaanapp.UI.dashboard.DashboardActivity;
import com.bakharaalief.peliharaanapp.UI.detail_aktifitas.DetailPetAktifitasActivity;
import com.bakharaalief.peliharaanapp.UI.detail_pet.DetailPetActivity;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.button.MaterialButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import org.w3c.dom.Text;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Objects;

public class DetailVaksinActivity extends AppCompatActivity implements Toolbar.OnMenuItemClickListener{

    public static String VAKSIN_DATA = "VAKSIN_DATA";
    private FirebaseFirestore db;
    private FirebaseAuth mAuth;
    private Vaksin vaksinData;
    private Pet petDataParcel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_vaksin);

        //set view
        MaterialToolbar topAppbar = findViewById(R.id.topAppBar);
        TextView namaVaksin = findViewById(R.id.vaksin_name);
        TextView dateVaksin = findViewById(R.id.vaksin_date);

        //getData from intent
        vaksinData = this.getIntent().getParcelableExtra(VAKSIN_DATA);
        petDataParcel = this.getIntent().getParcelableExtra(DetailPetActivity.PET_DATA);

        //set view
        namaVaksin.setText(vaksinData.getName());
        dateVaksin.setText(toDateString(vaksinData.getAktifitasDate().toDate()));

        //set firebase
        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();

        //top Appbar
        topAppbar.setNavigationOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(DetailVaksinActivity.this, VaksinActivity.class);
                intent.putExtra(DetailPetActivity.PET_DATA, petDataParcel);
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
        Intent intent = new Intent(DetailVaksinActivity.this, VaksinActivity.class);
        intent.putExtra(DetailPetActivity.PET_DATA, petDataParcel);
        startActivity(intent);
        finish();
    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public boolean onMenuItemClick(MenuItem menuItem) {
        switch (menuItem.getItemId()){
            case R.id.delete_menu:
                dialogBoxAction("Menghapus Vaksin", "Anda Yakin Ingin Menghapus Vaksin Ini ?");
                break;
        }
        return true;
    }

    private void delete(){
        db.collection("user_pets")
                .document(Objects.requireNonNull(mAuth.getCurrentUser()).getUid())
                .collection("activities")
                .document(petDataParcel.getUid())
                .collection("vaksin")
                .document(vaksinData.getUid())
                .delete();

        Toast.makeText(this, "Delete Vaksin Berhasil", Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(DetailVaksinActivity.this, VaksinActivity.class);
        intent.putExtra(DetailPetActivity.PET_DATA, petDataParcel);
        startActivity(intent);
        finish();
    }

    private Editable toDateString(Date date){
        @SuppressLint("SimpleDateFormat")
        SimpleDateFormat d = new SimpleDateFormat("d MMM yyyy");

        return Editable.Factory.getInstance().newEditable(d.format(date));
    }

    private void dialogBoxAction(String title, String message){
        AlertDialog.Builder builder = new AlertDialog.Builder(DetailVaksinActivity.this);
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
}