package com.andrewchelladurai.simplebible;

import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.ListViewCompat;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

public class V2ChapterVersesActivity
        extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener,
                   V2VersesFragment.InteractionListener {

    public static final  String ARG_BOOK_NUMBER = "BOOK_NUMBER";
    private static final String TAG             = "V2ChapterVersesActivity";
    private              int    bookNumber      = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_v2_chapter_verses);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        bookNumber = Integer.parseInt(getIntent().getStringExtra(ARG_BOOK_NUMBER));
        Log.d(TAG, "onCreate: bookNumber = " + bookNumber);

        AllBooks.Book book = AllBooks.getBook(bookNumber);

/*
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
*/

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open,
                R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

/*
        getSupportFragmentManager()
                .beginTransaction()
                .add(R.id.activity_v2_chapter_verses_container,
                         V2VersesFragment.getInstance(new ArrayList<String>(1)))
                .commit();
*/

        populateMenuItems(navigationView.getMenu(), book.getChapterCount());
        onNavigationItemSelected(navigationView.getMenu().getItem(0));
    }

    private void populateMenuItems(Menu menu, int chapterCount) {
        for (int i = 0; i < chapterCount; i++) {
            MenuItem item = menu.add("Chapter " + (i + 1));
            item.setIcon(R.drawable.ic_label_black_24dp);
        }
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.v2_chapter_verses, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        String title = item.getTitle().toString();
        String number = title.subSequence(title.indexOf(" "), title.length()).toString();
        AllBooks.Book book = AllBooks.getBook(bookNumber);

        title = book.getName() + " Chapter" + number;
        setTitle(title);

        V2VersesFragment fragment = V2VersesFragment.getInstance();
        fragment.refreshVersesList(bookNumber, Integer.parseInt(number.trim()));
        ListViewCompat lvc = (ListViewCompat) findViewById(R.id.fragment_v2_verses_list);
        lvc.setSelectionAfterHeaderView();

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void handleVersesFragmentInteraction(View view) {
        Toast.makeText(V2ChapterVersesActivity.this, "handleVersesFragmentInteraction",
                       Toast.LENGTH_SHORT).show();
    }
}