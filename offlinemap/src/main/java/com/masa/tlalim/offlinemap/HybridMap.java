package com.masa.tlalim.offlinemap;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.widget.FrameLayout;

import org.osmdroid.DefaultResourceProxyImpl;
import org.osmdroid.ResourceProxy;
import org.osmdroid.tileprovider.IRegisterReceiver;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.MapController;
import org.osmdroid.views.MapView;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class HybridMap extends FrameLayout {

//	private final int ZOOM = 10;
//	private double latCen = 31.7736407;
//	private double lonCen = 35.2106797;

	public MapView mv;
	private ResourceProxy resourceProxy;
	private Context context;
//	private MapController mapController;

	public HybridMap(Context context, AttributeSet attrs)
	{
		super(context, attrs);
		this.context = context;
		resourceProxy = new DefaultResourceProxyImpl(this.getContext());

//		useMapsforgeTiles(Environment.getExternalStorageDirectory().getPath() + File.separator + "israel.map");
		useMapsforgeTiles(R.raw.israel);
//		mapController = mv.getController();
//		mv.getController().setZoom(ZOOM);
//		center(latCen, lonCen);
	}

	public void useMapsforgeTiles(int mapResourceId) {
		Log.d("--->useMapsforgeTiles", "startMethod");
		if (mv != null) {
			this.removeView(mv);
			mv = null;
		}
		InputStream input = getResources().openRawResource(mapResourceId);
//File mapFile = new File(context.getCacheDir(), "colorado.map");
		Log.d("--->useMapsforgeTiles",getResources().getResourceEntryName(mapResourceId));
		File mapFile = new File(context.getCacheDir(), getResources().getResourceEntryName(mapResourceId));

		OutputStream output = null;
		try {
			output = new FileOutputStream(mapFile);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		try {
			byte[] buffer = new byte[input.available()/2]; // or other buffer size
			int read;
			while ((read = input.read(buffer)) != -1) {
				Log.d("--->","readed");
				output.write(buffer, 0, read);
			}
			output.flush();

		} catch (Exception e) {
			e.printStackTrace(); // handle exception, define IOException and others
		} finally {
			try {
				input.close();
				output.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

		MFTileProvider provider = new MFTileProvider((IRegisterReceiver) this.getContext(), mapFile);

		mv = new MapView(this.getContext(), provider.getTileSource().getTileSizePixels(), resourceProxy, provider);
		mv.setBuiltInZoomControls(true);
		mv.setMultiTouchControls(true);

		this.addView(mv);

	}

//	public void center(double latitude, double longitude) {
//		GeoPoint p = new GeoPoint(latitude, longitude);
//		mv.getController().setCenter(p);
//	}

}