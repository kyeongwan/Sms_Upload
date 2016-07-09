package kr.kyeongwan.smsupload;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Contacts;
import android.telephony.SmsMessage;
import android.util.Log;

import java.util.Date;

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
            Date curDate = new Date(smsMessages[0].getTimestampMillis());
            String origNumber = smsMessages[0].getOriginatingAddress();

            Log.i("test", curDate.toString() + " / " + getContactName(context, origNumber+"") + " / "+ MessageBody);
        }
    }

    public String getContactName(final Context context, final String phoneNumber)
    {
        Uri uri;
        String[] projection;
        Uri mBaseUri = Contacts.Phones.CONTENT_FILTER_URL;
        projection = new String[] { android.provider.Contacts.People.NAME };
        try {
            Class<?> c =Class.forName("android.provider.ContactsContract$PhoneLookup");
            mBaseUri = (Uri) c.getField("CONTENT_FILTER_URI").get(mBaseUri);
            projection = new String[] { "display_name" };
        }
        catch (Exception e) {
        }


        uri = Uri.withAppendedPath(mBaseUri, Uri.encode(phoneNumber));
        Cursor cursor = context.getContentResolver().query(uri, projection, null, null, null);

        String contactName = "";

        if (cursor.moveToFirst())
        {
            contactName = cursor.getString(0);
        }

        cursor.close();
        cursor = null;

        return contactName;
    }
}
