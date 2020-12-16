package com.example.tezu;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

//BottomSheetDialogFragment
public class BottomSheetDialog  extends BottomSheetDialogFragment {

//    private BottomSheetListener mListener;
    // Adding view to container
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.bottom_sheet_layout, container, false);
        //referencing to button

        Button button_post = v.findViewById(R.id.button_post);
        Button button_magazine = v.findViewById(R.id.button_magazine);
        Button button_article = v.findViewById(R.id.button_article);
        button_post.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openPostActivity();
            }

        });
        button_magazine.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openMagazineActivity();
            }

        });
        button_article.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openArticleActivity();
            }

        });
        return v;
    }

    private void openArticleActivity() {
        Intent goToPostMyArticle=new Intent(getActivity(),PostMyArticleActivity.class);
        startActivity(goToPostMyArticle);
    }

    private void openMagazineActivity() {
        Intent goToArticleUpload=new Intent(getActivity(), MagazineUpload.class);
        startActivity(goToArticleUpload);
    }

    public void openPostActivity() {
        Intent intent = new Intent(getActivity(), PostActivity.class);
        startActivity(intent);
    }

    //Creating an interface with a method in it
//    public interface BottomSheetListener{
//        void onButtonClicked(String text);
//
//    }

//    @Override
//    public void onAttach(@NonNull Context context) {
//        super.onAttach(context);
//
//        try {
//            mListener = (BottomSheetListener) context;
//        } catch (ClassCastException e) {
//            throw new ClassCastException(context.toString() + " must implement BottomSheetListener");
//        }
//
//    }
}
