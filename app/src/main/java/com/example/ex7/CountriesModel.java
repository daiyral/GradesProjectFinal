package com.example.ex7;

import static CountryParser.CountryXMLParser.exportCountries;
import static CountryParser.CountryXMLParser.importCountries;
import static CountryParser.CountryXMLParser.parseCountries;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.util.Log;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

import CountryParser.Country;

public class CountriesModel extends AndroidViewModel {
    MutableLiveData<ArrayList<Country>> countryLiveData;
    MutableLiveData<Integer> itemSelectedLive;
    Integer itemSelected;
    ArrayList<Country> countryList;
    private final String ALL_COUNTRIES = "countries.xml";
    private final String SAVED_COUNTRIES = "countries_after_removed.xml";

    public CountriesModel(Application app) {
        super(app);
        this.countryLiveData = new MutableLiveData<>();
        this.itemSelectedLive = new MutableLiveData<>();
        initCountriesList(app);

        this.itemSelected = RecyclerView.NO_POSITION;
        this.itemSelectedLive.setValue(this.itemSelected);
    }

    public void initCountriesList(Application app){
        Context context =  app.getApplicationContext();
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        boolean savedCountries = sharedPreferences.getBoolean("removed_countries", false);
        if (savedCountries){
            this.countryList = parseCountries(ALL_COUNTRIES, context);
            Set<String> countries = sharedPreferences.getStringSet("savedCountries", new HashSet<String>());
            this.countryList.removeIf(country -> !countries.contains(country.getName()));
        } else{
            this.countryList = parseCountries(ALL_COUNTRIES,context);
        }

//        if (savedCountries){
//            this.countryList = importCountries(context, SAVED_COUNTRIES);
//        }
//        else
//            this.countryList = parseCountries(ALL_COUNTRIES, context);
        this.countryLiveData.setValue(this.countryList);
    }

    public void setSPCountries(Context context){
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        boolean savedCountries = sharedPreferences.getBoolean("removed_countries", false);
        if (savedCountries){
            ArrayList<String> countryNames = (ArrayList<String>) this.countryList.stream()
                    .map(object -> object.getName())
                    .collect(Collectors.toList());
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putStringSet("savedCountries", new HashSet(countryNames));
            editor.commit();
        }
//        if (savedCountries){
//            exportCountries(context,this.countryList);
//        }
    }

    public MutableLiveData<ArrayList<Country>> getCountryLiveData() {
        return this.countryLiveData;
    }

    public MutableLiveData<Integer> getItemSelected() {
        return this.itemSelectedLive;
    }

    public void setItemSelected(int position){
        this.itemSelected = position;
        this.itemSelectedLive.setValue(this.itemSelected);
    }

    public int getPosition(){
        return this.itemSelected;
    }

    public void removeCountry(int position){
        countryList.remove(position);
        countryLiveData.setValue(countryList);
    }

    public Country getCountry(int position){
        return this.countryList.get(position);
    }
}
