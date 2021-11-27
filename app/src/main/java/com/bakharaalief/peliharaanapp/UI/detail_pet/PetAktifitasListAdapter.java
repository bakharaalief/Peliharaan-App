package com.bakharaalief.peliharaanapp.UI.detail_pet;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.text.Editable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bakharaalief.peliharaanapp.Data.model.Aktifitas;
import com.bakharaalief.peliharaanapp.Data.model.Pet;
import com.bakharaalief.peliharaanapp.R;
import com.bakharaalief.peliharaanapp.UI.detail_aktifitas.DetailPetAktifitasActivity;
import com.google.android.material.card.MaterialCardView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

class PetAktifitasListAdapter extends RecyclerView.Adapter<PetAktifitasListAdapter.ViewHolder> {

    private final ArrayList<Aktifitas> localDataSet;
    private final Pet localPetData;

    public PetAktifitasListAdapter(ArrayList<Aktifitas> dataSet, Pet petData) {
        localDataSet = dataSet;
        localPetData = petData;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private final TextView petAktifitasTime;
        private final TextView petAktifitasDate;
        private final MaterialCardView petItemAktifitas;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            petAktifitasTime = itemView.findViewById(R.id.pet_aktifitas_time);
            petAktifitasDate = itemView.findViewById(R.id.pet_aktifitas_date);
            petItemAktifitas = itemView.findViewById(R.id.pet_aktifitas_item_card);
        }

        public void setData(Aktifitas data, Pet petData){
            petAktifitasTime.setText(toTimeString(data.getAktifitasDate().toDate()));
            petAktifitasDate.setText(toDateString(data.getAktifitasDate().toDate()));
            petItemAktifitas.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(view.getContext(), DetailPetAktifitasActivity.class);
                    intent.putExtra(DetailPetAktifitasActivity.AKTIFITAS_DATA, data);
                    intent.putExtra(DetailPetAktifitasActivity.PET_DATA, petData);
                    view.getContext().startActivity(intent);
                }
            });
        }
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View petAktifitasItem = LayoutInflater
                .from(parent.getContext())
                .inflate(R.layout.pet_aktifitas_item, parent, false);

        return new ViewHolder(petAktifitasItem);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.setData(localDataSet.get(position), localPetData);
    }

    @Override
    public int getItemCount() {
        return localDataSet.size();
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
