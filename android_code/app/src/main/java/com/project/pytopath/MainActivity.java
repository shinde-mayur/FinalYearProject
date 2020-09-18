package com.project.pytopath;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import com.project.pytopath.databinding.ActivityMainBinding;
import com.project.pytopath.models.Disease;
import com.project.pytopath.utils.ApiUrls;
import com.project.pytopath.utils.DBHelper;
import com.project.pytopath.utils.Utils;
import com.project.pytopath.viewmodel.ViewModel;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.concurrent.TimeUnit;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";
    private final int pick_image = 1;
    private final int capture_image = 2;
    ActivityMainBinding binding;
    ClickHandler clickHandler;
    String imagePath;
    ResponseBottomSheet responseBottomSheet;
    DBHelper dbHelper;

    public void uploadRequest(Context context, Bitmap bitmap) {

        OkHttpClient client = new OkHttpClient.Builder()
                .connectTimeout(20, TimeUnit.SECONDS)
                .readTimeout(20,TimeUnit.SECONDS)
                .build();
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inPreferredConfig = Bitmap.Config.RGB_565;
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream);
        byte[] byteArray = stream.toByteArray();
        RequestBody requestBody = new MultipartBody.Builder()
                .setType(MultipartBody.FORM)
                .addFormDataPart("image", "leaf-image.jpg", RequestBody.create(MediaType.parse("image/*jpg"), byteArray))
                .build();

        Request request = new Request.Builder()
                .url(ApiUrls.upload_image)
                .post(requestBody)
                .build();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                // Cancel the post on failure.

                call.cancel();
                Log.e(TAG, "onFailure: " + e);
                runOnUiThread(() -> Toast.makeText(context, "Something went wrong", Toast.LENGTH_SHORT).show());

            }

            @Override
            public void onResponse(Call call, final Response response) throws IOException {
                Log.d(TAG, "onResponse: " + response.toString());
                runOnUiThread(() -> {
                    try {
                        String str = response.body().string();
                        switch (str) {
                            case "Disease not found":
                                ViewModel.getError().set("रोग आढळला नाही");
                                ViewModel.getIsError().set(true);
                                break;
                            default:
                                Disease disease = dbHelper.getDiseaseDetails(str);
                                ViewModel.getDisease().set(
                                        disease
                                );
                        }
//                        if (response.body().string().equalsIgnoreCase(""))

//                            ViewModel.getDetectedDisease().set(response.body().string());
                        ViewModel.getIsProcessing().set(false);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                });
            }
        });

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main);
        clickHandler = new ClickHandler();
        clickHandler.setContext(this);
        binding.setClickHandler(clickHandler);
        dbHelper = new DBHelper(this);
    }

    public void onPickImageClick(View view) {
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType("image/*");
        startActivityForResult(intent, pick_image);
    }

    public void onCaptureImageClick(View view) {
        startActivityForResult(new Intent(this, CameraActivity.class), capture_image);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case pick_image:
                if (resultCode == RESULT_OK) {
                    uploadImage(data.getData());
                }
                break;
            case capture_image:
                if (resultCode == RESULT_OK) {
                    uploadImage(Uri.fromFile(new File(data.getStringExtra("filepath"))));
                }
                break;
        }

    }

    private void uploadImage(Uri imageUri) {
        try {
            ViewModel.getIsProcessing().set(true);

//            imagePath = Utils.getPath(this, imageUri);
            Bitmap bitmap = Utils.decodeUri(this, imageUri, 300);
            if (bitmap != null) {
                binding.imageView.setImageBitmap(bitmap);
                binding.imageView.setVisibility(View.VISIBLE);
                uploadRequest(this, bitmap);
                responseBottomSheet = new ResponseBottomSheet();
                responseBottomSheet.show(getSupportFragmentManager(), "BottomSheet");
            } else
                Toast.makeText(MainActivity.this,
                        "Error while decoding image.",
                        Toast.LENGTH_SHORT).show();
        } catch (FileNotFoundException e) {
            Log.e(TAG, "onActivityResult: " + e.toString());
            e.printStackTrace();
        }
    }
}
