package ntu.im.colorfull;

import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.ImageFormat;
import android.graphics.Matrix;
import android.graphics.PixelFormat;
import android.graphics.Rect;
import android.graphics.YuvImage;
import android.hardware.Camera;
import android.hardware.Camera.AutoFocusCallback;
import android.hardware.Camera.PictureCallback;
import android.hardware.Camera.ShutterCallback;
import android.hardware.Camera.Size;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.util.Log;
import android.view.Display;
import android.view.Menu;
import android.view.MenuItem;
import android.view.Surface;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.RelativeLayout.LayoutParams;
import android.widget.Toast;



public class CameraActivity extends Activity implements SurfaceHolder.Callback
{	
	
	
	private SurfaceView camPreviewSurface;
	private SurfaceHolder previewHolder;
	private ImageButton takePicBtn;
	
	private Camera mCam;
	private AutoFocusCallback onStartAutoFocus;
	private AutoFocusCallback takePhotoAutoFocus;
	private boolean previewing = false;
	
	private Bitmap takenPicBm;
	
	private int degree = 0;
	private int result = 0;
	
	File path = null;
	File photo =  null;
	final static String ORIGIN_PHOTO_LOCAL_URL = "photoUri";
	private static String photoUri = null;
	
	private Intent intent;
	
	@SuppressWarnings("deprecation")
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		
		super.onCreate(savedInstanceState);
		
		//handler = new Handler();
		
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
	
		setContentView(R.layout.activity_camera);
		
		takePicBtn = (ImageButton) findViewById(R.id.takePicBtn);
		takePicBtn.setOnClickListener(new takePicBtnListener());
		
		InitializeActivity();
		getWindow().setFormat(PixelFormat.TRANSLUCENT);
		camPreviewSurface = (SurfaceView) findViewById(R.id.camPreviewSurface);
		previewHolder = camPreviewSurface.getHolder();
		previewHolder.addCallback(this);
		//TODO set the directory
		
							
		if(Build.VERSION.SDK_INT < Build.VERSION_CODES.HONEYCOMB)
			previewHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
		
		onStartAutoFocus = new AutoFocusCallback()
		{
			@Override
			public void onAutoFocus(boolean success, Camera camera) {
				// TODO Auto-generated method stub
				
			}
		};
		
		takePhotoAutoFocus = new AutoFocusCallback()
		{

			@Override
			public void onAutoFocus(boolean success, Camera camera) {
				// TODO Auto-generated method stub
				if(success)
					mCam.takePicture(shutterCallback, rawPicCallback, jpegPicCallback);
			}			
		};

	}
	
	@Override
	protected void onStart()
	{
		super.onStart();
	}
	
	@Override
	protected void onResume()
	{
		super.onResume();
	}
	
	@Override
	protected void onPause()
	{
		super.onPause();
	}
	
	
	/*
	@Override
	protected void onStop()
	{
		super.onStop();
	}
	*/
	@Override
	protected void onDestroy()
	{
		super.onDestroy();
	}
	
	public void InitializeActivity()
	{
		intent = getIntent();
	}
	


	@Override
	public void surfaceChanged(SurfaceHolder holder, int format, int width,
			int height) 
	{

		  if(previewing)
		  {
			  mCam.stopPreview();
			  previewing = false;
		  }
		  
		  try 
		  {
			  mCam.setPreviewDisplay(holder);
			  mCam.startPreview();
			  previewing = true;
			  
			  mCam.autoFocus(onStartAutoFocus);
		  } 
		  catch (IOException e) 
		  {
			  
			  e.printStackTrace();
		  }
	}

	@SuppressLint("NewApi")
	@Override
	public void surfaceCreated(SurfaceHolder holder) {
		
		mCam = Camera.open();
		try
		{
			Camera.CameraInfo camInfo = new Camera.CameraInfo();
			Camera.getCameraInfo(0, camInfo);
			
			int rotation = CameraActivity.this.getWindowManager().getDefaultDisplay().getRotation();
			
			
			switch(rotation)
			{
			case Surface.ROTATION_0:
				degree = 0;
				break;
			case Surface.ROTATION_90:
				degree = 90;
				break;
			case Surface.ROTATION_180:
				degree = 180;
				break;
			case Surface.ROTATION_270:
				degree = 270;
				break;	
				
			}
			
			result = (camInfo.orientation - degree + 360) % 360;
			mCam.setDisplayOrientation(result);
			
			Camera.Parameters camParam = mCam.getParameters();
			List<Size> previewSizes = camParam.getSupportedPreviewSizes();
			camParam.setPictureSize(previewSizes.get(0).width, previewSizes.get(0).height);
			camParam.setPreviewSize(previewSizes.get(0).width, previewSizes.get(0).height);

			mCam.setParameters(camParam);
			

		}
		catch(Exception e)
		{
			Toast.makeText(CameraActivity.this, "Fail to open the camera!", Toast.LENGTH_LONG).show();
		}
		
	}

	@Override
	public void surfaceDestroyed(SurfaceHolder holder) {
		
		
		if(previewing)
		{
			mCam.stopPreview();		
			previewing = false;
		}
		if(mCam != null)
		{
			mCam.release();
			mCam = null;
		}		
	}
	
	
	Camera.ShutterCallback shutterCallback = new Camera.ShutterCallback() 
	{
		
		@Override
		public void onShutter() {
			
			
		}
	};
	
	Camera.PictureCallback rawPicCallback = new Camera.PictureCallback() 
	{
		
		@Override
		public void onPictureTaken(byte[] data, Camera camera) {
			// TODO Auto-generated method stub
			
		}
	};
	
	Camera.PictureCallback jpegPicCallback = new Camera.PictureCallback() 
	{
		
		@Override
		public void onPictureTaken(byte[] data, Camera camera) {
			// TODO Auto-generated method stub
			 
			Bitmap takenPicBm = BitmapFactory.decodeByteArray(data, 0, data.length);
			Bitmap resultBm;
			
			
			int w, h;
			Matrix m;
	
			w = takenPicBm.getWidth();
			h = takenPicBm.getHeight();
			m = new Matrix();
			m.preRotate(result);
			
			resultBm = Bitmap.createBitmap(takenPicBm, 0, 0, w, h, m, false);


			path = (Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM));
			photo = new File(path, "photo.jpg");
			try
			{
				path.mkdirs();
				photoUri = photo.getAbsolutePath();
				OutputStream outStream = new BufferedOutputStream(new FileOutputStream(photo));
				
				resultBm.compress(Bitmap.CompressFormat.JPEG, 100, outStream);
				outStream.flush();
				outStream.close();
				
				MediaScannerConnection.scanFile(CameraActivity.this, new String[] { photo.toString() }, null,
		                new MediaScannerConnection.OnScanCompletedListener() {
		            public void onScanCompleted(String path, Uri uri) {
		                Log.i("ExternalStorage", "Scanned " + path + ":");
		                Log.i("ExternalStorage", "-> uri=" + uri);
		            }
				});
			}
			catch(IOException e)
			{
				e.printStackTrace();
			}
						
			 Intent it = new Intent(CameraActivity.this, ViewPhotoActivity.class);
			 Bundle bundle = new Bundle();
			
			 bundle.putString(ORIGIN_PHOTO_LOCAL_URL, photoUri);
			 bundle.putString(ViewPhotoActivity.SOURCE_KEY, ViewPhotoActivity.NEW_PHOTO);
			 it.putExtras(bundle);
			 startActivity(it);
			 finish();
		}
	};
	
	public class takePicBtnListener implements OnClickListener
	{
		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			mCam.autoFocus(takePhotoAutoFocus);  
		}
	}
	public static String getPhotoUri()
	{
		return photoUri;
	}
	
	
}


