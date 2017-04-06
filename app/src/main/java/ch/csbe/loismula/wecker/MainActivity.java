package ch.csbe.loismula.wecker;

import android.annotation.TargetApi;
import android.app.AlarmManager;
import android.app.DatePickerDialog;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.support.v4.app.NotificationCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import android.widget.Button;
import android.widget.DatePicker;
import android.widget.TextView;
import android.widget.TimePicker;
import java.util.Calendar;

import android.app.Dialog;
import android.support.v4.app.DialogFragment;
import java.text.DateFormat;

import java.util.GregorianCalendar;




public class MainActivity extends AppCompatActivity implements DatePickerDialog.OnDateSetListener {

    AlarmManager alarm_manager;
    TimePicker alarm_timepicker;
    TextView update_text;
    Context context;
    private View alarm_on;
    PendingIntent pending_intent;
    private PendingIntent pendingIntent;
    private android.net.Uri defaultSoundUri;
    private CharSequence message;
    private CharSequence title;
    private Button btnback;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        this.context = this;



        alarm_manager = (AlarmManager) getSystemService(ALARM_SERVICE);

        alarm_timepicker = (TimePicker) findViewById(R.id.timePicker);

        update_text = (TextView) findViewById(R.id.update_text);


        final Calendar calendar = Calendar.getInstance();

        Button start_alarm = (Button) findViewById(R.id.alarm_on);




        final Intent my_intent = new Intent(this.context, Alarm.class);


        start_alarm.setOnClickListener(new View.OnClickListener() {

            @TargetApi(Build.VERSION_CODES.M)
            @Override
            public void onClick(View v) {

                
                NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(context)
                        .setSmallIcon(R.mipmap.wecker)
                        .setLargeIcon(BitmapFactory.decodeResource(context.getResources(),
                                R.mipmap.wecker))
                        .setContentTitle(title)
                        .setContentText(message)
                        .setAutoCancel(true)
                        .setSound(defaultSoundUri)
                        .setContentIntent(pendingIntent);

                android.app.NotificationManager notificationManager =
                        (android.app.NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

                notificationManager.notify(0 , notificationBuilder.build());


                calendar.set(Calendar.HOUR_OF_DAY, alarm_timepicker.getHour());
                calendar.set(Calendar.MINUTE, alarm_timepicker.getMinute());

                int hour = alarm_timepicker.getHour();
                int minute = alarm_timepicker.getMinute();

                String hour_string = String.valueOf(hour);
                String minute_string = String.valueOf(minute);

                if (hour > 12) {
                    hour_string = String.valueOf(hour - 12);


                }

                set_alarm_text("Alarm set on: " + hour_string + ":" + minute_string);


                my_intent.putExtra("extra", "alarm on");

                pending_intent = PendingIntent.getBroadcast(MainActivity.this, 0,
                        my_intent, PendingIntent.FLAG_UPDATE_CURRENT);

                alarm_manager.set(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(),
                        pending_intent);


            }

        });

        Button alarm_off = (Button) findViewById(R.id.alarm_off);

        alarm_off.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                set_alarm_text("Alarm off!");


                alarm_manager.cancel(pending_intent);

                my_intent.putExtra("extra", "alarm off");

                sendBroadcast(my_intent);
            }});



        btnback = (Button) findViewById(R.id.back);

        btnback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //setContentView(R.layout.activity_main);
                Intent intent = new Intent(MainActivity.this, HomeActivity.class);
                startActivity(intent);


            }
        });



    }

    private void set_alarm_text(String output) {
        update_text.setText(output);
    }





    public void datePicker(View view){

        DatePickerFragment fragment = new DatePickerFragment();
        fragment.show(getSupportFragmentManager(), "date");
    }


    private void setDate(final Calendar calendar) {
        final DateFormat dateFormat = DateFormat.getDateInstance(DateFormat.MEDIUM);
        ((TextView) findViewById(R.id.showDate)).setText(dateFormat.format(calendar.getTime()));

    }


    @Override
    public void onDateSet(DatePicker view, int year, int month, int day) {
        Calendar cal = new GregorianCalendar(year, month, day);
        setDate(cal);
    }


    public static class DatePickerFragment extends DialogFragment {

        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            final Calendar c = Calendar.getInstance();
            int year = c.get(Calendar.YEAR);
            int month = c.get(Calendar.MONTH);
            int day = c.get(Calendar.DAY_OF_MONTH);


            return new DatePickerDialog(getActivity(),
                    (DatePickerDialog.OnDateSetListener)
                            getActivity(), year, month, day);
        }


    }

}
