package ntu.im.colorfull;

import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.commons.codec.binary.Base64;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;

import android.app.Fragment;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.FragmentActivity;
import android.support.v13.app.FragmentStatePagerAdapter;
import android.support.v4.app.NavUtils;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;


public class ViewPhotoFragment extends Fragment
{
	final static String POSITION_KEY = "position";
	final static String URL_KEY = "url";
	final static String ENTITY_KEY_FILE_NAME = "fileName";
	final static String COLORBLIND_PHOTO_NAME = "colorblindphoto.jpg";
	public String ENTITY_KEY_DATA = "data";
	public String ENTITY_KEY_ID = "id";
	private String TAG;
	private String SOURCE;
	final static String ORIGINAL = "original";
	final static String MODIFIED = "modified";
	
	
	private ViewGroup rootView;
	private ProgressBar prgBar;
	private TextView descript;
	private ImageView photoView;
	private String originPhotoLocalUrl;
	private String modifiedPhotoLocalUrl;
	
	public static ViewPhotoFragment create(int position, String photoUrl, String source)
	{
		ViewPhotoFragment frag = new ViewPhotoFragment();
		Bundle args = new Bundle();
		args.putInt(POSITION_KEY, position);
		args.putString(URL_KEY, photoUrl);
		args.putString(ViewPhotoActivity.SOURCE_KEY, source);
		frag.setArguments(args);
		return frag;
	}
	
	
	public ViewPhotoFragment()
	{

	}
	
	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		int position = getArguments().getInt(POSITION_KEY);
		originPhotoLocalUrl = getArguments().getString(URL_KEY);
		SOURCE = getArguments().getString(ViewPhotoActivity.SOURCE_KEY);
		if(position == 0)
			TAG = ORIGINAL;
		if(position == 1)
			TAG = MODIFIED;
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
	{
		rootView = (ViewGroup) inflater.inflate(R.layout.selected_frag_imageview, container, false);
		photoView = (ImageView) rootView.findViewById(R.id.selectedFragImageView);
		prgBar = (ProgressBar) rootView.findViewById(R.id.selectedViewProgressBar);
		descript = (TextView) rootView.findViewById(R.id.selectedViewProgressBarDescript);
		
		photoView.setVisibility(ImageView.INVISIBLE);
		prgBar.setVisibility(ProgressBar.VISIBLE);
		descript.setVisibility(TextView.VISIBLE);
		
		if(SOURCE.equals(ViewPhotoActivity.NEW_PHOTO))
		{
			if(TAG.equals(ORIGINAL))
			{
				Bitmap bitmap = BitmapFactory.decodeFile(originPhotoLocalUrl);
				photoView.setImageBitmap(bitmap);
				photoView.setVisibility(ImageView.VISIBLE);
				prgBar.setVisibility(ProgressBar.INVISIBLE);
				descript.setVisibility(TextView.INVISIBLE);
			}
			if(TAG.equals(MODIFIED))
			{
				new UpTransDownAsync().execute(originPhotoLocalUrl);
			}
		}
		if(SOURCE.equals(ViewPhotoActivity.VIEW_PHOTO))
		{
			File path = (Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM));
			File photo = new File(path, COLORBLIND_PHOTO_NAME);
			modifiedPhotoLocalUrl = photo.getAbsolutePath();
			
			if(TAG.equals(ORIGINAL))
			{
				Bitmap bitmap = BitmapFactory.decodeFile(originPhotoLocalUrl);
				photoView.setImageBitmap(bitmap);

			}
			if(TAG.equals(MODIFIED))
			{
				Bitmap bitmap = BitmapFactory.decodeFile(modifiedPhotoLocalUrl);
				photoView.setImageBitmap(bitmap);
			}
			photoView.setVisibility(ImageView.VISIBLE);
			prgBar.setVisibility(ProgressBar.INVISIBLE);
			descript.setVisibility(TextView.INVISIBLE);
		}
				
		return rootView;
	}
	
	@Override
	public void onStart()
	{

		super.onStart();
	}
	
	protected class UpTransDownAsync extends AsyncTask<String, Void, String>
	{
	
		@Override
		protected String doInBackground(String... params) 
		{
			// TODO Auto-generated method stub
			String uploadUrl = "http://210.61.27.43/picturePair/uploadPhoto.php";
			String origPhotoLocUrl = params[0];			
			Bitmap originalBm = BitmapFactory.decodeFile(origPhotoLocUrl);
			String originalEncodedString = null;
			String originalPhotoName = new String();
			String modifiedEncodedString = new String();
			String userId = MainActivity.getUserId();
			String modifiedPhotoLocalUrl = new String();
			
			try
			{
				ByteArrayOutputStream BAO = new ByteArrayOutputStream();
				originalBm.compress(Bitmap.CompressFormat.JPEG, 100, BAO);
				byte[] imageBytes = BAO.toByteArray();
				originalEncodedString = new String(Base64.encodeBase64(imageBytes));
			}
			catch(NullPointerException e)
			{
				e.printStackTrace();
			}
			
			Date date = new Date();
			DateFormat sdf = new SimpleDateFormat("yyyy-MM-dd_HH.mm.SS");
			try
			{
				originalPhotoName = sdf.format(date);
			}
			catch(Exception e)
			{
				e.printStackTrace();
			}
			
			HttpPost postRequest = new HttpPost(uploadUrl);
			HttpClient client = new DefaultHttpClient();
			MultipartEntityBuilder requestEntity = MultipartEntityBuilder.create();
			requestEntity.addTextBody(ENTITY_KEY_FILE_NAME, originalPhotoName);
			requestEntity.addTextBody(ENTITY_KEY_DATA, originalEncodedString);
			requestEntity.addTextBody(ENTITY_KEY_ID, userId);
			postRequest.setEntity(requestEntity.build());
			
			try 
			{
				HttpResponse postResponse = client.execute(postRequest);
				modifiedEncodedString = EntityUtils.toString(postResponse.getEntity());
				byte[] decodedBytes = android.util.Base64.decode(modifiedEncodedString, 0);
    			Bitmap modifiedPhotoBitmap = BitmapFactory.decodeByteArray(decodedBytes, 0, decodedBytes.length);

    			File path = (Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM));
    			File photo = new File(path, COLORBLIND_PHOTO_NAME);
    			
				path.mkdirs();
				modifiedPhotoLocalUrl = photo.getAbsolutePath();
				OutputStream outStream = new BufferedOutputStream(new FileOutputStream(photo));
				
				modifiedPhotoBitmap.compress(Bitmap.CompressFormat.JPEG, 100, outStream);
				outStream.flush();
				outStream.close();
				
				MediaScannerConnection.scanFile(getActivity(), new String[] { photo.toString() }, null,
		                new MediaScannerConnection.OnScanCompletedListener() {
		            public void onScanCompleted(String path, Uri uri) {
		                Log.i("ExternalStorage", "Scanned " + path + ":");
		                Log.i("ExternalStorage", "-> uri=" + uri);
		            }
				});
   						
			} catch (ClientProtocolException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			return modifiedPhotoLocalUrl;
		}
		
		@Override
		protected void onPostExecute(String modifiedLocalUrl)
		{
			modifiedPhotoLocalUrl = modifiedLocalUrl;
				
			Bitmap bitmap = BitmapFactory.decodeFile(modifiedPhotoLocalUrl);
			photoView.setImageBitmap(bitmap);
			photoView.setVisibility(ImageView.VISIBLE);
			prgBar.setVisibility(ProgressBar.INVISIBLE);
			descript.setVisibility(TextView.INVISIBLE);
		}
	}
	
}
