package blink.vietnam.weatherapp2.Activitis;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import blink.vietnam.weatherapp2.Adapters.HourlyAdapter;
import blink.vietnam.weatherapp2.Model.Hourly;
import blink.vietnam.weatherapp2.Model.Weather;
import blink.vietnam.weatherapp2.R;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    static final String API_KEY="d77cb61a5967bb592700365acc89844f";
    EditText editTextSearch;
    Button buttonSearch,buttonNext;
    TextView textViewCity,textViewTemp,textViewStatus,textViewDay,textViewCloud,textViewWind,textViewHumid;
    ImageView imageIcon;
    TextView txtTempFeel,txtVisibility,txtPressure;
    String city="";
    List<Hourly> hourlyList;
    HourlyAdapter adapterHourly;
    ListView listHourly;
    InputMethodManager hide;//ẩn bàn phím
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mapping();
        buttonSearch.setOnClickListener(this);
        hide = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);//ẩn bàn phím
        buttonNext.setOnClickListener(this);
            getJsonWeather(city);
        hourlyList = new ArrayList<>();
        adapterHourly=new HourlyAdapter(MainActivity.this,R.layout.activity_hourly,hourlyList);
        listHourly.setAdapter(adapterHourly);
            getJsonHourly(city);

    }
    public void getJsonWeather(String city){
        String requestCity = city.equals("") ? "Hanoi" : city;
        final String url="https://api.openweathermap.org/data/2.5/weather?q="+requestCity+"&appid="+API_KEY+"&lang=vi&units=metric";
        RequestQueue requestQueue= Volley.newRequestQueue(this);
        JsonObjectRequest jsonObjectRequest=new JsonObjectRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            JSONArray weatherArray=response.getJSONArray("weather");
                            JSONObject weatherObject=weatherArray.getJSONObject(0);
                            String icon=weatherObject.getString("icon");
                            String urlIcon="https://openweathermap.org/img/wn/"+icon+".png";//icon
                            Picasso.get().load(urlIcon).into(imageIcon);
                            String visibility=response.getString("visibility");
                            txtVisibility.setText(visibility+"m");
                            String name=response.getString("name");
                            textViewCity.setText(name);
                            String temperStatus=weatherObject.getString("description");// status
                            textViewStatus.setText(temperStatus);
                            JSONObject main=response.getJSONObject("main");
                            String  temp=main.getString("temp"); //temp
                            textViewTemp.setText(temp+"ºC");
                            String tempreal=main.getString("feels_like");
                            txtTempFeel.setText(tempreal+"ºC");
                            String humidity=main.getString("humidity");
                            textViewHumid.setText(humidity+"%");
                            String pressure=main.getString("pressure");
                            txtPressure.setText(pressure+"hPa");
                            JSONObject wind=response.getJSONObject("wind");
                            String speed=wind.getString("speed");
                            textViewWind.setText(speed+"m/s");
                            JSONObject clouds=response.getJSONObject("clouds");
                            String all=clouds.getString("all");
                            textViewCloud.setText(all+"%");
                            String day=response.getString("dt");
                            long lday=Long.parseLong(day);
                            SimpleDateFormat dateFormat=new SimpleDateFormat();
                            Date date=new Date(lday*1000L);
                            String currentTime=dateFormat.format(date);
                            textViewDay.setText(currentTime);
                            LinearLayout myLayout = findViewById(R.id.background_layout);
                            if("mây đứt đoạn".equals(temperStatus) || "ít mây".equals(temperStatus) ||
                                    "mây cụm".equals(temperStatus) || "mây thưa".equals(temperStatus) || "mây rải rác".equals(temperStatus)) {
                                myLayout.setBackgroundResource(R.drawable.banngay);
                            } else if("sương mù nồng độ cao".equals(temperStatus) || "sương mờ".equals(temperStatus) || "mây đen u ám".equals(temperStatus)) {
                                myLayout.setBackgroundResource(R.drawable.bui);
                            } else if("bầu trời quang đãng".equals(temperStatus)) {
                                myLayout.setBackgroundResource(R.drawable.clear);
                            }else if("mưa".equals(temperStatus)){
                                myLayout.setBackgroundResource(R.drawable.mua);
                            }else if("tuyết".equals(temperStatus)){
                                myLayout.setBackgroundResource(R.drawable.snow);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(MainActivity.this,"Không có dữ liệu cho thành phố "+city.toString(),Toast.LENGTH_SHORT).show();
                    }
                }
        );
        requestQueue.add(jsonObjectRequest);
    }

    private void getJsonHourly(String city) {
        String requestCity = city.equals("") ? "Hanoi" : city;
        String url="https://pro.openweathermap.org/data/2.5/forecast/hourly?q="+requestCity+"&cnt=10&appid=d77cb61a5967bb592700365acc89844f&units=metric";
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            JSONArray list = response.getJSONArray("list");
                            for (int i = 0; i < list.length(); i++) {
                                JSONObject item = list.getJSONObject(i);
                                String sNgay = item.getString("dt");
                                long lNgay = Long.parseLong(sNgay);
                                SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm");
                                Date date = new Date(lNgay*1000);
                                String currentHour = dateFormat.format(date); //ngay gio hien tai
                                JSONObject main = item.getJSONObject("main");
                                String tempHourly = main.getString("temp");
                                JSONArray weather = item.getJSONArray("weather");
                                JSONObject weatherItem = weather.getJSONObject(0);
                                String icon = weatherItem.getString("icon");
                                String iconHourly = "https://openweathermap.org/img/wn/"+icon+".png";
                                hourlyList.add(new Hourly(currentHour,tempHourly,iconHourly));
                            }
                            adapterHourly.notifyDataSetChanged();
                            } catch (JSONException e) {
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(MainActivity.this, "Không có dữ liệu theo giờ cho thành phố!"+city.toString(), Toast.LENGTH_SHORT).show();
                    }
                }
        );
        requestQueue.add(jsonObjectRequest);
    }
    private void mapping(){
        editTextSearch=findViewById(R.id.editTextSearch);
        buttonSearch=findViewById(R.id.buttonSearch);
        imageIcon=findViewById(R.id.imageIcon);
        textViewTemp=findViewById(R.id.textViewTemp);
        textViewStatus=findViewById(R.id.textViewStatus);
        textViewDay=findViewById(R.id.textViewDay);
        textViewCloud=findViewById(R.id.textViewCloud);
        textViewWind=findViewById(R.id.textViewWind);
        textViewHumid=findViewById(R.id.textViewHumid);
        buttonNext=findViewById(R.id.buttonNext);
        textViewCity=findViewById(R.id.textViewCity);
        txtVisibility=findViewById(R.id.txtVisibility);
        txtPressure=findViewById(R.id.txtPressure);
        txtTempFeel=findViewById(R.id.txtTempFeel);
        listHourly = findViewById(R.id.listHourly);
    }
    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.buttonSearch:
                city = editTextSearch.getText().toString().trim();
                getJsonWeather(city);
                hourlyList.clear();
                getJsonHourly(city);
                hide.hideSoftInputFromWindow(view.getWindowToken(), 0); //gọi hàm để ẩn bàn phím
                break;
            case R.id.buttonNext:
                Intent intent=new Intent(MainActivity.this, NextDayActivity.class);
                intent.putExtra("city",city);
                startActivity(intent);
                break;
        }
    }
}