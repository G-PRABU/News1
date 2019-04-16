package com.example.android.news1;

import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import butterknife.BindView;

public class MainActivity extends AppCompatActivity {

    private ViewPager viewPager;
    private NewsFragmentPageAdapter fragmentPageAdapter;
    @BindView (R.id.tab_layout)
    TabLayout tabLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        viewPager = (ViewPager) findViewById(R.id.main_view_pager);
        tabLayout = (TabLayout) findViewById(R.id.tab_layout);
        fragmentPageAdapter = new NewsFragmentPageAdapter(getSupportFragmentManager());
        viewPager.setAdapter(fragmentPageAdapter);
        tabLayout.setupWithViewPager(viewPager);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.main_menu,menu);
        return true;
    }

    private void refresh() {
       fragmentPageAdapter = new NewsFragmentPageAdapter(getSupportFragmentManager());
       viewPager.setAdapter(fragmentPageAdapter);
       tabLayout.setupWithViewPager(viewPager);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if(id==R.id.refresh) {
            refresh();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
