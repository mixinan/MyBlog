package cc.guoxingnan.myblog.ui;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ToggleButton;

import cc.guoxingnan.myblog.App;
import cc.guoxingnan.myblog.R;

public class SettingActivity extends AppCompatActivity implements View.OnClickListener{
    private ToggleButton soundBt;
    private App app;
    private boolean haveSound;
    private Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);

        app = (App) getApplication();
        haveSound = app.getSoundState();

        setViews();
        setListeners();

    }

    private void setListeners() {
        soundBt.setOnClickListener(this);
    }

    private void setViews() {
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("设置");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        soundBt = (ToggleButton) findViewById(R.id.btSound);
        soundBt.setChecked(haveSound);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btSound:
                if (soundBt.isChecked()){
                    app.openSound();
                }else {
                    app.closeSound();
                }
                break;
        }
    }


    /*
     * 以下为菜单相关代码
     */

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        switch (id){
            case android.R.id.home:   //返回箭头的ID
                finish();
                break;
        }
        return super.onOptionsItemSelected(item);
    }
}
