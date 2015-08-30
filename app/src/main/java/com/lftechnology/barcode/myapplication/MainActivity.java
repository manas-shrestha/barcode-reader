package com.lftechnology.barcode.myapplication;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.SparseArray;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.gms.vision.Frame;
import com.google.android.gms.vision.barcode.Barcode;
import com.google.android.gms.vision.barcode.BarcodeDetector;


public class MainActivity extends AppCompatActivity {
    Button btnScan, btnGms, btnGmv;
    TextView textv;
    BarcodeDetector detector;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        btnScan = (Button) findViewById(R.id.btn_scan);
        btnGms = (Button) findViewById(R.id.btn_scan_gps);
        btnGmv = (Button) findViewById(R.id.btn_view_finder);
        textv = (TextView) findViewById(R.id.mytext);

        getSupportActionBar().hide();

        Intent i = getIntent();
        if (i != null) {
            textv.setText("Data Content :" + i.getStringExtra("KEY_INFO") + "\n Format :" + i.getStringExtra("KEY_FORMAT"));
        } else {
            textv.setText("No Data");
        }

        btnGmv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, BarcodeGmv.class));
            }
        });

        btnGms.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                useGoogleMobileVision();
            }
        });

        btnScan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startScan();
            }
        });
    }

    private void startScan() {
        startActivity(new Intent(this, SimpleScannerActivity.class));
    }

    private void useGoogleMobileVision() {
        Bitmap myBitmap = BitmapFactory.decodeResource(
                getApplicationContext().getResources(),
                R.drawable.qr);
        detector =
                new BarcodeDetector.Builder(this)
                        .setBarcodeFormats(Barcode.DATA_MATRIX | Barcode.QR_CODE)
                        .build();
        if (!detector.isOperational()) {
            textv.setText("Could not set up the detector!");
            return;
        }

        Frame frame = new Frame.Builder().setBitmap(myBitmap).build();
        SparseArray<Barcode> barcodes = detector.detect(frame);

        Barcode thisCode = barcodes.valueAt(0);
        textv.setText(thisCode.rawValue);
    }


}
