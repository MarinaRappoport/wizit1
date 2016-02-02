package sun.bob.mcalendarview.adapters;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.view.View;

import sun.bob.mcalendarview.fragments.MonthFragment;
import sun.bob.mcalendarview.utils.CalendarUtil;
import sun.bob.mcalendarview.views.BaseCellView;
import sun.bob.mcalendarview.views.BaseMarkView;
import sun.bob.mcalendarview.vo.DateData;
import sun.bob.mcalendarview.vo.MonthData;

/**
 * Created by bob.sun on 15/8/27.
 */
public class CalendarViewAdapter extends FragmentStatePagerAdapter {

    private DateData date;

    private int dateCellId;
    private int markCellId;

    private Context context;

    public CalendarViewAdapter(FragmentManager fm) {
        super(fm);
    }

    public CalendarViewAdapter setDate(DateData date){
        this.date = date;
        return this;
    }

    public void setContext(Context context) {
        this.context = context;
    }

    public CalendarViewAdapter setDateCellId(int dateCellRes){
//        if (context == null)
//            throw new NullPointerException("Context is null! Use `setContext` to set context please.");
        this.dateCellId =  dateCellRes;
        return this;
    }

//    public CalendarViewAdapter setDateCell(BaseCellView dateCell){
//        this.dateCellId = dateCell;
//        return this;
//    }

    public CalendarViewAdapter setMarkCellId(int markCellId){
//        if (context == null)
//            throw new NullPointerException("Context is null! Use `setContext` to set context please.");
//        this.dateCellId = (BaseCellView) View.inflate(context, dateCellRes, null);
        this.markCellId = markCellId;
        return this;
    }

//    public CalendarViewAdapter setMarkCell(BaseMarkView markCell){
//        this.markCellId = markCell;
//        return this;
//    }

    @Override
    public Fragment getItem(int position) {
        int year = CalendarUtil.position2Year(position);
        int month = CalendarUtil.position2Month(position);

        MonthFragment fragment = new MonthFragment();
        MonthData monthData = new MonthData(new DateData(year, month, month / 2));
        fragment.setData(monthData, dateCellId, markCellId);
        return fragment;
    }
    @Override
    public int getCount() {
        return 1000;
    }
}
