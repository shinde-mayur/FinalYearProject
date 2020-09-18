package com.project.pytopath.viewmodel;

import androidx.databinding.ObservableBoolean;
import androidx.databinding.ObservableField;

import com.project.pytopath.models.Disease;

public class ViewModel {

    private static ObservableBoolean isProcessing = new ObservableBoolean(true);
    private static ObservableBoolean isError = new ObservableBoolean(false);
    private static ObservableField<String> detectedDisease = new ObservableField<>("");
    private static ObservableField<String> error = new ObservableField<>("");
    private static ObservableField<Disease> disease = new ObservableField<>();

    public static ObservableField<Disease> getDisease() {
        return disease;
    }

    public static ObservableBoolean getIsError() {
        return isError;
    }

    public static ObservableField<String> getError() {
        return error;
    }

    public static ObservableBoolean getIsProcessing() {
        return isProcessing;
    }

    public static ObservableField<String> getDetectedDisease() {
        return detectedDisease;
    }
}
