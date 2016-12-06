package cool.superfcking.apps.booklisting;

import android.app.LoaderManager;
import android.content.Context;
import android.content.Intent;
import android.content.Loader;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<List<Book>> {

    private final int LOADER_ID = 1;

    private BookAdapter mAdapter;
    private TextView mEmptyView;
    private ProgressBar mProgressBar;
    private EditText mSearchQuery;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mProgressBar = (ProgressBar) findViewById(R.id.bookProgress);
        mProgressBar.setVisibility(View.GONE);

        mEmptyView = (TextView) findViewById(R.id.emptyList);

        ListView bookListView = (ListView) findViewById(R.id.bookList);
        bookListView.setEmptyView(mEmptyView);

        mAdapter = new BookAdapter(MainActivity.this, new ArrayList<Book>());
        bookListView.setAdapter(mAdapter);

        bookListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                Book currentBook = mAdapter.getItem(position);
                Uri bookUri = Uri.parse(currentBook.getUrl());
                Intent websiteIntent = new Intent(Intent.ACTION_VIEW, bookUri);
                startActivity(websiteIntent);
            }
        });

        mSearchQuery = (EditText) findViewById(R.id.inputText);

        Button searchButton = (Button) findViewById(R.id.inputButton);
        searchButton.setOnClickListener(new Button.OnClickListener(){

            @Override
            public void onClick(View view) {
                InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(mSearchQuery.getWindowToken(),
                        InputMethodManager.RESULT_UNCHANGED_SHOWN);

                getLoaderManager().restartLoader(LOADER_ID, null, MainActivity.this);
            }
        });

        getLoaderManager().initLoader(LOADER_ID, null, this);

    }

    @Override
    public Loader<List<Book>> onCreateLoader(int i, Bundle bundle) {
        mProgressBar.setVisibility(View.VISIBLE);

        String query = mSearchQuery.getText().toString().trim();
        return new BookLoader(this, query);
    }

    @Override
    public void onLoadFinished(Loader<List<Book>> loader, List<Book> books) {
        mAdapter.clear();
        mProgressBar.setVisibility(View.GONE);

        ConnectivityManager cm = (ConnectivityManager) getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        boolean isConnected = activeNetwork != null &&
                activeNetwork.isConnectedOrConnecting();

        if(isConnected){
            mEmptyView.setText(R.string.error_no_books);
        } else {
            mEmptyView.setText(R.string.error_no_internet);
        }

        if (books != null && !books.isEmpty()) {
            mAdapter.addAll(books);
        }
    }

    @Override
    public void onLoaderReset(Loader<List<Book>> loader) {
        mAdapter.clear();
        mProgressBar.setVisibility(View.GONE);
    }
}
