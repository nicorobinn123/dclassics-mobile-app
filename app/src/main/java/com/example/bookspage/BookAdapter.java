package com.example.bookspage;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.content.Intent; // Import Intent

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.bookspage.R;

import java.util.List;

public class BookAdapter extends RecyclerView.Adapter<BookAdapter.BookViewHolder> {

    private List<Book> bookList;

    public BookAdapter(List<Book> bookList) {
        this.bookList = bookList;
    }

    // New method to update data
    @SuppressLint("NotifyDataSetChanged")
    public void updateData(List<Book> newBookList) {
        this.bookList = newBookList;
        notifyDataSetChanged(); // Tells the RecyclerView to redraw all items
    }

    @NonNull
    @Override
    public BookViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_books, parent, false);
        return new BookViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull BookViewHolder holder, int position) {
        final Book book = bookList.get(position);
        holder.bookCoverImageView.setImageResource(book.getCoverResourceId());
        holder.synopsisLabelTextView.setText("Synopsis"); // Ensure this is consistently set
        holder.synopsisTextView.setText(book.getSynopsis());
        holder.bookTitleTextView.setText(book.getTitle());
        holder.authorTextView.setText(book.getAuthor());

        // This is the listener for the entire item view.
        // It's good to keep this so users can click anywhere on the item.
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // We will use this same logic for the button
                startDetailActivity(v.getContext(), book);
            }
        });

        // *** THIS IS THE FIX ***
        // Set OnClickListener for the "Add to Cart" button to do the same thing.
        holder.addToCartButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // We start the detail activity here as well
                startDetailActivity(v.getContext(), book);
            }
        });
    }

    /**
     * Helper method to create an Intent and start the BookDetailActivity.
     * This avoids code duplication.
     * @param context The context from the view.
     * @param book The book to show details for.
     */
    private void startDetailActivity(Context context, Book book) {
        Intent intent = new Intent(context, BookDetailActivity.class);
        intent.putExtra("book_title", book.getTitle());
        intent.putExtra("book_author", book.getAuthor());
        intent.putExtra("book_synopsis", book.getSynopsis());
        intent.putExtra("book_cover_res_id", book.getCoverResourceId());
        intent.putExtra("book_price", book.getPrice()); // Pass the price here
        context.startActivity(intent);
    }

    @Override
    public int getItemCount() {
        return bookList.size();
    }

    public static class BookViewHolder extends RecyclerView.ViewHolder {
        ImageView bookCoverImageView;
        TextView synopsisLabelTextView;
        TextView synopsisTextView;
        TextView bookTitleTextView;
        TextView authorTextView;
        Button addToCartButton;

        public BookViewHolder(@NonNull View itemView) {
            super(itemView);
            bookCoverImageView = itemView.findViewById(R.id.bookCoverImageView);
            synopsisLabelTextView = itemView.findViewById(R.id.synopsisLabelTextView);
            synopsisTextView = itemView.findViewById(R.id.synopsisTextView);
            bookTitleTextView = itemView.findViewById(R.id.bookTitleTextView);
            authorTextView = itemView.findViewById(R.id.authorTextView);
            addToCartButton = itemView.findViewById(R.id.addToCartButton);
        }
    }
}
