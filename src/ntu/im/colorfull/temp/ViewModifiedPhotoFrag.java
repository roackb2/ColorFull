/*
package ntu.im.colorfull.temp;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Fragment;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.SurfaceHolder;
import android.view.SurfaceHolder.Callback;
import android.view.SurfaceView;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.*;


@SuppressLint("NewApi")
public class ViewModifiedPhotoFrag extends Fragment
{

	
	private ImageView modifiedView;
	private ProgressBar progressbar;
	private TextView progressbarDescript;
	private Button viewOriginPhotoBtn;
	
	//private TextView errorText;
	private Bitmap originPhotoBitmap;
	private Bitmap modifiedPhotoBitmap;
	private UIHandler handler;
	public static String modifiedPhotoUri = null;
	
	private viewOriginPhotoInterface viewOriginPhotoIF;
	
	@Override
	public void onAttach(Activity activity)
	{
		super.onAttach(activity);
		try
		{
			viewOriginPhotoIF = (viewOriginPhotoInterface) activity;
		}
		catch(ClassCastException e)
		{
			throw new ClassCastException(activity.toString() + "must implement viewOriginPhotoInterface");
		}
		
	}
		
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
	{
		View rootView = inflater.inflate(R.layout.modified_photo_view, container, false);
	//	viewPhotoSurface = (ViewPhotoSurface) rootView.findViewById(R.id.originPhotoView);
	//	photoSurfaceHolder = viewPhotoSurface.obtainHolder();
	//	testView = (ImageView) rootView.findViewById(R.id.testview);
	//	testView.setBackgroundColor(Color.rgb(0, 0, 0));
		modifiedView = (ImageView) rootView.findViewById(R.id.modifiedView);
		//modifiedView.setBackgroundColor(Color.rgb(0, 0, 0));
		progressbar = (ProgressBar) rootView.findViewById(R.id.downloadPrgBar);
		progressbarDescript = (TextView) rootView.findViewById(R.id.donloadDescript);
		viewOriginPhotoBtn = (Button) rootView.findViewById(R.id.viewOriginPhotoButton);
		//errorText = (TextView) rootView.findViewById(R.id.errtxt);
		handler = new UIHandler();
		
		viewOriginPhotoBtn.setOnClickListener(new OnClickListener()
		{

			@Override
			public void onClick(View v) 
			{
				// TODO Auto-generated method stub
				viewOriginPhotoIF.viewOriginPhoto();
			}
		});
		return rootView;
		
	}
	
	@Override
	public void onResume()
	{
		super.onResume();
		
		
	}
	
	@Override
	public void onActivityCreated(Bundle savedInstanceState)
	{
		super.onActivityCreated(savedInstanceState);
		
		if(this.getArguments() == null)
		{
			modifiedPhotoBitmap = BitmapFactory.decodeFile(modifiedPhotoUri);
			modifiedView.setImageBitmap(modifiedPhotoBitmap);
			progressbar.setVisibility(View.GONE);
    		progressbarDescript.setVisibility(View.GONE);
		}	
		else
		{
			Bundle extras = this.getArguments();
			String url = extras.getString(CameraActivity.photoBundleKey);
			if(url != null)
			{
				File file = new File(url);
				if(file != null)
				{
					originPhotoBitmap = BitmapFactory.decodeFile(url);
					UpTransDown UTD = new UpTransDown(handler, CameraActivity.getPhotoUri());
					UTD.upload();
				}
				else
					Toast.makeText(getActivity(), "No image exists", Toast.LENGTH_LONG).show();			
			}
			else
				Toast.makeText(getActivity(), "No image exists", Toast.LENGTH_LONG).show();	
		}
		
		
	}
	
	public class UIHandler extends Handler
	{
    	public void handleMessage(Message msg)
    	{
    		switch(msg.what)
    		{
    		case ViewPhotoActivity.UploadSuccess:
 
    			break;
    		case ViewPhotoActivity.UploadFailure:

    			break;
    		case ViewPhotoActivity.DownloadSuccess:

    		//	loadedImgView.setImageBitmap(loadedImg.getBitmap());
    			break;
    		case ViewPhotoActivity.UTDsuccess:
    			//errorText.setText((String)msg.obj);
    			
        		progressbar.setVisibility(View.GONE);
        		progressbarDescript.setVisibility(View.GONE);
    			byte[] decodedBytes = android.util.Base64.decode((String)msg.obj, 0);
    			modifiedPhotoBitmap = BitmapFactory.decodeByteArray(decodedBytes, 0, decodedBytes.length);
    			modifiedView.setImageBitmap(modifiedPhotoBitmap);

    			File path = (Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM));
    			File photo = new File(path, "colorblindphoto.jpg");
    			
    			
    			try
    			{
    				path.mkdirs();
    				modifiedPhotoUri = photo.getAbsolutePath();
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
    			}
    			catch(IOException e)
    			{
    				Log.e("ViewModifiedPhotoFrag:", "FileStream output failure", e);
    				
    			}
    			
    			break;
    		case ViewPhotoActivity.UTDfail:
    			//Toast.makeText(ViewPhotoActivity.this,  (String)msg.obj, Toast.LENGTH_LONG).show();
    		}
    		super.handleMessage(msg);
    	}
	}
	
	
	public interface viewOriginPhotoInterface
	{
		public void viewOriginPhoto();
	}

	
	public interface getIntentContent
	{
		public void getPicture(Intent it);
		
	}

	

}

*/