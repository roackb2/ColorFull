package ntu.im.colorfull.temp;

/*

package ntu.im.colorfull;

import android.os.AsyncTask;
import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;

public class OriginalGridviewFragment extends Fragment 
{

	private GridView baseGridView;
	private static String TAG_KEY = "tag";
	private String mode;
	private String TAG;
	
	private BaseGridViewAdapter viewAdapter;
	
	public static OriginalGridviewFragment create(int position)
	{
		
		OriginalGridviewFragment frag = new OriginalGridviewFragment();
		Bundle args = new Bundle();
		args.putInt(TAG_KEY, position);
		frag.setArguments(args);
		return frag;
	}
	
	public OriginalGridviewFragment()
	{
		
	}
	
	public void initialize(String mode)
	{
		this.mode = mode;
	}
	
	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		int position = getArguments().getInt(TAG_KEY);
		if(position == 0)
			TAG = "original";
		if(position == 1)
			TAG = "modified";
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
	{
		ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.base_frag_gridview, container, false);
		baseGridView = (GridView) rootView.findViewById(R.id.baseFragGridView);
		viewAdapter = new BaseGridViewAdapter(rootView.getContext());
		baseGridView.setAdapter(viewAdapter);
		return rootView;
	}
	
	private class setupComponentsAsync extends AsyncTask<String, Void, Void>
	{

		@Override
		protected Void doInBackground(String... params) 
		{
			// TODO Auto-generated method stub
			return null;
		}
		
	
		
	}

}
*/