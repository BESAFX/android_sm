package app.besafx.com.smartmanager;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

public class TasksView extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.tasks_view);

        // Get the Intent that started this activity and extract the string
        Intent intent = getIntent();
        String user_name = intent.getStringExtra("USER_NAME");
        String user_pass = intent.getStringExtra("USER_PASS");

        // Capture the layout's TextView and set the string as its text
        TextView textView_userName = (TextView) findViewById(R.id.txtView_user_name);
        TextView textView_userPass = (TextView) findViewById(R.id.txtView_user_password);
        textView_userName.setText(user_name);
        textView_userPass.setText(user_pass);
    }
}
