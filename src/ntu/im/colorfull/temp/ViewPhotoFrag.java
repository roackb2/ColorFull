/*
package ntu.im.colorfull.temp;

import java.io.File;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.SurfaceHolder;
import android.view.SurfaceHolder.Callback;
import android.view.View.OnClickListener;
import android.view.SurfaceView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;


@SuppressLint("NewApi")
public class ViewPhotoFrag extends Fragment
{
	//ImageView photoImage;
	
	//private ViewPhotoSurface viewPhotoSurface;
	private SurfaceHolder photoSurfaceHolder;
	
	private ImageView testView;
	
	private Bitmap photoBitmap;
	private String photoUri;
	private Button changePhotoBtn;
	
	private FragmentManager manager;
	
	
	private viewModifiedPhotoInterface changeViewInterface;
	
	@Override
	public void onAttach(Activity activity)
	{
		super.onAttach(activity);
		try
		{
			changeViewInterface = (viewModifiedPhotoInterface) activity;
		}
		catch(ClassCastException e)
		{
			throw new ClassCastException(activity.toString() + "must implement viewModifiedPhotoInterface");
		}
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
	{
		View rootView = inflater.inflate(R.layout.origin_photo_view, container, false);
	//	viewPhotoSurface = (ViewPhotoSurface) rootView.findViewById(R.id.originPhotoView);
	//	photoSurfaceHolder = viewPhotoSurface.obtainHolder();
		testView = (ImageView) rootView.findViewById(R.id.testview);
		testView.setBackgroundColor(Color.rgb(0, 0, 0));
		
		changePhotoBtn = (Button) rootView.findViewById(R.id.changePhotoButton);
		
		changePhotoBtn.setOnClickListener(new OnClickListener()
		{

			@Override
			public void onClick(View v) 
			{
				// TODO Auto-generated method stub
				if(MainActivity.getLoggingStatus())
					changeViewInterface.changePhoto();
				else
					Toast.makeText(getActivity(), "You havn't log in, please log in first", Toast.LENGTH_LONG).show();
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
		
		//photoImage = (ImageView) getView().findViewById(R.id.originPhotoView);
		Bundle extras = this.getArguments();
		String url = extras.getString(CameraActivity.photoBundleKey);
		if(url != null)
		{
			File file = new File(url);
			if(file != null)
			{
				photoBitmap = BitmapFactory.decodeFile(url);
				testView.setImageBitmap(photoBitmap);
			}
			else
				Toast.makeText(getActivity(), "No image exists", Toast.LENGTH_LONG).show();			
		}
		else
			Toast.makeText(getActivity(), "No image exists", Toast.LENGTH_LONG).show();	
		
		
	}
	
	


		
	public void getBitmap(Intent it)
	{
		Bundle extras =  it.getExtras();
		String url = extras.getString(CameraActivity.photoBundleKey);
		if(url != null)
		{
			File file = new File(url);
			if(file != null)
			{
				photoBitmap = BitmapFactory.decodeFile(url);
				testView.setImageBitmap(photoBitmap);
			}
			else
				Toast.makeText(getActivity(), "No image exists", Toast.LENGTH_LONG).show();			
		}
		else
			Toast.makeText(getActivity(), "No image exists", Toast.LENGTH_LONG).show();	

		//viewPhotoSurface.setPhotoBitmap(photoBitmap);
	}
	
	public interface viewModifiedPhotoInterface
	{
		public void changePhoto();
	}

}

*/