package ntu.im.colorfull;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import android.os.AsyncTask;
import android.os.Bundle;
import android.app.Activity;
import android.app.FragmentManager;
import android.util.Log;
import android.view.Menu;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import android.app.Fragment;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v4.app.FragmentActivity;
import android.support.v13.app.FragmentStatePagerAdapter;
import android.support.v4.app.NavUtils;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;

public class ItemSelectedActivity extends FragmentActivity 
{
	
	private ViewPager itemSelectedPager;
	private selectedPagerAdapter adpter;
	private String fileName;
	private String mode;
	private String TAG;
	selectedPagerAdapter adapter;
	
	private final static int selectedPagerNum = 2;

	@Override
	protected void onCreate(Bundle savedInstanceState) 
	{
		Bundle extras = getIntent().getExtras();
		mode = extras.getString(BaseGridViewFragment.MODE_KEY);
		TAG = extras.getString(BaseGridViewFragment.TAG_KEY);
		fileName = extras.getString(BaseGridViewFragment.FILE_NAME_KEY);
		
		//Toast.makeText(getApplication(), TAG, Toast.LENGTH_LONG).show();
		
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_item_selected);
		
		itemSelectedPager = (ViewPager) findViewById(R.id.itemSelectedActivityPager);
		adapter = new selectedPagerAdapter(getFragmentManager(), mode, fileName);
		itemSelectedPager.setAdapter(adapter);
		if(TAG.equals(BaseGridViewFragment.MODIFIED))
			itemSelectedPager.setCurrentItem(1);
		
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) 
	{
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.item_selected, menu);
		return true;
	}
	
	private class selectedPagerAdapter extends FragmentStatePagerAdapter
	{
		private String fileName;

		public selectedPagerAdapter(FragmentManager fm, String mode, String fileName) {
			super(fm);
			this.fileName = fileName;
			// TODO Auto-generated constructor stub
		}

		@Override
		public Fragment getItem(int arg0) {
			// TODO Auto-generated method stub
			SelectedImageViewFragment frag = SelectedImageViewFragment.create(arg0, mode, TAG, fileName);			
			return frag;
			
		}

		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return selectedPagerNum;
		}
		
	}

}
