package br.moviesdb;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;

import br.moviesdb.ui.popular.PopularFragment;

public class SimilaresActivity extends AppCompatActivity {

    ListView listView;
    ArrayList<Movie> similares;
    MovieListAdapter adapter;
    MovieService service;
    Movie movie;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_similares);
        listView = findViewById(R.id.lista);
        similares = new ArrayList<>();
        service = new MovieService(getApplicationContext());
        adapter = new MovieListAdapter(similares,this);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Movie movie = (Movie)((MovieListAdapter)adapterView.getAdapter()).getItem(i);
                Intent intent = new Intent(getApplicationContext(), MovieDetailActivity.class);
                intent.putExtra("movie",movie);
                startActivity(intent);
            }
        });
    }


    public class MyAsyncTask extends AsyncTask<String, Integer, Integer> {

        public ArrayList<Movie> lista = new ArrayList<Movie>();

        @Override
        protected Integer doInBackground(String... strings) {

            MovieService service = new MovieService(getApplicationContext());
            lista = service.getSimilares(movie);


            Log.d("SimilaresActivity()","API Similares received");
            return 0;
        }

        @Override
        protected void onPostExecute(Integer integer) {
            similares = lista;
            if(similares == null || similares.size() == 0) {
                Toast.makeText(getApplicationContext(),"Sem internet.",Toast.LENGTH_LONG).show();
            } else {
                adapter.setItens(similares);
                adapter.notifyDataSetChanged();
            }
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        movie = (Movie)getIntent().getExtras().getSerializable("movie");

        setTitle(getResources().getString(R.string.similares) + ": " + movie.getTitle());


        MyAsyncTask task = new MyAsyncTask();
        task.execute("");

    }
}
