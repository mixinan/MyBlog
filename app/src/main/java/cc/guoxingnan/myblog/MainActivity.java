package cc.guoxingnan.myblog;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    private ListView lv;
    private BlogAdapter adapter;
    private BlogModule module;
    private ArrayList<Blog> blogs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initView();
        initData();
        setListener();
    }


    private void initView() {
        lv = (ListView) findViewById(R.id.lv);
    }


    private void initData() {
        module = new BlogModule(this);
    }

    /**
     * 在Module层调用，传入获取的数据集合，防止在Activity里数据为空
     * @param blogs
     */
    public void setMyAdapter(ArrayList<Blog> blogs) {
        this.blogs = blogs;
        adapter = new BlogAdapter(this, blogs);
        lv.setAdapter(adapter);
    }


    private void setListener() {
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent();
                intent.setClass(MainActivity.this, BlogDetailActivity.class);
                intent.putExtra("url", blogs.get(position).getPath());
                intent.putExtra("position", position);
                startActivity(intent);
            }
        });
    }


}
