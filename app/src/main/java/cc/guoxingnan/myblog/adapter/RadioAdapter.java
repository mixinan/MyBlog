package cc.guoxingnan.myblog.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import cc.guoxingnan.myblog.R;
import cc.guoxingnan.myblog.entity.Radio;
import cc.guoxingnan.myblog.ui.fragment.FragmentRadio;
import cc.guoxingnan.myblog.util.NumberUtil;

/**
 * Created by mixinan on 2016/6/14.
 */
public class RadioAdapter extends RecyclerView.Adapter<RadioAdapter.MyViewHolder> {
    private Context context;
    private List<Radio> data;

    public RadioAdapter(Context context, List<Radio> data) {
        this.context = context;
        this.data = data;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_radio,null);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, final int position) {
        final Radio radio = data.get(position);

        holder.title.setText(radio.getName());
        holder.duration.setText(NumberUtil.durationTimeFormat(radio.getDuration()*1000));
        holder.playTimes.setText("播放:"+radio.getState().getPlay());
        holder.publishedTime.setText(NumberUtil.publishTimeFormat(radio.getCreate_time()));

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentRadio.getInstance().getClickData(radio.getName(),radio.getUrl(),position);
            }
        });
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder{
        TextView title, playTimes, publishedTime, duration;

        public MyViewHolder(View itemView) {
            super(itemView);
            title = (TextView) itemView.findViewById(R.id.title);
            playTimes = (TextView) itemView.findViewById(R.id.playtimes);
            publishedTime = (TextView) itemView.findViewById(R.id.publishedTime);
            duration = (TextView) itemView.findViewById(R.id.duration);
        }
    }
}
