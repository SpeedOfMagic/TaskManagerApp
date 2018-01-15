package team13.taskmanagerapp;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TimePicker;

/**
 * Created by kate on 15.01.2018.
 */

public class EditNotificationActivity extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.edit_notification);
        final TimePicker timePicker = findViewById(R.id.timePicker);
        timePicker.setIs24HourView(true);
        if (getIntent().hasExtra("current_hours") && getIntent().hasExtra("current_minutes")) {
            timePicker.setCurrentHour(getIntent().getIntExtra("current_hours", 0));
            timePicker.setCurrentMinute(getIntent().getIntExtra("current_minutes", 0));
        }

        final EditText message = findViewById(R.id.editText);
        if (getIntent().hasExtra("current_message")) {
            message.setText(getIntent().getStringExtra("current_message"));
        }

        Button ready = findViewById(R.id.ready);
        ready.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent data = new Intent();
                data.putExtra("message", message.getText().toString());
                data.putExtra("hours", timePicker.getCurrentHour());
                data.putExtra("minutes", timePicker.getCurrentMinute());
                setResult(RESULT_OK, data);
                EditNotificationActivity.this.finish();
            }
        });
    }
}
