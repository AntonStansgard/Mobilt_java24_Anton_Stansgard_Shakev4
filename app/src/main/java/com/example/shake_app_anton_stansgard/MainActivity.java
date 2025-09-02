package com.example.shake_app_anton_stansgard;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class MainActivity extends AppCompatActivity {

    private SensorManager sensorManager;
    private Sensor accelerometer;
    private TextView xVal, yVal, zVal, txtThresholdLabel, txtThresholdValue;
    private Switch switchSensor;
    private SeekBar seekThreshold;
    private Button btnReset;
    private float threshold = 2.5f; // standard threshold i gForce
    private long lastShakeTime = 0;

    private final SensorEventListener sensorListener = new SensorEventListener() {
        @Override
        public void onSensorChanged(SensorEvent event) {
            float x = event.values[0];
            float y = event.values[1];
            float z = event.values[2];

            // Uppdaterar GUI-n
            xVal.setText("X: " + String.format("%.2f", x));
            yVal.setText("Y: " + String.format("%.2f", y));
            zVal.setText("Z: " + String.format("%.2f", z));

            // Räknar ut g-kraften
            float gX = x / SensorManager.GRAVITY_EARTH;
            float gY = y / SensorManager.GRAVITY_EARTH;
            float gZ = z / SensorManager.GRAVITY_EARTH;
            float gForce = (float) Math.sqrt(gX * gX + gY * gY + gZ * gZ);

            // Kollar threshold med cooldown
            if (gForce > threshold) {
                long now = System.currentTimeMillis();
                if (now - lastShakeTime > 1000) {
                    lastShakeTime = now;
                    Log.d("ShakeDetector", "Shake detected! gForce=" + gForce);
                    Toast.makeText(MainActivity.this, "Shake detected!", Toast.LENGTH_SHORT).show();
                }
            }
        }

        @Override public void onAccuracyChanged(Sensor sensor, int accuracy) {}
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        // Gör så att layout inte hamnar under systemfältet
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // initiering av sensorn
        sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        accelerometer = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);

        // Kopplar samman UI-elementen
        xVal = findViewById(R.id.xVal);
        yVal = findViewById(R.id.yVal);
        zVal = findViewById(R.id.zVal);
        switchSensor = findViewById(R.id.switchSensor);
        seekThreshold = findViewById(R.id.seekThreshold);
        btnReset = findViewById(R.id.btnReset);
        txtThresholdLabel = findViewById(R.id.txtThresholdLabel);
        txtThresholdValue = findViewById(R.id.txtThresholdValue);

        // Sätter startvärdet
        txtThresholdLabel.setText("Justera threshold:");
        txtThresholdValue.setText("Threshold: " + threshold + " g");

        // Switch som startar/stoppar sensorn
        switchSensor.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
                registerSensor();
            } else {
                unregisterSensor();
            }
        });

        //  SeekBar för att ändrar thresholdet
        seekThreshold.setProgress((int) (threshold * 10));
        seekThreshold.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            //SKriver ut värdet för aktivt threshold
            @Override public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                threshold = Math.max(0.5f, progress / 10f);
                txtThresholdValue.setText("Threshold: " + threshold + " g");
            }
            @Override public void onStartTrackingTouch(SeekBar seekBar) {}
            @Override public void onStopTrackingTouch(SeekBar seekBar) {}
        });

        // Funktion för att reseta med hjälp av knappen
        btnReset.setOnClickListener(v -> {
            threshold = 2.5f;
            seekThreshold.setProgress((int) (threshold * 10));
            txtThresholdValue.setText("Threshold: " + threshold + " g");
            Toast.makeText(this, "Threshold reset to 2.5 g", Toast.LENGTH_SHORT).show();
        });
    }

    //Metod för att starta sensorn
    private void registerSensor() {
        if (accelerometer != null) {
            sensorManager.registerListener(sensorListener, accelerometer, SensorManager.SENSOR_DELAY_NORMAL);
        }
    }
     //Metod för att stoppa sensorn
    private void unregisterSensor() {
        sensorManager.unregisterListener(sensorListener);
    }

    @Override protected void onPause() {
        super.onPause();
        unregisterSensor();
    }

    @Override protected void onResume() {
        super.onResume();
        if (switchSensor.isChecked()) registerSensor();
    }
}
