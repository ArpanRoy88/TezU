package com.example.tezu.Fragment;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.tezu.ArticleActivity;
import com.example.tezu.MagzActivity;
import com.example.tezu.R;

public class MagazineFragment extends Fragment {

    ImageView magz;
    ImageView article;
    private TextView textView;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View v=inflater.inflate(R.layout.fragment_magazine,container,false);

        magz=v.findViewById(R.id.magz);

        article=v.findViewById(R.id.articles);

        textView = v.findViewById(R.id.customText);
        Typeface customFont = Typeface.createFromAsset(getActivity().getAssets(), "fonts/DrSugiyama-Regular.ttf");
        textView.setTypeface(customFont);

        magz.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(), MagzActivity.class));
            }
        });
        article.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getActivity(), ArticleActivity.class));
            }
        });


        return v;

    }
}
