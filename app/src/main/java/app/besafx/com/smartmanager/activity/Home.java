package app.besafx.com.smartmanager.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import app.besafx.com.smartmanager.R;
import app.besafx.com.smartmanager.entity.Person;
import app.besafx.com.smartmanager.enums.FragmentType;
import app.besafx.com.smartmanager.fragment.Menu1;

public class Home extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    protected static final String TAG = Home.class.getSimpleName();

    private NavigationView navigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.home);

        Intent intent = getIntent();
        Person person = (Person) intent.getSerializableExtra("person");

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        setHeaderInfo(person, navigationView);

        displaySelectedScreen(R.id.item_pending_incoming_tasks);

    }

    private void setHeaderInfo(Person person, NavigationView navigationView) {
        View headerView = navigationView.getHeaderView(0);
        TextView txtView_person_name = (TextView) headerView.findViewById(R.id.txtView_person_name);
        txtView_person_name.setText(person.getName());
        TextView txtView_person_role = (TextView) headerView.findViewById(R.id.txtView_person_role);
        txtView_person_role.setText(person.getQualification());
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
        getMenuInflater().inflate(R.menu.home, menu);
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

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        displaySelectedScreen(item.getItemId());
        return true;
    }

    private void displaySelectedScreen(int itemId) {

        //creating fragment object
        Fragment fragment = null;
        Bundle bundle = new Bundle();

        //initializing the fragment object which is selected
        switch (itemId) {
            case R.id.item_pending_outgoing_tasks:
                fragment = new Menu1();
                bundle.putSerializable("FragmentType", FragmentType.Outgoing_Pending_Tasks);
                fragment.setArguments(bundle);
                clearAllMenuItemSelection();
                navigationView.getMenu().findItem(R.id.item_pending_outgoing_tasks).setChecked(true);
                break;
            case R.id.item_auto_outgoing_tasks:
                fragment = new Menu1();
                bundle.putSerializable("FragmentType", FragmentType.Outgoing_Auto_Tasks);
                fragment.setArguments(bundle);
                clearAllMenuItemSelection();
                navigationView.getMenu().findItem(R.id.item_auto_outgoing_tasks).setChecked(true);
                break;
            case R.id.item_archive_outgoing_tasks:
                fragment = new Menu1();
                bundle.putSerializable("FragmentType", FragmentType.Outgoing_Archive_Tasks);
                fragment.setArguments(bundle);
                clearAllMenuItemSelection();
                navigationView.getMenu().findItem(R.id.item_archive_outgoing_tasks).setChecked(true);
                break;
            case R.id.item_pending_incoming_tasks:
                fragment = new Menu1();
                bundle.putSerializable("FragmentType", FragmentType.Incoming_Pending_Tasks);
                fragment.setArguments(bundle);
                clearAllMenuItemSelection();
                navigationView.getMenu().findItem(R.id.item_pending_incoming_tasks).setChecked(true);
                break;
            case R.id.item_auto_incoming_tasks:
                fragment = new Menu1();
                bundle.putSerializable("FragmentType", FragmentType.Incoming_Auto_Tasks);
                fragment.setArguments(bundle);
                clearAllMenuItemSelection();
                navigationView.getMenu().findItem(R.id.item_auto_incoming_tasks).setChecked(true);
                break;
            case R.id.item_archive_incoming_tasks:
                fragment = new Menu1();
                bundle.putSerializable("FragmentType", FragmentType.Incoming_Archive_Tasks);
                fragment.setArguments(bundle);
                clearAllMenuItemSelection();
                navigationView.getMenu().findItem(R.id.item_archive_incoming_tasks).setChecked(true);
                break;
        }

        //replacing the fragment
        if (fragment != null) {
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            ft.replace(R.id.content_frame, fragment);
            ft.commit();
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
    }

    private void clearAllMenuItemSelection(){
        navigationView.getMenu().findItem(R.id.item_pending_incoming_tasks).setChecked(false);
        navigationView.getMenu().findItem(R.id.item_auto_incoming_tasks).setChecked(false);
        navigationView.getMenu().findItem(R.id.item_archive_incoming_tasks).setChecked(false);
    }
}
