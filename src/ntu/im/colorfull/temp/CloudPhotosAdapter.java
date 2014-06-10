package ntu.im.colorfull.temp;
/*
package ntu.im.colorfull;

import java.io.File;
import java.io.FileFilter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;

public class CloudPhotosAdapter extends BaseAdapter 
{
	private Context context;
	private String userId;
	private static String originUrl, modifiedUrl;
	private String[] photoNames;
	private RequestThumbnail downloadThumbnail;
	private Bitmap photoBm = null;
		
//	private File[] photos;


	
	public CloudPhotosAdapter(Context c)
	{
		context = c;
		userId = MainActivity.getUserId();
		originUrl = "http://210.61.27.43/picturePair/" + userId + "/originalImages/thumbnails/";
		modifiedUrl = "http://210.61.27.43/picturePair/" + userId + "/modifiedImages/thumbnails/";
		
		//photoNames = new String[2];
		//photoNames[0] = "2013-12-15_22.16.85.jpg";
		//photoNames[1] = "2013-12-15_21.59.947.jpg";
		//Toast.makeText(context, photos[0].toString(), Toast.LENGTH_LONG).show();
	}
	
	public void setPhotos(String[] photos)
	{
		this.photoNames = photos;
	}

	@Override
	public int getCount() 
	{
		// TODO Auto-generated method stub
		return photoNames.length;
	}

	@Override
	public Object getItem(int position) 
	{
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public long getItemId(int position) 
	{
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) 
	{
		// TODO Auto-generated method stub
		ImageView photoView;
		
		if(convertView == null)
		{
			photoView = new ImageView(context);
			photoView.setLayoutParams(new GridView.LayoutParams(200, 300));
			photoView.setScaleType(ImageView.ScaleType.CENTER_CROP);
			photoView.setPadding(5, 5, 5, 5);
		}
		else
			photoView = (ImageView) convertView;
		
		
		downloadThumbnail = new RequestThumbnail(context, photoView, originUrl, photoNames[position]);
		downloadThumbnail.executeDownload();

			//Toast.makeText(context, "no photoBm exists", Toast.LENGTH_LONG).show();
		
		//String photoUri = photos[position].getAbsolutePath();
		//Toast.makeText(context, photoUri, Toast.LENGTH_LONG).show();
		//Bitmap photoBm = BitmapFactory.decodeFile(photoUri);
		//photoView.setImageBitmap(photoBm);

		return photoView;
	}
	


}


*/