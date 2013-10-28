package com.example.positionpulltest;

import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.view.Menu;
import android.view.View;
import android.widget.TextView;
import android.telephony.CellInfo;
import android.telephony.CellInfoCdma;
import android.telephony.CellInfoGsm;
import android.telephony.CellInfoLte;
import android.telephony.CellSignalStrengthCdma;
import android.telephony.CellSignalStrengthGsm;
import android.telephony.CellSignalStrengthLte;
import android.telephony.PhoneStateListener;
import android.telephony.SignalStrength;
import android.telephony.TelephonyManager;
import android.telephony.cdma.CdmaCellLocation;

public class MainActivity extends Activity {
	private LocationManager lm;
	private LocationListener locationListener;
	private TelephonyManager tm;
	private PhoneStateListener phoneListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        
        lm = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);
        tm = (TelephonyManager)getSystemService(Context.TELEPHONY_SERVICE);
        // Define a listener that responds to location updates
    	locationListener = new LocationListener() {
    		public void onLocationChanged(Location location) {
    			CdmaCellLocation ccl = (CdmaCellLocation)tm.getCellLocation();
    			// Called when a new location is found by the network location provider.
        		TextView textView = (TextView)findViewById(R.id.lngtd_val);
            	textView.setText("" + location.getLongitude());
            	textView = (TextView)findViewById(R.id.lattd_val);
            	textView.setText("" + location.getLatitude());
            	textView = (TextView)findViewById(R.id.Simoper);
            	textView.setText("" + tm.getNetworkOperator ());
            	textView = (TextView)findViewById(R.id.Simopername);
            	textView.setText("" + tm.getNetworkOperatorName ());
            	textView = (TextView)findViewById(R.id.DeviceId);
            	textView.setText("" + tm.getDeviceId ());   
            	textView = (TextView)findViewById(R.id.Simserial);
            	textView.setText("" + tm.getSimSerialNumber ());     
            	textView = (TextView)findViewById(R.id.Phonetype);
            	textView.setText("" + tm.getPhoneType ());              	
            	textView = (TextView)findViewById(R.id.Phonenumber);
            	textView.setText("" + tm.getLine1Number ());  
            	textView = (TextView)findViewById(R.id.Softwareversion);
            	textView.setText("" + tm.getDeviceSoftwareVersion ());   
            	textView = (TextView)findViewById(R.id.Towernetworkid);
            	textView.setText("" + ccl.getNetworkId ());    
            	textView = (TextView)findViewById(R.id.Towerid);
            	textView.setText("" + ccl.getBaseStationId ());             	
            	textView = (TextView)findViewById(R.id.Towerlatitude);
            	textView.setText("" + (ccl.getBaseStationLatitude ()*.000069444));    
            	textView = (TextView)findViewById(R.id.Towerlongitude);
            	textView.setText("" + (ccl.getBaseStationLongitude ()*.000069444));              	
    		}

			public void onStatusChanged(String provider, int status, Bundle extras) {}
    		public void onProviderEnabled(String provider) {}
    		public void onProviderDisabled(String provider) {}
    	};
    	
    	phoneListener = new PhoneStateListener() {
			@TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR1)
			public void onSignalStrengthsChanged(SignalStrength ss) {
    			TextView textView = null;
    			if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.JELLY_BEAN_MR1) {
	    			for (final CellInfo info : tm.getAllCellInfo()) {
	    		        if (info instanceof CellInfoGsm) {
	    		            final CellSignalStrengthGsm gsm = ((CellInfoGsm) info).getCellSignalStrength();
	    		            textView = (TextView)findViewById(R.id.signal_t_val);
	    	            	textView.setText("GSM");
	    		            textView = (TextView)findViewById(R.id.signal_b_val);
	    	            	textView.setText("" + gsm.getLevel());
	    	            	textView = (TextView)findViewById(R.id.signal_a_val);
	    	            	textView.setText("" + gsm.getAsuLevel());
	    	            	textView = (TextView)findViewById(R.id.signal_d_val);
	    	            	textView.setText("" + gsm.getDbm());
	    		        } else if (info instanceof CellInfoCdma) {
	    		            final CellSignalStrengthCdma cdma = ((CellInfoCdma) info).getCellSignalStrength();
	    		            textView = (TextView)findViewById(R.id.signal_t_val);
	    	            	textView.setText("CDMA");
	    		            textView = (TextView)findViewById(R.id.signal_b_val);
	    	            	textView.setText("" + cdma.getLevel());
	    	            	textView = (TextView)findViewById(R.id.signal_a_val);
	    	            	textView.setText("" + cdma.getAsuLevel());
	    	            	textView = (TextView)findViewById(R.id.signal_d_val);
	    	            	textView.setText("" + cdma.getDbm());
	    		        } else if (info instanceof CellInfoLte) {
	    		            final CellSignalStrengthLte lte = ((CellInfoLte) info).getCellSignalStrength();
	    		            textView = (TextView)findViewById(R.id.signal_t_val);
	    	            	textView.setText("LTE");
	    		            textView = (TextView)findViewById(R.id.signal_b_val);
	    	            	textView.setText("" + lte.getLevel());
	    	            	textView = (TextView)findViewById(R.id.signal_a_val);
	    	            	textView.setText("" + lte.getAsuLevel());
	    	            	textView = (TextView)findViewById(R.id.signal_d_val);
	    	            	textView.setText("" + lte.getDbm());
	    		        } else {
	    		        	textView = (TextView)findViewById(R.id.signal_t_val);
	    		        	textView.setText("Can't determine network type");
	    		        	textView = (TextView)findViewById(R.id.signal_b_val);
	    	            	textView.setText("Can't determine network type");
	    	            	textView = (TextView)findViewById(R.id.signal_a_val);
	    	            	textView.setText("Can't determine network type");
	    	            	textView = (TextView)findViewById(R.id.signal_d_val);
	    	            	textView.setText("Can't determine network type");
	    		        }
	    		    }
				} else {
					textView = (TextView)findViewById(R.id.textView15);
					textView.setVisibility(TextView.GONE);
					textView = (TextView)findViewById(R.id.textView16);
					textView.setVisibility(TextView.GONE);
					textView = (TextView)findViewById(R.id.signal_b_val);
	            	textView.setVisibility(TextView.GONE);
	            	textView = (TextView)findViewById(R.id.signal_a_val);
	            	textView.setVisibility(TextView.GONE);
	            	textView = (TextView)findViewById(R.id.signal_t_val);
		        	if (ss.isGsm())
		        		textView.setText("GSM [SDK < 17]");
		        	else
		        		textView.setText("Not GSM [SDK < 17]");
					textView = (TextView)findViewById(R.id.signal_d_val);
					textView.setText("" + ss.getCdmaDbm());
				}
    		}
    	};
    	
    	tm.listen(phoneListener, PhoneStateListener.LISTEN_SIGNAL_STRENGTHS);
    	
    	//lm.requestLocationUpdates(LocationManager.GPS_PROVIDER, 1000, 0, locationListener);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }
    
    @SuppressLint("NewApi")
	public void refreshPosition(View view) {
    	if (!lm.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
    		TextView textView = (TextView)findViewById(R.id.lngtd_val);
        	textView.setText("GPS Disabled");
        	textView = (TextView)findViewById(R.id.lattd_val);
        	textView.setText("GPS Disabled");
    	} else {
    		lm.requestSingleUpdate(LocationManager.GPS_PROVIDER, locationListener, getMainLooper());
    		Location location = lm.getLastKnownLocation(LocationManager.GPS_PROVIDER);
    		locationListener.onLocationChanged(location);
    	}
    }
}
