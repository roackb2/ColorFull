package ntu.im.colorfull;

import java.io.File;

import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.*;
import android.app.FragmentManager;
import android.support.v4.app.FragmentActivity;
import android.support.v13.app.FragmentStatePagerAdapter;
import android.support.v4.app.NavUtils;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;

public class ViewPhotoActivity extends FragmentActivity
{
	final static String SOURCE_KEY = "sourceKey";
	final static String NEW_PHOTO = "newPhoto";
	final static String VIEW_PHOTO = "viewPhoto";
	private String SOURCE;
	private Intent intent;
	private ViewPager viewPhotoPager;
	private viewPhotoPagerAdapter adapter;
	private final static int pageNum = 2;
	String localPhotoUrl;
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) 
	{
		intent = this.getIntent();
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
		setContentView(R.layout.activity_view_photo);
		localPhotoUrl = new String();
		SOURCE = new String();
		localPhotoUrl = intent.getExtras().getString(CameraActivity.ORIGIN_PHOTO_LOCAL_URL);
		SOURCE = intent.getExtras().getString(SOURCE_KEY);		
		viewPhotoPager = (ViewPager) findViewById(R.id.ViewPhotoAcitivityPager);	
		adapter = new viewPhotoPagerAdapter(getFragmentManager(), localPhotoUrl, SOURCE);			
		viewPhotoPager.setAdapter(adapter);
	}
	
	protected class viewPhotoPagerAdapter extends FragmentStatePagerAdapter
	{
		private String originPhotoLoaclUrl;
		private String source;
		
		public viewPhotoPagerAdapter(FragmentManager fm, String url, String source) 
		{
			super(fm);
			originPhotoLoaclUrl = url;
			this.source = source;
			// TODO Auto-generated constructor stub
		}

		@Override
		public Fragment getItem(int arg0) 
		{
			// TODO Auto-generated method stub
			return ViewPhotoFragment.create(arg0, originPhotoLoaclUrl, source);
		}

		@Override
		public int getCount() 
		{
			// TODO Auto-generated method stub
			return pageNum;
		}
		
	}
	
	

}


