package com.a15182.lucas.treasure_hunt_pdm;

import android.*;

import java.text.*;
import java.util.*;

import android.os.*;
import android.app.*;
import android.view.*;
import android.widget.*;
import android.content.*;
import android.provider.*;
import android.location.*;
import android.content.pm.*;
import android.support.v4.app.*;
import android.support.v7.app.*;
import android.support.annotation.*;

import me.dm7.barcodescanner.zxing.*;

import com.google.zxing.*;
import com.a15182.lucas.treasure_hunt_pdm.utils.*;

public class MainActivity extends AppCompatActivity {

    private final String SERVER_URL = "http://www.slmm.com.br/cotuca/restCotuca17.php";

    private final int GPS_PERMISSION = 666;
    private final int CAMERA_PERMISSION = 667;
    private final int INTERNET_PERMISSION = 668;

    private String qrCodeRead = "8";
    private ZXingScannerView scannerView;

    private double latitude;
    private double longitude;
    private LocationManager locationManager;
    private LocationListener locationListener;

    private TextView txtHint;
    private ImageView imgImage;

    private GetJson downloader;
    private ProgressDialog loadMessage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        this.locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);
        this.locationListener = new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {
                latitude = location.getLatitude();
                longitude = location.getLongitude();
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

        this.txtHint = (TextView) findViewById(R.id.txtHint);
        this.imgImage = (ImageView) findViewById(R.id.imgImage);

        this.downloader = new GetJson();
    }

    public void onClickScan(View view) {
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

    public void onClickGetData(View view) {
        if (ActivityCompat.checkSelfPermission(MainActivity.this, Manifest.permission.INTERNET) !=
                PackageManager.PERMISSION_GRANTED)
            ActivityCompat.requestPermissions(MainActivity.this,
                    new String[]{Manifest.permission.INTERNET}, INTERNET_PERMISSION);
        else
            startDownload();
    }

    public void startDownload() {
        this.downloader.execute();
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

            case INTERNET_PERMISSION:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED)
                    startDownload();
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

    private class GetJson extends AsyncTask<Void, Void, Hint> {
        @Override
        protected void onPreExecute() {
            loadMessage = ProgressDialog.show(MainActivity.this, "Por favor aguarde...",
                    "Recuperando informações do servidor...");
        }

        @Override
        protected Hint doInBackground(Void... params) {
            HashMap<String, String> payload = new HashMap<>();

            payload.put("lat", (new Double(latitude)).toString());
            payload.put("lon", (new Double(longitude)).toString());
            payload.put("id", qrCodeRead);
            payload.put("action", "get");
            payload.put("ra", "15182");

            DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            dateFormat.setTimeZone(TimeZone.getTimeZone("GMT-3:00"));
            Date date = new Date();

            payload.put("dt", dateFormat.format(date).toString());

            return (new Utils()).getInfo(SERVER_URL, payload);
        }

        @Override
        protected void onPostExecute(Hint hint) {
            txtHint.setText(R.string.txtHint + hint.getDica().split("Ok")[0]);
            imgImage.setImageBitmap(hint.getImage());

            loadMessage.dismiss();
        }
    }
}
