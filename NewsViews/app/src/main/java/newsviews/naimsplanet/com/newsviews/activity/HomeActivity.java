package newsviews.naimsplanet.com.newsviews.activity;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import newsviews.naimsplanet.com.newsviews.ItemClickSupport;
import newsviews.naimsplanet.com.newsviews.R;
import newsviews.naimsplanet.com.newsviews.adapter.ArticleAdapter;
import newsviews.naimsplanet.com.newsviews.auth.LoginActivity;
import newsviews.naimsplanet.com.newsviews.model.Article;
import newsviews.naimsplanet.com.newsviews.model.ArticleList;
import newsviews.naimsplanet.com.newsviews.retrofit.ApiService;
import newsviews.naimsplanet.com.newsviews.retrofit.RetroClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.widget.LinearLayout.HORIZONTAL;
import static android.widget.LinearLayout.VERTICAL;


public class HomeActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener{


    Toolbar mToolbar;
    DrawerLayout mDrawyerLayout;
    NavigationView mNavigationView;
    private ProgressDialog pDialog;
    private ArrayList<Article>articleList;
    private RecyclerView recyclerView;
    private ArticleAdapter articleAdapter;
    private FirebaseAuth mAuth;
    private TextView name,email;
    private ImageView image;

    private AlertDialog mDialog;
    private AlertDialog.Builder mBuilder;

    private SwipeRefreshLayout refreshLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.navigation_drawer);

        mToolbar = findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);

        refreshLayout = findViewById(R.id.swipe_to_refresh);

        refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                articleAdapter.notifyDataSetChanged();
                refreshLayout.setRefreshing(false);
            }
        });


        mAuth = FirebaseAuth.getInstance();

        mDrawyerLayout = findViewById(R.id.drawer_layout);
        mNavigationView = findViewById(R.id.navigation_view);




        View headerView = mNavigationView.getHeaderView(0);

        name = headerView.findViewById(R.id.navigation_drawyer_text);
        email = headerView.findViewById(R.id.navigation_drawyer_email);
        image = headerView.findViewById(R.id.navigation_drwayer_image);

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

                    DividerItemDecoration decoration = new DividerItemDecoration(getApplicationContext(), VERTICAL);
                    recyclerView.addItemDecoration(decoration);
                    DividerItemDecoration decoration1 = new DividerItemDecoration(getApplicationContext(), HORIZONTAL);
                    recyclerView.addItemDecoration(decoration1);

                    ItemClickSupport.addTo(recyclerView).setOnItemClickListener(new ItemClickSupport.OnItemClickListener() {
                        @Override
                        public void onItemClicked(RecyclerView recyclerView, final int position, View v)
                        {
                            //Toast.makeText(HomeActivity.this, "You clicked "+articleList.get(position).getTitle(), Toast.LENGTH_SHORT).show();
                            mBuilder = new AlertDialog.Builder(HomeActivity.this);
                            mBuilder.setMessage("Do you want to open in webview?");
                            mBuilder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {

                                    Intent intent = new Intent(HomeActivity.this,DetailsActivity.class);
                                    intent.putExtra("url",articleList.get(position).getUrl().toString());
                                    startActivity(intent);

                                }
                            });
                            mBuilder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {

                                }
                            });
                            mDialog = mBuilder.create();
                            mDialog.show();

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

    @Override
    protected void onStart() {
        super.onStart();
        if(mAuth.getCurrentUser()==null)
        {
            startActivity(new Intent(HomeActivity.this, LoginActivity.class));
            fileList();
        }
        else
        {
            name.setText(mAuth.getCurrentUser().getDisplayName());
            email.setText(mAuth.getCurrentUser().getEmail());
            Picasso.get().load(mAuth.getCurrentUser().getPhotoUrl()).into(image);
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_menu,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId())
        {
            case R.id.singout:
                mAuth.signOut();
                startActivity(new Intent(HomeActivity.this,LoginActivity.class));
                finish();

        }
        return super.onOptionsItemSelected(item);
    }
}
