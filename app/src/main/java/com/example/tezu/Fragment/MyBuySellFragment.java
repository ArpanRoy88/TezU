package com.example.tezu.Fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.tezu.Adapter.MyBuySellRecyclerAdapter;
import com.example.tezu.Model.AdUpload;
import com.example.tezu.NewAdPost;
import com.example.tezu.R;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;


public class MyBuySellFragment extends Fragment {


    private RecyclerView buySellRecyclerView;
    private List<AdUpload> AdList;

    private DatabaseReference databaseReference;

    private MyBuySellRecyclerAdapter buySellRecyclerAdapter;
    private BottomSheetDialog bottomSheetDialog;
    private SearchView searchView;
    private TextView textView;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_mybuysell,container,false);
        // Inflate the layout for this fragment
        AdList = new ArrayList<>();

        searchView = view.findViewById(R.id.buySellSearchView);

        buySellRecyclerView = view.findViewById(R.id.BuySellRecyclerView);

        //setOnClickListener();

        buySellRecyclerAdapter = new MyBuySellRecyclerAdapter(AdList);

        buySellRecyclerView.setAdapter(buySellRecyclerAdapter);
        buySellRecyclerView.setLayoutManager(new GridLayoutManager(getContext(), 2));


        final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        databaseReference = FirebaseDatabase.getInstance().getReference("AdUploads");

        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot ds:dataSnapshot.getChildren()){

                    AdUpload uploads = ds.getValue(AdUpload.class);
                    if(uploads.getAdUserId().equals(user.getUid())) {
                        AdList.add(uploads);
                    }

                }
                buySellRecyclerView.setAdapter(buySellRecyclerAdapter);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });



        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        if (searchView != null){
            searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
                @Override
                public boolean onQueryTextSubmit(String query) {
                    return false;
                }

                @Override
                public boolean onQueryTextChange(String newText) {
                    search(newText);
                    return true;
                }
            });
        }
    }

    private void search(String str) {
        ArrayList<AdUpload> list = new ArrayList<>();
        for (AdUpload object : AdList){
            if(object.getAdTitle().toLowerCase().contains(str.toLowerCase())){
                list.add(object);
            }
            else if (object.getAdCategory().toLowerCase().contains(str.toLowerCase())){
                list.add(object);
            }else if (object.getAdDesc().toLowerCase().contains(str.toLowerCase())){
                list.add(object);
            }
        }
        buySellRecyclerAdapter = new MyBuySellRecyclerAdapter(list);
        buySellRecyclerView.setAdapter(buySellRecyclerAdapter);
        buySellRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));


    }
}