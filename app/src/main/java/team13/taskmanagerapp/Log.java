package team13.taskmanagerapp;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class Log extends AppCompatActivity {

    private Button btn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_log);
        btn = findViewById(R.id.sendBtn);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Toast.makeText(getApplicationContext(), login.getText() +  " | " + password.getText() ,Toast.LENGTH_LONG).show();// toast check. Remove it in future pls

                Intent intent = new Intent(Log.this, WebLog.class);
                startActivity(intent);
            }
        });
    }
}
