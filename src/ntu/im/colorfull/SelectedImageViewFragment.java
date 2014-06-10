package ntu.im.colorfull;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import android.app.Fragment;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;




public class SelectedImageViewFragment extends Fragment
{
	private static String POSITION_KEY = "position";
	private int position;
	private static String TAG_KEY = "tag";
	private static String ORIGINAL = "original";
	private static String MODIFIED = "modified";
	private static String FILE_NAME_KEY = "fileName";
	private String fileName;
	private String MainTag;
	private String childTag;
	private String parentTag;
	//mode for cloudPhotos or onlineGallery 
	private static String MODE_KEY = "mode";
	private String mode;

	private static String ID_KEY = "id";
	private String userId;
	
	private ViewGroup rootView;
	private ImageView selectedPhotoView;
	private ProgressBar selectedViewPrgBar;
	private TextView prgBarDescript;

	public static SelectedImageViewFragment create(int position, String mode, String tag, String fileName)
	{
		SelectedImageViewFragment frag = new SelectedImageViewFragment();		
		Bundle args = new Bundle();
		args.putInt(POSITION_KEY, position);
		args.putString(MODE_KEY, mode);
		args.putString(TAG_KEY, tag);
		args.putString(FILE_NAME_KEY, fileName);
		frag.setArguments(args);
		return frag;
	}
	
	public SelectedImageViewFragment()
	{
		
	}
	
	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		String file = getArguments().getString(FILE_NAME_KEY);
		parentTag = getArguments().getString(TAG_KEY);
		int position = getArguments().getInt(POSITION_KEY);
		mode = getArguments().getString(MODE_KEY);
		if(position == 0)
			childTag = ORIGINAL;
		if(position == 1)
			childTag = MODIFIED;
		
		if(parentTag.equals(ORIGINAL))
		{
			if(childTag.equals(ORIGINAL))
			{
				fileName = file;
			}
			if(childTag.equals(MODIFIED))
			{
				fileName = "cb" + file;
			}				
		}
		if(parentTag.equals(MODIFIED))
		{
			if(childTag.equals(ORIGINAL))
			{
				fileName = file.substring(2, file.length());
			}
			if(childTag.equals(MODIFIED))
			{
				fileName = file;
			}				
		}
		
		//Toast.makeText(getActivity(), childTag, Toast.LENGTH_LONG).show();
		
	}
	
	@Override 
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
	{
		rootView = (ViewGroup) inflater.inflate(R.layout.selected_frag_imageview, container, false);
		selectedPhotoView = (ImageView) rootView.findViewById(R.id.selectedFragImageView);
		selectedViewPrgBar = (ProgressBar) rootView.findViewById(R.id.selectedViewProgressBar);
		prgBarDescript = (TextView) rootView.findViewById(R.id.selectedViewProgressBarDescript);
		
		selectedPhotoView.setVisibility(ImageView.INVISIBLE);
		
		//Toast.makeText(rootView.getContext(), fileName + ", " + mode + ", " + childTag, Toast.LENGTH_LONG).show();
		new downloadPhoto().execute(fileName, mode, childTag);
		
		return rootView;
	}
	
	private class downloadPhoto extends AsyncTask<String, Void, Bitmap>
	{

		@Override
		protected Bitmap doInBackground(String... params) 
		{
			// TODO Auto-generated method stub
			String fileName = params[0];
			String mode = params[1];
			String tag = params[2];
			String url = null;
			Bitmap bitmap = null;
			if(mode.equals(MainActivity.CLOUD_PHOTOS))
			{
				userId = MainActivity.getUserId();
				if(tag.equals(BaseGridViewFragment.ORIGINAL))
				{
					url = "http://210.61.27.43/picturePair/" + userId + "/originalImages/" + fileName;
				}
				if(tag.equals(BaseGridViewFragment.MODIFIED))
				{
					url = "http://210.61.27.43/picturePair/" + userId + "/modifiedImages/" + fileName;
				}
			}
			if(mode.equals(MainActivity.ONLINE_GALLERY))
			{
				if(tag.equals(BaseGridViewFragment.ORIGINAL))
				{
					url = "http://210.61.27.43/picturePair/gallery/originalImages/" + fileName;
				}
				if(tag.equals(BaseGridViewFragment.MODIFIED))
				{
					url = "http://210.61.27.43/picturePair/gallery/modifiedImages/" + fileName;
				}				
			}
			
			try
			{
				URL imgUrl = new URL(url);
				HttpURLConnection httpUrlConnection = (HttpURLConnection) imgUrl.openConnection();
				httpUrlConnection.connect();
				InputStream imgInputStream = httpUrlConnection.getInputStream();
				int length = (int) httpUrlConnection.getContentLength();
				int tmpLen = 512;
				int readLen = 0, desPos = 0;
				byte img[] = new byte[length];
				byte temp[] = new byte[tmpLen];
				if(length != -1)
				{
					while((readLen = imgInputStream.read(temp)) > 0)
					{
						System.arraycopy(temp, 0, img, desPos, readLen);
						desPos += readLen;
					}
					bitmap = BitmapFactory.decodeByteArray(img, 0, img.length);
					if(desPos != length)
					{
						throw new IOException("Only read " + desPos + " bytes."); 
					}
				}
				httpUrlConnection.disconnect();
			} catch (MalformedURLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
	
			return bitmap;
		}
		
		@Override
		protected void onPostExecute(Bitmap bitmap)
		{
			selectedPhotoView = (ImageView) rootView.findViewById(R.id.selectedFragImageView);
			selectedViewPrgBar = (ProgressBar) rootView.findViewById(R.id.selectedViewProgressBar);
			prgBarDescript = (TextView) rootView.findViewById(R.id.selectedViewProgressBarDescript);
			selectedPhotoView.setImageBitmap(bitmap);
			selectedPhotoView.setVisibility(ImageView.VISIBLE);
			selectedViewPrgBar.setVisibility(ProgressBar.INVISIBLE);
			prgBarDescript.setVisibility(TextView.INVISIBLE);			
		}
		
	}
	

		
}
