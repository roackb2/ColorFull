package ntu.im.colorfull;

import android.app.Fragment;
import android.app.FragmentManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.widget.ImageSwitcher;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ViewSwitcher.ViewFactory;
import android.support.v4.app.FragmentActivity;
import android.support.v13.app.FragmentStatePagerAdapter;
import android.support.v4.app.NavUtils;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;

public class ViewServerPhotosActivity extends FragmentActivity implements ViewFactory
{
	private Intent intent;
	private String mode;
	
	private ViewPager basePager;
	private basePagerAdapter adapter;
	private final static int pageNumber = 2; 
	

	@Override
	protected void onCreate(Bundle savedInstanceState) 
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_view_server_photos);
		
		intent = getIntent();
		Bundle extras = intent.getExtras();
		mode = extras.getString(MainActivity.MODE_KEY);
		//Toast.makeText(this, mode, Toast.LENGTH_LONG).show(); //for test
 		
		basePager = (ViewPager) findViewById(R.id.viewServerPhotoPager);
		
		adapter = new basePagerAdapter(getFragmentManager());
		basePager.setAdapter(adapter);
		basePager.setOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener()
		{
			@Override
			public void onPageSelected(int position)
			{
				
			}
		});
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) 
	{
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.online_gallery, menu);
		return true;
	}

	@Override
	public View makeView() 
	{
		// TODO Auto-generated method stub
		
		ImageView selectedItemView = new ImageView(this);
		selectedItemView.setBackgroundColor(0xFF000000);
		selectedItemView.setScaleType(ImageView.ScaleType.FIT_CENTER);
		
		// TODO setup the switcher
		selectedItemView.setLayoutParams(new ImageSwitcher.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
			
		return selectedItemView;
	}
	
	private class basePagerAdapter extends FragmentStatePagerAdapter
	{
		

		public basePagerAdapter(FragmentManager fm) 
		{
			super(fm);
			// TODO Auto-generated constructor stub
		}

		@Override
		public Fragment getItem(int arg0) {
			// TODO Auto-generated method stub
			BaseGridViewFragment frag = BaseGridViewFragment.create(arg0, mode);
			return frag;
		}

		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return pageNumber;
		}
		
	}

}
