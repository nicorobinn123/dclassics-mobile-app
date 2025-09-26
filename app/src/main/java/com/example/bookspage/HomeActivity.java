package com.example.bookspage;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.Interpolator;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.Scroller;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.ViewPager2;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import adapter.BookAdapter;
import adapter.StoreAdapter;
import model.Book;
import model.Store;

public class HomeActivity extends AppCompatActivity {

    private ViewPager2 viewPagerStores;
    private RecyclerView featuredBooksRecyclerView;
    private StoreAdapter storeAdapter;
    private BookAdapter featuredBookAdapter;
    private List<Store> popularStoresList = new ArrayList<>();
    private List<Book> featuredBooksList = new ArrayList<>();

    private Handler sliderHandler = new Handler(Looper.getMainLooper());
    private Runnable sliderRunnable;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home); // Ensure you have activity_home.xml

        // --- Find all views by their ID ---
        viewPagerStores = findViewById(R.id.view_pager_stores);
        featuredBooksRecyclerView = findViewById(R.id.rv_featured_books);
        ImageButton btnPrev = findViewById(R.id.btn_prev);
        ImageButton btnNext = findViewById(R.id.btn_next);
        ImageView ivMenu = findViewById(R.id.iv_menu); // This is your hamburger icon
        TextView greetingText = findViewById(R.id.greetingText);

        // --- Set up dynamic greeting ---
        String username = getIntent().getStringExtra("USERNAME_EXTRA");
        if (username == null || username.isEmpty()) {
            username = "User"; // Default if username is not passed
        }
        // Ensure you have a string resource named 'hello_user' like "Hello, %1$s!"
        greetingText.setText(getString(R.string.hello_user, username));


        // --- Setup Store Carousel ---
        loadPopularStores();
        storeAdapter = new StoreAdapter(this, popularStoresList);
        viewPagerStores.setAdapter(storeAdapter);

        // --- Visual settings for ViewPager2 ---
        viewPagerStores.setClipToPadding(false);
        viewPagerStores.setClipChildren(false);
        viewPagerStores.setOffscreenPageLimit(3);
        viewPagerStores.getChildAt(0).setOverScrollMode(RecyclerView.OVER_SCROLL_NEVER);

        int middlePosition = Integer.MAX_VALUE / 2;
        viewPagerStores.setCurrentItem(middlePosition - (middlePosition % popularStoresList.size()), false);


        // --- Setup Featured Books RecyclerView ---
        loadFeaturedBooks();
        featuredBooksRecyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        // Ensure BookAdapter has a constructor that accepts Context, List<Book>, and a type parameter
        featuredBookAdapter = new BookAdapter(this, featuredBooksList, BookAdapter.TYPE_FEATURED);
        featuredBooksRecyclerView.setAdapter(featuredBookAdapter);

        // --- Button clicks ---
        btnPrev.setOnClickListener(v -> viewPagerStores.setCurrentItem(viewPagerStores.getCurrentItem() - 1, true));
        btnNext.setOnClickListener(v -> viewPagerStores.setCurrentItem(viewPagerStores.getCurrentItem() + 1, true));

        ivMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showPopupMenu(v);
            }
        });


        // --- Auto-Scroll logic ---
        sliderRunnable = () -> viewPagerStores.setCurrentItem(viewPagerStores.getCurrentItem() + 1, true);

        viewPagerStores.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
                sliderHandler.removeCallbacks(sliderRunnable);
                sliderHandler.postDelayed(sliderRunnable, 3000);
            }
        });

        // --- Scroll speed control ---
        try {
            Field scrollerField = ViewPager2.class.getDeclaredField("mScroller");
            scrollerField.setAccessible(true);
            FixedSpeedScroller scroller = new FixedSpeedScroller(viewPagerStores.getContext());
            scrollerField.set(viewPagerStores, scroller);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void showPopupMenu(View anchorView) {
        LayoutInflater inflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
        View popupView = inflater.inflate(R.layout.popup_menu_layout, null); // Ensure you have popup_menu_layout.xml

        popupView.measure(View.MeasureSpec.UNSPECIFIED, View.MeasureSpec.UNSPECIFIED);

        PopupWindow popupWindow = new PopupWindow(
                popupView,
                getResources().getDimensionPixelSize(R.dimen.popup_menu_width), // Ensure popup_menu_width in dimens.xml
                popupView.getMeasuredHeight(),
                true
        );
        popupWindow.setBackgroundDrawable(ContextCompat.getDrawable(this, android.R.color.transparent)); // Background transparency

        TextView tvHome = popupView.findViewById(R.id.tv_menu_home);
        TextView tvOurStore = popupView.findViewById(R.id.tv_menu_our_store);
        Button btnLogOut = popupView.findViewById(R.id.btn_menu_logout);

        tvHome.setOnClickListener(v -> {
            popupWindow.dismiss();
            // Implement navigation for "All Books" to go to MainActivity
            Intent homeIntent = new Intent(HomeActivity.this, MainActivity.class);
            startActivity(homeIntent);
            // Optionally, if you want HomeActivity to be finished after navigating to MainActivity:
            // finish();
        });

        tvOurStore.setOnClickListener(v -> {
            popupWindow.dismiss();
            // Implement navigation for "Our Store" to go to StoresActivity
            Intent storeIntent = new Intent(HomeActivity.this, StoresActivity.class);
            startActivity(storeIntent);
        });

        btnLogOut.setOnClickListener(v -> {
            popupWindow.dismiss();
            // Logout logic: Navigate back to LoginActivity and clear the activity stack
            Intent logoutIntent = new Intent(HomeActivity.this, LoginActivity.class);
            logoutIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK); // Clears activity history
            startActivity(logoutIntent); // Starts LoginActivity
            finish(); // Finishes HomeActivity so it's not on the back stack
        });

        // Positioning for the popup menu
        int xOffset = -8; // Aligns right edge of popup with right edge of anchor, with 8dp margin
        int yOffset = 4;  // Positions popup 4dp below the anchor
        popupWindow.showAsDropDown(anchorView, xOffset, yOffset, Gravity.END | Gravity.TOP);
    }


    private void loadPopularStores() {
        popularStoresList.clear();
        popularStoresList.add(new Store("s1", "DAUNT BOOKS", R.drawable.dauntbooks, "83", "Popular"));
        popularStoresList.add(new Store("s2", "City Lights", R.drawable.store, "75", "Popular"));
        popularStoresList.add(new Store("s3", "Shakespeare & Co", R.drawable.balfourbooks, "90", "Popular"));
        popularStoresList.add(new Store("s4", "Barnes Bookshop", R.drawable.barnesbookshop, "90", "Popular"));
    }

    private void loadFeaturedBooks() {
        featuredBooksList.clear();
        featuredBooksList.add(new Book("b1", "To Kill A Mockingbird", "Harper Lee", R.drawable.mocking_bird, "To Kill a Mockingbird is a powerful novel set in the racially charged American South during the 1930s. It follows young Scout Finch as she navigates childhood in the town of Maycomb, Alabama. Through the eyes of Scout, we witness her father, Atticus Finch, defend Tom Robinson, a Black man falsely accused of assaulting a white woman. The story explores themes of justice, morality, and the loss of innocence in a deeply divided society.", "100$", "Fiction"));
        featuredBooksList.add(new Book("b2", "The Lord of The Rings", "J.R.R. Tolkien", R.drawable.lord_of_rings, "The Lord of the Rings is an epic fantasy tale set in Middle-earth, where a young hobbit named Frodo Baggins is tasked with destroying a powerful ring that could bring darkness to the world. Joined by a fellowship of allies, Frodo must journey across dangerous lands to Mount Doom, facing evil forces and inner struggles. The story explores themes of courage, friendship, and the fight against corruption.","150$", "Fiction"));
        featuredBooksList.add(new Book("b3", "The Great Gatsby", "F. Scott Fitzgerald", R.drawable.great_gatsby, "Set in the Roaring Twenties, The Great Gatsby tells the story of Jay Gatsby, a mysterious millionaire obsessed with rekindling a lost romance with Daisy Buchanan. Narrated by Nick Carraway, the novel explores themes of wealth, illusion, and the American Dream's decay.", "75$", "Fiction"));
        featuredBooksList.add(new Book("b4", "1984", "George Orwell", R.drawable.one_ninety, "In a dystopian future ruled by a totalitarian regime, 1984 follows Winston Smith, a man who begins to question the oppressive surveillance state led by Big Brother. As he seeks truth and freedom, the novel explores themes of control, propaganda, and individual thought.", "50$", "Fiction"));
        featuredBooksList.add(new Book("b5", "The Little Prince", "Antoine de Saint", R.drawable.the_little_prince, "Harry Potter follows a young wizard who discovers his magical heritage and attends Hogwarts School of Witchcraft and Wizardry. With the help of his friends, he faces dark forces—especially the dark wizard Voldemort—while uncovering secrets about his past and destiny.", "125$", "Fiction"));
    }

    @Override
    protected void onPause() {
        super.onPause();
        sliderHandler.removeCallbacks(sliderRunnable);
    }

    @Override
    protected void onResume() {
        super.onResume();
        sliderHandler.postDelayed(sliderRunnable, 3000);
    }

    // --- Helper class for controlling scroll speed ---
    public class FixedSpeedScroller extends Scroller {
        private int mDuration = 800; // Adjust duration as needed

        public FixedSpeedScroller(Context context) {
            super(context);
        }

        public FixedSpeedScroller(Context context, Interpolator interpolator) {
            super(context, interpolator);
        }

        @Override
        public void startScroll(int startX, int startY, int dx, int dy, int duration) {
            super.startScroll(startX, startY, dx, dy, mDuration);
        }

        @Override
        public void startScroll(int startX, int startY, int dx, int dy) {
            super.startScroll(startX, startY, dx, dy, mDuration);
        }
    }
}