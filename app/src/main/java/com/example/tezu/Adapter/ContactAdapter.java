package com.example.tezu.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.tezu.Model.Forum;
import com.example.tezu.Model.contactmodel;
import com.example.tezu.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.List;

public class ContactAdapter extends RecyclerView.Adapter<ContactAdapter.ViewHolder> {
    public List<contactmodel> mContact;
    public Context mContext;

    private FirebaseUser firebaseUser;

    public ContactAdapter(Context mContext, List<contactmodel> mContact) {

        this.mContext = mContext;
        this.mContact = mContact;

    }
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.contacts_item, parent, false);
        return new ContactAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        final contactmodel contactmodel = mContact.get(position);

        if (contactmodel.getContactName().equals("")){
            holder.name.setVisibility(View.GONE);
        }
        else {
            holder.name.setVisibility(View.VISIBLE);
            holder.name.setText(contactmodel.getContactName());
            holder.phnNumber.setText(contactmodel.getContactNumber());
        }

    }

    @Override
    public int getItemCount() {
        return mContact.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        TextView name,phnNumber;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.name);
            phnNumber = itemView.findViewById(R.id.phn_number);
        }
    }
}
