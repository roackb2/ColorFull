package ntu.im.colorfull;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;

public class BaseGridViewAdapter extends BaseAdapter 
{

	
	private Context context;
	private String userId;
	private static String url;
	private String[] photoNames;
	private RequestThumbnail downloadThumbnail;
	private Bitmap photoBm = null;
	
	public BaseGridViewAdapter(Context context, String url)
	{
		this.context = context;
		this.url = url;
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
		
		
		downloadThumbnail = new RequestThumbnail(context, photoView, url, photoNames[position]);
		downloadThumbnail.executeDownload();

		return photoView;
		
	}

}
