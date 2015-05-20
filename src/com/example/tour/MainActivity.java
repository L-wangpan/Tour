package com.example.tour;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONObject;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends Activity {

	TextView tv;
	EditText input;
	Button b;
	Handler handler;
	View tourMessage;
	TourInfo tio;
	public static final String BAIDU_URI = "http://api.map.baidu.com/telematics/v3/travel_city?location=";

	public static final String BAIDU_LAST = "&day=3&output=json&ak=WAKoAN3TNbO5K37xIHLHexWz";

	@SuppressLint("HandlerLeak")
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_main);

		iniview();
		handler = new Handler() {
			
			public void handleMessage(Message msg) {
				if (msg.what == 0x123) {
					TourInfo t = (TourInfo)msg.obj;
					tv.setText(t.getcityname()+t.getpath());
				}
			}

		};

	}
public void iniview(){
	tv = (TextView) findViewById(R.id.show);
	input = (EditText) findViewById(R.id.input);
}
	public void onclick(View v) {
		switch (v.getId()) {
		case R.id.button1:
			new MyTask().execute(BAIDU_URI + input.getText().toString()
					+ BAIDU_LAST);
			City_Message(v,tio);
			break;

		default:
			break;
		}
	}

	public class MyTask extends
			AsyncTask<String, Void, List<Map<String, String>>> {

		protected List<Map<String, String>> doInBackground(String... params) {
			List<Map<String, String>> list = new ArrayList<Map<String, String>>();
			try {
				// 获取网络JSON格式数据
				HttpClient httpClient = new DefaultHttpClient();
				HttpPost httpPost = new HttpPost(params[0]);
				HttpResponse httpResponse = httpClient.execute(httpPost);

				if (httpResponse.getStatusLine().getStatusCode() == 200) {

					String jsonString = EntityUtils.toString(
							httpResponse.getEntity(), "utf-8");
					JSONObject jsonObject = new JSONObject(jsonString);
					JSONObject JSO = jsonObject.getJSONObject("result");
					JSONObject JSOposion = JSO.getJSONObject("location");
					JSONArray JSOit1 = JSO.getJSONArray("itineraries");
					JSONObject JSOit = JSOit1.getJSONObject(0);
					
					String[][] s = new String[8][3];
					String all ="";
					JSONArray JSOinit2 = JSOit.getJSONArray("itineraries");
					//JSONArray JSOinit = JSOinit2.getJSONObject(0).getJSONArray(
					//		"path");
					for (int i = 0; i < JSOinit2.length(); i++) {
						JSONObject job = JSOinit2.getJSONObject(i);
						s[i][0] = job.getString("description");
						s[i][1] = job.getString("dinning");
						s[i][2] = job.getString("accommodation");
						all += s[i][0] + '\n' + s[i][1] + '\n' + s[i][2] + '\n';
					}
					//for
					Map<String, String> map = new HashMap<String, String>();
					map.put("place", JSO.getString("cityname"));
					map.put("abstract", JSO.getString("abstract"));
					Message msg = new Message();
					msg.what = 0x123;
					/*msg.obj = JSO.getString("cityname") + '\n'
							+ JSO.getString("abstract").toString() + '\n'
							+ JSO.getString("description") + '\n'
							+ JSOit.getString("name") + '\n'
							+ JSOit.getString("description") + '\n' + all;*/
					tio = new TourInfo(JSO.getString("cityname"), JSOposion.getDouble("lng"), JSOposion.getDouble("lat"),
							JSO.getString("abstract"), JSO.getString("description"), all,JSOit.getString("name"));
					msg.obj = tio; 
					handler.sendMessage(msg);
					list.add(map);
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
			return list;
		}

		protected void onPostExecute(List<Map<String, String>> result) {
			super.onPostExecute(result);

		}
	}
	
	public void City_Message(View arg,TourInfo t){
		tourMessage = this.getLayoutInflater().inflate(R.layout.result, null);
		int mScreenWidth = getWindowManager().getDefaultDisplay().getWidth();
		int mScreenHeight = getWindowManager().getDefaultDisplay().getHeight();
		final PopupWindow popup = new PopupWindow(tourMessage, mScreenWidth,
				mScreenHeight, true);
		popup.setFocusable(true);
		// 以下拉方式显示。
		// popup.showAsDropDown(v);
		// 将PopupWindow显示在指定位置
	
		popup.showAtLocation(arg, Gravity.CENTER, 0, 0);
		/*tourMessage.findViewById(R.id.ret).setOnClickListener(
				new View.OnClickListener() {
					public void onClick(View v) {
						// 关闭PopupWindow
						popup.dismiss(); // ①
					}
				});*/
	}
}
