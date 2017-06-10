package com.a15182.lucas.treasure_hunt_pdm;

import android.*;
import android.os.*;
import android.view.*;
import android.widget.*;
import android.content.*;
import android.provider.*;
import android.location.*;
import android.content.pm.*;
import android.support.v4.app.*;
import android.support.v7.app.*;
import android.support.annotation.*;

import com.google.zxing.*;

import me.dm7.barcodescanner.zxing.*;

public class MainActivity extends AppCompatActivity {

    private final int GPS_PERMISSION = 666;
    private final int CAMERA_PERMISSION = 667;

    private String qrCodeRead;
    private ZXingScannerView scannerView;

    private TextView txtCoords;

    private LocationManager locationManager;
    private LocationListener locationListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        this.txtCoords = (TextView) findViewById(R.id.txtCoords);

        this.locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);
        this.locationListener = new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {
                txtCoords.append("\n" + location.getLatitude() + ", " + location.getLongitude());
            }

            @Override
            public void onStatusChanged(String provider, int status, Bundle extras) {
            }

            @Override
            public void onProviderEnabled(String provider) {
            }

            @Override
            public void onProviderDisabled(String provider) {
                startActivity(new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS));
            }
        };
    }

    public void onClickScan(View v) {
        if (ActivityCompat.checkSelfPermission(MainActivity.this, Manifest.permission.CAMERA) !=
                PackageManager.PERMISSION_GRANTED)
            ActivityCompat.requestPermissions(MainActivity.this,
                    new String[]{Manifest.permission.CAMERA}, CAMERA_PERMISSION);
        else
            startScanner();
    }

    public void startScanner() {
        this.scannerView = new ZXingScannerView(MainActivity.this);
        this.scannerView.setResultHandler(new ZXingScannerResultHandler());

        setContentView(this.scannerView);
        this.scannerView.startCamera();
    }

    public void onClickLocation(View view) {
        if (ActivityCompat.checkSelfPermission(MainActivity.this,
                Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&

                ActivityCompat.checkSelfPermission(MainActivity.this,
                        Manifest.permission.ACCESS_COARSE_LOCATION) !=
                        PackageManager.PERMISSION_GRANTED)

            ActivityCompat.requestPermissions(MainActivity.this, new String[]{
                    Manifest.permission.ACCESS_COARSE_LOCATION,
                    Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.INTERNET}, GPS_PERMISSION);
        else
            startGPS();
    }

    private void startGPS() {
        if (ActivityCompat.checkSelfPermission(MainActivity.this,
                Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&

                ActivityCompat.checkSelfPermission(MainActivity.this,
                        Manifest.permission.ACCESS_COARSE_LOCATION) !=
                        PackageManager.PERMISSION_GRANTED)
            return;

        this.locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 10000, 2, this.locationListener);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        switch (requestCode) {
            case CAMERA_PERMISSION:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED)
                    startScanner();
                break;

            case GPS_PERMISSION:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED)
                    startGPS();
                break;
        }
    }

    @Override
    protected void onPause() {
        super.onPause();

        if (this.scannerView != null)
            this.scannerView.stopCamera();
    }

    @Override
    protected void onResume() {
        super.onResume();

        if (this.scannerView != null)
            this.scannerView.startCamera();
    }

    private class ZXingScannerResultHandler implements ZXingScannerView.ResultHandler {

        @Override
        public void handleResult(Result result) {
            qrCodeRead = result.getText();

            Toast.makeText(MainActivity.this, qrCodeRead, Toast.LENGTH_SHORT).show();

            setContentView(R.layout.activity_main);
            scannerView.stopCamera();
        }
    }
}
