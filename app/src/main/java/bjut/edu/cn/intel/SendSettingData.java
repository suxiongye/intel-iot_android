package bjut.edu.cn.intel;

import android.content.SharedPreferences;
import android.util.Log;

import java.io.IOException;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by ray on 2016/9/14.
 */
public class SendSettingData {

    private static final  String TAG = SendSettingData.class.getSimpleName();

    private String name;
    private String phone;

    protected OkHttpClient client;

    private String url = "";

    public SendSettingData(String name, String phone) {
        client = new OkHttpClient();
        this.name = name;
        this.phone = phone;
    }

    public String sendSetting(String ip) {

        if(!(ip.equals("") || ip == null)) {

            url = "http://"+ip+":8080/intel-iot/setting?name="+this.name+"&phone="+this.phone;

            Log.i(TAG, "访问的 URl 为"+ url);

            Request request = new Request.Builder().url(url).build();

            try {
                Response response = client.newCall(request).execute();

                return response.body().string();

            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        return "";
    }
}