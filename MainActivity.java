package com.example.pc.expathelper;

//import androidx.appcompat.app.AppCompatActivity;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;

import com.google.gson.Gson;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    ListView lv; EditText etCity;
    private ArrayList<String> cityes = new ArrayList<String>();
    ArrayAdapter<String> adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        lv = findViewById(R.id.cities_list);
        etCity = findViewById(R.id.city);
        int[] base_cityes = getResources().getIntArray(R.array.base_cityes);
        CountTask task = new CountTask();
        task.execute(base_cityes);
        adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, cityes);
        lv.setAdapter(adapter);
    }

    //Добрый день, товарищи студенты!
    class CountTask extends AsyncTask<int[], Integer, Void> {

        public Weather getTemperatureByCity(int cityID) {
            String API_KEY = "d79091018cda7ef56338f8756ec7a36a"; // укажите свой ключ для API
            String sampleURL = "https://api.openweathermap.org/data/2.5/weather?id=" + String.valueOf(cityID) + "&apikey=" + API_KEY;
            try {
                URL url = new URL(sampleURL); // заменить на правильный адрес, как в API https://openweathermap.org/current
                InputStream stream = (InputStream) url.getContent();
                Gson gson = new Gson();
                Weather weather = gson.fromJson(new InputStreamReader(stream), Weather.class);
                // создать класс Weather, соответствующий структуре данных в JSON
                // в Weather должен быть внутренний класс
                return weather; // указать нужное поле класса
            } catch (IOException e) {
                Log.d("mytag", e.getLocalizedMessage()); // выводим ошибку в журнал
                return new Weather();
            }

        }
        @Override
        protected Void doInBackground(int[]... cities) {
            for (int cityID: cities[0]){
                Weather weather = getTemperatureByCity(cityID);
                Log.d("mytag", "temperature for " + cityID + " is "+ weather.main.temp);
                if (weather != null)
                {
                    String str = String.valueOf(cityID) + ": " + String.valueOf((int)weather.main.temp - 273);
                    cityes.add(str);
                }
            }
            return null;
        }
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
        }
        @Override
        protected void onProgressUpdate(Integer... values) {
            super.onProgressUpdate(values);
        }
    }

    public void onClick(View v)
    {
        if (v.getId() == R.id.add)
        {
            CountTask task = new CountTask();
            task.execute(new int[] {Integer.parseInt(etCity.getText().toString())});
        }

        if (v.getId() == R.id.clear)
        {
            adapter.clear();
        }
    }
}
