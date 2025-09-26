package com.example.bookspage;

import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class StoresActivity extends AppCompatActivity {

    private RecyclerView nearYouRecyclerView;
    private RecyclerView mostPopularRecyclerView;
    private StoreAdapter nearYouAdapter;
    private StoreAdapter mostPopularAdapter;
    private List<Store> nearYouStoreList;
    private List<Store> mostPopularStoreList;

    // This is the ImageView for the menu icon, declared as a member variable
    // to be accessible throughout the activity.
    private ImageView ivMenu; // Declare ivMenu here

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_stores);

        // ivMenu must be found AFTER setContentView
        ivMenu = findViewById(R.id.menuIcon); // Assuming your menu icon ID is 'menuIcon' in activity_stores.xml


        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Initialize lists
        nearYouStoreList = new ArrayList<>();
        mostPopularStoreList = new ArrayList<>();


        nearYouStoreList.add(new Store("Balfour Books", R.drawable.balfourbooks));
        nearYouStoreList.add(new Store("Barnes Bookshop", R.drawable.barnesbookshop));
        nearYouStoreList.add(new Store("Daunt Books", R.drawable.dauntbooks));
        nearYouStoreList.add(new Store("Book Store", R.drawable.store));


        mostPopularStoreList.add(new Store("Balfour Books", R.drawable.balfourbooks));
        mostPopularStoreList.add(new Store("Barnes Bookshop", R.drawable.barnesbookshop));
        mostPopularStoreList.add(new Store("Daunt Books", R.drawable.dauntbooks));
        mostPopularStoreList.add(new Store("Book Store", R.drawable.store));

        // Setup "Near You" RecyclerView
        nearYouRecyclerView = findViewById(R.id.nearYouRecyclerView);
        nearYouRecyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        nearYouAdapter = new StoreAdapter(nearYouStoreList);
        nearYouRecyclerView.setAdapter(nearYouAdapter);

        // Setup "Most Popular" RecyclerView
        mostPopularRecyclerView = findViewById(R.id.mostPopularRecyclerView);
        mostPopularRecyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        mostPopularAdapter = new StoreAdapter(mostPopularStoreList);
        mostPopularRecyclerView.setAdapter(mostPopularAdapter);

        // Add a click listener to the menu icon
        ivMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Call the showPopupMenu method, which is now a member of StoresActivity
                showPopupMenu(v);
            }
        });

        // If you need to handle clicks on individual store items, you would do it in the StoreAdapter.
        // For example, you could pass an interface/callback to the adapter from the activity.
    }

    // showPopupMenu method moved OUTSIDE of onCreate
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

        // Navigation for "All Books" (tvHome)
        // Changed to navigate to HomeActivity, which is your main book listing
        tvHome.setOnClickListener(v -> {
            popupWindow.dismiss();
            Intent homeIntent = new Intent(StoresActivity.this, MainActivity.class); // Corrected context
            startActivity(homeIntent);
            finish(); // Finish StoresActivity if you want to go back to HomeActivity
        });

        // Navigation for "Our Store" (tvOurStore)
        // If already in StoresActivity, just dismiss the popup
        tvOurStore.setOnClickListener(v -> {
            popupWindow.dismiss();
            // User is already in StoresActivity, no need to navigate again.
        });

        // Logout logic
        btnLogOut.setOnClickListener(v -> {
            popupWindow.dismiss();
            // Logout logic: Navigate back to LoginActivity and clear the activity stack
            Intent logoutIntent = new Intent(StoresActivity.this, LoginActivity.class); // Corrected context
            logoutIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK); // Clears activity history
            startActivity(logoutIntent); // Starts LoginActivity
            finish(); // Finishes StoresActivity so it's not on the back stack
        });

        // Positioning for the popup menu
        int xOffset = -8; // Aligns right edge of popup with right edge of anchor, with 8dp margin
        int yOffset = 4;  // Positions popup 4dp below the anchor
        popupWindow.showAsDropDown(anchorView, xOffset, yOffset, Gravity.END | Gravity.TOP);
    }
}