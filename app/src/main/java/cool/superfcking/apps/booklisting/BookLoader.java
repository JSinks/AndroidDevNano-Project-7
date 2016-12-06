package cool.superfcking.apps.booklisting;

import android.content.Context;
import android.content.AsyncTaskLoader;
import android.util.Log;

import java.util.List;

/**
 * Created by jsinclair on 5/12/16.
 */
public class BookLoader extends AsyncTaskLoader<List<Book>> {
    /** Tag for the log messages */
    public static final String LOG_TAG = Utils.class.getSimpleName();

    private final String BASE_BOOKS_URL = "https://www.googleapis.com/books/v1/volumes?q=";
    private String mUrl;

    public BookLoader(Context context, String query) {
        super(context);
        mUrl = BASE_BOOKS_URL + query;
    }

    @Override
    protected void onStartLoading() {
        forceLoad();
    }

    @Override
    public List<Book> loadInBackground() {
        List<Book> books = Utils.fetchBooks(mUrl);
        return books;
    }
}
