package team13.taskmanagerapp;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class log extends AppCompatActivity {

    private Button btn;
    private EditText login;
    private EditText password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_log);

        login = findViewById(R.id.login);
        password = findViewById(R.id.password);


        Toast.makeText(getApplicationContext(), "GetLogPage",Toast.LENGTH_LONG).show();// toast check. Remove it in future pls


        btn = findViewById(R.id.sendbtn);

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Toast.makeText(getApplicationContext(), login.getText() +  " | " + password.getText() ,Toast.LENGTH_LONG).show();// toast check. Remove it in future pls

                
            }
        });
    }
}
