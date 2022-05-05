package com.example.runtimepermissionsapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    public static final int WRITE_PERMISSION_RQ = 123;
    private EditText mInput;
    private Button mWrite;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mInput = findViewById(R.id.et_input);
        mWrite = findViewById(R.id.btn_write);

        mWrite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String textToWrite = mInput.getText().toString();
                writeToFileIsNotEmpty(textToWrite);
            }
        });
    }

    private void writeToFileIsNotEmpty(String textToWrite) {
        if (TextUtils.isEmpty(textToWrite)) {
            Toast.makeText(this, "Text is empty", Toast.LENGTH_SHORT).show();
        } else {
            writeToFileWithPermissions(textToWrite);
        }
    }

    private void writeToFileWithPermissions(String textToWrite) {
        if (isWritePermissionGranted()) {
            writeToFile(textToWrite);
        } else {
            requestWritePermission();
        }
    }

    private void requestWritePermission() {
        if (ActivityCompat.shouldShowRequestPermissionRationale(this,Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
            // Show rationale
            new AlertDialog.Builder(this)
                    .setMessage("Без разрешения невозможно записать текст в файл")
                    .setPositiveButton("Понятно", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, WRITE_PERMISSION_RQ);
                        }
                    })
                    .show();
        } else {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, WRITE_PERMISSION_RQ);
        }
    }

    private void writeToFile(String textToWrite) {
        Toast.makeText(this,"Text is written to file", Toast.LENGTH_SHORT).show();
    }

    // Обработка ответа пользователя на запрос разрешения
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode != WRITE_PERMISSION_RQ) return;
        if (grantResults.length != 1) return;

        if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            String textToWrite = mInput.getText().toString();
            writeToFile(textToWrite);
        } else {
            new AlertDialog.Builder(this)
                    .setMessage("Вы можете дать разрешение в настройках приложения")
                    .setPositiveButton("Понятно", null)
                    .show();
        }

    }

    private boolean isWritePermissionGranted() {
        return ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED;
    }


}