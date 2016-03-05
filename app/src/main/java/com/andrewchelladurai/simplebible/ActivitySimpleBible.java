package com.andrewchelladurai.simplebible;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

public class ActivitySimpleBible
        extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener,
                   FragmentHome.InteractionListener,
                   FragmentAbout.InteractionListener,
                   FragmentSearch.InteractionListener,
                   FragmentBooksList.InteractionListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_simple_bible);

        DatabaseUtility.getInstance(getBaseContext());
        AllBooks.populateBooks(getResources().getStringArray(
                R.array.books_n_chapter_count_array));

        Toolbar toolbar = (Toolbar) findViewById(R.id.activity_simple_bible_toolbar);
        setSupportActionBar(toolbar);

/*
        FloatingActionButton fab =
                (FloatingActionButton) findViewById(R.id.activity_simple_bible_fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
*/

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.activity_simple_bible_drawer_layout);
        ActionBarDrawerToggle toggle =
                new ActionBarDrawerToggle(this, drawer, toolbar,
                                          R.string.navigation_drawer_open,
                                          R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView =
                (NavigationView) findViewById(R.id.activity_simple_bible_nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        onNavigationItemSelected(navigationView.getMenu().getItem(0));
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.activity_simple_bible_drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.activity_simple_bible, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_settings:
            case R.id.activity_simple_bible_action_settings:
                Intent intent = new Intent(getApplicationContext(),ActivitySettings.class);
                startActivity(intent);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        Fragment fragment = getSupportFragmentManager()
                .findFragmentById(R.id.activity_simple_bible_fragment_container);
        StringBuilder title = new StringBuilder(getString(R.string.app_name));
        switch (item.getItemId()) {
            case R.id.activity_simple_bible_navbar_welcome:
                if (!(fragment instanceof FragmentHome)) {
                    getSupportFragmentManager()
                            .beginTransaction()
                            .replace(R.id.activity_simple_bible_fragment_container,
                                     FragmentHome.getInstance(""))
                            .commit();
                }
                break;
            case R.id.activity_simple_bible_navbar_otbooks: {
                Utilities utilities = Utilities.getInstance();
                int rotation = getWindowManager().getDefaultDisplay().getRotation();
                int columnCount = utilities.getChapterListColumnCount(rotation, getResources());

                getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.activity_simple_bible_fragment_container,
                                 FragmentBooksList.getInstance(
                                         FragmentBooksList.ARG_OLD_TESTAMENT_LIST,
                                         columnCount))
                        .commit();
                title.append(" : Old Testament");
            }
            break;
            case R.id.activity_simple_bible_navbar_ntbooks: {
                Utilities utilities = Utilities.getInstance();
                int rotation = getWindowManager().getDefaultDisplay().getRotation();
                int columnCount = utilities.getChapterListColumnCount(rotation, getResources());

                getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.activity_simple_bible_fragment_container,
                                 FragmentBooksList.getInstance(
                                         FragmentBooksList.ARG_NEW_TESTAMENT_LIST,
                                         columnCount))
                        .commit();
                title.append(" : New Testament");
            }
            break;
            case R.id.activity_simple_bible_navbar_bookmarked:
                // TODO : Implement and activate a Bookmark Fragment
                title.append(" : Bookmarked");
                break;
            case R.id.activity_simple_bible_navbar_search:
                if (!(fragment instanceof FragmentSearch)) {
                    getSupportFragmentManager()
                            .beginTransaction()
                            .replace(
                                    R.id.activity_simple_bible_fragment_container,
                                    FragmentSearch.getInstance())
                            .commit();
                }
                title.append(" : Search");
                break;
            case R.id.activity_simple_bible_navbar_about:
                if (!(fragment instanceof FragmentAbout)) {
                    getSupportFragmentManager()
                            .beginTransaction()
                            .replace(
                                    R.id.activity_simple_bible_fragment_container,
                                    FragmentAbout.getInstance())
                            .commit();
                }
                title.append(" : About");
                break;
            default:
        }
        setTitle(title);
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.activity_simple_bible_drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onHomeFragmentInteraction(View view) {
        Toast.makeText(ActivitySimpleBible.this,
                       "onHomeFragmentInteraction", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onAboutFragmentInteraction(View view) {
        Toast.makeText(ActivitySimpleBible.this,
                       "onAboutFragmentInteraction", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onSearchFragmentInteraction(View view) {
        Toast.makeText(ActivitySimpleBible.this,
                       "onSearchFragmentInteraction", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onBooksListFragmentInteraction(AllBooks.Book item) {
        Intent intent = new Intent(this, ActivityChapterVerses.class);
        intent.putExtra(ActivityChapterVerses.ARG_BOOK_NUMBER, item.getBookNumber());
        startActivity(intent);
    }
}