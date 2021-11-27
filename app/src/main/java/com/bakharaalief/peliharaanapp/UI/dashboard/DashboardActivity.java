package com.bakharaalief.peliharaanapp.UI.dashboard;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bakharaalief.peliharaanapp.Data.model.Pet;
import com.bakharaalief.peliharaanapp.R;
import com.bakharaalief.peliharaanapp.UI.auth.LoginActivity;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.button.MaterialButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Objects;
import java.util.function.Consumer;

public class DashboardActivity extends AppCompatActivity implements View.OnClickListener {

    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);

        //set view
        ImageView settingButton = findViewById(R.id.setting_button);
        TextView nameText = findViewById(R.id.user_name);
        MaterialButton addPetButton = findViewById(R.id.add_pet_button);
        RecyclerView petItemRv = findViewById(R.id.pet_item_rv);

        //set firebase
        mAuth = FirebaseAuth.getInstance();
        FirebaseFirestore db = FirebaseFirestore.getInstance();

        //getData user
        db.collection("users")
                .document(Objects.requireNonNull(mAuth.getCurrentUser()).getUid())
                .get()
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        nameText.setText(Objects.requireNonNull(Objects.requireNonNull(documentSnapshot).get("name")).toString());
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.d("wadaw", "get failed with ", e);
                    }
                });

        //getData pets
        ArrayList<Pet> petList = new ArrayList<Pet>();
        db.collection("user_pets")
                .document(Objects.requireNonNull(mAuth.getCurrentUser()).getUid())
                .collection("pets")
                .get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        queryDocumentSnapshots.forEach(new Consumer<QueryDocumentSnapshot>() {
                            @Override
                            public void accept(QueryDocumentSnapshot queryDocumentSnapshot) {
                                Pet pet = new Pet(
                                        queryDocumentSnapshot.getId(),
                                        queryDocumentSnapshot.getTimestamp("birth"),
                                        Objects.requireNonNull(queryDocumentSnapshot.get("name")).toString(),
                                        Objects.requireNonNull(queryDocumentSnapshot.get("type")).toString()
                                );
                                petList.add(pet);
                            }
                        });

                        PetListAdapter listPetAdapter = new PetListAdapter(petList);
                        petItemRv.setAdapter(listPetAdapter);
                    }
                });

        //all about pet item rv
        PetListAdapter listPetAdapter = new PetListAdapter(petList);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);

        petItemRv.setLayoutManager(linearLayoutManager);
        petItemRv.setAdapter(listPetAdapter);

        //view with click listener
        settingButton.setOnClickListener(this);
        addPetButton.setOnClickListener(this);
    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.setting_button :
                mAuth.signOut();
                Intent loginIntent = new Intent(this, LoginActivity.class);
                startActivity(loginIntent);
                finish();
                break;

            case R.id.add_pet_button:
                Intent addPetIntent = new Intent(this, AddPetActivity.class);
                startActivity(addPetIntent);
                break;
        }
    }
}