package com.a15182.lucas.treasure_hunt_pdm;

import android.os.*;
import android.view.*;
import android.support.v7.app.*;
import android.widget.Toast;

import com.google.zxing.*;

import me.dm7.barcodescanner.zxing.*;

public class MainActivity extends AppCompatActivity {

    private ZXingScannerView scannerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void scanCode(View view) {
        this.scannerView = new ZXingScannerView(this);
        this.scannerView.setResultHandler(new ZXingScannerResultHandler());

        setContentView(this.scannerView);
        this.scannerView.startCamera();
    }

    @Override
    protected void onPause() {
        super.onPause();

        this.scannerView.stopCamera();
    }

    private class ZXingScannerResultHandler implements ZXingScannerView.ResultHandler {

        @Override
        public void handleResult(Result result) {
            String resultCode = result.getText();

            Toast.makeText(MainActivity.this, resultCode, Toast.LENGTH_SHORT).show();

            setContentView(R.layout.activity_main);
            scannerView.stopCamera();
        }
    }
}
