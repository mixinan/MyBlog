package cc.guoxingnan.myblog;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

/**
 * Created by mixinan on 2016/5/22.
 */
public class BlogAdapter extends BaseAdapter {
    private Context mContext;
    private List<Blog> data;

    public BlogAdapter(Context mContext, List<Blog> data) {
        this.mContext = mContext;
        this.data = data;
    }

    @Override
    public int getCount() {
        return data == null ? 0 : data.size();
    }

    @Override
    public Object getItem(int position) {
        return data.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null){
            convertView = LayoutInflater.from(mContext).inflate(R.layout.item,null);
            holder = new ViewHolder();
            holder.title = (TextView) convertView.findViewById(R.id.tv_title);
            holder.time = (TextView) convertView.findViewById(R.id.tv_time);
            holder.content = (TextView) convertView.findViewById(R.id.tv_content);
            convertView.setTag(holder);
        }else {
            holder = (ViewHolder) convertView.getTag();
        }
        Blog blog = data.get(position);
        holder.title.setText(blog.getTitle());
        holder.time.setText(blog.getTime());
        holder.content.setText(blog.getContent());

        return convertView;
    }

    class ViewHolder{
        TextView title;
        TextView time;
        TextView content;
    }
}
