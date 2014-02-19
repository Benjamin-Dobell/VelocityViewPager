package au.com.glassechidna.velocityviewpager.sample;

import java.util.Comparator;
import java.util.Locale;
import java.util.Random;

import android.graphics.Color;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.ActionBar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.app.FragmentPagerAdapter;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import au.com.glassechidna.velocityviewpager.VelocityViewPager;

public class SampleActivity extends ActionBarActivity {

    /**
     * The {@link android.support.v4.view.PagerAdapter} that will provide
     * fragments for each of the sections. We use a
     * {@link FragmentPagerAdapter} derivative, which will keep every
     * loaded fragment in memory. If this becomes too memory intensive, it
     * may be best to switch to a
     * {@link android.support.v4.app.FragmentStatePagerAdapter}.
     */
    SectionsPagerAdapter mSectionsPagerAdapter;

    /**
     * The {@link ViewPager} that will host the section contents.
     */
    VelocityViewPager mViewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sample);

        // Create the adapter that will return a fragment for each of the three
        // primary sections of the activity.
        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());

        // Set up the ViewPager with the sections adapter.
        mViewPager = (VelocityViewPager) findViewById(R.id.pager);
        mViewPager.setOffscreenPageLimit(4);
        mViewPager.setAdapter(mSectionsPagerAdapter);
        mViewPager.setPageTransformer(false, new PageTransformer());
        mViewPager.setDrawOrderComparator(new ViewComparator());
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.sample, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    

    /**
     * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
     * one of the sections/tabs/pages.
     */
    public class SectionsPagerAdapter extends FragmentPagerAdapter {

        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            // getItem is called to instantiate the fragment for the given page.
            // Return a PlaceholderFragment (defined as a static inner class below).
            return PlaceholderFragment.newInstance(position + 1);
        }

        @Override
        public int getCount() {
            return 50;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            Locale l = Locale.getDefault();
            return getString(R.string.title_section, position).toUpperCase(l);
        }

        @Override
        public float getPageWidth(int position) {
            return 0.2f;
        }
    }

    public class PageTransformer implements VelocityViewPager.PageTransformer {

        private static final float MIN_SCALE = 0.55f;
        private static final float MAX_SCALE = 1.0f;
        private static final float MIN_ALPHA = 0.2f;
        private static final float MAX_ALPHA = 1.0f;

        public void transformPage(View view, float position) {
            int pageWidth = view.getWidth();

            position -= 2.0f * 0.2f;

            if (position < -1.0f) {
                view.setAlpha(0.0f);
            } else if (position <= 1.0f) {
                float factor = Math.abs(position);
                factor *= factor;
                float factorComplement = 1.0f - factor;

                float scale =  factorComplement * (MAX_SCALE - MIN_SCALE) + MIN_SCALE;
                view.setScaleX(scale);
                view.setScaleY(scale);

                float alpha = factorComplement * (MAX_ALPHA - MIN_ALPHA) + MIN_ALPHA;
                view.setAlpha(alpha);

                float translationFactor = 0.5f * (float) Math.sin(Math.PI * position - 0.5f * Math.PI) + 0.5f;
                translationFactor *= translationFactor;
                float translationX = -Math.signum(position) * 2.0f * pageWidth * translationFactor;
                view.setTranslationX(translationX);
            } else {
                view.setAlpha(0.0f);
            }
        }
    }

    public class ViewComparator implements Comparator<View> {

        @Override
        public int compare(View view, View view2) {
            final int middle = mViewPager.getWidth() / 2 + mViewPager.getScrollX();
            final float viewX = view.getX();
            final float view2X = view2.getX();
            final int viewDistance = Math.abs(middle - (int) (viewX + 0.5f * view.getWidth()));
            final int view2Distance = Math.abs(middle - (int) (view2X + 0.5f * view2.getWidth()));
            return view2Distance - viewDistance;
        }
    }

    /**
     * A placeholder fragment containing a simple view.
     */
    public static class PlaceholderFragment extends Fragment {
        /**
         * The fragment argument representing the section number for this
         * fragment.
         */
        private static final String ARG_SECTION_NUMBER = "section_number";

        /**
         * Returns a new instance of this fragment for the given section
         * number.
         */
        public static PlaceholderFragment newInstance(int sectionNumber) {
            PlaceholderFragment fragment = new PlaceholderFragment();
            Bundle args = new Bundle();
            args.putInt(ARG_SECTION_NUMBER, sectionNumber);
            fragment.setArguments(args);
            return fragment;
        }

        public PlaceholderFragment() {
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_sample, container, false);
            TextView textView = (TextView) rootView.findViewById(R.id.section_label);
            textView.setText(getString(R.string.title_section, getArguments().getInt(ARG_SECTION_NUMBER)));
            textView.setTextSize(24.0f);

            Random random = new Random();
            int backgroundColor = Color.argb(255, random.nextInt(256), random.nextInt(256), random.nextInt(256));
            rootView.setBackgroundColor(backgroundColor);

            return rootView;
        }
    }

}
