package ntu.im.colorfull.temp;

/*
package ntu.im.colorfull;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.widget.TextView;

public class RequestPhotoNames 
{
	final static String ORIGINAL = "original", MODIFIED = "modified";
	private final static int success = 1, getResponse = 2;
	private String userId;
	private static String url;
	private Context context;
	private UIHandler handler;
	
	private HttpClient client;
	private HttpPost postRequest;
	private HttpResponse postResponse;
	private MultipartEntityBuilder multipartEntity;
	
	private String EntityKeyId = "id";
	private String EntityKeyOp = "option";

	private ArrayList<String> result;
	
	private TextView serverstatus;

	public RequestPhotoNames(Context context, String option, TextView view)
	{
		this.serverstatus = view;
		this.context = context;
		userId = MainActivity.getUserId();
		handler = new UIHandler();
		url = "http://210.61.27.43/picturePair/getFileNames.php";

		result = new ArrayList<String>();
		
		postRequest = new HttpPost(url);
		multipartEntity = MultipartEntityBuilder.create();
		
		client = new DefaultHttpClient();
		multipartEntity.addTextBody(EntityKeyId, userId);
		if(option == ORIGINAL)
		{
			multipartEntity.addTextBody(EntityKeyOp, ORIGINAL);
		}
		if(option == MODIFIED)
		{
			multipartEntity.addTextBody(EntityKeyOp, MODIFIED);
		}
		postRequest.setEntity(multipartEntity.build());
		
		
		Thread thread = new Thread(new Runnable()
		{

			@Override
			public void run() 
			{
				// TODO Auto-generated method stub
				try {
					HttpResponse postResponse = client.execute(postRequest);
					String response = EntityUtils.toString(postResponse.getEntity());
					
					Message msg = new Message();
					msg.what = getResponse;
					msg.obj = response;
					handler.sendMessage(msg);					
					
					String[] result = response.split(":");
					
					ArrayList<String> resultList = new ArrayList<String>();
					for(String word : result)
					{
						resultList.add(word);
					}
										
					msg = new Message();
					msg.what = success;
					msg.obj = resultList;
					handler.sendMessage(msg);
					
				} catch (ClientProtocolException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		});
		thread.start();
		try {
			thread.join();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public class UIHandler extends Handler
	{
		@Override
		public void  handleMessage(Message msg)
		{
			switch(msg.what)
			{
			case getResponse:
				serverstatus.setText((String)msg.obj);
				break;
			case success:
				synchronized(result)
				{
					result = (ArrayList<String>)msg.obj;
					result.notify();
				}
				break;
			}
		}
	}
	
	public synchronized ArrayList<String> getPhotoNames()
	{

			if(result.isEmpty())
			{
				try {
					wait();
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}			
			}

			return result;
		
	}
}
*/