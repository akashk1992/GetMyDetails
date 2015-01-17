package ask.askk.tabsfragment;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.location.LocationManager;
import android.os.BatteryManager;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.telephony.TelephonyManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class GetMyDetails extends Fragment {

	Button button;
	TextView display;
	TelephonyManager tm;
	LocationManager lm;
	String phoneType;
	BroadcastReceiver batteryRec;
	IntentFilter intentFilter;
	String plug;
	String batInfo;

	@Override
	public void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		getActivity().unregisterReceiver(batteryRec);
	}

	@Override
	public void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		getActivity().registerReceiver(batteryRec, intentFilter);
	}

	// Context context = this;
	@Override
	public void onAttach(Activity activity) {
		// TODO Auto-generated method stub
		super.onAttach(activity);
		FragmentActivity fragAct = (FragmentActivity) super.getActivity();
	}// onAttach ends here

	@Override
	public void onActivityCreated(@Nullable Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onActivityCreated(savedInstanceState);
		intentFilter = new IntentFilter(Intent.ACTION_BATTERY_CHANGED);

		batteryRec = new BroadcastReceiver() {

			@Override
			public void onReceive(Context context, Intent intent) {
				// TODO Auto-generated method stub
				// int batLevel= intent.getIntExtra(BatteryManager.EXTRA_HEALTH,
				// 0);
				int level = intent.getIntExtra(BatteryManager.EXTRA_LEVEL, -1);
				int scale = intent.getIntExtra(BatteryManager.EXTRA_SCALE, -1);
				float batLevel = ((float) level / (float) scale) * 100.0f;
				int plugged = intent.getIntExtra(BatteryManager.EXTRA_PLUGGED,
						0);
				if (plugged == 1) {
					plug = "AC Charging Plugged";
				} else if (plugged == 2) {
					plug = "USB Charging";
				} else if (plugged == 4) {
					plug = "Wireless Charging";
				} else {
					plug = "Your Device Is Not Charging";
				}

				int batTemp = intent.getIntExtra(
						BatteryManager.EXTRA_TEMPERATURE, 0);
				batInfo = "Battery Level: " + batLevel + "%" + "\nCharging: "
						+ plug + "\nBattery Temprature: " + batTemp + "c";
			}// onRecive ends
		};

		tm = (TelephonyManager) getActivity().getSystemService(
				Context.TELEPHONY_SERVICE);
		button = (Button) getActivity().findViewById(R.id.details);
		display = (TextView) getActivity().findViewById(R.id.textView);
		button.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				/********* To check gps status ************/
				String gpsStatus = "";
				getActivity();
				final LocationManager manager = (LocationManager) getActivity()
						.getSystemService(FragmentActivity.LOCATION_SERVICE);

				if (!manager.isProviderEnabled(LocationManager.GPS_PROVIDER))
					gpsStatus = "OFF";
				else
					gpsStatus = "Active";
				/***********************/

				String number = tm.getLine1Number();
				String IMEI = tm.getDeviceId();
				String softVer = tm.getDeviceSoftwareVersion();
				String simCountryISO = tm.getSimCountryIso();
				boolean isRoaming = tm.isNetworkRoaming();
				String operatorName = tm.getNetworkOperatorName();
				int dstate = tm.getDataState();
				String dataState = "";
				switch (dstate) {
				case (TelephonyManager.DATA_CONNECTED):
					dataState = "Data Connection ON";
					break;

				case (TelephonyManager.DATA_CONNECTING):
					dataState = "Data Connection Connecting";
					break;

				case (TelephonyManager.DATA_DISCONNECTED):
					dataState = "Data Connection OFF";
					break;

				case (TelephonyManager.DATA_SUSPENDED):
					dataState = "Data Connection Suspended";
					break;

				default:
					break;
				}// switc dataSate

				int ptype = tm.getPhoneType();
				switch (ptype) {
				case (TelephonyManager.PHONE_TYPE_CDMA):
					phoneType = "CDMA";
					break;
				case (TelephonyManager.PHONE_TYPE_GSM):
					phoneType = "GSM";
					break;
				case (TelephonyManager.PHONE_TYPE_NONE):
					phoneType = "NONE";
					break;
				}// switch..
				String simSerial = tm.getSimSerialNumber();

				/******* Display details **********/
				display.setText("Your Phone Number: " + number
						+ "\nIMEI Number: " + IMEI + "\nSoftware Version: "
						+ softVer + "\nSim Country ISO: " + simCountryISO
						+ "\nRoaming: " + isRoaming + "\nNetwork Operator: "
						+ operatorName + "\nPhone Network Type: " + phoneType
						+ "\nSim Serial Number: " + simSerial
						+ "\nData Connection: " + dataState + "\nGPS Status: "
						+ gpsStatus + "\n" + batInfo);

				/*****************/

			}// onClick edns here..
		});

	}// onActivityCreated ends here

	@Override
	public View onCreateView(LayoutInflater inflater,
			@Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		// TODO Auto-generated method stub

		RelativeLayout rl = (RelativeLayout) inflater.inflate(
				R.layout.mydetails, container, false);
		rl.findViewById(R.id.rl);

		return rl;
	}

}
