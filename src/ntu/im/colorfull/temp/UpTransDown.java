/*
 * package ntu.im.colorfull.temp;

import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;


import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import org.apache.commons.codec.binary.Base64;
import org.apache.http.HttpResponse;
import org.apache.http.ParseException;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.apache.commons.codec.*;

public class UpTransDown 
{

	final static String uploadUrl = "http://210.61.27.43/picturePair/uploadPhoto.php";
	final static String downloadUrl = "http://210.61.27.34/picturePair/modifiedImages/";
	
	private HttpClient client;
	private HttpPost postRequest;
	private HttpResponse postResponse;
	private String response;
	private MultipartEntityBuilder requestEntity;

	private String originPhotoName;
	private Handler UIhandler;
	private String originFilePath;
	private File originPhotoFile;	
	private Bitmap originPhotoBitmap;
	private String originEncodedPhoto;
	private String userid;
	
	public String entityKeyFileName = "fileName";
	public String entityKeyData = "data";
	public String entityKeyId = "id";
	
	public UpTransDown(Handler UIhandler, String filePath)
	{
		this.UIhandler = UIhandler;
		this.originFilePath = filePath;
		userid = MainActivity.getUserId();
		originPhotoFile = new File(originFilePath);
		
		originPhotoBitmap = BitmapFactory.decodeFile(originFilePath);
		
		try
		{
			ByteArrayOutputStream BAO = new ByteArrayOutputStream();
			originPhotoBitmap.compress(Bitmap.CompressFormat.JPEG, 100, BAO);
			byte[] imageBytes = BAO.toByteArray();
			originEncodedPhoto = new String(Base64.encodeBase64(imageBytes));
		}
		catch(NullPointerException e)
		{
			Log.e("NullPointerException: ", "fail to compress bitmap.");
		}	
	}
	
	public void upload()
	{
		postRequest = new HttpPost(uploadUrl);
		client = new DefaultHttpClient();
		requestEntity = MultipartEntityBuilder.create();
		requestEntity.setMode(HttpMultipartMode.BROWSER_COMPATIBLE);
		this.setFileName();
		requestEntity.addTextBody(entityKeyFileName, originPhotoName);
		requestEntity.addTextBody(entityKeyData, originEncodedPhoto);
		requestEntity.addTextBody(entityKeyId, userid);
		postRequest.setEntity(requestEntity.build());
		
		//TODO new a thread and create runnable to upload
		Thread uploadTask = new Thread(new requestRunnable(UIhandler));
		uploadTask.start();
	}
	
	public void setFileName()
	{
		Date date = new Date();
		DateFormat sdf = new SimpleDateFormat("yyyy-MM-dd_HH.mm.SS");
		try
		{
			originPhotoName = sdf.format(date);
		}
		catch(Exception e)
		{
			Log.e("Exception:", "fail to create file name.");
		}
		
	}
	
	public class requestRunnable implements Runnable
	{
		Handler UIhandle;
		public requestRunnable(Handler UIhandle)
		{
			this.UIhandle = UIhandler;
		}

		@Override
		public void run() 
		{
			// TODO Auto-generated method stub
			try 
			{
				postResponse = client.execute(postRequest);
			} 
			catch (ClientProtocolException e) 
			{
				// TODO Auto-generated catch block
				e.printStackTrace();
			} 
			catch (IOException e) 
			{
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			if(postResponse.getStatusLine().getStatusCode() == 200)
			{
				try 
				{
					response = EntityUtils.toString(postResponse.getEntity());
					
					Message msg = new Message();
					msg.what = ViewPhotoActivity.UTDsuccess;					
					msg.obj = response;
					UIhandler.sendMessage(msg);		
				} 
				catch (ParseException e) 
				{
					// TODO Auto-generated catch block
					e.printStackTrace();
				} 
				catch (IOException e) 
				{
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			else
			{
				Log.e("RequestError: ", "fail to get server response");
				Message msg = new Message();
				msg.what = ViewPhotoActivity.UTDfail;
				msg.obj = postResponse.getStatusLine().getReasonPhrase();
				UIhandler.sendMessage(msg);
			}
		}
		
	}
	
}

 */
