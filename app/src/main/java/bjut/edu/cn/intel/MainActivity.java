package bjut.edu.cn.intel;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;


public class MainActivity extends AppCompatActivity {

    private static final String TAG = MainActivity.class.getSimpleName();

    private TextView temp_data;

    private TextView pir_data;

    private TextView press_data;

    private TextView flame_data;

    private ImageButton imageButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();

        new Thread(new GetDataTaskThread()).start();

        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this, SettingActivity.class));
            }
        });

    }


    private void initView() {
        temp_data = (TextView) findViewById(R.id.temp_data);
        pir_data = (TextView) findViewById(R.id.pir_data);
        press_data = (TextView) findViewById(R.id.press_data);
        flame_data = (TextView) findViewById(R.id.flame_data);
        imageButton = (ImageButton) findViewById(R.id.setting);
    }


    private class GetDataTaskThread implements Runnable {

        @Override
        public void run() {

            Context ctx = MainActivity.this;
            SharedPreferences sp = ctx.getSharedPreferences("sp", MODE_PRIVATE);
            String ip_address = sp.getString("ip", "");
            while (true) {
                (new GetDataTesk()).execute(ip_address);

                try {
                    Thread.sleep(3000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }


    private class GetDataTesk extends AsyncTask<String, Void, String> {


        @Override
        protected String doInBackground(String... strings) {

            String ip_address = strings[0];

            String result = "";

            if (!ip_address.equals("")) {
                GetRemoteData grd = new GetRemoteData();
                result = grd.getApiData(ip_address);
            }


            return result;
        }

        @Override
        protected void onPostExecute(String s) {

            Log.i(TAG, "请求结果为" + s);
            if (!s.equals("")) {
                JSONObject data = JSON.parseObject(s);

                JSONObject pir = data.getJSONObject("pir");
                JSONObject temp = data.getJSONObject("temp");
                JSONObject press = data.getJSONObject("press");
                JSONObject flame = data.getJSONObject("flame");

                if (temp != null) {
                    temp_data.setText(temp.getString("value"));
                }
                if (pir != null) {
                    Config.PIR_BATHROOM = pir.getLong("createTime");
                    if (!timeNear(Config.PIR_BATHROOM, Config.NearTime)) {
                        pir_data.setText("近期无感应");
                    } else {
                        pir_data.setText(pir.getString("value"));
                    }
                }
                if (press != null) {
                    Config.PRESS = press.getLong("createTime");
                    if (!timeNear(Config.PRESS, Config.NearTime)) {
                        press_data.setText("安全状态");
                    } else {
                        press_data.setText(press.getString("value"));
                    }
                }
                if (flame != null) {
                    if (flame.getString("value").equals("Fire")) {
                        Config.LAST_FLAME = flame.getLong("createTime");
                    }
                    if (timeNear(Config.LAST_FLAME, 60)) {
                        flame_data.setText("家中可能有火灾！");
                    } else {
                        flame_data.setText(flame.getString("value"));
                    }
                }
            }

        }

        /**
         * 返回时间是否在最近sec秒内
         *
         * @return
         */
        private boolean timeNear(long createTime, int sec) {
            try {
                if (System.currentTimeMillis() - createTime < 1000 * sec) {
                    return true;
                }
            } catch (Exception e) {

            }
            return false;
        }
    }


}
