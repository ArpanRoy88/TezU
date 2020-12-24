package com.example.tezu;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.tezu.Model.ArticleModel;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.DateFormat;
import java.util.Calendar;

import static android.widget.Toast.LENGTH_SHORT;

public class PostMyArticleActivity extends AppCompatActivity {

    private EditText title,author,body,dept;
    private String ArticleId;

    private Button post;
    ProgressDialog loadingBar;


    DatabaseReference databaseReference;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post_my_article);

        loadingBar = new ProgressDialog(this);


        title=findViewById(R.id.editTextTitle);
        author=findViewById(R.id.editTextAuthor);
        body=findViewById(R.id.editTextBody);
        dept=findViewById(R.id.editTextDept);

        post=findViewById(R.id.postMy);


        post.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                postMyArticle();
            }
        });


    }

    private void postMyArticle() {

        final String Title=title.getText().toString();
        final String Author=author.getText().toString();
        final String Body=body.getText().toString();
        final String Dept=dept.getText().toString();

        Calendar calendar=Calendar.getInstance();
        final String currentDate = DateFormat.getDateInstance(DateFormat.FULL).format(calendar.getTime());





        databaseReference = FirebaseDatabase.getInstance().getReference("articles");
        ArticleId=databaseReference.push().getKey();


        if (TextUtils.isEmpty(Title)){
            //Toast.makeText(this,"Please Enter ", LENGTH_SHORT).show();
            title.setError("Enter Title");
            title.requestFocus();
            return;

        }
        else if (TextUtils.isEmpty(Author)){
            //Toast.makeText(this,"Please Enter ", LENGTH_SHORT).show();
            author.setError("Enter Author");
            author.requestFocus();
            return;

        }
        else if (TextUtils.isEmpty(Dept)){
            //Toast.makeText(this,"Please Enter ", LENGTH_SHORT).show();
            dept.setError("Enter Department");
            dept.requestFocus();
            return;

        }
        else if (TextUtils.isEmpty(Body)){
            //Toast.makeText(this,"Please Enter ", LENGTH_SHORT).show();
            body.setError("Please Write Something");
            body.requestFocus();
            return;

        }
        else {
            loadingBar.setTitle("Posting article");
            loadingBar.setMessage("Please wait ...");
            loadingBar.show();
            loadingBar.setCanceledOnTouchOutside(true);



            ArticleModel articleModel=new ArticleModel(
                    Title,
                    currentDate,
                    Body,
                    Author,
                    Dept,
                    ArticleId
            );

            String unique = databaseReference.push().getKey();
            FirebaseDatabase.getInstance().getReference("articles").child(unique).setValue(articleModel)
                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            Toast.makeText(PostMyArticleActivity.this, "Article sent", LENGTH_SHORT).show();
                            loadingBar.dismiss();


                            Intent gotoLoginIntent = new Intent(PostMyArticleActivity.this,ArticleActivity.class);
                            startActivity(gotoLoginIntent);
                            finish();
                        }
                    });






        }


    }
}