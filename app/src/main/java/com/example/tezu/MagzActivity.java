package com.example.tezu;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;


import com.example.tezu.Adapter.MagazineAdapter;
import com.example.tezu.Model.model;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class MagzActivity extends AppCompatActivity {



    private RecyclerView recview;
    private MagazineAdapter adapter;
    FloatingActionButton floatingActionButton;

    private List<model> modelList;

    private DatabaseReference databaseReference;







   @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_magz);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        modelList=new ArrayList<>();

        recview = findViewById(R.id.recview);
       floatingActionButton=findViewById(R.id.floatingActionButton3);


        adapter=new MagazineAdapter(modelList,MagzActivity.this);

        RecyclerView.LayoutManager manager = new LinearLayoutManager(this);
        recview.setAdapter(adapter);
        recview.setLayoutManager(manager);



       databaseReference=FirebaseDatabase.getInstance().getReference("magazine");
       databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
           @Override
           public void onDataChange(@NonNull DataSnapshot snapshot) {
               adapter.notifyDataSetChanged();
               for(DataSnapshot ds:snapshot.getChildren()){

                   model md=ds.getValue(model.class);
                   modelList.add(md);
               }

           }

           @Override
           public void onCancelled(@NonNull DatabaseError error) {

           }
       });

       floatingActionButton.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View view) {
               Intent goToArticleUpload=new Intent(getApplicationContext(), MagazineUpload.class);
               startActivity(goToArticleUpload);
           }
       });

    }

}
