package com.bakharaalief.peliharaanapp.UI.detail_pet;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;
import android.widget.Toolbar;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bakharaalief.peliharaanapp.Data.model.Aktifitas;
import com.bakharaalief.peliharaanapp.Data.model.Pet;
import com.bakharaalief.peliharaanapp.R;
import com.bakharaalief.peliharaanapp.UI.dashboard.DashboardActivity;
import com.bakharaalief.peliharaanapp.UI.dashboard.EditPetActivity;
import com.bakharaalief.peliharaanapp.UI.vaksin_pet.VaksinActivity;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.button.MaterialButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Objects;
import java.util.function.Consumer;

public class DetailPetActivity extends AppCompatActivity implements Toolbar.OnMenuItemClickListener {

    public static String PET_DATA = "PET_DATA";
    private FirebaseFirestore db;
    private FirebaseAuth mAuth;
    private Pet petData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_pet);

        //set view
        MaterialToolbar topAppbar = findViewById(R.id.topAppBar);
        MaterialButton addActivityButton = findViewById(R.id.add_pet_activity_button);
        RecyclerView petAktifitasItemRv = findViewById(R.id.pet_item_activity_rv);

        //set firebase
        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();

        //getData from intent
        petData = this.getIntent().getParcelableExtra(PET_DATA);

        //top Appbar
        topAppbar.setTitle(petData.getName());
        topAppbar.setNavigationOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(DetailPetActivity.this, DashboardActivity.class);
                startActivity(intent);
                finish();
            }
        });

        //add activity button
        addActivityButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(DetailPetActivity.this, AddPetAktifitasActivity.class);
                intent.putExtra(PET_DATA, petData);
                startActivity(intent);
            }
        });

        //menu top appbar clickable
        topAppbar.setOnMenuItemClickListener(this::onMenuItemClick);

        //getData pets
        ArrayList<Aktifitas> petAktifitasList = new ArrayList<Aktifitas>();
        db.collection("user_pets")
                .document(Objects.requireNonNull(mAuth.getCurrentUser()).getUid())
                .collection("activities")
                .document(petData.getUid())
                .collection("daily")
                .get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        queryDocumentSnapshots.forEach(new Consumer<QueryDocumentSnapshot>() {
                            @Override
                            public void accept(QueryDocumentSnapshot queryDocumentSnapshot) {
                                Aktifitas aktifitas = new Aktifitas(
                                        queryDocumentSnapshot.getId(),
                                        queryDocumentSnapshot.getTimestamp("date"),
                                        Objects.requireNonNull(queryDocumentSnapshot.get("type")).toString(),
                                        Objects.requireNonNull(queryDocumentSnapshot.get("note")).toString()
                                );
                                petAktifitasList.add(aktifitas);
                            }
                        });

                        PetAktifitasListAdapter aktifitasListPetAdapter = new PetAktifitasListAdapter(petAktifitasList, petData);
                        petAktifitasItemRv.setAdapter(aktifitasListPetAdapter);
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.d("wadaw", e.toString());
                    }
                });

        //all about pet item aktifitas rv
        PetAktifitasListAdapter aktifitasListPetAdapter = new PetAktifitasListAdapter(petAktifitasList, petData);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);

        petAktifitasItemRv.setLayoutManager(linearLayoutManager);
        petAktifitasItemRv.setAdapter(aktifitasListPetAdapter);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(this, DashboardActivity.class);
        startActivity(intent);
        finish();
    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public boolean onMenuItemClick(MenuItem menuItem) {
        switch (menuItem.getItemId()){
            case R.id.vaksin_menu:
                vaksin();
                break;
            case R.id.edit_menu:
                edit();
                break;
            case R.id.delete_menu:
                dialogBoxAction("Menghapus Peliharaan", "Anda Yakin Ingin Menghapus Peliharaan ?");
                break;
        }
        return true;
    }

    private void vaksin(){
        Intent intent = new Intent(this, VaksinActivity.class);
        intent.putExtra(PET_DATA, petData);
        startActivity(intent);
    }

    private void edit(){
        Intent intent = new Intent(this, EditPetActivity.class);
        intent.putExtra(PET_DATA, petData);
        startActivity(intent);
    }

    private void delete(){

        //colection all activity pet
        DocumentReference activityDoc = db.collection("user_pets")
                .document(Objects.requireNonNull(mAuth.getCurrentUser()).getUid())
                .collection("activities")
                .document(petData.getUid());

        //delete pet data
        db.collection("user_pets")
                .document(Objects.requireNonNull(mAuth.getCurrentUser()).getUid())
                .collection("pets")
                .document(petData.getUid())
                .delete()
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        db.batch().delete(activityDoc);

                        Toast.makeText(DetailPetActivity.this, "Delete " + petData.getName(), Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(DetailPetActivity.this, DashboardActivity.class);
                        startActivity(intent);
                        finish();
                    }
                });
    }

    private void dialogBoxAction(String title, String message){
        AlertDialog.Builder builder = new AlertDialog.Builder(DetailPetActivity.this);
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