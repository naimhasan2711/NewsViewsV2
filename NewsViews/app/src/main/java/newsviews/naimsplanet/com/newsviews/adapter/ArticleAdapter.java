package newsviews.naimsplanet.com.newsviews.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.List;

import newsviews.naimsplanet.com.newsviews.R;
import newsviews.naimsplanet.com.newsviews.model.Article;

public class ArticleAdapter extends RecyclerView.Adapter<ArticleAdapter.CustomViewHolder>
{
    private List<Article> articles;

    public ArticleAdapter(List<Article> articles) {
        this.articles = articles;
    }

    @Override
    public CustomViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.article_list, parent, false);

        return new CustomViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(CustomViewHolder holder, int position) {
        Article article = articles.get(position);

        holder.title.setText(article.getTitle());
        holder.author.setText("Author : "+article.getAuthor());
        holder.description.setText(article.getDescription());
        holder.url.setText(article.getUrl());
        holder.date.setText(article.getPublishedAt());
        Picasso.get().load(article.getUrlToImage()).into(holder.poster);

    }

    @Override
    public int getItemCount() {
        return articles.size();
    }

    public class CustomViewHolder extends RecyclerView.ViewHolder {
        public TextView title,author,description,url,date;
        public ImageView poster;

        public CustomViewHolder(View view) {
            super(view);

            title = view.findViewById(R.id.title_text);
            author = view.findViewById(R.id.author_id);
            description = view.findViewById(R.id.description_text);
            url = view.findViewById(R.id.url_text);
            date = view.findViewById(R.id.date_text);

            poster = view.findViewById(R.id.poster_image);

        }
    }
}
