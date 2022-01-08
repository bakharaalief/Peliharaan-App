package com.bakharaalief.peliharaanapp.UI.detail_pet;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.text.Editable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
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
        private final ImageView petAktifitasIcon;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            petAktifitasTime = itemView.findViewById(R.id.pet_aktifitas_time);
            petAktifitasDate = itemView.findViewById(R.id.pet_aktifitas_date);
            petItemAktifitas = itemView.findViewById(R.id.pet_aktifitas_item_card);
            petAktifitasIcon = itemView.findViewById(R.id.pet_aktifitas_icon);
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
            petAktifitasIcon.setImageResource(changeIcon(data.getType()));
            petItemAktifitas.setCardBackgroundColor(
                    petItemAktifitas.getContext().getColor(changeColor(data.getType()))
            );
        }

        private int changeIcon(String type){
            int iconId = 0;

            switch (type){
                case "Bath" :
                    iconId = R.drawable.bath_icon;
                    break;
                case "Eat" :
                    iconId = R.drawable.eat_icon;
                    break;
                default:
                    iconId = R.drawable.ic_baseline_pets_24;
                    break;
            }

            return iconId;
        }

        private int changeColor(String type){
            int colorId = 0;

            switch (type){
                case "Bath" :
                    colorId = R.color.blue;
                    break;
                case "Eat" :
                    colorId = R.color.green;
                    break;
                default:
                    colorId = R.color.purple;
                    break;
            }

            return colorId;
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
