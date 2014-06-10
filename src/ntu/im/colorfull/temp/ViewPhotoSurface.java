package ntu.im.colorfull.temp;
/*

package ntu.im.colorfull;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.util.AttributeSet;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

public class ViewPhotoSurface extends SurfaceView implements SurfaceHolder.Callback
{
	private SurfaceHolder holder;
	private Bitmap photoBm;
	private drawingThread drawing;
	
	public ViewPhotoSurface(Context context)
	{
		super(context);
		holder = this.getHolder();
		holder.addCallback(this);
		drawing = new drawingThread(holder, this);
	}

	public ViewPhotoSurface(Context context, AttributeSet attrs) 
	{
		super(context, attrs);
		holder = this.getHolder();
		holder.addCallback(this);
		drawing = new drawingThread(holder, this);
		// TODO Auto-generated constructor stub
	}

	public ViewPhotoSurface(Context context, AttributeSet attrs, int defstyle)
	{
		super(context, attrs, defstyle);
		holder = this.getHolder();
		holder.addCallback(this);
		drawing = new drawingThread(holder, this);
	}

	@Override
	protected void onDraw(Canvas canvas)
	{
		super.onDraw(canvas);
		
		canvas.drawColor(Color.BLACK);
		canvas.drawBitmap(photoBm, photoBm.getWidth(), photoBm.getHeight(), null);
	}


	@Override
	public void surfaceCreated(SurfaceHolder holder) {
		// TODO Auto-generated method stub
		drawing.setRunning(true);
		drawing.start();
	}


	@Override
	public void surfaceChanged(SurfaceHolder holder, int format, int width,
			int height) {
		// TODO Auto-generated method stub
		
	}


	@Override
	public void surfaceDestroyed(SurfaceHolder holder) {
		// TODO Auto-generated method stub
		boolean retry = true;
		drawing.setRunning(false);
		while(retry)
		{
			try
			{
				drawing.join();
				retry = false;
			}
			catch(InterruptedException e)
			{
				e.printStackTrace();
			}
		}
	}
	
	
	public class drawingThread extends Thread
	{
		private SurfaceHolder holder;
		private ViewPhotoSurface surface;
		private boolean running = false;
		
		public drawingThread(SurfaceHolder holder, ViewPhotoSurface surface)
		{
			this.holder = holder;
			this.surface = surface;
		}
		
		public void setRunning(boolean run)
		{
			running = run;
		}
		
		@SuppressLint("WrongCall")
		@Override 
		public void run()
		{
			Canvas c;
			while(running)
			{
				c = null;
				try
				{
					c = holder.lockCanvas(null);
					synchronized(holder)
					{
						surface.onDraw(c);
					}
				}
				finally
				{
					if(c != null)
					{
						holder.unlockCanvasAndPost(c);
					}
				}
				
			}
			
		}
		
	}
	
	public SurfaceHolder obtainHolder()
	{
		return holder;
	}
	
	public void setPhotoBitmap(Bitmap bm)
	{
		photoBm = bm;
	}
}
*/