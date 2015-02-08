package com.gzfgeh.service;

import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import android.util.Log;

import com.baidu.frontia.api.FrontiaPushMessageReceiver;
import com.gzfgeh.note.MainActivity;

/** 
 * Push消息处理receiver。请编写您需要的回调函数， 一般来说： onBind是必须的，用来处理startWork返回值； 
 * onMessage用来接收透传消息； onSetTags、onDelTags、onListTags是tag相关操作的回调； 
 * onNotificationClicked在通知被点击时回调； onUnbind是stopWork接口的返回值回调 
 *  
 * 返回值中的errorCode，解释如下：  
 *  0 - Success 
 *  10001 - Network Problem 
 *  30600 - Internal Server Error 
 *  30601 - Method Not Allowed  
 *  30602 - Request Params Not Valid 
 *  30603 - Authentication Failed  
 *  30604 - Quota Use Up Payment Required  
 *  30605 - Data Required Not Found  
 *  30606 - Request Time Expires Timeout  
 *  30607 - Channel Token Timeout  
 *  30608 - Bind Relation Not Found  
 *  30609 - Bind Number Too Many 
 *  
 * 当您遇到以上返回错误时，如果解释不了您的问题，请用同一请求的返回值requestId和errorCode联系我们追查问题。 
 *  
 */  

public class DataReceiver extends FrontiaPushMessageReceiver {
	public static final String TAG = "DataReceiver";
	
	 /** 
     * 调用PushManager.startWork后，sdk将对push 
     * server发起绑定请求，这个过程是异步的。绑定请求的结果通过onBind返回。 如果您需要用单播推送，需要把这里获取的channel 
     * id和user id上传到应用server中，再调用server接口用channel id和user id给单个手机或者用户推送。 
     *  
     * @param context 
     *            BroadcastReceiver的执行Context 
     * @param errorCode 
     *            绑定接口返回值，0 - 成功 
     * @param appid 
     *            应用id。errorCode非0时为null 
     * @param userId 
     *            应用user id。errorCode非0时为null 
     * @param channelId 
     *            应用channel id。errorCode非0时为null 
     * @param requestId 
     *            向服务端发起的请求id。在追查问题时有用； 
     * @return none 
     */  
	
	@Override
	public void onBind(Context context, int errorCode, String appid, String userId,
			String channelId, String requestId) {
		// TODO Auto-generated method stub
		String responseString = "onBind errorCode=" + errorCode + " appid="  
                + appid + " userId=" + userId + " channelId=" + channelId  
                + " requestId=" + requestId;  
        Log.e(TAG, responseString); 
        
        if (errorCode == 0){
        	SharedPreferencesData.bind(context);
        }
        
        updateContent(context,responseString);
        
	}

	@Override
	public void onDelTags(Context context, int errorCode, List<String> sucessTags,
			List<String> failTags, String requestId) {
		// TODO Auto-generated method stub
		String responseString = "onDelTags errorCode=" + errorCode  
                + " sucessTags=" + sucessTags + " failTags=" + failTags  
                + " requestId=" + requestId;  
        Log.e(TAG, responseString);  
  
        // Demo更新界面展示代码，应用请在这里加入自己的处理逻辑  
        updateContent(context, responseString); 
	}

	@Override
	public void onListTags(Context context, int errorCode, List<String> tags,
			String requestId) {
		// TODO Auto-generated method stub
		String responseString = "onListTags errorCode=" + errorCode + " tags="  
                + tags;  
        Log.e(TAG, responseString);  
  
        // Demo更新界面展示代码，应用请在这里加入自己的处理逻辑  
        updateContent(context, responseString); 
	}

	@Override
	public void onMessage(Context context, String message, String customContentString) {
		// TODO Auto-generated method stub
		String messageString = "透传消息 message=\"" + message  
                + "\" customContentString=" + customContentString;  
        Log.e(TAG, messageString);  
  
        // 自定义内容获取方式，mykey和myvalue对应透传消息推送时自定义内容中设置的键和值  
        if (!TextUtils.isEmpty(customContentString)) {  
            JSONObject customJson = null;  
            try {  
                customJson = new JSONObject(customContentString);  
                String myvalue = null;  
                if (customJson.isNull("mykey")) {  
                    myvalue = customJson.getString("mykey");  
                }  
            } catch (JSONException e) {  
                // TODO Auto-generated catch block  
                e.printStackTrace();  
            }  
        }  
  
        // Demo更新界面展示代码，应用请在这里加入自己的处理逻辑  
        updateContent(context, messageString);  
	}

	@Override
	public void onNotificationClicked(Context context, String title, String description,
			String customContentString) {
		// TODO Auto-generated method stub
		String notifyString = "通知点击 title=\"" + title + "\" description=\""  
                + description + "\" customContent=" + customContentString;  
        Log.e(TAG, notifyString);  
  
        // 自定义内容获取方式，mykey和myvalue对应通知推送时自定义内容中设置的键和值  
        if (!TextUtils.isEmpty(customContentString)) {  
            JSONObject customJson = null;  
            try {  
                customJson = new JSONObject(customContentString);  
                String myvalue = null;  
                if (customJson.isNull("mykey")) {  
                    myvalue = customJson.getString("mykey");  
                }  
            } catch (JSONException e) {  
                // TODO Auto-generated catch block  
                e.printStackTrace();  
            }  
        }  
  
        // Demo更新界面展示代码，应用请在这里加入自己的处理逻辑  
        updateContent(context, notifyString);  
	}

	@Override
	public void onSetTags(Context context, int errorCode, List<String> sucessTags,
			List<String> failTags, String requestId) {
		// TODO Auto-generated method stub
		String responseString = "onSetTags errorCode=" + errorCode  
                + " sucessTags=" + sucessTags + " failTags=" + failTags  
                + " requestId=" + requestId;  
        Log.e(TAG, responseString);  
  
        // Demo更新界面展示代码，应用请在这里加入自己的处理逻辑  
        updateContent(context, responseString); 
	}

	@Override
	public void onUnbind(Context context, int errorCode, String requestId) {
		// TODO Auto-generated method stub
		String responseString = "onUnbind errorCode=" + errorCode  
                + " requestId = " + requestId;  
        Log.e(TAG, responseString);  
  
        // 解绑定成功，设置未绑定flag，  
        if (errorCode == 0) {  
            SharedPreferencesData.unbind(context);  
        }  
        // Demo更新界面展示代码，应用请在这里加入自己的处理逻辑  
        updateContent(context, responseString); 
	}

	private void updateContent(Context context, String content) {
		// TODO Auto-generated method stub
		Intent intent = new Intent();  
        intent.putExtra("result", content);  
        intent.setClass(context.getApplicationContext(), help.class);  
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);  
        context.getApplicationContext().startActivity(intent); 
	}

}
