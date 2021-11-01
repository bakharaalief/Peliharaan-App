package com.bakharaalief.peliharaanapp.UI;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;
import android.widget.Toolbar;

import androidx.appcompat.app.AppCompatActivity;

import com.bakharaalief.peliharaanapp.Data.model.Pet;
import com.bakharaalief.peliharaanapp.R;
import com.bakharaalief.peliharaanapp.UI.dashboard.DashboardActivity;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Objects;

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
                DetailPetActivity.super.onBackPressed();
            }
        });

        //menu top appbar clickable
        topAppbar.setOnMenuItemClickListener(this::onMenuItemClick);
    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public boolean onMenuItemClick(MenuItem menuItem) {
        switch (menuItem.getItemId()){
            case R.id.edit_menu:
                edit();
                break;
            case R.id.delete_menu:
                delete();
                break;
        }
        return true;
    }

    private void edit(){
        Intent intent = new Intent(this, EditPetActivity.class);
        intent.putExtra(PET_DATA, petData);
        startActivity(intent);
    }

    private void delete(){
        db.collection("user_pets")
                .document(Objects.requireNonNull(mAuth.getCurrentUser()).getUid())
                .collection("pets")
                .document(petData.getUid())
                .delete();

        Toast.makeText(this, "Delete " + petData.getName(), Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(this, DashboardActivity.class);
        startActivity(intent);
        finish();
    }
}