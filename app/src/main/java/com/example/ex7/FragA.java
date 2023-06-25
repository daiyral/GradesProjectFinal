package com.example.ex7;

//import android.app.Fragment;

import android.content.Context;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.io.IOException;
import java.util.ArrayList;

import CountryParser.Country;


public class FragA extends Fragment{
	private FragAListener listener;
	private CountriesModel viewModel;

	@Override
	public void onAttach(@NonNull Context context) {
		try{
			this.listener = (FragAListener)context;
		}catch(ClassCastException e){
			throw new ClassCastException("the class " +
					context.getClass().getName() +
					" must implements the interface 'FragAListener'");
		}
		super.onAttach(context);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		return inflater.inflate(R.layout.country_frag, container,false);
	}

	@Override
	public void onViewCreated(View view,@Nullable Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);
		RecyclerView rvCountries = (RecyclerView) view.findViewById(R.id.countryRec);
		viewModel = new ViewModelProvider(requireActivity()).get(CountriesModel.class);
		CountryAdapter adapter = new CountryAdapter(view.getContext(), getActivity(), viewModel, this.listener);
		rvCountries.setAdapter(adapter);
		rvCountries.setLayoutManager(new LinearLayoutManager(view.getContext()));
	}

	public interface FragAListener{
		void OnClickCountry();
	}

}
