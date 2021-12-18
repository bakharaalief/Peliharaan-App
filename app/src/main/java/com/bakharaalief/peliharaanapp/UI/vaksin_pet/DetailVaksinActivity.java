package com.bakharaalief.peliharaanapp.UI.vaksin_pet;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.text.Editable;
import android.widget.TextView;

import com.bakharaalief.peliharaanapp.Data.model.Pet;
import com.bakharaalief.peliharaanapp.Data.model.Vaksin;
import com.bakharaalief.peliharaanapp.R;
import com.bakharaalief.peliharaanapp.UI.detail_pet.DetailPetActivity;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.button.MaterialButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import org.w3c.dom.Text;

import java.text.SimpleDateFormat;
import java.util.Date;

public class DetailVaksinActivity extends AppCompatActivity {

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
        MaterialButton deleteButton = findViewById(R.id.delete_button);
        TextView namaVaksin = findViewById(R.id.vaksin_name);
        TextView dateVaksin = findViewById(R.id.vaksin_date);

        //getData from intent
        vaksinData = this.getIntent().getParcelableExtra(VAKSIN_DATA);
        petDataParcel = this.getIntent().getParcelableExtra(DetailPetActivity.PET_DATA);

        //set view
        namaVaksin.setText(vaksinData.getName());
        dateVaksin.setText(toDateString(vaksinData.getAktifitasDate().toDate()));

//        //set firebase
//        mAuth = FirebaseAuth.getInstance();
//        db = FirebaseFirestore.getInstance();
    }

    private Editable toDateString(Date date){
        @SuppressLint("SimpleDateFormat")
        SimpleDateFormat d = new SimpleDateFormat("d MMM yyyy");

        return Editable.Factory.getInstance().newEditable(d.format(date));
    }
}