package com.example.tezu;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.tezu.Adapter.ContactAdapter;
import com.example.tezu.Model.Forum;
import com.example.tezu.Model.contactmodel;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class Contacts extends AppCompatActivity {

    private RecyclerView recyclerView;
    private ContactAdapter contactAdapter;;
    private List<contactmodel> contactmodelList;
    ImageButton backbutton;
    TextView name,phnNumber;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contacts);

        contactmodelList = new ArrayList<>();
        recyclerView = findViewById(R.id.rec_contacts);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(linearLayoutManager);

        contactAdapter = new ContactAdapter(getApplicationContext(),contactmodelList);
        recyclerView.setAdapter(contactAdapter);

        readContacts();
        backbutton = findViewById(R.id.backbutton);
        backbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent back = new Intent(Contacts.this, MainActivity.class);
                startActivity(back);
            }
        });
    }

    private void readContacts() {
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("emergencyContacts");

        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                contactmodelList.clear();
                for (DataSnapshot snapshot1 : snapshot.getChildren()){
                    contactmodel contact = snapshot1.getValue(contactmodel.class);
                    contactmodelList.add(contact);
                }

                contactAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}