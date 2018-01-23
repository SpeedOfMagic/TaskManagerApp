package team13.taskmanagerapp;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;
public class AuthorizationActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        setTitle("Авторизация");
        findViewById(R.id.authorizeBtn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                TextView textView = findViewById(R.id.login);
                CharSequence login = textView.getText();
                textView = findViewById(R.id.password);
                CharSequence pass = textView.getText();
                User user=new User(login,pass);
                /*TODO создать функцию verify()
                if (verify(user)){
                    finish();
                } else {
                    textView=findViewById(R.id.incorrectPassOrLogin);
                    textView.setVisibility(View.VISIBLE);
                }
                 */
            }
        });
        final Context context=this;
        findViewById(R.id.registerLink).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(context,RegisterActivity.class));
            }
        });
    }
    @Override
    public void onBackPressed(){}
}
