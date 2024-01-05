package blink.vietnam.weatherapp2.Activitis;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
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

import blink.vietnam.weatherapp2.Adapters.WeatherAadapter;
import blink.vietnam.weatherapp2.Model.Weather;
import blink.vietnam.weatherapp2.R;

public class NextDayActivity extends AppCompatActivity {
    List<Weather> weatherList;
    WeatherAadapter adapter;
    ListView lvNextDay;
    TextView txtCountry,txtCity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_next_day);
        lvNextDay = findViewById(R.id.lvNextDay);
        txtCountry = findViewById(R.id.txtCountry);
        txtCity = findViewById(R.id.txtCity);
        Intent intent = getIntent();
        String city = intent.getStringExtra("city");
        weatherList = new ArrayList<>();
        adapter = new WeatherAadapter(NextDayActivity.this, R.layout.row_weather, weatherList);
        lvNextDay.setAdapter(adapter);
        getJsonNextDay(city);

        ImageView buttonNextPage = findViewById(R.id.back);
        buttonNextPage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Tạo Intent để chuyển đến hoạt động mới
                Intent intent = new Intent(NextDayActivity.this, MainActivity.class);
                startActivity(intent);
            }
        });
    }
    private void getJsonNextDay(String city) {
        String requestCity = city.equals("") ? "Hanoi" : city;
        String url2="https://api.openweathermap.org/data/2.5/forecast/daily?q="+requestCity+"&cnt=7&appid=d77cb61a5967bb592700365acc89844f&lang=vi&units=metric";
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url2, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            JSONObject infor=response.getJSONObject("city");
                            String  country=infor.getString("country"); //temp
                            txtCountry.setText(country);
                            String name=infor.getString("name");
                            txtCity.setText(name);
                            JSONArray list = response.getJSONArray("list");
                            for (int i = 0; i < list.length(); i++) {
                                JSONObject item = list.getJSONObject(i);
                                String sNgay = item.getString("dt");
                                long lNgay = Long.parseLong(sNgay);
                                SimpleDateFormat dateFormat = new SimpleDateFormat("EEEE   dd/MM/yyyy");
                                Date date = new Date(lNgay*1000);
                                String currentTime = dateFormat.format(date); //ngay gio hien tai
                                JSONObject main = item.getJSONObject("temp");
                                String min = main.getString("min");
                                String max = main.getString("max");
                                JSONArray weather = item.getJSONArray("weather");
                                JSONObject weatherItem = weather.getJSONObject(0);
                                String temperStatus = weatherItem.getString("description");
                                String icon = weatherItem.getString("icon");
                                String urlIcon = "https://openweathermap.org/img/wn/"+icon+".png";
                                weatherList.add(new Weather(currentTime, temperStatus, urlIcon, min, max));

                            }
                            adapter.notifyDataSetChanged();
                        } catch (JSONException e) {
                            throw new RuntimeException(e);
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(NextDayActivity.this, "Không có dữ liệu theo ngày cho thành phố!", Toast.LENGTH_SHORT).show();
                        Log.d("MyError:", error.toString());
                    }
                }
        );
        requestQueue.add(jsonObjectRequest);
    }
}
