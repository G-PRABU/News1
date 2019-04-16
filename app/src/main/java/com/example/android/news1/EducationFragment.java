package com.example.android.news1;

import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.Loader;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;


public class EducationFragment extends Fragment implements LoaderManager.LoaderCallbacks<List<News>>,SwipeRefreshLayout.OnRefreshListener {

    private static final String GUARDIAN_REQUEST_URL = "https://content.guardianapis.com/search?q=education&show-tags=contributor&show-fields=thumbnail&api-key=22a2e1c8-2523-49f2-8ffb-d8d8a3a3b5c9";
    private NewsAdapter newsAdapter;
    private static final int NEWS_LOADER_ID = 1;
    private boolean isLoaderStarted;
    @BindView(R.id.education_empty_tv) TextView emptyTextView;
    @BindView(R.id.education_lv) ListView educationNewsList;
    @BindView(R.id.education_loading_indicator) ProgressBar educationLoadingIndicator;
    @BindView(R.id.education_swipe_layout) SwipeRefreshLayout swipeLayout;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View educationNewsView = inflater.inflate(R.layout.fragment_education, container, false);
        ButterKnife.bind(this,educationNewsView);
        swipeLayout.setColorSchemeColors(ContextCompat.getColor(getContext(), R.color.colorAccent));
        swipeLayout.setOnRefreshListener(this);
        newsAdapter = new NewsAdapter(getActivity(),new ArrayList<News>());
        educationNewsList.setAdapter(newsAdapter);
        educationNewsList.setEmptyView(emptyTextView);
        if(NewsUtils.isConnected(getContext())) {
            educationLoadingIndicator.setVisibility(View.VISIBLE);
            getLoaderManager().initLoader(NEWS_LOADER_ID, null, this);
            isLoaderStarted = true;
        } else {
            educationLoadingIndicator.setVisibility(View.INVISIBLE);
            emptyTextView.setText(getString(R.string.no_internet_connection));
        }
        educationNewsList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                News clickedNews = newsAdapter.getItem(position);
                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.setData(Uri.parse(clickedNews.getNewsUrl()));
                startActivity(intent);
            }
        });
        return educationNewsView;
    }

    @NonNull
    @Override
    public Loader<List<News>> onCreateLoader(int id, @Nullable Bundle args) {
        return new NewsLoader(getContext(),GUARDIAN_REQUEST_URL);
    }

    @Override
    public void onLoadFinished(@NonNull Loader<List<News>> loader, List<News> data) {
        educationLoadingIndicator.setVisibility(View.INVISIBLE);
        swipeLayout.setRefreshing(false);
        newsAdapter.clear();
        if(data!=null && !data.isEmpty()){
            newsAdapter.addAll(data);
        } else {
            emptyTextView.setText(getString(R.string.no_data_fond));
        }
    }

    @Override
    public void onLoaderReset(@NonNull Loader<List<News>> loader) {
        newsAdapter.clear();
    }

    @Override
    public void onRefresh() {
        newsAdapter.clear();
        if(NewsUtils.isConnected(getContext())){
            if(isLoaderStarted) {
                getLoaderManager().restartLoader(NEWS_LOADER_ID, null, this);
            } else {
                getLoaderManager().initLoader(NEWS_LOADER_ID, null, this);
            }
        } else {
            swipeLayout.setRefreshing(false);
            emptyTextView.setText(getString(R.string.no_internet_connection));
        }
    }
}
