package ask.askk.tabsfragment;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import android.app.ActivityManager;
import android.app.ProgressDialog;
import android.app.ActivityManager.RunningAppProcessInfo;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class TaskManager extends Fragment {
	List<ApplicationInfo> packages;
	PackageManager pm;
	Button killbtn, listApps, cachebtn, clearcachebtn;
	// Context context;
	ActivityManager actmanager;
	TextView tv;
	String apps = "";
	ArrayList<String> list;
	ArrayAdapter<String> adapter;
	private ListView listView;
	List<RunningAppProcessInfo> runningapplist;

	@Override
	public void onActivityCreated(@Nullable Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onActivityCreated(savedInstanceState);

		FragmentActivity v = getActivity();
		killbtn = (Button) v.findViewById(R.id.killbtn);
		listApps = (Button) v.findViewById(R.id.getappsbtn);
		cachebtn = (Button) v.findViewById(R.id.cachesizebtn);
		clearcachebtn = (Button) v.findViewById(R.id.clearcachebtn);
		listView = (ListView) v.findViewById(R.id.listViewapps);
		// tv=(TextView)v.findViewById(R.id.tv);
		// context=this;
		list = new ArrayList<String>();
		adapter = new ArrayAdapter<String>(getActivity(),
				android.R.layout.simple_list_item_1, list);
		actmanager = (ActivityManager) getActivity().getSystemService(
				Context.ACTIVITY_SERVICE);
		final Intent mainIntent = new Intent(Intent.ACTION_MAIN, null);
		mainIntent.addCategory(Intent.CATEGORY_LAUNCHER);
		listView.setAdapter(adapter);
		cachebtn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				File cacheDir = getActivity().getCacheDir();
				if (cacheDir != null) {
					long cachesize = 0;
					File[] filelist = cacheDir.listFiles();
					for (File f : filelist) {
						cachesize = cachesize + f.length();

					}
					Toast.makeText(getActivity(),
							"Cache Size: " + cachesize + " kb",
							Toast.LENGTH_LONG).show();
				}// if ends
				else {
					Toast.makeText(getActivity(), "Cache dir not found..!",
							Toast.LENGTH_LONG).show();
				}
			}

		});// cachebtn ends

		clearcachebtn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				long cachesize = 0;
				File cacheDir = getActivity().getCacheDir();
				if (cacheDir != null) {
					File[] filelist = cacheDir.listFiles();
					for (File f : filelist) {
						cachesize = cachesize + f.length();
						f.delete();
					}
					Toast.makeText(getActivity(),
							cachesize + "kb " + "Cache Cleared..!",
							Toast.LENGTH_LONG).show();
				}// if ends
				else {
					Toast.makeText(getActivity(), "Cache dir not found..!",
							Toast.LENGTH_LONG).show();
				}
			}
		});// clearcachebtn ends here

		listApps.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub

				pm = getActivity().getPackageManager();
				runningapplist = actmanager.getRunningAppProcesses();

				for (int i = 0; i < runningapplist.size(); i++) {
					ActivityManager.RunningAppProcessInfo info = (runningapplist
							.get(i));
					String str[] = info.pkgList;
					for (String s : str) {
						if (s.contains("com.")) {
							// leave it
						} else {
							try {
								apps = "";
								CharSequence c = pm.getApplicationLabel(pm
										.getApplicationInfo(info.processName,
												PackageManager.GET_META_DATA));
								apps = apps + c.toString();
								list.add(apps);
								adapter.notifyDataSetChanged();
							} catch (NameNotFoundException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
						}
					}

				}// for ends here
			}
		});// listApps ends here

		killbtn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View view) {
				// TODO Auto-generated method stub
				// ProgressDialog dialog=ProgressDialog.show(getActivity(),
				// "Killing in process..", "Loading");
				pm = getActivity().getPackageManager();
				packages = pm.getInstalledApplications(0);

				new Thread(new Runnable() {

					@Override
					public void run() {
						// TODO Auto-generated method stub
						for (ApplicationInfo packageInfo : packages) {
							if ((packageInfo.flags & ApplicationInfo.FLAG_SYSTEM) == 1)
								continue;
							actmanager
									.killBackgroundProcesses(packageInfo.packageName);
						}
					}
				});
				/*
				 * for (ApplicationInfo packageInfo : packages) {
				 * if((packageInfo.flags &
				 * ApplicationInfo.FLAG_SYSTEM)==1)continue;
				 * if(packageInfo.packageName.equals("mypackage")) continue;
				 * actmanager.killBackgroundProcesses(packageInfo.packageName);
				 * }
				 */if (runningapplist != null) {
					runningapplist.clear();
					adapter.clear();
				}
				Toast.makeText(getActivity(),
						"All Background Task Have Been Killed",
						Toast.LENGTH_LONG).show();

			}
		});// killbtn
	}

	@Override
	public View onCreateView(LayoutInflater inflater,
			@Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		return inflater.inflate(R.layout.taskmanager, container, false);
	}// onCreateView ends here..

}
