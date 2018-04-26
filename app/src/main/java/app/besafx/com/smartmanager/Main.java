package app.besafx.com.smartmanager;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import app.besafx.com.smartmanager.entity.Notification;
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

    @Override
    protected void onStart() {
        super.onStart();
        checkConnection();
    }

    private void checkConnection() {
        TextView txtView_error_message = (TextView) findViewById(R.id.error_message);
        //Check connection is a live
        if (!isInternetAvailable()) {
            txtView_error_message.setText("Check You Connection Please.");
            txtView_error_message.setTextColor(Color.parseColor("#d9e5f3"));
            txtView_error_message.setBackgroundColor(Color.parseColor("#E91E63"));
            txtView_error_message.setVisibility(View.VISIBLE);
            return;
        }
    }

    public void login(View view) {

        checkConnection();

        EditText editText_userName = (EditText) findViewById(R.id.user_name);
        EditText editText_userPassword = (EditText) findViewById(R.id.user_password);
        USER_NAME = editText_userName.getText().toString();
        USER_PASS = editText_userPassword.getText().toString();

        //Call login from web services
        new RestLogin(USER_NAME, USER_PASS).execute();
    }

    public boolean isInternetAvailable() {
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        return cm.getActiveNetworkInfo() != null;
    }

    private class RestLogin extends AsyncTask<Void, Void, Notification> {

        private String userName, password;

        public RestLogin(String userName, String password) {
            this.userName = userName;
            this.password = password;
        }

        @Override
        protected Notification doInBackground(Void... params) {
            try {
                final String url = "https://ararhni.herokuapp.com/api/android/login/" + userName + "/" + password;
                RestTemplate restTemplate = new RestTemplate();
                restTemplate.getMessageConverters().add(new MappingJackson2HttpMessageConverter());
                Notification notification = restTemplate.getForObject(url, Notification.class);
                return notification;
            } catch (Exception e) {
                Log.e("MainActivity", e.getMessage(), e);
            }

            return null;
        }

        @Override
        protected void onPostExecute(Notification notification) {
            TextView txtView_error_message = (TextView) findViewById(R.id.error_message);
            txtView_error_message.setText(notification.getMessage());
            txtView_error_message.setTextColor(Color.parseColor("#d9e5f3"));

            switch (notification.getCode()){
                case "SUCCESS":
                    txtView_error_message.setBackgroundColor(Color.parseColor("#2E7D32"));

                    Intent intent = new Intent(getApplicationContext(), TasksView.class);
                    intent.putExtra("USER_NAME", USER_NAME);
                    intent.putExtra("USER_PASS", USER_PASS);
                    startActivity(intent);

                    break;
                case "FAILED":
                    txtView_error_message.setBackgroundColor(Color.parseColor("#E91E63"));
                    break;
            }

            txtView_error_message.setVisibility(View.VISIBLE);
        }

    }
}
