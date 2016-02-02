package wizit.cm.wizit;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.with.tourbuild.GuideSing;

/**
 * Created by Petrucco on 20.08.2015.
 */
public class GuidesAdapter extends BaseAdapter {

    private Context mContext;
    public GuidesAdapter(Context mContext) {
        this.mContext = mContext;
    }

    @Override
    public int getCount() {
        return GuideSing.getInstance().getGuideArrayList().size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        RelativeLayout rl = null;
        LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        rl = (RelativeLayout) inflater.inflate(R.layout.guide_in_table, parent, false);
        TextView name = (TextView)rl.findViewById(R.id.guideNameTab);
        TextView spec = (TextView)rl.findViewById(R.id.guideSpecTab);
        RatingBar rb = (RatingBar)rl.findViewById(R.id.ratingGuiInTab);

        ImageView iv = (ImageView)rl.findViewById(R.id.photoGuideTab);
        rb.setRating(GuideSing.getInstance().getGuideArrayList().get(position).finRate());
        name.setText(GuideSing.getInstance().getGuideArrayList().get(position).getmGuideName());
        spec.setText(GuideSing.getInstance().getGuideArrayList().get(position).getSpecs());
        if (GuideSing.getInstance().getGuideArrayList().get(position).getGuidePhoto() != null){
        iv.setImageDrawable(GuideSing.getInstance().getGuideArrayList().get(position).getGuidePhoto());}
        return rl;
    }
}
