package com.example.tezu;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;


public class DisplayArticle extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_article);
        getIncoming();
    }

    private void getIncoming(){

        if(getIntent().hasExtra("dispArticleTitle") && getIntent().hasExtra("dispArticleAuthor")
                && getIntent().hasExtra("dispArticleDept") && getIntent().hasExtra("dispArticleDate")
                && getIntent().hasExtra("dispArticleDescription")){
            String displayArticleTitle=getIntent().getStringExtra("dispArticleTitle");
            String displayArticleAuthor=getIntent().getStringExtra("dispArticleAuthor");
            String displayArticleDept=getIntent().getStringExtra("dispArticleDept");
            String displayArticleDate=getIntent().getStringExtra("dispArticleDate");
            String displayArticleDescription=getIntent().getStringExtra("dispArticleDescription");
            setValues(displayArticleTitle,displayArticleAuthor,displayArticleDept,displayArticleDate,displayArticleDescription);
        }
    }
    private void setValues(String displayArticleTitle,String displayArticleAuthor,String displayArticleDept,String displayArticleDate,String displayArticleDescription){
        TextView title=findViewById(R.id.display_article_title);
        TextView author=findViewById(R.id.display_article_author);
        TextView dept=findViewById(R.id.display_article_dept);
        TextView date=findViewById(R.id.display_article_date);
        TextView description=findViewById(R.id.display_article_description);

        title.setText(displayArticleTitle);
        author.setText(displayArticleAuthor);
        dept.setText(displayArticleDept);
        date.setText(displayArticleDate);
        description.setText(displayArticleDescription);

    }
}