package bjut.edu.cn.intel;

import android.util.Log;

import java.io.IOException;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by ray on 2016/9/14.
 */
public class GetRemoteData {

    private static final  String TAG = GetRemoteData.class.getSimpleName();

    protected OkHttpClient client;

    protected String apiUrl;


    public GetRemoteData()
    {
        client = new OkHttpClient();

    }


    public String getApiData(String ip_address)
    {
        apiUrl = "http://"+ip_address+":8080/intel-iot/allInfo";

        Log.i(TAG, "请求 URL 为"+ apiUrl);
        Request request = new Request.Builder().url(apiUrl).build();

        try {
            Response response = client.newCall(request).execute();
            return response.body().string();

        } catch (IOException e) {
            e.printStackTrace();
        }
        return "";
    }

}
