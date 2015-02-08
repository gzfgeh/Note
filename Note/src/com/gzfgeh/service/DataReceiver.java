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
 * Push��Ϣ����receiver�����д����Ҫ�Ļص������� һ����˵�� onBind�Ǳ���ģ���������startWork����ֵ�� 
 * onMessage��������͸����Ϣ�� onSetTags��onDelTags��onListTags��tag��ز����Ļص��� 
 * onNotificationClicked��֪ͨ�����ʱ�ص��� onUnbind��stopWork�ӿڵķ���ֵ�ص� 
 *  
 * ����ֵ�е�errorCode���������£�  
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
 * �����������Ϸ��ش���ʱ��������Ͳ����������⣬����ͬһ����ķ���ֵrequestId��errorCode��ϵ����׷�����⡣ 
 *  
 */  

public class DataReceiver extends FrontiaPushMessageReceiver {
	public static final String TAG = "DataReceiver";
	
	 /** 
     * ����PushManager.startWork��sdk����push 
     * server�������������������첽�ġ�������Ľ��ͨ��onBind���ء� �������Ҫ�õ������ͣ���Ҫ�������ȡ��channel 
     * id��user id�ϴ���Ӧ��server�У��ٵ���server�ӿ���channel id��user id�������ֻ������û����͡� 
     *  
     * @param context 
     *            BroadcastReceiver��ִ��Context 
     * @param errorCode 
     *            �󶨽ӿڷ���ֵ��0 - �ɹ� 
     * @param appid 
     *            Ӧ��id��errorCode��0ʱΪnull 
     * @param userId 
     *            Ӧ��user id��errorCode��0ʱΪnull 
     * @param channelId 
     *            Ӧ��channel id��errorCode��0ʱΪnull 
     * @param requestId 
     *            �����˷��������id����׷������ʱ���ã� 
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
  
        // Demo���½���չʾ���룬Ӧ��������������Լ��Ĵ����߼�  
        updateContent(context, responseString); 
	}

	@Override
	public void onListTags(Context context, int errorCode, List<String> tags,
			String requestId) {
		// TODO Auto-generated method stub
		String responseString = "onListTags errorCode=" + errorCode + " tags="  
                + tags;  
        Log.e(TAG, responseString);  
  
        // Demo���½���չʾ���룬Ӧ��������������Լ��Ĵ����߼�  
        updateContent(context, responseString); 
	}

	@Override
	public void onMessage(Context context, String message, String customContentString) {
		// TODO Auto-generated method stub
		String messageString = "͸����Ϣ message=\"" + message  
                + "\" customContentString=" + customContentString;  
        Log.e(TAG, messageString);  
  
        // �Զ������ݻ�ȡ��ʽ��mykey��myvalue��Ӧ͸����Ϣ����ʱ�Զ������������õļ���ֵ  
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
  
        // Demo���½���չʾ���룬Ӧ��������������Լ��Ĵ����߼�  
        updateContent(context, messageString);  
	}

	@Override
	public void onNotificationClicked(Context context, String title, String description,
			String customContentString) {
		// TODO Auto-generated method stub
		String notifyString = "֪ͨ��� title=\"" + title + "\" description=\""  
                + description + "\" customContent=" + customContentString;  
        Log.e(TAG, notifyString);  
  
        // �Զ������ݻ�ȡ��ʽ��mykey��myvalue��Ӧ֪ͨ����ʱ�Զ������������õļ���ֵ  
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
  
        // Demo���½���չʾ���룬Ӧ��������������Լ��Ĵ����߼�  
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
  
        // Demo���½���չʾ���룬Ӧ��������������Լ��Ĵ����߼�  
        updateContent(context, responseString); 
	}

	@Override
	public void onUnbind(Context context, int errorCode, String requestId) {
		// TODO Auto-generated method stub
		String responseString = "onUnbind errorCode=" + errorCode  
                + " requestId = " + requestId;  
        Log.e(TAG, responseString);  
  
        // ��󶨳ɹ�������δ��flag��  
        if (errorCode == 0) {  
            SharedPreferencesData.unbind(context);  
        }  
        // Demo���½���չʾ���룬Ӧ��������������Լ��Ĵ����߼�  
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
