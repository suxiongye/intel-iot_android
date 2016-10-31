package bjut.edu.cn.intel;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class SettingActivity extends AppCompatActivity {

    private static final  String TAG = SettingActivity.class.getSimpleName();

    private EditText name;

    private EditText phone;

    private EditText ip;

    private Button save;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);
        initView();

        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Context ctx = SettingActivity.this;
                SharedPreferences sp = ctx.getSharedPreferences("sp", MODE_PRIVATE);
                if(! ip.getText().toString().equals("")) {

                    SharedPreferences.Editor editor = sp.edit();
                    editor.putString("ip", ip.getText().toString());
                    editor.commit();
                }
                new SendSettingDataTask().execute(name.getText().toString(), phone.getText().toString());

                Toast toast = Toast.makeText(getApplicationContext(), "保存成功", Toast.LENGTH_SHORT);
                toast.show();
                startActivity(new Intent(SettingActivity.this, MainActivity.class));
            }
        });

    }

    private class SendSettingDataTask extends AsyncTask<String,String, String> {
        @Override
        protected String doInBackground(String... strings) {
            Context ctx = SettingActivity.this;
            SharedPreferences sp = ctx.getSharedPreferences("sp", MODE_PRIVATE);
            String ip_address = sp.getString("ip", "");
            SendSettingData client = new SendSettingData(strings[0], strings[1]);
            client.sendSetting(ip_address);
            Log.i(TAG, "ip地址"+ip_address);

            return "";
        }
    }


    private void initView() {
        name = (EditText) findViewById(R.id.name);
        phone = (EditText) findViewById(R.id.phone);
        ip = (EditText) findViewById(R.id.ip_address);
        save = (Button) findViewById(R.id.save);
    }


}
