package com.example.shlokpatel.weather;

import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.util.Scanner;
import java.net.URL;

import static android.provider.ContactsContract.CommonDataKinds.Website.URL;

public class MainActivity extends AppCompatActivity {
    TextView textCity, textCountry, textTemp, textHumid, textWeather;
    ImageView imageView;
    CardView cardView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        cardView = findViewById(R.id.cardView);
        final EditText editText = findViewById(R.id.edit);
        Button button = findViewById(R.id.btn);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cardView.setVisibility(View.GONE);
                String s = editText.getText().toString();
                if (s.equals(""))
                    Toast.makeText(getBaseContext(), "Enter a city", Toast.LENGTH_SHORT).show();
                else {
                    MyNetworkTask myNetworkTask = new MyNetworkTask();
                    myNetworkTask.execute("https://api.apixu.com/v1/current.json?key=fa92d0c19e9b4840a28100602182906&q=" + s);
                    editText.setText("");
                }
            }
        });

    }

    public Result convertJsonToResponse(String s) {
        try {
            JSONObject jsonObject = new JSONObject(s);
            JSONObject jsonObject1 = jsonObject.getJSONObject("location");
            String city = jsonObject1.getString("name");
            String country = jsonObject1.getString("country");
            JSONObject jsonObject2 = jsonObject.getJSONObject("current");
            Double temp = jsonObject2.getDouble("temp_c");
            Double humidity = jsonObject2.getDouble("humidity");
            JSONObject jsonObject3 = jsonObject2.getJSONObject("condition");
            String text = jsonObject3.getString("text");
            String pic = jsonObject3.getString("icon");
            Result result = new Result(city, country, text, pic, temp, humidity);
            return result;
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }

    class MyNetworkTask extends AsyncTask<String, Void, String> {


        @Override
        protected String doInBackground(String... strings) {
            String string = strings[0];

            try {
                URL url = new URL(string);

                //Open a new Connection using the URL
                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();

                //Store the contents of the web-page as a Stream
                InputStream inputStream = httpURLConnection.getInputStream();

                //Create a Scanner from the Stream to get data in a human readable form
                Scanner scanner = new Scanner(inputStream);

                //Tells the scanner to read the file from the very start to the very end of file
                scanner.useDelimiter("\\A");

                String result = "";

                if (scanner.hasNext()) {
                    //Read the entire content of scanner in a go, otherwise scanner reads individual bytes one by one
                    result = scanner.next();
                }
                return result;
            } catch (IOException e) {
                e.printStackTrace();
            }
            return "";
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            Result result = convertJsonToResponse(s);
            if (result != null) {
                textCity = findViewById(R.id.city);
                textCountry = findViewById(R.id.country);
                textTemp = findViewById(R.id.temp);
                textHumid = findViewById(R.id.humidity);
                textWeather = findViewById(R.id.weather);
                imageView = findViewById(R.id.picture);
                String url = "https:" + result.getImageUrl().toString();
                Picasso.with(getApplicationContext()).load(url).into(imageView);
                textCity.setText(result.getCity());
                textCountry.setText(result.getCountry());
                textTemp.setText(result.getTemp().toString() + "°C");
                textHumid.setText("Humidity:" + result.getHumidity().toString());
                textWeather.setText(result.getCondition());

                cardView.setVisibility(View.VISIBLE);
            }
            else
                Toast.makeText(getBaseContext(),"Enter a valid city",Toast.LENGTH_SHORT).show();
        }
    }
}
