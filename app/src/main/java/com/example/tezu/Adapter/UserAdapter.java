//package com.example.tezu.Adapter;
//
//
//import android.content.Context;
//import android.view.View;
//import android.view.ViewGroup;
//import android.widget.TextView;
//
//import androidx.annotation.NonNull;
//import androidx.recyclerview.widget.RecyclerView;
//import androidx.recyclerview.widget.RecyclerView.ViewHolder;
//
//import com.example.tezu.Model.User;
//import com.google.firebase.auth.FirebaseUser;
//
//import java.util.List;
//
//import de.hdodenhof.circleimageview.CircleImageView;
//
//public class UserAdapter extends RecyclerView.Adapter<UserAdapter.ViewHolder> {
//
//    private Context mContext;
//    private List<User> mUser;
//
//    private FirebaseUser firebaseUser;
//
//
//    public UserAdapter(Context mContext, List<User> mUser) {
//        this.mContext = mContext;
//        this.mUser = mUser;
//    }
//
////    @NonNull
////    @Override
////    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i){
////
////    }
////
////    @Override
////    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
////
////    }
//
//    @Override
//    public int getItemCount() {
//        return 0;
//    }
//
//    public class ViewHolder extends RecyclerView.ViewHolder{
//
//        public TextView username;
//        public TextView fullname;
//        public CircleImageView image_profile;
//
//        public ViewHolder(@NonNull View view) {
//            super(view);
//        }
//    }
//}
