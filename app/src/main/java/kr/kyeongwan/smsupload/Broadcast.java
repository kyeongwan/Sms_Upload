package kr.kyeongwan.smsupload;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.telephony.SmsMessage;
import android.util.Log;

/**
 * Created by lk on 16. 7. 8..
 */
public class Broadcast extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent.getAction().equals("android.provider.Telephony.SMS_RECEIVED")) {

            Bundle bundle = intent.getExtras();
            if (bundle == null) {
                return;
            }
            Object[] pdusObj = (Object[]) bundle.get("pdus");
            if (pdusObj == null) {
                return;
            }
            String MessageBody = "";

            SmsMessage[] smsMessages = new SmsMessage[pdusObj.length];
            for (int i = 0; i < pdusObj.length; i++) {
                smsMessages[i] = SmsMessage.createFromPdu((byte[]) pdusObj[i]);
                MessageBody += smsMessages[i].getMessageBody();
            }
            Log.i("test", MessageBody);
        }
    }
}
