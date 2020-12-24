package com.example.tezu;

import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;


public class DisplayArticle extends AppCompatActivity {

    int like_count, views_count;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_article);
        getIncoming();
    }

    private void getIncoming() {

        if (getIntent().hasExtra("dispArticleTitle") && getIntent().hasExtra("dispArticleAuthor")
                && getIntent().hasExtra("dispArticleDept") && getIntent().hasExtra("dispArticleDate")
                && getIntent().hasExtra("dispArticleDescription") && getIntent().hasExtra("postKey")) {
            String displayArticleTitle = getIntent().getStringExtra("dispArticleTitle");
            String displayArticleAuthor = getIntent().getStringExtra("dispArticleAuthor");
            String displayArticleDept = getIntent().getStringExtra("dispArticleDept");
            String displayArticleDate = getIntent().getStringExtra("dispArticleDate");
            String displayArticleDescription = getIntent().getStringExtra("dispArticleDescription");
            String postKey = getIntent().getStringExtra("postKey");

            setValues(displayArticleTitle, displayArticleAuthor, displayArticleDept, displayArticleDate, displayArticleDescription, postKey);
        }
    }

    private void setValues(String displayArticleTitle, String displayArticleAuthor, String displayArticleDept, String displayArticleDate, String displayArticleDescription, String postKey) {
        TextView title = findViewById(R.id.display_article_title);
        TextView author = findViewById(R.id.display_article_author);
        TextView dept = findViewById(R.id.display_article_dept);
        TextView date = findViewById(R.id.display_article_date);
        TextView description = findViewById(R.id.display_article_description);

        title.setText(displayArticleTitle);
        author.setText(displayArticleAuthor);
        dept.setText(displayArticleDept);
        date.setText(displayArticleDate);
        description.setText(displayArticleDescription);
        setViewStatus(postKey);
        setLikeButonStatus(postKey);

    }

    private void setLikeButonStatus(final String postKey) {
        DatabaseReference likeRef;
        final TextView likes = findViewById(R.id.likeCounter);
        final ImageView likeImg = findViewById(R.id.like);


        likeRef = FirebaseDatabase.getInstance().getReference("Articlelikes");
        FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        final String userId = firebaseUser.getUid();

        likeRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.child(postKey).hasChild(userId)) {
                    like_count = (int) snapshot.child(postKey).getChildrenCount();
                    likeImg.setImageResource(R.drawable.is_interested);
                    likes.setText(Integer.toString(like_count));

                } else {
                    like_count = (int) snapshot.child(postKey).getChildrenCount();
                    likeImg.setImageResource(R.drawable.ic_interested);
                    likes.setText(Integer.toString(like_count));

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

    private void setViewStatus(final String postKey) {
        DatabaseReference viewRef;

        final TextView views = findViewById(R.id.viewsCounter);

        viewRef = FirebaseDatabase.getInstance().getReference("ArticleViews");
        FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        final String userId = firebaseUser.getUid();


        viewRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                if (snapshot.child(postKey).hasChild(userId)) {
                    views_count = (int) snapshot.child(postKey).getChildrenCount();
                    views.setText(Integer.toString(views_count));
                    System.out.println("ViewsInside  :" + views_count);

                } else {
                    views_count = (int) snapshot.child(postKey).getChildrenCount();
                    views.setText(Integer.toString(views_count));
                    System.out.println("ViewsInside  :" + views_count);

                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }
}