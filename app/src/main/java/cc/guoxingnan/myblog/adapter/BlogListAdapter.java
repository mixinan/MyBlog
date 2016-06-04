package cc.guoxingnan.myblog.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

import cc.guoxingnan.myblog.entity.Blog;
import cc.guoxingnan.myblog.ui.BlogDetailActivity;
import cc.guoxingnan.myblog.R;
import cc.guoxingnan.myblog.ui.MainActivity;
import cc.guoxingnan.myblog.util.NetUtil;
import cc.guoxingnan.myblog.util.ToastUtil;

/**
 * Created by mixinan on 2016/5/29.
 */
public class BlogListAdapter extends RecyclerView.Adapter<BlogListAdapter.MyViewHolder>{
    private Context context;
    private ArrayList<Blog> data;

    public BlogListAdapter(Context context, ArrayList<Blog> data) {
        this.context = context;
        this.data = data;
    }


    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item,null);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, final int position) {

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
                    Intent intent = new Intent();
                    intent.setClass(context, BlogDetailActivity.class);
                    intent.putExtra("url", blog.getPath());
                    intent.putExtra("position", position);
                    context.startActivity(intent);

                    MainActivity activity = (MainActivity) context;
                    activity.stopRefreshing();
                }
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
