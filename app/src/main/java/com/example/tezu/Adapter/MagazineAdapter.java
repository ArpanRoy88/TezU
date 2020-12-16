package com.example.tezu.Adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;


import com.example.tezu.Model.model;
import com.example.tezu.R;
import com.example.tezu.viewPdf;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;

public class MagazineAdapter extends RecyclerView.Adapter<MagazineAdapter.myviewholder> {



    public List<model> mlist=new ArrayList<>();
    public Context context;
    public FirebaseDatabase firebaseDatabase;
    public DatabaseReference databaseReference;

    private FirebaseUser firebaseUser;


    public MagazineAdapter(@NonNull List<model> mlist, Context applicationContext) {
        this.mlist=mlist;
    }



    @NonNull
    @Override
    public myviewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.magazine_list_design,parent,false);
        context=parent.getContext();
        firebaseDatabase=firebaseDatabase.getInstance();

        return new myviewholder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final myviewholder holder, int position) {


        final model mo = mlist.get(position);

        holder.header.setText(mo.getMagazineTitle());
        holder.publisher.setText(mo.getMagazinePublisher());
        holder.semester.setText(mo.getSemester());



       holder.relativeLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(holder.img1.getContext(), viewPdf.class);
                intent.putExtra("file",mo.getFile());
                intent.putExtra("title",mo.getMagazineTitle());

                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                holder.img1.getContext().startActivity(intent);
            }
        });

    }

    @Override
    public int getItemCount() {
        return mlist.size();
    }

    public class myviewholder extends RecyclerView.ViewHolder
    {


        public ImageView img1;
        public TextView header;
        public TextView publisher,semester;
        public RelativeLayout relativeLayout;


        public myviewholder(@NonNull View itemView) {
            super(itemView);

            img1=itemView.findViewById(R.id.img1);
            header=itemView.findViewById(R.id.header);
            publisher=itemView.findViewById(R.id.txtPublisherInRecycler);
            semester=itemView.findViewById(R.id.txtSemesterInRecycler);
            relativeLayout=itemView.findViewById(R.id.magazineRecyclerRelative);



        }


    }


}
