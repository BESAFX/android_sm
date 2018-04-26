package app.besafx.com.smartmanager;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.EditText;
import android.widget.TextView;
import app.besafx.com.smartmanager.entity.Company;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.client.RestTemplate;

public class Main extends AppCompatActivity {

    public static String USER_NAME = "";
    public static String USER_PASS = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
    }

    /** Called when the user taps the login button */
    public void login(View view) {
        Intent intent = new Intent(this, TasksView.class);
        EditText editText_userName = (EditText) findViewById(R.id.user_name);
        EditText editText_userPassword = (EditText) findViewById(R.id.user_password);
        USER_NAME = editText_userName.getText().toString();
        USER_PASS = editText_userPassword.getText().toString();
        intent.putExtra("USER_NAME", USER_NAME);
        intent.putExtra("USER_PASS", USER_PASS);

        TextView txtView_error_message = (TextView) findViewById(R.id.error_message);
        txtView_error_message.setVisibility(View.VISIBLE);
//        startActivity(intent);
    }

    private class HttpRequestTask extends AsyncTask<Void, Void, Company> {
        @Override
        protected Company doInBackground(Void... params) {
            try {
                final String url = "http://192.168.1.24:8080/api/company/get";
                RestTemplate restTemplate = new RestTemplate();
                restTemplate.getMessageConverters().add(new MappingJackson2HttpMessageConverter());
                Company greeting = restTemplate.getForObject(url, Company.class);
                return greeting;
            } catch (Exception e) {
                Log.e("MainActivity", e.getMessage(), e);
            }

            return null;
        }

        @Override
        protected void onPostExecute(Company greeting) {
//            TextView greetingIdText = (TextView) findViewById(R.id.txt_greeting);
//            greetingIdText.setText(greeting.getName());
        }

    }
}
