package matrix.raisewake;

import android.net.Uri;
import android.os.Bundle;
import android.app.Activity;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.widget.TextView;

import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.appindexing.Thing;
import com.google.android.gms.common.api.GoogleApiClient;

public class MainActivity extends Activity implements SensorEventListener {

    private SensorManager sensorManager = null;
    private Sensor gyroSensor = null;
    private TextView vX;
    private TextView vY;
    private TextView vZ;
    private TextView status;
    private static final float NS2S = 1.0f / 1000000000.0f;
    private float[] angle = new float[3];
    private float[] angle_cache = new float[50];
    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    private GoogleApiClient client;

    @SuppressWarnings("deprecation")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        vX = (TextView) findViewById(R.id.vx);
        vY = (TextView) findViewById(R.id.vy);
        vZ = (TextView) findViewById(R.id.vz);
        status = (TextView) findViewById(R.id.status);
        sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        gyroSensor = sensorManager
                .getDefaultSensor(Sensor.TYPE_ORIENTATION);

        client = new GoogleApiClient.Builder(this).addApi(AppIndex.API).build();
    }

    public MainActivity() {
// TODO Auto-generated constructor stub
        angle[0] = 0;
        angle[1] = 0;
        angle[2] = 0;
    }

    @Override
    protected void onPause() {
// TODO Auto-generated method stub
        super.onPause();
        sensorManager.unregisterListener(this); // 解除监听器注册
    }

    @Override
    protected void onResume() {
// TODO Auto-generated method stub
        super.onResume();
        sensorManager.registerListener(this, gyroSensor,
                SensorManager.SENSOR_DELAY_GAME); //为传感器注册监听器
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
// TODO Auto-generated method stub

    }

    @Override
    public void onSensorChanged(SensorEvent event) {
// TODO Auto-generated method stub
// if (event.accuracy == SensorManager.SENSOR_STATUS_UNRELIABLE)
// {
// return;
// }

// if (timestamp != 0) {
// final float dT = (event.timestamp - timestamp) * NS2S;
// angle[0] += event.values[0] * dT * 100;
// angle[1] += event.values[1] * dT * 100;
// angle[2] += event.values[2] * dT * 100;
// }
// timestamp = event.timestamp;
//
//
// vX.setText("X: " + Float.toString(angle[0]));
// vY.setText("Y: " + Float.toString(angle[1]));
// vZ.setText("Z: " + Float.toString(angle[2]));

// 方向传感器提供三个数据，分别为azimuth、pitch和roll。
//
// azimuth：方位，返回水平时磁北极和Y轴的夹角，范围为0°至360°。
// 0°=北，90°=东，180°=南，270°=西。
//
// pitch：x轴和水平面的夹角，范围为-180°至180°。
// 当z轴向y轴转动时，角度为正值。
//
// roll：y轴和水平面的夹角，由于历史原因，范围为-90°至90°。
// 当x轴向z轴移动时，角度为正值。

        vX.setText("Orientation X: " + event.values[0]);
        vY.setText("Orientation Y: " + event.values[1]);
        vZ.setText("Orientation Z: " + event.values[2]);

        float[] angle_tmp = new float[50];
        System.arraycopy(angle_cache, 1, angle_tmp, 0, angle_cache.length-1);
        angle_tmp[49] = event.values[1];
        System.arraycopy(angle_tmp, 0, angle_cache, 0, angle_tmp.length);


        float[] angle_sum = new float[5];
        for(int i=0; i<5; i++) {
            angle_sum[i] = 0;
            for (int j = 0; j < 10; j++) {
                angle_sum[i] = angle_sum[i] + angle_cache[i*10+j];
            }
        }

        if(angle_sum[0]>angle_sum[1] && angle_sum[1]>angle_sum[2] && angle_sum[2]>angle_sum[3] && angle_sum[3]>angle_sum[4] && angle_cache[0]-angle_cache[49]>40){
            status.setText("yes!!!");
        }



    }

    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    public Action getIndexApiAction() {
        Thing object = new Thing.Builder()
                .setName("SensorTest Page") // TODO: Define a title for the content shown.
                // TODO: Make sure this auto-generated URL is correct.
                .setUrl(Uri.parse("http://[ENTER-YOUR-URL-HERE]"))
                .build();
        return new Action.Builder(Action.TYPE_VIEW)
                .setObject(object)
                .setActionStatus(Action.STATUS_TYPE_COMPLETED)
                .build();
    }

    @Override
    public void onStart() {
        super.onStart();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client.connect();
        AppIndex.AppIndexApi.start(client, getIndexApiAction());
    }

    @Override
    public void onStop() {
        super.onStop();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        AppIndex.AppIndexApi.end(client, getIndexApiAction());
        client.disconnect();
    }
}
