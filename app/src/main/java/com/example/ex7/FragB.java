package com.example.ex7;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.RecyclerView;

import java.text.DecimalFormat;
import java.util.ArrayList;

import CountryParser.Country;

public class FragB extends Fragment {
    private CountriesModel viewModel;
    private Country position = null;


    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.country_detail_frag, container,false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        TextView countryDetails = view.findViewById(R.id.detail_text);
        viewModel = new ViewModelProvider(requireActivity()).get(CountriesModel.class);
        viewModel.getCountryLiveData().observe(getActivity(), new Observer<ArrayList<Country>>() {
            @Override
            public void onChanged(ArrayList<Country> countries) {

                if (position != null && !countries.contains(position))
                    countryDetails.setText("");
            }
        });
        viewModel.getItemSelected().observe(getActivity(), new Observer<Integer>() {
            @Override
            public void onChanged(Integer integer) {
                if (integer >= 0){
                    position = viewModel.getCountry(integer);
                    if (position != null)
                        countryDetails.setText(position.getDetails());
                    else
                        countryDetails.setText("");
                }
            }
        });
    }
}
