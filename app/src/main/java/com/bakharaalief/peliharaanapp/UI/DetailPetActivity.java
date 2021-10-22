package com.bakharaalief.peliharaanapp.UI;

import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import com.bakharaalief.peliharaanapp.Data.model.Pet;
import com.bakharaalief.peliharaanapp.R;
import com.google.android.material.appbar.MaterialToolbar;

public class DetailPetActivity extends AppCompatActivity {

    public static String PET_DATA = "PET_DATA";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_pet);

        //set view
        MaterialToolbar topAppbar = findViewById(R.id.topAppBar);

        //getData from intent
        Pet petData = this.getIntent().getParcelableExtra(PET_DATA);

        //top Appbar
        topAppbar.setTitle(petData.getName());
        topAppbar.setNavigationOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                DetailPetActivity.super.onBackPressed();
            }
        });
    }


}