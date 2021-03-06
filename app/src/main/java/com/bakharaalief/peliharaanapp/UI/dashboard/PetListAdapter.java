package com.bakharaalief.peliharaanapp.UI.dashboard;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bakharaalief.peliharaanapp.Data.model.Pet;
import com.bakharaalief.peliharaanapp.R;
import com.bakharaalief.peliharaanapp.UI.detail_pet.DetailPetActivity;
import com.google.android.material.card.MaterialCardView;

import java.util.ArrayList;

class PetListAdapter extends RecyclerView.Adapter<PetListAdapter.ViewHolder> {

    private final ArrayList<Pet> localDataSet;

    public PetListAdapter(ArrayList<Pet> dataSet) {
        localDataSet = dataSet;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        private final TextView petItemName;
        private final MaterialCardView petItem;
        private final ImageView petItemIcon;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            petItemName = itemView.findViewById(R.id.pet_item_name);
            petItemIcon = itemView.findViewById(R.id.pet_item_icon);
            petItem = itemView.findViewById(R.id.pet_item_card);
        }

        public void setData(Pet data){
            petItemName.setText(data.getName());
            petItem.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(view.getContext(), DetailPetActivity.class);
                    intent.putExtra(DetailPetActivity.PET_DATA, data);
                    view.getContext().startActivity(intent);
                }
            });
            petItemIcon.setImageResource(changeIcon(data.getType()));
        }

        private int changeIcon(String type){
            int iconId = 0;

            switch (type){
                case "Cat" :
                    iconId = R.drawable.cat_icon;
                    break;
                case "Dog" :
                    iconId = R.drawable.dog_icon;
                    break;
                case "Fish" :
                    iconId = R.drawable.fish_icon;
                    break;
                case "Bird" :
                    iconId = R.drawable.bird_icon;
                    break;
                default:
                    iconId = R.drawable.ic_baseline_pets_24;
                    break;
            }

            return iconId;
        }
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View petItem = LayoutInflater
                .from(parent.getContext())
                .inflate(R.layout.pet_item, parent, false);

        return new ViewHolder(petItem);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.setData(localDataSet.get(position));
    }

    @Override
    public int getItemCount() { return localDataSet.size(); }
}
