package app.besafx.com.smartmanager.activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import app.besafx.com.smartmanager.R;
import app.besafx.com.smartmanager.entity.Person;
import org.springframework.http.*;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.client.RestTemplate;

import java.util.Collections;

public class Main extends LoaderActivity {

    protected static final String TAG = Main.class.getSimpleName();

    public static HttpHeaders requestHeaders;

    public static Person me;

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
            txtView_error_message.setText("تأكد من اتصالك بالأنترنت");
            txtView_error_message.setTextColor(Color.parseColor("#d9e5f3"));
            txtView_error_message.setBackgroundColor(Color.parseColor("#E91E63"));
            txtView_error_message.setVisibility(View.VISIBLE);
            return;
        }
    }

    public void login(View view) {

        checkConnection();

        storeUserLogin();
    }

    private void storeUserLogin() {
        EditText editText_userName = (EditText) findViewById(R.id.user_name);
        EditText editText_userPassword = (EditText) findViewById(R.id.user_password);
        String userName = editText_userName.getText().toString();
        String password = editText_userPassword.getText().toString();

        if(userName.matches("") || password.matches("")){
            Toast.makeText(this, "تأكد من اكتمال كافة الحقول المطلوبة", Toast.LENGTH_SHORT).show();
            return;
        }else{
            //Call login from web services
            new FetchSecuredResourceTask().execute();
        }
    }

    private boolean isInternetAvailable() {
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        return cm.getActiveNetworkInfo() != null;
    }

    private class FetchSecuredResourceTask extends AsyncTask<Void, Void, Person> {

        private String username;

        private String password;

        @Override
        protected void onPreExecute() {
            showLoadingProgressDialog();

            ProgressBar progressBar = (ProgressBar) findViewById(R.id.progressBar);
            progressBar.setVisibility(View.VISIBLE);

            // build the message object
            EditText editText = (EditText) findViewById(R.id.user_name);
            this.username = editText.getText().toString();

            editText = (EditText) findViewById(R.id.user_password);
            this.password = editText.getText().toString();
        }

        @Override
        protected Person doInBackground(Void... params) {
            final String url = getString(R.string.rest_url) + "/api/person/findActivePerson";

            // Populate the HTTP Basic Authentication header with the username and password
            HttpAuthentication authHeader = new HttpBasicAuthentication(username, password);
            requestHeaders = new HttpHeaders();
            requestHeaders.setAuthorization(authHeader);
            requestHeaders.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));

            // Create a new RestTemplate instance
            RestTemplate restTemplate = new RestTemplate();
            restTemplate.getMessageConverters().add(new MappingJackson2HttpMessageConverter());

            try {
                // Make the network request
                ResponseEntity<Person> response = restTemplate.exchange(url, HttpMethod.GET, new HttpEntity<>(requestHeaders), Person.class);
                return response.getBody();
            } catch (Exception e) {
                Log.e(TAG, e.getLocalizedMessage(), e);
                return null;
            }
        }

        @Override
        protected void onPostExecute(Person person) {
            dismissProgressDialog();

            me = person;

            ProgressBar progressBar = (ProgressBar) findViewById(R.id.progressBar);
            progressBar.setVisibility(View.INVISIBLE);
            TextView txtView_error_message = (TextView) findViewById(R.id.error_message);
            txtView_error_message.setTextColor(Color.parseColor("#d9e5f3"));

            if(person == null){
                txtView_error_message.setBackgroundColor(Color.parseColor("#E91E63"));
                txtView_error_message.setText("فضلاً تأكد من بيانات الدخول");
            }else{
                txtView_error_message.setBackgroundColor(Color.parseColor("#2E7D32"));
                txtView_error_message.setText("تم تسجيل الدخول بنجاح");

                Intent intent = new Intent(getApplicationContext(), Home.class);
                intent.putExtra("person", person);
                startActivity(intent);
            }
            txtView_error_message.setVisibility(View.VISIBLE);
        }

    }
}
