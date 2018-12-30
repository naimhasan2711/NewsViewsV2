package newsviews.naimsplanet.com.newsviews.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import java.util.ArrayList;

import newsviews.naimsplanet.com.newsviews.ItemClickSupport;
import newsviews.naimsplanet.com.newsviews.R;
import newsviews.naimsplanet.com.newsviews.adapter.ArticleAdapter;
import newsviews.naimsplanet.com.newsviews.model.Article;
import newsviews.naimsplanet.com.newsviews.model.ArticleList;
import newsviews.naimsplanet.com.newsviews.retrofit.ApiService;
import newsviews.naimsplanet.com.newsviews.retrofit.RetroClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class HomeActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener{


    Toolbar mToolbar;
    DrawerLayout mDrawyerLayout;
    NavigationView mNavigationView;
    private ProgressDialog pDialog;
    private ArrayList<Article>articleList;
    private RecyclerView recyclerView;
    private ArticleAdapter articleAdapter;

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

        pDialog = new ProgressDialog(HomeActivity.this);
        pDialog.setMessage("Loading Data.. Please wait...");
        pDialog.setIndeterminate(false);
        pDialog.setCancelable(false);
        pDialog.show();

        //Creating an object of our api interface
        ApiService api = RetroClient.getApiService();

        //calling gson
        Call<ArticleList> call = api.getMyGson();

        call.enqueue(new Callback<ArticleList>() {
            @Override
            public void onResponse(Call<ArticleList> call, Response<ArticleList> response) {
                pDialog.dismiss();
                if(response.isSuccessful())
                {
                    articleList = response.body().getArticles();
                    recyclerView = findViewById(R.id.recyclerView);
                    articleAdapter = new ArticleAdapter(articleList);
                    GridLayoutManager gridLayoutManager = new GridLayoutManager(getApplicationContext(),2,LinearLayoutManager.VERTICAL,false);
                    //RecyclerView.LayoutManager layoutManager = new GridLayoutManager(getApplicationContext(),);
                    recyclerView.setLayoutManager(gridLayoutManager);
                    recyclerView.setItemAnimator(new DefaultItemAnimator());
                    recyclerView.setAdapter(articleAdapter);

                    ItemClickSupport.addTo(recyclerView).setOnItemClickListener(new ItemClickSupport.OnItemClickListener() {
                        @Override
                        public void onItemClicked(RecyclerView recyclerView, int position, View v) {
                            Toast.makeText(HomeActivity.this, "You clicked on "+articleList.get(position).getTitle().toString(), Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }

            @Override
            public void onFailure(Call<ArticleList> call, Throwable t) {
                pDialog.dismiss();
            }
        });



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
