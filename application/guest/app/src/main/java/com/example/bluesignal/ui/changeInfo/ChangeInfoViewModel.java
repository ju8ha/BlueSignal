package com.example.bluesignal.ui.changeInfo;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class ChangeInfoViewModel extends ViewModel {

    private MutableLiveData<String> mText;

    public ChangeInfoViewModel(){
        mText = new MutableLiveData<>();
        mText.setValue("This is ChangeInfo fragment");
    }

    public LiveData<String> getText(){
        return mText;
    }
}
