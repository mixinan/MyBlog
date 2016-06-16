package cc.guoxingnan.myblog.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.List;

import cc.guoxingnan.myblog.R;
import cc.guoxingnan.myblog.entity.Video;
import cc.guoxingnan.myblog.ui.WebViewActivity;

/**
 * Created by mixinan on 2016/6/14.
 */
public class VideoAdapter extends RecyclerView.Adapter<VideoAdapter.MyViewHolder> {
    private Context context;
    private List<Video> data;

    public VideoAdapter(Context context, List<Video> data) {
        this.context = context;
        this.data = data;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_video,null);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        final Video video = data.get(position);

        Glide.with(context).load(video.getImage()).into(holder.image);
        holder.title.setText(video.getTitle());
        holder.duration.setText(video.getDuration());
        holder.playTimes.setText("播放次数:"+video.getPlayTimes());
        holder.publishedTime.setText(video.getPublishedTime());

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, WebViewActivity.class);
                intent.putExtra("url",video.getUrl());
                intent.putExtra("title",video.getTitle());
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder{
        ImageView image;
        TextView title, playTimes, publishedTime, duration;

        public MyViewHolder(View itemView) {
            super(itemView);
            image = (ImageView) itemView.findViewById(R.id.image);
            title = (TextView) itemView.findViewById(R.id.title);
            playTimes = (TextView) itemView.findViewById(R.id.playtimes);
            publishedTime = (TextView) itemView.findViewById(R.id.publishedTime);
            duration = (TextView) itemView.findViewById(R.id.duration);
        }
    }
}
