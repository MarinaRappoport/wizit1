package com.masa.tlalim.offlinemap;

//Adapted from code found here : http://www.sieswerda.net/2012/08/15/upping-the-developer-friendliness/

import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;

import org.mapsforge.android.maps.DebugSettings;
import org.mapsforge.android.maps.mapgenerator.JobParameters;
import org.mapsforge.android.maps.mapgenerator.MapGeneratorJob;
import org.mapsforge.android.maps.mapgenerator.databaserenderer.DatabaseRenderer;
import org.mapsforge.core.model.Tile;
import org.mapsforge.map.reader.MapDatabase;
import org.mapsforge.map.reader.header.FileOpenResult;
import org.mapsforge.map.rendertheme.InternalRenderTheme;
import org.mapsforge.map.rendertheme.XmlRenderTheme;
import org.osmdroid.ResourceProxy.string;
import org.osmdroid.tileprovider.MapTile;
import org.osmdroid.tileprovider.tilesource.BitmapTileSourceBase;

import java.io.File;

public class MFTileSource extends BitmapTileSourceBase {

	protected File mapFile;

	// Reasonable defaults ..
	public static final int minZoom = 8;
	public static final int maxZoom = 18;
	public static final int tileSizePixels = 256;

	private DatabaseRenderer renderer;
	private MapDatabase mapDatabase;
	private XmlRenderTheme jobTheme;
	private JobParameters jobParameters;
	private DebugSettings debugSettings;

	// Required for the superclass
	public static final string resourceId = string.offline_mode;

	/**
	 * The reason this constructor is protected is because all parameters,
	 * except file should be determined from the archive file. Therefore a
	 * factory method is necessary.
	 * 
	 * @param minZoom
	 * @param maxZoom
	 * @param tileSizePixels
	 * @param file
	 */
	protected MFTileSource(int minZoom, int maxZoom, int tileSizePixels, File file) {
		super("MFTiles", resourceId, minZoom, maxZoom, tileSizePixels, ".png");

		mapDatabase = new MapDatabase();

		//Make sure the database can open the file
		FileOpenResult fileOpenResult = this.mapDatabase.openFile(file);
		if (fileOpenResult.isSuccess()) {
			mapFile = file;
		}
		else{
			mapFile = null;
		}

		renderer = new DatabaseRenderer(mapDatabase);

		//  For this to work I had to edit org.mapsforge.map.rendertheme.InternalRenderTheme.getRenderThemeAsStream()  to:
		//  return this.getClass().getResourceAsStream(this.absolutePath + this.file);
		jobTheme = InternalRenderTheme.OSMARENDER;    		
		jobParameters = new JobParameters(jobTheme, 1);
		debugSettings = new DebugSettings(false, false, false);

	}

	/**
	 * Creates a new MFTileSource from file.
	 * 
	 * Parameters minZoom and maxZoom are obtained from the
	 * database. If they cannot be obtained from the DB, the default values as
	 * defined by this class are used.
	 * 
	 * @param file
	 * @return
	 */
	public static MFTileSource createFromFile(File file) {
		//TODO - set these based on .map file info
		int minZoomLevel = 8;
		int maxZoomLevel = 18;
		int tileSizePixels = 256;

		return new MFTileSource(minZoomLevel, maxZoomLevel, tileSizePixels, file);
	}

	//The synchronized here is VERY important.  If missing, the mapDatbase read gets corrupted by multiple threads reading the file at once.
	public synchronized Drawable renderTile(MapTile pTile) {

		Tile tile = new Tile((long)pTile.getX(), (long)pTile.getY(), (byte)pTile.getZoomLevel());

		//Create a bitmap to draw on
		Bitmap bitmap = Bitmap.createBitmap(tileSizePixels, tileSizePixels, Bitmap.Config.RGB_565);

		//You could try something like this to load a custom theme
		//try{
		//	jobTheme = new ExternalRenderTheme(themeFile);
		//}
		//catch(Exception e){
		//	jobTheme = InternalRenderTheme.OSMARENDER;
		//}

		try{
			//Draw the tile
			MapGeneratorJob mapGeneratorJob = new MapGeneratorJob(tile, mapFile, jobParameters, debugSettings);
			renderer.executeJob(mapGeneratorJob, bitmap);
		}
		catch(Exception ex){
			//Make the bad tile easy to spot
			bitmap.eraseColor(Color.YELLOW);
		}

		Drawable d = new BitmapDrawable(bitmap);
		return d;
	}

}
