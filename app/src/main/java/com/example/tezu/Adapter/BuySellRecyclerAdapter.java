package com.example.tezu.Adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.tezu.ChatActivity;
import com.example.tezu.Model.AdUpload;
import com.example.tezu.Model.Forum;
import com.example.tezu.Model.User;
import com.example.tezu.R;
import com.example.tezu.StudentDetails;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class BuySellRecyclerAdapter extends RecyclerView.Adapter<BuySellRecyclerAdapter.ViewHolder> {

    public List<AdUpload> adUploadList = new ArrayList<>();
    public Context context;
    public FirebaseDatabase firebaseDatabase;
    public DatabaseReference databaseReference;
    private FirebaseUser firebaseUser;
   // private RecyclerViewClickListener listener;



    public BuySellRecyclerAdapter(List<AdUpload> adUploadList) {
        this.adUploadList = adUploadList;
        //this.listener = listener;
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.buysell_list_item,parent,false);
        context = parent.getContext();
        firebaseDatabase = FirebaseDatabase.getInstance();

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, int position) {

        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();

        final AdUpload post = adUploadList.get(position);
        String user_id = adUploadList.get(position).getAdUserId();

        holder.categoryView.setText(post.getAdCategory());
        holder.priceView.setText(post.getAdPrice());
        holder.titleView.setText(post.getAdTitle());

        Picasso.get().load(post.getAdImage()).into(holder.adImageView);

        adUserDetails(holder.adUserName,post.getAdUserId());

        holder.adImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final BottomSheetDialog bt = new BottomSheetDialog(context,R.style.BottomSheetTheme);
                View view_bsl = LayoutInflater.from(context).inflate(R.layout.bottom_sheet_layout_buysell,null);
                //holder.setDescText(desc_data);

                ImageView bottomSheetProfile = view_bsl.findViewById(R.id.circleImageViewInBottomSheet);
                TextView bottomSheetDescription = view_bsl.findViewById(R.id.txtDescInBottomSheet);
                TextView bottomSheetUserName = view_bsl.findViewById(R.id.txtUserNameInBottomSheet);
                TextView bottomSheetTitle = view_bsl.findViewById(R.id.txtTitleInBottomSheet);
                TextView bottomSheetPrice = view_bsl.findViewById(R.id.txtPriceInBottomSheet);
                ImageView bottomSheetPicture = view_bsl.findViewById(R.id.imgAdPicInBottomSheet);
                ImageButton bottomSheetSendMessage = view_bsl.findViewById(R.id.btnSendMessageInBottomSheet);

                Picasso.get().load(post.getAdImage()).into(bottomSheetPicture);
                fetchData(bottomSheetProfile, post.getAdUserId());

                bottomSheetSendMessage.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent goToChatActivity = new Intent(context, ChatActivity.class);
                        context.startActivity(goToChatActivity);
                    }
                });

                getData(bottomSheetDescription,bottomSheetTitle,bottomSheetPrice,post.getAdUploadId());
                adUserDetails(bottomSheetUserName,post.getAdUserId());
                bt.setContentView(view_bsl);
                bt.show();
            }
        });


    }


    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        private View aView;

        private TextView titleView;
        private TextView categoryView;
        private TextView priceView;
        private ImageView adImageView;
        private TextView adUserName;



        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            aView = itemView;


            adImageView = aView.findViewById(R.id.imgAdPictureInRecyclerView);
            adUserName = aView.findViewById(R.id.txtAdOwnerNameInRecyclerView);
            titleView = aView.findViewById(R.id.txtAdTitleInRecyclerView);
            categoryView = aView.findViewById(R.id.txtAdCategoryInRecyclerView);
            priceView = aView.findViewById(R.id.txtAdPriceInRecyclerView);


            aView.setOnClickListener(this);
        }


        @Override
        public void onClick(View view) {
        }

       /* @Override
        public void onClick(View view) {
            listener.onClick(view,getAdapterPosition());
        }

        */
    }

    private void fetchData(final ImageView studentProfilePicture, final String userId) {

        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Student").child(userId);

        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                User user = snapshot.getValue(User.class);
                if (user.getProfilePic().equals("Null")) {
                    Picasso.get().load("https://firebasestorage.googleapis.com/v0/b/tezu-33542." +
                            "appspot.com/o/Student%2Fuserphoto.png?alt=media&token=db4d39c7-e8a5-" +
                            "47fe-8624-c40a4d22a626").into(studentProfilePicture);
                } else {
                    Picasso.get().load(user.getProfilePic()).into(studentProfilePicture);
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }


    private void getData(final TextView description,final TextView title,final TextView price,final String uploadId) {
        databaseReference = FirebaseDatabase.getInstance().getReference("AdUploads").child(uploadId);

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                AdUpload uploadDetails = dataSnapshot.getValue(AdUpload.class);

                description.setText(uploadDetails.getAdDesc());
                title.setText(uploadDetails.getAdTitle());
                price.setText(uploadDetails.getAdPrice());

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


    }


    @Override
    public int getItemCount() {

        return adUploadList.size();
    }



    private void adUserDetails(final TextView username,final String userId){

        databaseReference = FirebaseDatabase.getInstance().getReference("Student").child(userId);

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                User user = dataSnapshot.getValue(User.class);

                if (user != null) {
                    username.setText(user.getStudentName());
                }else {
                    System.out.println("HERE");
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }


}
