package com.example.bookspage; // IMPORTANT: Ensure this package name matches your project's manifest and other files

import android.content.res.ColorStateList;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import android.view.View;

import androidx.appcompat.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import androidx.core.content.ContextCompat;

import android.view.LayoutInflater;


public class BookDetailActivity extends AppCompatActivity {
    // UI elements for purchase form
    private EditText etAddress;
    private EditText etPhoneNumber;
    private TextView tvGeneralErrorMessage;

    // Class-level variables to store data retrieved from Intent
    private String bookTitle;
    private String bookAuthor;
    private String bookSynopsis;
    private int bookCoverResId;
    private String bookPrice;

    // UI element variables (named to match your layout IDs based on your provided code)
    private TextView tvBookDetailTitle; // This might be for a general "Book Detail" header, or the main book title
    private ImageView detailBookCover;
    private TextView detailSynopsisLabel; // This is likely a static "Synopsis" label
    private TextView detailBookSynopsis; // This TextView will display the actual book's synopsis
    private TextView bookTitleTextView; // This TextView will display the actual book's title
    private TextView detailBookAuthor; // This TextView will display the actual book's author
    private TextView tvBookPrice; // This TextView will display the actual book's price


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_detail);

        // --- 1. Find all your UI elements from activity_book_detail.xml ---
        tvBookDetailTitle = findViewById(R.id.tv_book_detail_title); // Assuming this is for a general header or main title
        detailBookCover = findViewById(R.id.detail_book_cover);
        detailSynopsisLabel = findViewById(R.id.detail_synopsis_label); // This seems to be a static label
        detailBookSynopsis = findViewById(R.id.detail_book_synopsis); // This is where the actual synopsis will go
        bookTitleTextView = findViewById(R.id.detail_book_title); // This is where the actual book title will go
        detailBookAuthor = findViewById(R.id.detail_book_author); // This is where the actual author will go
        tvBookPrice = findViewById(R.id.tv_book_price); // This is where the actual price will go

        etAddress = findViewById(R.id.et_address);
        etPhoneNumber = findViewById(R.id.et_phone_number);
        tvGeneralErrorMessage = findViewById(R.id.tv_general_error_message);

        Button btnBack = findViewById(R.id.btn_back);
        Button btnBuy = findViewById(R.id.btn_buy);


        // --- 2. Retrieve data passed via Intent ---
        // Using getIntent().getStringExtra/getIntExtra is safer than getExtras().getString/getInt
        // as it allows for default values and avoids crashes if an extra is missing.
        Intent intent = getIntent();
        if (intent != null) {
            bookTitle = intent.getStringExtra("book_title");
            bookAuthor = intent.getStringExtra("book_author");
            bookSynopsis = intent.getStringExtra("book_synopsis");
            // Provide a default image resource ID (e.g., R.drawable.mocking_bird)
            // in case the extra isn't found, to prevent crashes.
            bookCoverResId = intent.getIntExtra("book_cover_res_id", R.drawable.mocking_bird); //
            bookPrice = intent.getStringExtra("book_price");
        }


        // --- 3. Display the retrieved data on your UI elements ---
        // Populate the TextViews and ImageView with the actual book data.
        if (bookTitleTextView != null && bookTitle != null) {
            bookTitleTextView.setText(bookTitle); // Set the actual book title
        }
        // If tvBookDetailTitle is meant for a static "Book Detail" header, keep it as is.
        // If it's *also* meant to display the book title, uncomment the line below.
        /*
        if (tvBookDetailTitle != null && bookTitle != null) {
            tvBookDetailTitle.setText(bookTitle);
        } else if (tvBookDetailTitle != null) {
            tvBookDetailTitle.setText("Book Detail"); // Fallback or static header
        }
        */

        if (detailBookCover != null && bookCoverResId != 0) {
            detailBookCover.setImageResource(bookCoverResId);
        } else if (detailBookCover != null) {
            // Fallback to a default image if bookCoverResId is 0 or extra wasn't found
            detailBookCover.setImageResource(R.drawable.mocking_bird); // Use your default image resource
        }

        if (detailBookSynopsis != null && bookSynopsis != null) {
            detailBookSynopsis.setText(bookSynopsis);
        }

        if (detailBookAuthor != null && bookAuthor != null) {
            detailBookAuthor.setText(bookAuthor);
        }

        if (tvBookPrice != null && bookPrice != null) {
            tvBookPrice.setText("Price: " + bookPrice);
        } else if (tvBookPrice != null) {
            tvBookPrice.setText("Price: N/A"); // Display "N/A" if price is not available
        }


        // --- 4. Set click listeners for buttons ---
        btnBack.setOnClickListener(v -> finish());

        btnBuy.setOnClickListener(v -> {
            // Clear previous error messages
            tvGeneralErrorMessage.setText("");
            tvGeneralErrorMessage.setVisibility(View.GONE);

            String address = etAddress.getText().toString().trim();
            String phoneNumber = etPhoneNumber.getText().toString().trim();

            boolean isValid = true;
            String errorMessage = "";

            // Address Validation
            if (address.isEmpty()) {
                errorMessage = "Please fill your Address.";
                isValid = false;
            }

            // Phone Number Validation (only if address is valid)
            if (isValid) {
                if (phoneNumber.isEmpty()) {
                    errorMessage = "Please fill your Phone Number.";
                    isValid = false;
                } else if (!phoneNumber.matches("^[0-9]+$")) {
                    errorMessage = "Phone number must contain only digits.";
                    isValid = false;
                }
            }

            // Display error or proceed
            if (!isValid) {
                tvGeneralErrorMessage.setText(errorMessage);
                tvGeneralErrorMessage.setVisibility(View.VISIBLE);
            } else {
                showEmailConfirmationDialogAndGoBack();
            }
        });
    }


    /**
     * Displays a custom AlertDialog to confirm email has been sent and navigates back to HomeActivity on OK.
     */
    private void showEmailConfirmationDialogAndGoBack() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        // 1. Inflate the custom layout for the dialog
        LayoutInflater inflater = getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.dialog_custom_confirmation, null);
        builder.setView(dialogView); // Set the custom view to the AlertDialog

        // 2. Find views within the custom dialog layout
        TextView tvConfirmationMessage = dialogView.findViewById(R.id.tv_confirmation_message_dialog);
        Button btnDialogOk = dialogView.findViewById(R.id.btn_dialog_ok);

        // 3. Create the AlertDialog
        AlertDialog dialog = builder.create();

        // 4. Apply rounded corners to the dialog window (using your existing drawable)
        if (dialog.getWindow() != null) {
            dialog.getWindow().setBackgroundDrawableResource(R.drawable.rounded_dialog_background);
        }

        dialog.show(); // Show the dialog before setting button listeners

        // 5. Set the "OK" button color and click listener
        if (btnDialogOk != null) {
            int buttonColor = ContextCompat.getColor(this, R.color.ok_button_color);
            btnDialogOk.setBackgroundTintList(ColorStateList.valueOf(buttonColor));

            btnDialogOk.setOnClickListener(v -> {
                dialog.dismiss(); // Close the dialog

                // Navigate back to HomeActivity
                Intent intent = new Intent(BookDetailActivity.this, HomeActivity.class); // Ensure this is HomeActivity.class
                // Optional: If you want to clear all other activities from the stack and go straight to HomeActivity's root:
                // intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent); // Start HomeActivity

                finish(); // Finish the current BookDetailActivity
            });
        }
    }

}