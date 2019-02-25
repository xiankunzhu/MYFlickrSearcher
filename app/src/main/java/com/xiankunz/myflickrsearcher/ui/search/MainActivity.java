package com.xiankunz.myflickrsearcher.ui.search;

import android.app.Activity;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AutoCompleteTextView;
import android.widget.Button;

import com.google.gson.Gson;
import com.xiankunz.myflickrsearcher.R;
import android.support.v7.widget.RecyclerView;

import com.xiankunz.myflickrsearcher.model.Photo;
import com.xiankunz.myflickrsearcher.model.Result;
import com.xiankunz.myflickrsearcher.ui.search.SearchResultAdapter;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private final String TAG = "MainActivity";
    private final int SPAN_COUNT = 2;
    private Button mSearchBtn;
    private RecyclerView mSearchResultRV;
    private AutoCompleteTextView mSearchInputACTV;
    private Toolbar myToolbar;
    private boolean mIsCurrentLineLayout = true;
    private RecyclerView.LayoutManager mLayoutManager;

    private SearchResultAdapter mRecycleViewAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        myToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(myToolbar);

        mSearchInputACTV = (AutoCompleteTextView) findViewById(R.id.search_input_auto);

        initializeSearchButton();
        initializeSearchResultRecyclerView();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_toggle_layout:
                toggleRecyclerViewLayoutManager(item);
                return true;

            default:
                // If we got here, the user's action was not recognized.
                // Invoke the superclass to handle it.
                return super.onOptionsItemSelected(item);

        }
    }

    private void toggleRecyclerViewLayoutManager(MenuItem item) {
        int scrollPosition = 0;

        // If a layout manager has already been set, get current scroll position.
        if (mSearchResultRV.getLayoutManager() != null) {
            scrollPosition = ((LinearLayoutManager) mSearchResultRV.getLayoutManager())
                    .findFirstCompletelyVisibleItemPosition();
        }
        if (mIsCurrentLineLayout) {
            mLayoutManager = new GridLayoutManager(this, SPAN_COUNT);
            item.setIcon(R.drawable.images_list); // if new is grid layout, then menu change back to list
        } else {
            mLayoutManager = new LinearLayoutManager(this);
            item.setIcon(R.drawable.images_grid);
        }
        mIsCurrentLineLayout = !mIsCurrentLineLayout;
        mSearchResultRV.setLayoutManager(mLayoutManager);
        mSearchResultRV.scrollToPosition(scrollPosition);

    }

    public class SearchResultTask extends AsyncTask<Void, Void, List<Photo>> {
        final String TAG = "SearchResultTask";
        final static String queryUrlBegin = "https://api.flickr.com/services/rest/?method=flickr.photos.search&api_key=675894853ae8ec6c242fa4c077bcf4a0&text=";
        final static String queryUrlTrail = "&extras=url_s&format=json&nojsoncallback=1";

        final int READ_TIMEOUT = 15; // unit is second
        String queryUrl;

        public SearchResultTask(String queryParam) {
            this.queryUrl = queryUrlBegin + queryParam + queryUrlTrail;
        }

        @Override
        protected List<Photo> doInBackground(Void... params) {
            Log.d(TAG, "query url is : " + this.queryUrl);
            List<Photo> photos = new ArrayList<>();
            try {
                URL url = new URL(queryUrl);
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.setRequestMethod("GET");
                connection.setReadTimeout(READ_TIMEOUT * 1000);
                connection.connect();

                InputStream content = connection.getInputStream();
                BufferedReader reader = new BufferedReader(new InputStreamReader(content));
                Gson gson = new Gson();
                Result searchResult = gson.fromJson(reader, Result.class);
                if (searchResult.getStat().equals("ok")) {
                    photos.addAll(searchResult.getPhotos().getPhotoList());
                }

            } catch (IOException e) {
                e.printStackTrace();
            }
            return photos;
        }

        @Override
        protected void onPostExecute(List<Photo> photos) {
            mRecycleViewAdapter = new SearchResultAdapter(MainActivity.this, photos);
            mSearchResultRV.setAdapter(mRecycleViewAdapter);
            mRecycleViewAdapter.notifyDataSetChanged();
        }
    }

    private void doSearch() {
        String queryParams = "";
        if (mSearchInputACTV.getText().toString() != null && !mSearchInputACTV.getText().toString().isEmpty()) {
            queryParams = mSearchInputACTV.getText().toString();
        } else {
            queryParams = mSearchInputACTV.getHint().toString();
        }

        new SearchResultTask(queryParams).execute();
    }

    private void initializeSearchButton() {
        mSearchBtn = (Button) findViewById(R.id.search_btn);
        mSearchBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                doSearch();
                hideKeyboard();
            }
        });
    }

    public void hideKeyboard() {
        InputMethodManager imm = (InputMethodManager) this.getSystemService(Activity.INPUT_METHOD_SERVICE);
        View view = this.getCurrentFocus();
        if (view != null) {
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }

    private void initializeSearchResultRecyclerView() {
        mLayoutManager = new LinearLayoutManager(this);
        mSearchResultRV = (RecyclerView) findViewById(R.id.search_result_recyleview);
        mSearchResultRV.setHasFixedSize(true);
        mSearchResultRV.setLayoutManager(mLayoutManager);
    }
}
