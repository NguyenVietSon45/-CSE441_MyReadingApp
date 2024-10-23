package com.example.myreadingapp;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;


import com.example.myreadingapp.Adapters.GenreAdapter;
import com.example.myreadingapp.Info.Genre;

import java.util.ArrayList;
import java.util.List;

public class Dang_truyen extends AppCompatActivity {
    private Spinner spnGenre;
    private GenreAdapter genreAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.dang_truyen);

        spnGenre=findViewById(R.id.spn_genre);
        genreAdapter=new GenreAdapter(this,R.layout.genre_selected, getListGenre());
        spnGenre.setAdapter(genreAdapter);
        spnGenre.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                Toast.makeText(Dang_truyen.this,genreAdapter.getItem(i).getName(), Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

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