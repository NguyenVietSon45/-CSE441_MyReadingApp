package com.example.myreadingapp;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;


import com.example.myreadingapp.Adapters.GenreAdapter;
import com.example.myreadingapp.Info.Genre;

import java.util.ArrayList;
import java.util.List;

public class tim_kiem_genre extends AppCompatActivity {
    private Spinner spnGenre;
    private LinearLayout linear;
    private ImageView imgview;
    private GenreAdapter genreAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_tim_kiem_genre);

        spnGenre=findViewById(R.id.spn_genre_1);
        genreAdapter=new GenreAdapter(this,R.layout.genre_selected, getListGenre());
        spnGenre.setAdapter(genreAdapter);
        spnGenre.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                Toast.makeText(tim_kiem_genre.this,genreAdapter.getItem(i).getName(), Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        linear=findViewById(R.id.search_layout);
        linear.setVisibility(View.GONE);
        boolean isToggled = false;
        // lúc bấm thì set none để hiện
        imgview=findViewById(R.id.sort);
        imgview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isToggled == false){
                    linear.setVisibility(View.VISIBLE);
                }else {
                    linear.setVisibility(View.GONE);

                }
            }
        });
    }


    private List<Genre> getListGenre() {
        List<Genre> List= new ArrayList<>();
        List.add(new Genre("Tình cảm"));
        List.add(new Genre("Hành động"));
        List.add(new Genre("Trinh thám"));
        List.add(new Genre("Thể thao"));
        List.add(new Genre("Hài hước"));
        List.add(new Genre("Học đường"));
        List.add(new Genre("Shoujo"));
        List.add(new Genre("Josei"));
        List.add(new Genre("Bi kịch"));
        List.add(new Genre("Smut"));
        List.add(new Genre("Bí ẩn"));
        List.add(new Genre("Tâm lí"));

        return List;
    }
}