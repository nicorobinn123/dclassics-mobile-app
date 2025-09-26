package com.example.bookspage;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.widget.TextView;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.widget.PopupWindow;

import java.util.ArrayList;
import java.util.List;

import androidx.core.content.ContextCompat;

public class MainActivity extends AppCompatActivity {

    private RecyclerView booksRecyclerView;
    private BookAdapter bookAdapter;
    private List<Book> nonFictionBooks;
    private List<Book> fictionBooks;

    private Button btnNonFiction;
    private Button btnFiction;
    private Button addToCartButton; // Make sure this is in your layout if used
    private ImageView ivMenuHamburger; // Renamed to match the variable name


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_books);

        // Make sure this ID matches your layout file for the hamburger icon
        ivMenuHamburger = findViewById(R.id.iv_menu_books); // Assuming R.id.iv_menu_hamburger from my previous example, adjust if yours is different

        ivMenuHamburger.setOnClickListener(v -> showPopupMenu(v));


        booksRecyclerView = findViewById(R.id.rv_books);
        booksRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        btnNonFiction = findViewById(R.id.btn_non_fiction);
        btnFiction = findViewById(R.id.btn_fiction);

        // --- Prepare your book data ---
        nonFictionBooks = new ArrayList<>();
        nonFictionBooks.add(new Book(
                "To Kill A Mockingbird",
                "Harper Lee",
                "To Kill a Mockingbird is a powerful novel set in the racially charged American South during the 1930s. It follows young Scout Finch as she navigates childhood in the town of Maycomb, Alabama. Through the eyes of Scout, we witness her father, Atticus Finch, defend Tom Robinson, a Black man falsely accused of assaulting a white woman. The story explores themes of justice, morality, and the loss of innocence in a deeply divided society.",
                R.drawable.mockingbird,
                "100$"

        ));
        nonFictionBooks.add(new Book(
                "The Lord of the Rings",
                "J.R.R. Tolkien",
                "The Lord of the Rings is an epic fantasy tale set in Middle-earth, where a young hobbit named Frodo Baggins is tasked with destroying a powerful ring that could bring darkness to the world. Joined by a fellowship of allies, Frodo must journey across dangerous lands to Mount Doom, facing evil forces and inner struggles. The story explores themes of courage, friendship, and the fight against corruption.",
                R.drawable.lordoftherings,
                "150$"
        ));
        nonFictionBooks.add(new Book(
                "The Great Gatsby",
                "F. Scott Fitzgerald",
                "Set in the Roaring Twenties, The Great Gatsby tells the story of Jay Gatsby, a mysterious millionaire obsessed with rekindling a lost romance with Daisy Buchanan. Narrated by Nick Carraway, the novel explores themes of wealth, illusion, and the American Dream's decay.",
                R.drawable.greatgatsby,
                "75$"
        ));
        nonFictionBooks.add(new Book(
                "1984",
                "George Orwell",
                "In a dystopian future ruled by a totalitarian regime, 1984 follows Winston Smith, a man who begins to question the oppressive surveillance state led by Big Brother. As he seeks truth and freedom, the novel explores themes of control, propaganda, and individual thought.",
                R.drawable.georgeorwell,
                "50$"
        ));


        // --- Prepare Fiction book data ---
        fictionBooks = new ArrayList<>();
        fictionBooks.add(new Book(
                "Harry Potter",
                "J.K. Rowling",
                "Harry Potter follows a young wizard who discovers his magical heritage and attends Hogwarts School of Witchcraft and Wizardry. With the help of his friends, he faces dark forces—especially the dark wizard Voldemort—while uncovering secrets about his past and destiny.",
                R.drawable.harrypotter,
                "150$"
        ));
        fictionBooks.add(new Book(
                "The Brothers Karamazov",
                "Fyodor Dostoevsky",
                "A philosophical drama centered on the troubled Karamazov family, the novel explores deep questions of faith, free will, and morality. When the father is murdered, suspicion falls on his sons—each representing different aspects of human nature.",
                R.drawable.thebrothers,
                "200$"
        ));
        fictionBooks.add(new Book(
                "Gone With the Wind",
                "Margaret Mitchell",
                "Set during the American Civil War, Gone with the Wind follows Scarlett O’Hara, a determined Southern belle navigating love, survival, and social upheaval. The novel explores themes of resilience, pride, and the fall of the old South.",
                R.drawable.gonewiththewinds,
                "225$"
        ));
        fictionBooks.add(new Book(
                "Anne of Green Gables",
                "L.M. Montgomery",
                "When spirited orphan Anne Shirley is mistakenly sent to live with siblings Marilla and Matthew Cuthbert, she transforms their quiet lives in Avonlea. The novel celebrates imagination, belonging, and the joys and trials of growing up.",
                R.drawable.anneofgreengables,
                "300$"
        ));
        fictionBooks.add(new Book(
                "The Little Prince",
                "Antoine de Saint-Exupéry",
                "The Little Prince is a poetic tale of a young prince who travels from planet to planet, meeting quirky characters and learning life lessons. Through his journey and conversations, the story reflects on childhood, love, loss, and the importance of seeing with the heart.",
                R.drawable.thelittleprince,
                "125$"
        ));


        // --- Initialize and set the adapter with non-fiction books by default ---
        bookAdapter = new BookAdapter(nonFictionBooks);
        booksRecyclerView.setAdapter(bookAdapter);

        // --- Set initial button state ---
        selectButton(btnNonFiction);

        // --- Set up button click listeners ---
        btnNonFiction.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bookAdapter.updateData(nonFictionBooks);
                selectButton(btnNonFiction);
            }
        });

        btnFiction.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bookAdapter.updateData(fictionBooks);
                selectButton(btnFiction);
            }
        });
    }


    private void selectButton(Button selectedButton) {
        btnNonFiction.setBackgroundResource(R.drawable.filter_button_unselected_bordered);
        btnNonFiction.setTextColor(getResources().getColor(R.color.white));
        btnFiction.setBackgroundResource(R.drawable.filter_button_unselected_bordered);
        btnFiction.setTextColor(getResources().getColor(R.color.white));

        selectedButton.setBackgroundResource(R.drawable.filter_button_selected_white);
        selectedButton.setTextColor(getResources().getColor(R.color.black));
    }

    private void showPopupMenu(View anchorView) {
        LayoutInflater inflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
        View popupView = inflater.inflate(R.layout.popup_menu_layout, null);

        popupView.measure(View.MeasureSpec.UNSPECIFIED, View.MeasureSpec.UNSPECIFIED);

        PopupWindow popupWindow = new PopupWindow(
                popupView,
                getResources().getDimensionPixelSize(R.dimen.popup_menu_width),
                popupView.getMeasuredHeight(),
                true
        );
        popupWindow.setBackgroundDrawable(ContextCompat.getDrawable(this, android.R.color.transparent));

        TextView tvHome = popupView.findViewById(R.id.tv_menu_home);
        TextView tvOurStore = popupView.findViewById(R.id.tv_menu_our_store);
        Button btnLogOut = popupView.findViewById(R.id.btn_menu_logout);

        tvHome.setOnClickListener(v -> {
            popupWindow.dismiss();
            // Implement navigation for "All Books" to go to MainActivity
            Intent homeIntent = new Intent(MainActivity.this, MainActivity.class);
            startActivity(homeIntent);
            // Optionally, if you want HomeActivity to be finished after navigating to MainActivity:
            // finish();
        });

        tvOurStore.setOnClickListener(v -> {
            popupWindow.dismiss();
            // Implement navigation to Our Store here
        });

        btnLogOut.setOnClickListener(v -> {
            popupWindow.dismiss();
            // Implement your logout logic here
        });
        int xOffset = -8;
        int yOffset = 4;

        popupWindow.showAsDropDown(anchorView, xOffset, yOffset, Gravity.END | Gravity.TOP);
    }
}