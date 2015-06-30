package com.cooltechworks.bitmapmerger;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;

import com.cooltechworks.bitmapmerger.tasks.BitmapMergerTask;
import com.cooltechworks.bitmapmerger.ui.fragments.BitmapAngleFragment;
import com.cooltechworks.bitmapmerger.ui.fragments.BitmapCenterFragment;
import com.cooltechworks.bitmapmerger.ui.fragments.BitmapOffsetFragment;


public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        showFragment(BitmapMergerTask.BitmapMergeOptions.MERGE_AT_CENTER);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        switch (id) {
            case R.id.action_angle:
                showFragment(BitmapMergerTask.BitmapMergeOptions.MERGE_AT_ANGLE_OFF);
                break;
            case R.id.action_offset:
                showFragment(BitmapMergerTask.BitmapMergeOptions.MERGE_FROM_TOP_LEFT);
                break;
            case R.id.action_center:
                showFragment(BitmapMergerTask.BitmapMergeOptions.MERGE_AT_CENTER);
        }

        return super.onOptionsItemSelected(item);
    }

    private void showFragment(BitmapMergerTask.BitmapMergeOptions mergeOptions) {

        Fragment fragment;

        switch (mergeOptions) {
            case MERGE_AT_ANGLE_OFF:
                fragment = new BitmapAngleFragment();
                break;
            case MERGE_FROM_TOP_LEFT:
                fragment = new BitmapOffsetFragment();
                break;
            default:
                fragment = new BitmapCenterFragment();
        }

        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, fragment).commit();


    }


}
