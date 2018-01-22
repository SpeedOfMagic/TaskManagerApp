package team13.taskmanagerapp;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
public class LoginActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        setTitle("Авторизация");
        findViewById(R.id.authorize).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                TextView textView = findViewById(R.id.login);
                CharSequence login = textView.getText();
                textView = findViewById(R.id.password);
                CharSequence pass = textView.getText();
                /*TODO создать класс User и функцию verify()
                int result=verify(new User(login,pass));
                if (result==1){
                    finish();
                } else if (result==0){

                } else {

                }
                 */
            }
        });
        final Context context=this;
        findViewById(R.id.register).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(context,RegisterActivity.class));
            }
        });
    }
    @Override
    public void onBackPressed(){}
}
