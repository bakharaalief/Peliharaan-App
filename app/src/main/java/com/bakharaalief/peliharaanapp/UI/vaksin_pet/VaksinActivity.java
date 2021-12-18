package com.bakharaalief.peliharaanapp.UI.vaksin_pet;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bakharaalief.peliharaanapp.Data.model.Pet;
import com.bakharaalief.peliharaanapp.Data.model.Vaksin;
import com.bakharaalief.peliharaanapp.R;
import com.bakharaalief.peliharaanapp.UI.detail_pet.DetailPetActivity;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.button.MaterialButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Objects;
import java.util.function.Consumer;

public class VaksinActivity extends AppCompatActivity {

    private FirebaseFirestore db;
    private FirebaseAuth mAuth;
    private Pet petData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vaksin);

        //set view
        MaterialToolbar topAppbar = findViewById(R.id.topAppBar);
        MaterialButton addVaksinButton = findViewById(R.id.add_pet_vaksin_button);
        RecyclerView petVaksinItemRv = findViewById(R.id.pet_item_vaksin_rv);

        //set firebase
        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();

        //getData from intent
        petData = this.getIntent().getParcelableExtra(DetailPetActivity.PET_DATA);

        //top Appbar
        topAppbar.setTitle(petData.getName());
        topAppbar.setNavigationOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(VaksinActivity.this, DetailPetActivity.class);
                intent.putExtra(DetailPetActivity.PET_DATA, petData);
                startActivity(intent);
                finish();
            }
        });

        //getData vaksin
        ArrayList<Vaksin> petVaksinList = new ArrayList<Vaksin>();
        db.collection("user_pets")
                .document(Objects.requireNonNull(mAuth.getCurrentUser()).getUid())
                .collection("activities")
                .document(petData.getUid())
                .collection("vaksin")
                .get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        queryDocumentSnapshots.forEach(new Consumer<QueryDocumentSnapshot>() {
                            @Override
                            public void accept(QueryDocumentSnapshot queryDocumentSnapshot) {
                                Vaksin vaksin = new Vaksin(
                                        queryDocumentSnapshot.getId(),
                                        queryDocumentSnapshot.getTimestamp("date"),
                                        Objects.requireNonNull(queryDocumentSnapshot.get("name")).toString()
                                );
                                petVaksinList.add(vaksin);
                            }
                        });

                        VaksinListAdapter vaksinListAdapter = new VaksinListAdapter(petVaksinList, petData);
                        petVaksinItemRv.setAdapter(vaksinListAdapter);
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.d("wadaw", e.toString());
                    }
                });

        //all about pet item aktifitas rv
        VaksinListAdapter vaksinListAdapter = new VaksinListAdapter(petVaksinList, petData);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);

        petVaksinItemRv.setLayoutManager(linearLayoutManager);
        petVaksinItemRv.setAdapter(vaksinListAdapter);


        addVaksinButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(VaksinActivity.this, AddVaksinActivity.class);
                intent.putExtra(DetailPetActivity.PET_DATA, petData);
                startActivity(intent);
            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(VaksinActivity.this, DetailPetActivity.class);
        intent.putExtra(DetailPetActivity.PET_DATA, petData);
        startActivity(intent);
        finish();
    }
}