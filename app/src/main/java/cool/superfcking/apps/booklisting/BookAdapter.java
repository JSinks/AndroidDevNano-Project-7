package cool.superfcking.apps.booklisting;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

/**
 * Created by jsinclair on 5/12/16.
 */
public class BookAdapter extends ArrayAdapter<Book> {

    public BookAdapter(Context context, List<Book> books){
        super(context, 0, books);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        final Book book = getItem(position);

        if (convertView == null ){
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.book_item, parent, false);
        }

        TextView titleText = (TextView) convertView.findViewById(R.id.bookTitle);
        TextView authorText = (TextView) convertView.findViewById(R.id.bookAuthors);

        titleText.setText(book.getTitle());
        authorText.setText(book.getAuthor());

        return convertView;

    }
}
