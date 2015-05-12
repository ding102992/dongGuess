package org.wb.hust.dongguess.bmob;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import cn.bmob.push.PushConstants;

/**
 * Created by Administrator on 2015/1/21.
 */
public class PushMsgReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        if(intent.getAction().equals(PushConstants.ACTION_MESSAGE)){
            JSONTokener jsonTokener = new JSONTokener(intent.getStringExtra(PushConstants.EXTRA_PUSH_MESSAGE_STRING));
            try {
                JSONObject msgObj = (JSONObject)jsonTokener.nextValue();
                String msg = msgObj.getString("alert");
                Toast.makeText(context, msg,Toast.LENGTH_SHORT).show();

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }
}
