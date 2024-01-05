package blink.vietnam.weatherapp2.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.List;

import blink.vietnam.weatherapp2.Model.Hourly;
import blink.vietnam.weatherapp2.Model.Weather;
import blink.vietnam.weatherapp2.R;

public class HourlyAdapter extends BaseAdapter {
    Context context;
    int layoutHourly;
    List<Hourly> listHourly;

    public HourlyAdapter(Context context, int layoutHourly, List<Hourly> listHourly) {
        this.context = context;
        this.layoutHourly = layoutHourly;
        this.listHourly = listHourly;
    }

    @Override
    public int getCount() {
        return listHourly.size();
    }

    @Override
    public Object getItem(int i) {
        return null;
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    class ViewHolder {
        TextView txtCurentTime;
        TextView txtTempHourly;
        ImageView imgHourly;


    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        HourlyAdapter.ViewHolder viewHolder = null;
        if (view == null) {
            viewHolder = new ViewHolder();
            LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = layoutInflater.inflate(layoutHourly, null);
            viewHolder.txtCurentTime = view.findViewById(R.id.txtCurrentTime);
            viewHolder.imgHourly = view.findViewById(R.id.imgIconHourly);
            viewHolder.txtTempHourly = view.findViewById(R.id.txtTempHourly);
            view.setTag(viewHolder);
        } else {
            viewHolder = (HourlyAdapter.ViewHolder) view.getTag();
        }
        Hourly hourly = listHourly.get(i);
        viewHolder.txtCurentTime.setText(hourly.getCurrentHour());
        Picasso.get().load(hourly.getIconHourly()).into(viewHolder.imgHourly);
        viewHolder.txtTempHourly.setText(hourly.getTempHourly()+"º");
        return view;
    }
}
