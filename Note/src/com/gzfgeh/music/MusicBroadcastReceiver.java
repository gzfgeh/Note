package com.gzfgeh.music;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class MusicBroadcastReceiver extends BroadcastReceiver {

	@Override
	public void onReceive(Context context, Intent intent) {
		// TODO Auto-generated method stub
		String artistName = intent.getStringExtra("artist");
		String album = intent.getStringExtra("album");
		String track = intent.getStringExtra("track");
        boolean playing = intent.getBooleanExtra("playing",false);
        long duration = intent.getLongExtra("duration",3000);
        long position = intent.getLongExtra("position",1000);
	}

}
