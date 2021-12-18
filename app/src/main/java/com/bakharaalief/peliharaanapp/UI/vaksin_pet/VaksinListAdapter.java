package com.bakharaalief.peliharaanapp.UI.vaksin_pet;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.text.Editable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bakharaalief.peliharaanapp.Data.model.Pet;
import com.bakharaalief.peliharaanapp.Data.model.Vaksin;
import com.bakharaalief.peliharaanapp.R;
import com.bakharaalief.peliharaanapp.UI.detail_aktifitas.DetailPetAktifitasActivity;
import com.google.android.material.card.MaterialCardView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

class VaksinListAdapter extends RecyclerView.Adapter<VaksinListAdapter.ViewHolder> {

    private final ArrayList<Vaksin> localDataSet;
    private final Pet localPetData;

    public VaksinListAdapter(ArrayList<Vaksin> dataSet, Pet petData) {
        localDataSet = dataSet;
        localPetData = petData;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private final TextView petVaksinName;
        private final TextView petVaksinTime;
        private final MaterialCardView petItemVaksin;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            petVaksinName = itemView.findViewById(R.id.vaksin_name);
            petVaksinTime = itemView.findViewById(R.id.vaksin_date);
            petItemVaksin = itemView.findViewById(R.id.vaksin_card);
        }

        public void setData(Vaksin data, Pet petData){
            petVaksinName.setText(data.getName());
            petVaksinTime.setText(toDateString(data.getAktifitasDate().toDate()));
            petItemVaksin.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(view.getContext(), DetailVaksinActivity.class);
                    intent.putExtra(DetailVaksinActivity.VAKSIN_DATA, data);
                    intent.putExtra(DetailPetAktifitasActivity.PET_DATA, petData);
                    view.getContext().startActivity(intent);
                }
            });
        }

        private Editable toDateString(Date date){
            @SuppressLint("SimpleDateFormat")
            SimpleDateFormat d = new SimpleDateFormat("d MMM yyyy");

            return Editable.Factory.getInstance().newEditable(d.format(date));
        }
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View petVaksinItem = LayoutInflater
                .from(parent.getContext())
                .inflate(R.layout.pet_vaksin_item, parent, false);

        return new VaksinListAdapter.ViewHolder(petVaksinItem);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.setData(localDataSet.get(position), localPetData);
    }

    @Override
    public int getItemCount() {
        return localDataSet.size();
    }
}
