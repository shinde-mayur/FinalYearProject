package com.project.pytopath;

import android.content.Context;
import android.content.Intent;
import android.view.View;

public class ClickHandler {

    private Context context = null;

    public void setContext(Context context) {
        this.context = context;
    }


    public void onCaptureImageClick(View view) {
        context.startActivity(new Intent(context, CameraActivity.class));
    }
}
