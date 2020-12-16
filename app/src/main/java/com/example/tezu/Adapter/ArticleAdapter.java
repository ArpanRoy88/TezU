package com.example.tezu.Adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;


import com.example.tezu.DisplayArticle;
import com.example.tezu.Model.ArticleModel;
import com.example.tezu.R;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;

public class ArticleAdapter extends RecyclerView.Adapter<ArticleAdapter.myviewholder> {

    public List<ArticleModel> alist=new ArrayList<>();
    public Context context;
    public FirebaseDatabase firebaseDatabase;
    public DatabaseReference databaseReference;

    private FirebaseUser firebaseUser;

    public ArticleAdapter(@NonNull List<ArticleModel> alist, Context applicationContext) {
        this.alist = alist;
    }

    @NonNull
    @Override
    public myviewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.article_list_design,parent,false);
        context=parent.getContext();
        firebaseDatabase=firebaseDatabase.getInstance();

        return new myviewholder(view);

    }

    @Override
    public void onBindViewHolder(@NonNull final myviewholder holder, int position) {

        final ArticleModel articleModel=alist.get(position);

        holder.article_title.setText(articleModel.getArticleTitle());
        holder.article_desc.setText(articleModel.getArticleBody());


        System.out.println("ARTICLE  :" + articleModel.getArticleTitle());

        holder.article_body.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                Intent intent=new Intent(context, DisplayArticle.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

                intent.putExtra("dispArticleTitle",articleModel.getArticleTitle());
                intent.putExtra("dispArticleAuthor",articleModel.getArticleAuthor());
                intent.putExtra("dispArticleDept",articleModel.getArticleDepartment());
                intent.putExtra("dispArticleDate",articleModel.getArticleDate());
                intent.putExtra("dispArticleDescription",articleModel.getArticleBody());



                context.startActivity(intent);
            }
        });

    }

    @Override
    public int getItemCount() {
        return alist.size();
    }

    public class myviewholder extends RecyclerView.ViewHolder {

        public RelativeLayout article_body;
        public TextView article_title;
        public TextView article_desc;
        public ImageView like,views;
        public TextView like_count,views_count;






        public myviewholder(@NonNull View itemView) {
            super(itemView);

            article_body=itemView.findViewById(R.id.articleBodyRelativeLayout);
            article_title=itemView.findViewById(R.id.articleTitleInRecycler);
            article_desc=itemView.findViewById(R.id.articleDescInRecycler);
            like=itemView.findViewById(R.id.articleLike);
            views=itemView.findViewById(R.id.articleViews);
            like_count=itemView.findViewById(R.id.likeCount);
            views_count=itemView.findViewById(R.id.articleViewsCount);

        }
    }
}
