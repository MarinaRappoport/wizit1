package wizit.cm.wizit;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.view.MotionEvent;

import com.with.tourbuild.Chat;
import com.with.tourbuild.MySing;

import org.osmdroid.views.MapView;
import org.osmdroid.views.overlay.ItemizedIconOverlay;
import org.osmdroid.views.overlay.OverlayItem;

import java.util.List;

public class MyItemizedOverlay extends ItemizedIconOverlay<OverlayItem> {



	protected Context mContext;

	public MyItemizedOverlay(final Context context,
			final List<OverlayItem> aList) {
		super(context, aList, new OnItemGestureListener<OverlayItem>() {
			@Override
			public boolean onItemSingleTapUp(final int index,
					final OverlayItem item) {
				return false;
			}

			@Override
			public boolean onItemLongPress(final int index,
					final OverlayItem item) {
				return false;
			}
		});
		// TODO Auto-generated constructor stub
		mContext = context;
	}

	@Override
	public boolean addItem(OverlayItem item) {
		return super.addItem(item);
	}
	@Override
	protected boolean onSingleTapUpHelper(final int index,
			final OverlayItem item, final MapView mapView) {
		AlertDialog.Builder dialog = new AlertDialog.Builder(mContext);
		dialog.setTitle(item.getTitle());
		dialog.setMessage(item.getSnippet());
		if(item.getTitle()!= MySing.getInstance().getName()){
		dialog.setPositiveButton("SEND MESSAGE", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int id) {
				Intent intent = new Intent(mContext, Chat.class).putExtra("name", item.getTitle());
				mContext.startActivity(intent);
			}
		});}
		dialog.show();
		return true;
	}

	@Override
	public boolean onSingleTapUp(MotionEvent e, MapView mapView) {
		float x = e.getRawX();
		float y = e.getRawY();
		//GeoPoint gp = (GeoPoint) mapView.getProjection().fromPixels(x,y);
		return super.onSingleTapUp(e, mapView);
	}

}