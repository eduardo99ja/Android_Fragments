package com.eduardo.dm_t2_2.ui.listarActivos;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class ListarActivosViewModel extends ViewModel {
    // TODO: Implement the ViewModel
    private MutableLiveData<String> mText;

    public ListarActivosViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("This is listar fragment");
    }

    public LiveData<String> getText() {
        return mText;
    }
}