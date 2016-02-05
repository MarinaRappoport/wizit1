package wizit.cm.wizit;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.Drawable;

import com.with.tourbuild.Chat;
import com.with.tourbuild.MySing;

import org.osmdroid.ResourceProxy;
import org.osmdroid.views.MapView;
import org.osmdroid.views.overlay.ItemizedIconOverlay;
import org.osmdroid.views.overlay.OverlayItem;

import java.util.List;

/**
 * Created by Marina on 03.02.2016.
 */
public class MyItemizedOverlayPublic extends ItemizedIconOverlay<OverlayItem> {
    protected Context mContext;

    public MyItemizedOverlayPublic(final Context context,
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
    protected boolean onSingleTapUpHelper(final int index,
                                          final OverlayItem item, final MapView mapView) {
        AlertDialog.Builder dialog = new AlertDialog.Builder(mContext);
        dialog.setTitle(item.getTitle());
        dialog.setMessage(item.getSnippet());
        dialog.show();
        return true;
    }

    @Override
    public boolean addItem(OverlayItem item) {
        return super.addItem(item);
    }
}
