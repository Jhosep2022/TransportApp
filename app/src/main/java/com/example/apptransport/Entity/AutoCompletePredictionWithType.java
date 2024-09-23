package com.example.apptransport.Entity;

import com.apreciasoft.mobile.asremis.Util.Globales;
import com.google.android.libraries.places.api.model.AutocompletePrediction;

public class AutoCompletePredictionWithType {
    public AutocompletePrediction autocompletePrediction;
    public Globales.AutocompleteType autocompleteType;

    public AutoCompletePredictionWithType(){}

    public AutoCompletePredictionWithType(AutocompletePrediction _autocompletePrediction, Globales.AutocompleteType _autocompleteType)
    {
        autocompletePrediction=_autocompletePrediction;
        autocompleteType = _autocompleteType;
    }
}
