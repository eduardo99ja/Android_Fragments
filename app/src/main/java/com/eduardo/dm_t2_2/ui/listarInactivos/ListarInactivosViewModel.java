package com.eduardo.dm_t2_2.ui.listarInactivos;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class ListarInactivosViewModel extends ViewModel {
    // TODO: Implement the ViewModel
    private MutableLiveData<String> mText;

    public ListarInactivosViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("This is listar fragment");
    }

    public LiveData<String> getText() {
        return mText;
    }
}