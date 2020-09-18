package com.project.pytopath;

import android.app.Dialog;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;

import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.project.pytopath.databinding.ResponseBottomSheetBinding;
import com.project.pytopath.viewmodel.ViewModel;

public class ResponseBottomSheet extends BottomSheetDialogFragment {

    BottomSheetBehavior bottomSheetBehavior;
    ResponseBottomSheetBinding sheetBinding;

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        BottomSheetDialog bottomSheetDialog = (BottomSheetDialog) super.onCreateDialog(savedInstanceState);
        View view = View.inflate(getContext(), R.layout.response_bottom_sheet, null);
        sheetBinding = DataBindingUtil.bind(view);
        bottomSheetDialog.setContentView(view);
        sheetBinding.setClickHandler(new ClickHandler());
        sheetBinding.setViewModel(new ViewModel());
        bottomSheetBehavior = BottomSheetBehavior.from((View) (view.getParent()));
        bottomSheetBehavior.setPeekHeight(BottomSheetBehavior.PEEK_HEIGHT_AUTO);
        return bottomSheetDialog;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        sheetBinding.cancelBtn.setOnClickListener(v -> dismiss());
    }
}
