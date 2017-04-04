package ch.csbe.loismula.wecker;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

/**
 * Created by lois.mula on 23.03.2017.
 */

public class Alarm extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {



        String get_your_string = intent.getExtras().getString("extra");



        Intent service_intent = new Intent(context, RingtonePlayingService.class);

        service_intent.putExtra("extra", get_your_string);

        context.startService(service_intent);
    }
}
