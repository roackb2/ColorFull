package ntu.im.colorfull;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.widget.ImageView;
import android.widget.Toast;

public class RequestThumbnail 
{
	private Bitmap bitmap = null;
	private Context context;
	private String fileName;
	private String url;
	private UIhandler handler;
	private downloadRunnable downloadrunnable;
	
	final static int downloadSuccess = 1;
	private Thread downloadThread;
	private ImageView correspondingView;
	
	
	public RequestThumbnail(Context context, ImageView view, String url, String fileName)
	{
		this.context = context;
		this.fileName = fileName;
		this.url = url;
		this.correspondingView = view;
		downloadrunnable = new downloadRunnable();
		handler = new UIhandler();
		//Toast.makeText(context, "could show message in RequestThumbnail", Toast.LENGTH_LONG).show();
	}
	
	public void executeDownload()
	{
		Thread thread = new Thread(downloadrunnable);
		thread.start();
	}
	
	public class downloadRunnable implements Runnable
	{

		@Override
		public void run() 
		{
			// TODO Auto-generated method stub
			Bitmap webImgBitmap = null;
			
			URL imgUrl;
			try 
			{
				imgUrl = new URL(url + fileName);
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
					webImgBitmap = BitmapFactory.decodeByteArray(img, 0, img.length);

					if(desPos != length)
					{
						throw new IOException("Only read " + desPos + " bytes."); 
					}			
					Message msg = new Message();
					msg.what = downloadSuccess;
					msg.obj = webImgBitmap;
					handler.sendMessage(msg);					
				}
				httpUrlConnection.disconnect();
				
			} catch (MalformedURLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
	}
	

	public class UIhandler extends Handler
	{
		@Override
		public void handleMessage(Message msg)
		{
			switch(msg.what)
			{
			case downloadSuccess:
				//Toast.makeText(context, "download success", Toast.LENGTH_LONG).show();
				bitmap = (Bitmap) msg.obj;
				correspondingView.setImageBitmap(bitmap);
				break;
			}
			
		}
	}

}
