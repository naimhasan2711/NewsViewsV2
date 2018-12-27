package newsviews.naimsplanet.com.newsviews;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

public class HomeActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener{


    Toolbar mToolbar;
    DrawerLayout mDrawyerLayout;
    NavigationView mNavigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.navigation_drawer);

        mToolbar = findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);

        mDrawyerLayout = findViewById(R.id.drawer_layout);
        mNavigationView = findViewById(R.id.navigation_view);

        mNavigationView.setNavigationItemSelectedListener(this);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this,mDrawyerLayout,mToolbar,R.string.open_navigation,R.string.close_navigation);
        mDrawyerLayout.setDrawerListener(toggle);
        toggle.syncState();

    }

    @Override
    public void onBackPressed()
    {
        if(mDrawyerLayout.isDrawerOpen(GravityCompat.START))
        {
            mDrawyerLayout.closeDrawer(GravityCompat.START);
        }
        else
        {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item)
    {
        int id = item.getItemId();

        switch (id)
        {
            case R.id.navigation_menu_home:
                mDrawyerLayout.closeDrawer(Gravity.START,false);
                break;
            case R.id.navigation_menu_about:
                startActivity(new Intent(HomeActivity.this,AboutActivity.class));
                break;
            case R.id.navigation_menu_exit:
                finish();
                System.exit(0);
                break;
        }

        mDrawyerLayout.closeDrawers();
        return true;
    }


}
