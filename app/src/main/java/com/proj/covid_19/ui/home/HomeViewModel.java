package com.proj.covid_19.ui.home;

import android.widget.LinearLayout;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class HomeViewModel extends ViewModel {

    private MutableLiveData<String> mText;
    private MutableLiveData<LinearLayout> mCity_linear_layout;
    int counter;

    public HomeViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("");

        counter = 0;
    }

    public LiveData<String> getText() {
        return mText;
    }

    public MutableLiveData<LinearLayout> getCity_linear_layout() {
        return mCity_linear_layout;
    }

    public void setCity_linear_layout(MutableLiveData<LinearLayout> mCity_linear_layout) {
        this.mCity_linear_layout = mCity_linear_layout;
    }

}