package com.example.apptransport.viewmodels;

import com.apreciasoft.mobile.asremis.Entity.paramEntity;

import java.util.List;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class NewDriverActivityViewModel extends ViewModel {

    private MutableLiveData<List<paramEntity>> listParamsMutableData;

    public NewDriverActivityViewModel()
    {
        super();
        listParamsMutableData =new MutableLiveData<>();
    }

    @Override
    protected void onCleared() {
        super.onCleared();
    }

    public MutableLiveData<List<paramEntity>> listParams(){
        if(listParamsMutableData ==null)
        {
            listParamsMutableData =new MutableLiveData<>();
        }
        return listParamsMutableData;
    }

    public void postParams(List<paramEntity> params)
    {
        listParamsMutableData.postValue(params);
    }

}
