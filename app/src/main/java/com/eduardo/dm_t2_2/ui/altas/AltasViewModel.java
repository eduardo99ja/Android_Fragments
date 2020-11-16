package com.eduardo.dm_t2_2.ui.altas;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class AltasViewModel extends ViewModel {

    private MutableLiveData<String> mText;

    public AltasViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("This is altas fragment");
    }

    public LiveData<String> getText() {
        return mText;
    }
}