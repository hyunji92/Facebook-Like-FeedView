package info.androidhive.listviewfeed;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import ib.push.helper.GCMReceiver;
import ib.push.helper.InboxHelper;

public class GcmPushReceiver extends GCMReceiver {
	static final String TAG = "IPSTest";

	@Override
	public void onGCMError(Context context, String msg) {

		Log.i(TAG, "onGCMError");
	}

	@Override
	public void onGCMMessage(Context context, Intent intent) {
		Bundle bundle = intent.getExtras();


		Bundle payload = intent.getExtras();
		String msgKey = payload.getString("IB_MK");
		String pushTitle = payload.getString("IB_PUSH_TITLE");
		String pushText = payload.getString("IB_PUSH_TEXT");

		Log.i(TAG, "onGCMMessage");

		Log.i(TAG, "msgKey : "+msgKey);
		Log.i(TAG, "pushTitle : "+pushTitle);
		Log.i(TAG, "pushText : "+pushText);

		//InboxHelper.sendReceivePushNotiFromGCM(context, msgKey);

        if( ShowMsgActivity.bShowMsgActivity == true )
        {
        	ShowMsgActivity.mShowMsgActivity.finish();
        	ShowMsgActivity.bShowMsgActivity = false;
        }

    	Intent dialogIntent = new Intent(context, ShowMsgActivity.class);
    	dialogIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
    	dialogIntent.putExtra("kill", "0");
    	dialogIntent.putExtra("msgKey", msgKey);
    	dialogIntent.putExtra("pushTitle", pushTitle);
    	dialogIntent.putExtra("pushText", pushText);
    	context.startActivity(dialogIntent);



	}

	@Override
	public void onGCMRegistered(Context context, String regId){
		Log.i(TAG, "onGCMRegistered regId:"+regId);



	}

}
