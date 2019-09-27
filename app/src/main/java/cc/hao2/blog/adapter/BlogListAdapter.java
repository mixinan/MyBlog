package cc.hao2.blog.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.RotateAnimation;
import android.widget.TextView;

import java.util.List;


import cc.hao2.blog.R;
import cc.hao2.blog.entity.Blog;
import cc.hao2.blog.ui.BlogDetailActivity;
import cc.hao2.blog.ui.MainActivity;
import cc.hao2.blog.util.NetUtil;
import cc.hao2.blog.util.ToastUtil;

import static android.view.animation.RotateAnimation.*;

/**
 * Created by mixinan on 2016/5/29.
 */
public class BlogListAdapter extends RecyclerView.Adapter<BlogListAdapter.MyViewHolder>{
    private Context context;
    private List<Blog> data;

    public BlogListAdapter(Context context, List<Blog> data) {
        this.context = context;
        this.data = data;
    }


    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item,null);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {

        final Blog blog = data.get(position);

        holder.title.setText(blog.getTitle());
        holder.time.setText(blog.getTime());
        holder.text.setText(blog.getContent());

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!NetUtil.haveNet(context)){
                    ToastUtil.showToast(context,"没有网络，再好的内容也打不开");
                }else{
                    MainActivity activity = (MainActivity) context;
                    Intent intent = new Intent();
                    intent.setClass(activity, BlogDetailActivity.class);
                    intent.putExtra("url", blog.getPath());
                    intent.putExtra("title",blog.getTitle());
                    intent.putExtra("text",blog.getContent());
                    intent.putExtra("position", position);
                    activity.startActivity(intent);
                    activity.stopRefreshing();
                }
            }
        });

        holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                RotateAnimation anim = new RotateAnimation(0f, 360f, RELATIVE_TO_SELF, 0.5F, RELATIVE_TO_SELF, 0.5F);
                anim.setDuration(500);
                holder.itemView.startAnimation(anim);
                return true;
            }
        });
    }

    @Override
    public int getItemCount() {
        return data == null ? 0 : data.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder{
        TextView title,time,text;

        public MyViewHolder(View itemView) {
            super(itemView);

            title = (TextView) itemView.findViewById(R.id.tv_title);
            time = (TextView) itemView.findViewById(R.id.tv_time);
            text = (TextView) itemView.findViewById(R.id.tv_content);
        }
    }

    public void removeData(int position){
        data.remove(position);
        notifyItemRemoved(position);
    }

    public void addData(int position,Blog blog){
        data.add(position,blog);
        notifyItemInserted(position);
    }
}
