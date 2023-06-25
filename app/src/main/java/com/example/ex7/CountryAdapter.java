package com.example.ex7;

import android.content.Context;
import android.content.res.Resources;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.Observer;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import CountryParser.Country;

public class CountryAdapter extends RecyclerView.Adapter<CountryAdapter.ViewHolder> {
    private final Context context;
    private final CountriesModel viewModel;
    private ArrayList<Country> countryList;
    private FragA.FragAListener listener;
    private int selectedPosition = RecyclerView.NO_POSITION;
    private boolean isSelected;

    public CountryAdapter(Context context, FragmentActivity activity, CountriesModel viewModel, FragA.FragAListener listener) {
        this.viewModel = viewModel;
        this.context = context;
        this.listener = listener;

        viewModel.getCountryLiveData().observe(activity, new Observer<ArrayList<Country>>() {
            @Override
            public void onChanged(ArrayList<Country> countries) {
                setCountryList(countries);
            }
        });
    }

    @NonNull
    @Override
    public CountryAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        View countryView = inflater.inflate(R.layout.country_row, parent, false);
        return new ViewHolder(countryView);
    }

    @Override
    public void onBindViewHolder(@NonNull CountryAdapter.ViewHolder holder, int position) {
        Country country = countryList.get(position);
        this.selectedPosition = this.viewModel.getPosition();
        if (this.selectedPosition == position)
            holder.itemView.setBackgroundResource(R.color.white);
        else
            holder.itemView.setBackgroundResource(R.color.transparent);
        holder.setCountry(position, country);
    }

    @Override
    public int getItemCount() {
        return countryList.size();
    }

    public void setCountryList(ArrayList<Country> countryList){
        this.countryList = countryList;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private final TextView countryName;
        private final TextView population;
        private final ImageView countryFlag;
        private final View view;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            countryName = (TextView) itemView.findViewById(R.id.country);
            population = (TextView) itemView.findViewById(R.id.population);
            countryFlag = (ImageView) itemView.findViewById(R.id.flag);
            this.view = itemView;
        }

        public void setCountry(int position, Country country){
            this.countryFlag.setImageResource(searchCountryFlag(country.getFlag()));
            this.countryName.setText(country.getName());
            this.population.setText(country.getShorty());
            this.view.setOnLongClickListener(new View.OnLongClickListener() {
                private final int pos = position;
                @Override
                public boolean onLongClick(View view) {
                    viewModel.removeCountry(position);
                    if (position == selectedPosition)
                        viewModel.setItemSelected(RecyclerView.NO_POSITION);
                    else if (position < selectedPosition)
                        viewModel.setItemSelected(selectedPosition - 1);
                    notifyDataSetChanged();
                    return true;
                }
            });
            this.view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    viewModel.setItemSelected(position);
                    listener.OnClickCountry();
                    notifyDataSetChanged();
                }
            });
        }

        private int searchCountryFlag(String countryFlag) {
            Resources resources = context.getResources();
            return resources.getIdentifier(countryFlag, "drawable", context.getPackageName());
        }
    }
}
