package fr.medes.android.app;

import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;

import fr.medes.android.R;

public class ViewPagerIndicatorActivity extends AppCompatActivity {

    private ViewPager mViewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Initialize the indicator. We need some information here:
        // * What page do we start on.
        // * How many pages are there in total
        // * A callback to get page titles
        setContentView(R.layout.aml__view_pager);

        // Retrieve our viewpager
        mViewPager = (ViewPager) findViewById(R.id.aml__pager);
    }

    public ViewPager getViewPager() {
        return mViewPager;
    }

    public void changePageIfNeeded() {
        int current = mViewPager.getCurrentItem();
        int count = mViewPager.getAdapter().getCount();
        if (current >= count) {
            mViewPager.setCurrentItem(count - 1, true);
        }
    }

}