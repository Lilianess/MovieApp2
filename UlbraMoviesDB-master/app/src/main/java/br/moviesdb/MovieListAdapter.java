package br.moviesdb;

import android.app.Activity;
import android.content.Intent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import br.moviesdb.Movie;
import br.moviesdb.R;

public class MovieListAdapter extends BaseAdapter {

    private ArrayList<Movie> itens;
    private Activity activity;


    public MovieListAdapter(ArrayList<Movie> itens, Activity act) {
        this.itens = itens;
        this.activity = act;
    }

    public void setItens(ArrayList<Movie> itens) {
        this.itens = itens;
    }

    @Override
    public int getCount() {
        return itens.size();
    }

    @Override
    public Object getItem(int i) {
        return itens.get(i);
    }

    @Override
    public long getItemId(int i) {
        return itens.get(i).getId();
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        String basePath = "https://image.tmdb.org/t/p/w300_and_h450_bestv2";
        View v = activity.getLayoutInflater().inflate(R.layout.movie_item,viewGroup,false);
        final Movie m = itens.get(i);
        ((TextView)v.findViewById(R.id.titulo)).setText(m.getTitle());
        ((TextView)v.findViewById(R.id.popularidade)).setText(activity.getResources().getText(R.string.popularity) + ": " + m.getPopularity());
        ImageView imageView = (ImageView)v.findViewById(R.id.imageView);
        Picasso.get().load(basePath + m.getThumb()).into(imageView);
        return v;
    }
}
