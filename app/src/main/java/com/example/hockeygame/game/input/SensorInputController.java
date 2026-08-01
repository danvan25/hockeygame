package com.example.hockeygame.game.input;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;

public class SensorInputController implements SensorEventListener {
    private static final float RAD_TO_DEG = (float) (180.0 / Math.PI);
    private static final float SMOOTHING_FACTOR = 0.85f;
    private final SensorManager sensorManager;
    private final Sensor rotationVectorSensor;
    private final Listener listener;
    private final float[] rotationMatrix = new float[9];
    private final float[] orientationValues = new float[3];
    private float centerPitch;
    private float centerRoll;
    private float smoothedPitch;
    private float smoothedRoll;
    private boolean calibrated;
    private boolean firstSensorValueReceived;

    public interface Listener {
        void onTiltChanged(float tiltX, float tiltY);
    }
    public SensorInputController(
            Context context,
            Listener listener
    ) {
        this.listener = listener;

        sensorManager =
                (SensorManager) context.getSystemService(
                        Context.SENSOR_SERVICE
                );

        rotationVectorSensor =
                sensorManager.getDefaultSensor(
                        Sensor.TYPE_ROTATION_VECTOR
                );
    }

    public boolean isSensorAvailable() {
        return rotationVectorSensor != null;
    }

    public void start() {
        if (rotationVectorSensor == null) {
            return;
        }

        sensorManager.registerListener(
                this,
                rotationVectorSensor,
                SensorManager.SENSOR_DELAY_GAME
        );
    }

    public void stop() {
        sensorManager.unregisterListener(this);
    }

    public void calibrate() {
        centerPitch = smoothedPitch;
        centerRoll = smoothedRoll;
        calibrated = true;
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        if (event.sensor.getType()
                != Sensor.TYPE_ROTATION_VECTOR) {
            return;
        }

        SensorManager.getRotationMatrixFromVector(
                rotationMatrix,
                event.values
        );

        SensorManager.getOrientation(
                rotationMatrix,
                orientationValues
        );

        float pitch = orientationValues[1] * RAD_TO_DEG;
        float roll = orientationValues[2] * RAD_TO_DEG;

        if (!firstSensorValueReceived) {
            smoothedPitch = pitch;
            smoothedRoll = roll;

            centerPitch = pitch;
            centerRoll = roll;

            calibrated = true;
            firstSensorValueReceived = true;

            if (listener != null) {
                listener.onTiltChanged(0f, 0f);
            }

            return;
        }

        smoothedPitch = SMOOTHING_FACTOR * smoothedPitch + (1f - SMOOTHING_FACTOR) * pitch;
        smoothedRoll = SMOOTHING_FACTOR * smoothedRoll + (1f - SMOOTHING_FACTOR) * roll;

        float relativePitch = smoothedPitch - centerPitch;
        float relativeRoll = smoothedRoll - centerRoll;

        if (listener != null) {
            listener.onTiltChanged(
                    relativeRoll,
                    relativePitch
            );
        }
    }

    @Override
    public void onAccuracyChanged(
            Sensor sensor,
            int accuracy
    ) {
        // Egyelőre nincs vele teendő.
    }


}