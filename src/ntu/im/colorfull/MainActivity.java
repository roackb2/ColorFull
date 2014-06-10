package ntu.im.colorfull;


import java.io.File;

import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends Activity 
{

	private String encodedImage;
	
	//Thread related variables
	
	private ImageButton useCamera;
	private ImageButton viewPhoto;
	private ImageButton cloudPhotos;
	private ImageButton onlineGallery;
	private ImageButton register;
	private ImageButton logout;
	private TextView accountMessage;
	
	private final static String loggingStatusKey = "logginStatus";
	private final static String userIdKey = "userId";
	private static boolean isLoggedIn = false;
	private static String userId = null;
	
	final static String MODE_KEY = "key";
	final static String CLOUD_PHOTOS = "cloudPhotos";
	final static String ONLINE_GALLERY = "onlineGallery";
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) 
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		useCamera = (ImageButton) findViewById(R.id.useCamera);
		viewPhoto = (ImageButton) findViewById(R.id.viewPhoto);
		cloudPhotos = (ImageButton) findViewById(R.id.cloudPhotos);
		onlineGallery = (ImageButton) findViewById(R.id.onlineGallery);
		register = (ImageButton) findViewById(R.id.register);
		logout = (ImageButton) findViewById(R.id.logOut);
		accountMessage = (TextView) findViewById(R.id.accountMessage);
		
		
		if(savedInstanceState != null)
		{
			isLoggedIn = savedInstanceState.getBoolean(loggingStatusKey);
			userId = savedInstanceState.getString(userIdKey);
		}
		
		
		
		useCamera.setOnClickListener(new OnClickListener()
		{

			@Override
			public void onClick(View v) 
			{
				// TODO Auto-generated method stub
				if(isLoggedIn)
				{
					if(isNetworkAvailable())
					{
						Intent it = new Intent(MainActivity.this, CameraActivity.class);
						startActivity(it);
					}
					else 
						Toast.makeText(MainActivity.this, "Network not available", Toast.LENGTH_LONG).show();
					
				}
				else
					Toast.makeText(getApplication(), "You havn't log in, please log in first.", Toast.LENGTH_LONG).show();
			}
		});
		
		viewPhoto.setOnClickListener(new OnClickListener()
		{

			@Override
			public void onClick(View v) 
			{
				if(isLoggedIn)
				{
					// TODO Auto-generated method stub
					File path = (Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM));
					File originPhoto = new File(path,"photo.jpg");
					File modifiedPhoto = new File(path, "colorblindphoto.jpg");
					if(originPhoto.exists() && modifiedPhoto.exists())
					{
						Intent it = new Intent(MainActivity.this, ViewPhotoActivity.class);
						String photoUrl = originPhoto.getAbsolutePath();
						Bundle bundle = new Bundle();
						bundle.putString(CameraActivity.ORIGIN_PHOTO_LOCAL_URL, photoUrl);
						bundle.putString(ViewPhotoActivity.SOURCE_KEY, ViewPhotoActivity.VIEW_PHOTO);
						it.putExtras(bundle);
						startActivity(it);
					}
					else
					{
						Toast.makeText(MainActivity.this, "Sorry, no original or modified photo exists, please take a picture first", Toast.LENGTH_LONG).show();
					}
				}
				else
					Toast.makeText(getApplication(), "You havn't log in, please log in first.", Toast.LENGTH_LONG).show();
			}
			
		});
		
		cloudPhotos.setOnClickListener(new OnClickListener()
		{

			@Override
			public void onClick(View v) 
			{
				if(isLoggedIn)
				{
					if(isNetworkAvailable())
					{
						// TODO Auto-generated method stub
						Intent it = new Intent(MainActivity.this, ViewServerPhotosActivity.class);
						Bundle extras = new Bundle();
						extras.putString(MODE_KEY, CLOUD_PHOTOS);
						it.putExtras(extras);
						startActivity(it);		
					}
					else
						Toast.makeText(MainActivity.this, "Network not available", Toast.LENGTH_LONG).show();
				}
				else
					Toast.makeText(getApplication(), "You havn't log in, please log in first.", Toast.LENGTH_LONG).show();

			}
		});
		
		onlineGallery.setOnClickListener(new OnClickListener()
		{

			@Override
			public void onClick(View v) 
			{
				if(isNetworkAvailable())
				{
					// TODO Auto-generated method stub
					Intent it = new Intent(MainActivity.this, ViewServerPhotosActivity.class);
					Bundle extras = new Bundle();
					extras.putString(MODE_KEY, ONLINE_GALLERY);
					it.putExtras(extras);
					startActivity(it);
				}
				else
					Toast.makeText(MainActivity.this, "Network not available", Toast.LENGTH_LONG).show();
				
			}
		});
		
		register.setOnClickListener(new OnClickListener()
		{

			@Override
			public void onClick(View v) 
			{
				// TODO Auto-generated method stub
				Intent it = new Intent(MainActivity.this, LoginActivity.class);
				startActivity(it);
			}
		});
		
		logout.setOnClickListener(new OnClickListener()
		{

			@Override
			public void onClick(View v) 
			{
				// TODO Auto-generated method stub
				if(isLoggedIn)
				{
					logout();
					userId = null;
					Toast.makeText(MainActivity.this, "Logged out", Toast.LENGTH_LONG).show();
					accountMessage.setText("Please log in");
				}
				else
				{
					Toast.makeText(MainActivity.this, "You havn't log in", Toast.LENGTH_LONG).show();
				}
			
			}
		});
	}
	

	@Override 
	public void onResume()
	{
		super.onResume();
		
		if(isLoggedIn)
			accountMessage.setText("Welcome, " + userId);
	}
	

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}
	
	@Override
	public void onSaveInstanceState(Bundle outState)
	{
		super.onSaveInstanceState(outState);
		
		outState.putBoolean(loggingStatusKey, isLoggedIn);
		outState.putString(userIdKey, userId);
	}
	
	public static boolean getLoggingStatus()
	{
		return isLoggedIn;
	}
	
	
	public static void login()
	{
		isLoggedIn = true;
	}
	
	public static void logout()
	{
		isLoggedIn = false;
	}

	
	public static void setUserId(String id)
	{
		userId = id;
	}
	
	public static String getUserId()
	{
		return userId;
	}
	
	public boolean isNetworkAvailable()
	{
		ConnectivityManager netManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo netInfo = netManager.getActiveNetworkInfo();
		
		return netInfo != null && netInfo.isConnected();
	}
}
