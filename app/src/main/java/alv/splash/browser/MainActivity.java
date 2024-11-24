package alv.splash.browser;

import android.content.Context;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.GestureDetector;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.viewpager2.widget.ViewPager2;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity{

    FloatingActionButton fab;
    DrawerLayout drawerLayout;
    NavigationView navigationView;
    Toolbar toolbar;
     LinearLayout titleAddressBar, inputAddressBarContainer, mainAddressBarContainer;
     TextInputLayout textInputAddressBar;
    TextView addressTextView;
    BottomNavigationView bottomNavigationView;
    BottomSheetDialog bsDialogControlTab;
    BottomSheetBehavior<View> bottomSheetBehavior;
    View bottomSheetControlTab;
    LinearCorner rootBottomDialogControl;
    private ViewPager2 vpMainBottom;
    private Button btnSwitchPager;
    private int currentPage = 0; // Untuk tracking halaman aktif di ViewPager
    GestureDetector gestureDetector;
    ImageView addNewTab, menuAddressBar;
    TextInputEditText editTextAddress;
    PopupWindow popupWindowMain;
    private FragmentCommunication fragmentCommunication;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        // Create a BottomSheetDialog
        bsDialogControlTab = new BottomSheetDialog(this);

        // Inflate the bottom sheet layout
        bottomSheetControlTab = getLayoutInflater().inflate(R.layout.bottomsheet_main_control, null);
        bsDialogControlTab.setContentView(bottomSheetControlTab);

        // Find views inside the bottom sheet
         titleAddressBar = bottomSheetControlTab.findViewById(R.id.titleAddressBar);
         inputAddressBarContainer = bottomSheetControlTab.findViewById(R.id.inputAddressContainer);
         mainAddressBarContainer = bottomSheetControlTab.findViewById(R.id.mainAddressBarContainer);
         textInputAddressBar = bottomSheetControlTab.findViewById(R.id.textInputAddressBar);
         editTextAddress = bottomSheetControlTab.findViewById(R.id.editTextAddress);
         rootBottomDialogControl = bottomSheetControlTab.findViewById(R.id.rootBottomControlTab);
         addressTextView = bottomSheetControlTab.findViewById(R.id.addressTextView);
         addNewTab = bottomSheetControlTab.findViewById(R.id.addNewTab);
         menuAddressBar = bottomSheetControlTab.findViewById(R.id.menuAddressBar);



         // Inisialisasi ViewPager dan tombol
        vpMainBottom = bottomSheetControlTab.findViewById(R.id.vpMainBottom);


        bottomNavigationView = findViewById(R.id.bottomNavigationView);
        fab = findViewById(R.id.fab);
        drawerLayout = findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.nav_view);
        toolbar = findViewById(R.id.toolbar);

        setSupportActionBar(toolbar);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.open_nav, R.string.close_nav);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction().replace(R.id.frame_container, new HomeFragment()).commit();
            navigationView.setCheckedItem(R.id.nav_home);
        }


        replaceFragment(new HomeFragment());
        bottomNavigationView.setBackground(null);
        bottomNavigationView.setSelectedItemId(R.id.home);
        bottomNavigationView.setOnItemSelectedListener(item -> {
            int itemId = item.getItemId();
            if (itemId == R.id.home) {
                replaceFragment(new HomeFragment());
            } else if (itemId == R.id.drawMenu) {
                if (!drawerLayout.isDrawerOpen(GravityCompat.START)) {
                    drawerLayout.openDrawer(GravityCompat.START); // Buka Navigation Drawer di sisi kiri
                }
            } else if (itemId == R.id.bottomSettings) {
                replaceFragment(new SettingsFragment());
            } else if (itemId == R.id.myDownloads) {
                replaceFragment(new DownloadsFragment());
            }
            return true;
        });


        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showBottomMainControl();
            }
        });


    }//akhir onCreate

    private  void replaceFragment(Fragment fragment) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.frame_container, fragment);
        fragmentTransaction.commit();
    }

    // method untuk set komunikator
    public void setFragmentCommunication(FragmentCommunication communication) {
        this.fragmentCommunication = communication;
    }

    private void showBottomMainControl() {

        // Mengatur `BottomSheetBehavior`
        bottomSheetBehavior = BottomSheetBehavior.from((View) rootBottomDialogControl.getParent());

        // Atur agar `BottomSheet` dimulai pada setengah layar
        bottomSheetBehavior.setState(BottomSheetBehavior.STATE_HALF_EXPANDED);
        bottomSheetBehavior.setHalfExpandedRatio(0.5f);  // 50% layar untuk posisi setengah
        bottomSheetBehavior.setFitToContents(false);     // Support posisi half-expanded
        bottomSheetBehavior.setHideable(true);           // Support dismiss dengan swipe down

        // Set the click listener for titleAddressBar to toggle visibility
        titleAddressBar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showKeyboard(v);
                // Hide titleAddressBar and show textInputAddressBar with SearchView
                mainAddressBarContainer.setVisibility(View.GONE);
                inputAddressBarContainer.setVisibility(View.VISIBLE);

                // Focus and select all text in SearchView
                editTextAddress.requestFocus();
                editTextAddress.performClick();
                editTextAddress.selectAll();

               // showKeyboard(searchViewAddress);

                //searchViewAddress.setQuery(searchViewAddress.getQuery(), true);
            }
        });

        bsDialogControlTab.setCanceledOnTouchOutside(true);
        bsDialogControlTab.setOnDismissListener(dialog -> {
            // Mengatur textInputAddressBar menjadi GONE dan menampilkan titleAddressBar
            inputAddressBarContainer.setVisibility(View.GONE);
            mainAddressBarContainer.setVisibility(View.VISIBLE);
        });

        textInputAddressBar.setStartIconOnClickListener(v -> {
            // klik start ikon

        });
        textInputAddressBar.setEndIconOnClickListener(v -> {
            // klik start ikon
            editTextAddress.setText(null);
        });


        editTextAddress.setOnFocusChangeListener((v, hasFocus) -> {
            if (hasFocus) {
                // Ketika EditText mendapatkan fokus
                showKeyboard(v);
            } else {
                // Ketika EditText kehilangan fokus
                inputAddressBarContainer.setVisibility(View.GONE);
                mainAddressBarContainer.setVisibility(View.VISIBLE);
            }
        });

        // Setup listener untuk aksi GO/ENTER pada EditText
        editTextAddress.setOnEditorActionListener((v, actionId, event) -> {
            if (actionId == EditorInfo.IME_ACTION_GO) {
                String address = editTextAddress.getText().toString().trim();
                if (!address.isEmpty() && fragmentCommunication != null) {
                    fragmentCommunication.loadUrlInGeckoView(address);

                    // Optional: sembunyikan keyboard
                    InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
                }
                return true;
            }
            return false;
        });




        editTextAddress.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                // Sebelum teks diubah
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // Saat teks berubah
            }

            @Override
            public void afterTextChanged(Editable s) {
                // Setelah teks berubah
            }
        });


        // Set adapter untuk ViewPager
        setupViewPager();
        setupGestureDetector();

        // Set listener untuk button switch ViewPager

        addNewTab.setOnClickListener(v -> switchViewPager());
        menuAddressBar.setOnClickListener(this::showPopupMenu);
        // Show the BottomSheetDialog
        bsDialogControlTab.show();
    }


    private void openDrawMenu() {
        if (!drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.openDrawer(GravityCompat.START); // Buka Navigation Drawer di sisi kiri
        }
    }
    private void setupViewPager() {
        List<Fragment> fragments = new ArrayList<>();
        fragments.add(new LeftFragment());
        fragments.add(new RightFragment());

        ViewPagerAdapter adapter = new ViewPagerAdapter(this, fragments);
        vpMainBottom.setAdapter(adapter);

        // Gesture swipe left/right menggunakan ViewPager2
        vpMainBottom.setUserInputEnabled(false);
    }

    // Metode untuk beralih antar ViewPager
    private void switchViewPager() {
        currentPage = (currentPage == 0) ? 1 : 0; // Jika di ViewPager kiri, berpindah ke kanan, dan sebaliknya
        vpMainBottom.setCurrentItem(currentPage, true);
    }

    private void setupGestureDetector() {
        gestureDetector = new GestureDetector(this, new GestureDetector.SimpleOnGestureListener() {
            private static final int SWIPE_THRESHOLD = 45;
            private static final int SWIPE_VELOCITY_THRESHOLD = 65;

            @Override
            public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
                float diffX = e2.getX() - e1.getX();
                if (Math.abs(diffX) > SWIPE_THRESHOLD && Math.abs(velocityX) > SWIPE_VELOCITY_THRESHOLD) {
                    if (diffX > 0) {
                        // Swipe right -> pindah ke halaman sebelumnya
                        if (currentPage == 1) {
                            currentPage = 0;
                            vpMainBottom.setCurrentItem(currentPage, true);
                        }
                    } else {
                        // Swipe left -> pindah ke halaman berikutnya
                        if (currentPage == 0) {
                            currentPage = 1;
                            vpMainBottom.setCurrentItem(currentPage, true);
                        }
                    }
                    return true;
                }
                return false;
            }
        });

        titleAddressBar.setOnTouchListener((v, event) -> gestureDetector.onTouchEvent(event));
        addressTextView.setOnTouchListener((v, event) -> gestureDetector.onTouchEvent(event));
    }

    // Fungsi untuk menampilkan keyboard
    private void showKeyboard(View view) {
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.showSoftInput(view, InputMethodManager.SHOW_IMPLICIT);
    }

    private void showPopupMenu(View anchor) {
        if (popupWindowMain != null && popupWindowMain.isShowing()) {
            popupWindowMain.dismiss();
            return;
        }

        // Inflate menu layout
        View menuView = LayoutInflater.from(this).inflate(R.layout.popup_menu, null);
        popupWindowMain = new PopupWindow(menuView, LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT, true);

        // Tambahkan event untuk setiap opsi di menu
        menuView.findViewById(R.id.popDesktopMode).setOnClickListener(v -> updateTextView("Desktop Mode"));
        menuView.findViewById(R.id.popSettings).setOnClickListener(v -> updateTextView("Settings"));
        menuView.findViewById(R.id.popBookmarks).setOnClickListener(v -> updateTextView("Bookmarks added"));
        menuView.findViewById(R.id.popIncognito).setOnClickListener(v -> updateTextView("Private tab"));
        menuView.findViewById(R.id.popExtensions).setOnClickListener(v -> updateTextView("Extensions"));
        menuView.findViewById(R.id.popDownloads).setOnClickListener(v -> updateTextView("Downloads"));



        // Tampilkan popup di bawah menuAddressBar
        popupWindowMain.showAsDropDown(anchor, 0, 10);
    }

    private void updateTextView(String text) {
        if (popupWindowMain != null) {
            popupWindowMain.dismiss();
        }
        addressTextView.setText(text);
    }

}

