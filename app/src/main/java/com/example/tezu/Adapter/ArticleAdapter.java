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

import com.example.tezu.DisplayArticle;
import com.example.tezu.Model.ArticleModel;
import com.example.tezu.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class ArticleAdapter extends RecyclerView.Adapter<ArticleAdapter.myviewholder> {

    public List<ArticleModel> alist=new ArrayList<>();
    public Context context;
    public FirebaseDatabase firebaseDatabase;
    public DatabaseReference likeReference,viewReference;

    Boolean likeChecker=false,viewChecker=false;


    public ArticleAdapter(@NonNull List<ArticleModel> alist, Context applicationContext) {
        this.alist = alist;
    }



    @NonNull
    @Override
    public myviewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.article_list_design,parent,false);
        context=parent.getContext();
        firebaseDatabase=firebaseDatabase.getInstance();

        viewReference=firebaseDatabase.getReference("views");

        return new myviewholder(view);

    }

    @Override
    public void onBindViewHolder(@NonNull final myviewholder holder, int position) {

        final FirebaseUser firebaseUser= FirebaseAuth.getInstance().getCurrentUser();
        final String currentUserId=firebaseUser.getUid();


        final ArticleModel articleModel=alist.get(position);

        holder.article_title.setText(articleModel.getArticleTitle());
        holder.article_desc.setText(articleModel.getArticleBody());


        final String postKey=articleModel.getArticleId();



//        System.out.println("ARTICLE  :" + postKey);

        holder.setViewStatus(postKey);



        holder.article_body.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                viewReference=firebaseDatabase.getReference("ArticleViews");
                viewChecker=true;

                viewReference.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {

                        if(viewChecker.equals(true)){
                            if(snapshot.child(postKey).hasChild(currentUserId)){
//                                holder.setViewStatus(postKey);
                                viewChecker=false;
                            }
                            else {
                                viewReference.child(postKey).child(currentUserId).setValue(true);
                                holder.setViewStatus(postKey);
                                viewChecker=false;
                            }

                        }

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });




                Intent intent=new Intent(context, DisplayArticle.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

                intent.putExtra("dispArticleTitle",articleModel.getArticleTitle());
                intent.putExtra("dispArticleAuthor",articleModel.getArticleAuthor());
                intent.putExtra("dispArticleDept",articleModel.getArticleDepartment());
                intent.putExtra("dispArticleDate",articleModel.getArticleDate());
                intent.putExtra("dispArticleDescription",articleModel.getArticleBody());
                intent.putExtra("postKey",articleModel.getArticleId());

                System.out.println("ArticleTitle :"+articleModel.getArticleTitle());

                context.startActivity(intent);
            }
        });

        holder.setLikeButonStatus(postKey);
        holder.like.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                System.out.println("ARTICLE  :" + postKey);

                likeReference=firebaseDatabase.getReference("Articlelikes");


                likeChecker=true;
                likeReference.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {


                        if(likeChecker.equals(true)){
                            if(snapshot.child(postKey).hasChild(currentUserId)){
                                likeReference.child(postKey).child(currentUserId).removeValue();
                                holder.setLikeButonStatus(postKey);
                                System.out.println("inside "+postKey  + currentUserId);
                                likeChecker=false;
                            }
                            else {
                                likeReference.child(postKey).child(currentUserId).setValue(true);
                                holder.setLikeButonStatus(postKey);
                                likeChecker=false;
                            }

                        }


                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
            }
        });

    }

    @Override
    public int getItemCount() {
        return alist.size();
    }

    public class myviewholder extends RecyclerView.ViewHolder {

        public RelativeLayout article_body;
        public TextView article_title;
        public TextView article_desc,like_display,view_display;
        public ImageView like,views;
        int like_count,views_count;

        DatabaseReference likeRef,viewRef;






        public myviewholder(@NonNull View itemView) {
            super(itemView);

            article_body=itemView.findViewById(R.id.articleBodyRelativeLayout);
            article_title=itemView.findViewById(R.id.articleTitleInRecycler);
            article_desc=itemView.findViewById(R.id.articleDescInRecycler);


        }

        public void setLikeButonStatus(final String postKey) {

            like=itemView.findViewById(R.id.articleLike);
            views=itemView.findViewById(R.id.articleViews);
            like_display=itemView.findViewById(R.id.likeCount);
            view_display=itemView.findViewById(R.id.articleViewsCount);

            likeRef=FirebaseDatabase.getInstance().getReference("Articlelikes");
            FirebaseUser firebaseUser= FirebaseAuth.getInstance().getCurrentUser();
            final String userId=firebaseUser.getUid();


            likeRef.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if(snapshot.child(postKey).hasChild(userId)){
                        like_count=(int)snapshot.child(postKey).getChildrenCount();
                        like.setImageResource(R.drawable.is_interested);
                        like_display.setText(Integer.toString(like_count));

                    }else {
                        like_count=(int)snapshot.child(postKey).getChildrenCount();
                        like.setImageResource(R.drawable.ic_interested);
                        like_display.setText(Integer.toString(like_count));

                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });



        }

        public void setViewStatus(final String postKey) {
            views=itemView.findViewById(R.id.articleViews);
            view_display=itemView.findViewById(R.id.articleViewsCount);

            viewRef=FirebaseDatabase.getInstance().getReference("ArticleViews");
            FirebaseUser firebaseUser= FirebaseAuth.getInstance().getCurrentUser();
            final String userId=firebaseUser.getUid();

            viewRef.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {

                    if(snapshot.child(postKey).hasChild(userId)){
                        views_count=(int)snapshot.child(postKey).getChildrenCount();
                        view_display.setText(Integer.toString(views_count));

                    }
                    else {
                        views_count=(int)snapshot.child(postKey).getChildrenCount();
                        view_display.setText(Integer.toString(views_count));

                    }


                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });

        }
    }
}

