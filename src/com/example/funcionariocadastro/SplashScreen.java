package com.example.funcionariocadastro;

import android.app.Activity;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;


public class SplashScreen extends Activity {

	// Splash screen timer
	private static int SPLASH_TIME_OUT = 3000;
	MediaPlayer media;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_splash);
		media = MediaPlayer.create(this, R.raw.audio);
		media.start();

		new Handler().postDelayed(new Runnable() {

			@Override
			public void run() {

				Intent i = new Intent(SplashScreen.this, MainActivity.class);
				startActivity(i);

				finish();

			}

		}, SPLASH_TIME_OUT);

	}

	@Override
	public void onResume() {
		super.onResume();
		media.start();

	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		media.stop();

	}

	@Override
	public void onStart() {
		super.onStart();
		media.start();

	}

	@Override
	public void onStop() {
		super.onStop();
		media.stop();

	}
}
