package team13.taskmanagerapp;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.CheckBox;
import android.widget.TextView;

public class RegisterActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        setTitle(R.string.registration);
        final Context thisContext=this;
        findViewById(R.id.authorizeLink).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(thisContext,AuthorizationActivity.class));
            }
        });
        findViewById(R.id.registerBtn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                TextView textView=findViewById(R.id.loginRegister);
                CharSequence login=textView.getText();
                textView=findViewById(R.id.passwordRegister);
                CharSequence pass=textView.getText();
                User user=new User(login,pass);
                CheckBox rememberMeCheckBox=findViewById(R.id.rememberMeReg);
                boolean rememberMe=rememberMeCheckBox.isChecked();
                //addUser(user); TODO создать функцию addUser
                //createCurrentUserSession(user,rememberMe); TODO создать функцию createCurrentUserSession()
            }
        });
    }
    @Override
    public void onBackPressed(){}
}
