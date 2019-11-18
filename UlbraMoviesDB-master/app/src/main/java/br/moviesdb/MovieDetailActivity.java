package br.moviesdb;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import org.w3c.dom.Text;

public class MovieDetailActivity extends AppCompatActivity {

    Movie movie;
    Button btnFavoritos;
    Button btnSimilares;
    TextView titulo;
    TextView codigo;
    TextView popularidade;
    TextView descricao;
    ImageView image;
    MovieService movieService;
    boolean isFavorito;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_detail);
        titulo = findViewById(R.id.titulo);
        codigo = findViewById(R.id.codigo);
        popularidade = findViewById(R.id.popularidade);
        descricao = findViewById(R.id.descricao);
        image = findViewById(R.id.imagem);
        btnFavoritos = findViewById(R.id.btnFavoritos);
        btnSimilares = findViewById(R.id.btnSimilares);
        movieService = new MovieService(getApplicationContext());

        movie = (Movie)getIntent().getExtras().getSerializable("movie");
        if(movie == null) {
            movie = new Movie();
            Log.d("MovieDetailActivity()","objeto nulo da intent");
        } else {
            Log.d("MovieDetailActivity()","objeto: " + movie);
        }

        btnFavoritos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(isFavorito) {
                    movieService.deleteFavorite(movie);
                    btnFavoritos.setText(R.string.add_favoritos);
                } else {
                    movieService.addFavorite(movie);
                    btnFavoritos.setText(R.string.del_favoritos);
                }
            }
        });

        btnSimilares.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(),SimilaresActivity.class);
                intent.putExtra("movie",movie);
                startActivity(intent);
            }
        });


    }

    @Override
    protected void onResume() {
        super.onResume();

        setTitle(getResources().getString(R.string.filme) + ": " + movie.getTitle());

        String basePath = "https://image.tmdb.org/t/p/w300_and_h450_bestv2";
        titulo.setText(movie.getTitle());
        codigo.setText(String.valueOf(movie.getId()));
        popularidade.setText(getResources().getText(R.string.popularity)  + ": " + movie.getPopularity());
        descricao.setText(movie.getOverview());
        Picasso.get().load(basePath + movie.getThumb()).into(image);
        isFavorito = movieService.isFavorite(movie);

        if(isFavorito) {
            btnFavoritos.setText(R.string.del_favoritos);
        } else {
            btnFavoritos.setText(R.string.add_favoritos);
        }


    }
}
