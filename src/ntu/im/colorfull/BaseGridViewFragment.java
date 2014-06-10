package ntu.im.colorfull;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

import org.apache.http.HttpResponse;
import org.apache.http.ParseException;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;

import android.os.AsyncTask;
import android.os.Bundle;
import android.app.Fragment;
import android.app.FragmentManager;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import android.support.v4.app.FragmentActivity;
import android.support.v13.app.FragmentStatePagerAdapter;
import android.support.v4.app.NavUtils;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;


public class BaseGridViewFragment extends Fragment 
{

	private GridView baseGridView;
	
	//TAG for original or modified;
	final static String TAG_KEY = "tag";
	final static String ORIGINAL = "original";
	final static String MODIFIED = "modified";
	final static String FILE_NAME_KEY = "fileName";
	private String TAG;
	//mode for cloudPhotos or onlineGallery 
	final static String MODE_KEY = "mode";
	private String mode;

	final static String ID_KEY = "id";
	private String userId;
	final static String photoNamesUrl = "http://210.61.27.43/picturePair/getFileNames.php";
	private String thumbnailsUrl;
	
	private String[] photoNames;
	
	private ViewGroup rootView;
	private ProgressBar prgBar;
	private TextView downloadDescript;
	
	private BaseGridViewAdapter viewAdapter;
	
	private ViewPager selectedViewPager;

	private AdapterView.OnItemClickListener photoClickListener;
	
	private int GridViewPosition;
	
	public static BaseGridViewFragment create(int position, String mode)
	{
		
		BaseGridViewFragment frag = new BaseGridViewFragment();
		Bundle args = new Bundle();
		args.putInt(TAG_KEY, position);
		args.putString(MODE_KEY, mode);
		frag.setArguments(args);
		return frag;
	}
	
	public BaseGridViewFragment()
	{
		
	}
	
	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		thumbnailsUrl = new String(); 
		userId = new String();
		mode = new String();
		TAG = new String();
		int position = getArguments().getInt(TAG_KEY);
		mode = getArguments().getString(MODE_KEY);
		//Toast.makeText(getActivity(), mode, Toast.LENGTH_LONG).show(); //for test
		if(position == 0)
			TAG = ORIGINAL;
		if(position == 1)
			TAG = MODIFIED;
		if(mode.equals(MainActivity.CLOUD_PHOTOS))
		{
			userId = MainActivity.getUserId();
			if(TAG.equals(ORIGINAL))
			{
				thumbnailsUrl = "http://210.61.27.43/picturePair/" + userId + "/originalImages/thumbnails/";
			
			}
			if(TAG.equals(MODIFIED))
			{
				thumbnailsUrl = "http://210.61.27.43/picturePair/" + userId +  "/modifiedImages/thumbnails/";
			}
			
			
		}
		if(mode.equals(MainActivity.ONLINE_GALLERY))
		{
			if(TAG.equals(ORIGINAL))
			{
				thumbnailsUrl = "http://210.61.27.43/picturePair/gallery/originalImages/thumbnails/";
			}
			if(TAG.equals(MODIFIED))
			{
				thumbnailsUrl = "http://210.61.27.43/picturePair/gallery/modifiedImages/thumbnails/";
			}
		}
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
	{
		rootView = (ViewGroup) inflater.inflate(R.layout.base_frag_gridview, container, false);
		baseGridView = (GridView) rootView.findViewById(R.id.baseFragGridView);
		prgBar = (ProgressBar) rootView.findViewById(R.id.BaseFragGridViewProgressBar);
		downloadDescript = (TextView) rootView.findViewById(R.id.BaseFragGridViewProgressBarDescript);
		
		
		//downloadDescript.setText(photoNamesUrl + ", " + mode + ", " + TAG + ", " + thumbnailsUrl); //for test

		
		return rootView;
	}
	
	@Override
	public void onStart()
	{
		super.onStart();
		
		new setupComponentsAsync().execute(photoNamesUrl, mode, TAG);
		
		baseGridView.setVisibility(GridView.INVISIBLE);
		prgBar.setVisibility(ProgressBar.VISIBLE);
		downloadDescript.setVisibility(TextView.VISIBLE);
		
	}
	

	
	private class setupComponentsAsync extends AsyncTask<String, Void, ArrayList<String>>
	{

		@Override
		protected ArrayList<String> doInBackground(String... params) 
		{
			// TODO Auto-generated method stub
			String url = params[0];
			String mode = params[1];
			String tag = params[2];
			String userId = "0";
			String response = null;
			HttpResponse postResponse = null;

			ArrayList<String> resultList = new ArrayList<String>();
			
			HttpPost postRequest = new HttpPost(url);
			MultipartEntityBuilder multipartEntity = MultipartEntityBuilder.create();
			
			HttpClient client = new DefaultHttpClient();
			//multipartEntity.addTextBody(KEY_ID, userId);
			if(mode.equals(MainActivity.CLOUD_PHOTOS))
			{
				userId = MainActivity.getUserId();			
			}
			
			multipartEntity.addTextBody(ID_KEY, userId);
			multipartEntity.addTextBody(MODE_KEY, mode);
			multipartEntity.addTextBody(TAG_KEY, tag);

			postRequest.setEntity(multipartEntity.build());
			
			
			try {
				postResponse = client.execute(postRequest);
				response = EntityUtils.toString(postResponse.getEntity());
			} catch (ClientProtocolException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}	
			
			String[] result = response.split(":");

			for(String word : result)
			{
				resultList.add(word);
			}
			resultList.remove(0);
			
			return resultList;
		}
		
	
		@Override
		protected void onPostExecute(ArrayList<String> resultList)
		{
			//Toast.makeText(getActivity(), "Async task finished", Toast.LENGTH_LONG).show();

			
			photoNames = new String[resultList.size()];
			photoNames = resultList.toArray(photoNames);
			
			if(photoNames.length == 0)
				Toast.makeText(getActivity(), "No photo exists", Toast.LENGTH_LONG).show();
			
			photoClickListener = new AdapterView.OnItemClickListener() 
			{

				@Override
				public void onItemClick(AdapterView<?> parent, View view,
						int position, long id) 
				{
					baseGridView.setVisibility(ViewPager.GONE);
					// TODO Auto-generated method stub
					selectedViewPager = (ViewPager) rootView.findViewById(R.id.selectedViewPagerr);
					String fileName = photoNames[position];
					
					Intent intent = new Intent(rootView.getContext(), ItemSelectedActivity.class);
					Bundle extras = new Bundle();
					extras.putString(FILE_NAME_KEY, fileName);
					extras.putString(MODE_KEY, mode);
					extras.putString(TAG_KEY, TAG);
					intent.putExtras(extras);
					startActivity(intent);
					
					//selectedAdapter = new selectedPagerAdapter(getFragmentManager(), mode, fileName);
					
					//downloadDescript.setText(selectedAdapter.toString());
					
					//selectedViewPager.setAdapter(selectedAdapter);
					//GridViewPosition = position;
					
					//rootView.requestDisallowInterceptTouchEvent(true);
				}
			};
						
			viewAdapter = new BaseGridViewAdapter(rootView.getContext(), thumbnailsUrl);
			viewAdapter.setPhotos(photoNames);
			baseGridView.setAdapter(viewAdapter);
			baseGridView.setOnItemClickListener(photoClickListener);
			
			baseGridView.setVisibility(GridView.VISIBLE);
			prgBar.setVisibility(ProgressBar.INVISIBLE);
			//downloadDescript.setText(thumbnailsUrl + " end");
			downloadDescript.setVisibility(TextView.INVISIBLE);
			

		}
	}
	


}
