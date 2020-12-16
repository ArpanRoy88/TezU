package com.example.tezu;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;


import com.example.tezu.Adapter.ArticleAdapter;
import com.example.tezu.Model.ArticleModel;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class ArticleActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private ArticleAdapter myadapter;
    FloatingActionButton floatingActionButton;

    private List<ArticleModel> articleModelList;
    private DatabaseReference db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_article);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        articleModelList=new ArrayList<>();

        recyclerView=findViewById(R.id.recyclerViewInArticles);
        floatingActionButton=findViewById(R.id.floatingActionButton2);


        myadapter=new ArticleAdapter(articleModelList,ArticleActivity.this);

        RecyclerView.LayoutManager manager = new LinearLayoutManager(this);
        recyclerView.setAdapter(myadapter);
        recyclerView.setLayoutManager(manager);

        db= FirebaseDatabase.getInstance().getReference("articles");
        db.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                myadapter.notifyDataSetChanged();
                for(DataSnapshot ds:snapshot.getChildren()){
                    ArticleModel articleModel=ds.getValue(ArticleModel.class);
                    articleModelList.add(articleModel);
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent goToPostMyArticle=new Intent(getApplicationContext(),PostMyArticleActivity.class);
                startActivity(goToPostMyArticle);
            }
        });


    }
}