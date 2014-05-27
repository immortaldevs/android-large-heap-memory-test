package com.immortaldevs.largeheapmemorytest;

import com.immortaldevs.largeheapmemorytest.R;

import android.app.ActivityManager;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.ActionBarActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class LargeHeapTestActivity extends ActionBarActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		FragmentManager fm = getSupportFragmentManager();
		Fragment fragment = new PlaceholderFragment();
		fm.beginTransaction()
			.add(R.id.container, fragment)
			.commit();
	}


	/**
	 * A placeholder fragment containing a simple view.
	 */
	public static class PlaceholderFragment extends Fragment {

		public PlaceholderFragment() {
		}
		
		@Override
		public void onCreate(Bundle savedInstanceState) {
			super.onCreate(savedInstanceState);
			setRetainInstance(true);
		}

		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container,
				Bundle savedInstanceState) {
			View rootView = inflater.inflate(R.layout.fragment_main, container,
					false);
			
			/*
			 * Get the soft limit (memory class) of the device
			 * 
			 * This is the suggested amount you limit your application to in order to
			 * allow prevent the application from interfering with normal device operation
			 * 
			 */
			
			ActivityManager am = (ActivityManager) getActivity().getSystemService(ACTIVITY_SERVICE);
			int softLimit = am.getMemoryClass();
			
			
			/*
			 * Get the hard limit (max memory) of the device
			 * 
			 * This is the largest value to which the heap can grow.
			 * Exceeding this limit will (almost certainly) cause an
			 * OutOfMemory Exception
			 * 
			 */
			Runtime rt = Runtime.getRuntime();
			long hardLimit = rt.maxMemory();
			
			int hardLimitMemMult = 0;
			
			while (hardLimit > 1000) {
				hardLimitMemMult++;
				hardLimit = hardLimit / 1000;
			}
			
			String hardLimitMemFactor = getMemoryMultiplier(hardLimitMemMult);
			
			TextView softTextView = (TextView) rootView.findViewById(R.id.heap_softLimitTextView);
			String softLimitString = getActivity().getString(R.string.soft_limit, softLimit);
			softTextView.setText(softLimitString);
			
			TextView hardTextView = (TextView) rootView.findViewById(R.id.heap_hardLimitTextView);
			String hardLimitString = getActivity().getString(R.string.hard_limit, hardLimit, hardLimitMemFactor);
			hardTextView.setText(hardLimitString);
			
			return rootView;
		}
		
		private String getMemoryMultiplier(int factor) {
			switch(factor) {
			case 1:
				return "kB";
			case 2:
				return "MB";
			default:
				/*
				 * 	In the distant future when we have heaps that can expand to 
				 * a GB and beyond, then we can add that. Until then, let's assume that
				 * the default applies.
				 */
				return "B";
			}
		}
	}
}
