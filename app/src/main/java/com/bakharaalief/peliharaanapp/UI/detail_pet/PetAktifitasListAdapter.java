package com.bakharaalief.peliharaanapp.UI.detail_pet;

import android.annotation.SuppressLint;
import android.text.Editable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bakharaalief.peliharaanapp.Data.model.Aktifitas;
import com.bakharaalief.peliharaanapp.R;
import com.google.android.material.card.MaterialCardView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

class PetAktifitasListAdapter extends RecyclerView.Adapter<PetAktifitasListAdapter.ViewHolder> {

    private final ArrayList<Aktifitas> localDataSet;

    public PetAktifitasListAdapter(ArrayList<Aktifitas> dataSet) {
        localDataSet = dataSet;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private final TextView petAktifitasTime;
        private final TextView petAktifitasDate;
        private final MaterialCardView petItemAktifitas;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            petAktifitasTime = (TextView) itemView.findViewById(R.id.pet_aktifitas_time);
            petAktifitasDate = (TextView) itemView.findViewById(R.id.pet_aktifitas_date);
            petItemAktifitas = itemView.findViewById(R.id.pet_aktifitas_item_card);
        }

        public void setData(Aktifitas data){
            petAktifitasTime.setText(toTimeString(data.getAktifitasDate().toDate()));
            petAktifitasDate.setText(toDateString(data.getAktifitasDate().toDate()));
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
        holder.setData(localDataSet.get(position));
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
