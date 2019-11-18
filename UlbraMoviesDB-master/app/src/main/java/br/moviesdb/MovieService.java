package br.moviesdb;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONObject;
import java.util.ArrayList;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class MovieService {

    private String API_KEY = "1162711f0a32afb07189bfd839506b26";

    private String API_URL = "https://api.themoviedb.org/3";

    private String METHOD_GET_POPULAR = "/movie/popular";
    private String METHOD_GET_TOPRATED = "/movie/top_rated";
    private String METHOD_GET_DETAIL = "/movie/";
    private String METHOD_GET_SIMILARES = "/movie/";

    private SQLiteDatabase db;
    private CriarBanco banco;


    public MovieService(Context context) {
        banco = new CriarBanco(context);
    }

    private String call(String method) throws Exception {
        try {
            String url = API_URL + method + "?api_key=" + API_KEY;
            OkHttpClient client = new OkHttpClient();
            Request request = new Request.Builder().url(url).build();
            Response response = client.newCall(request).execute();
            String json = response.body().string();
            Log.d("MovieService()->call: ",json);
            return json;
        } catch(Exception e) {
            throw e;
        }
    }

    public ArrayList<Movie> getToprated() {
        Log.d("getToprated()","executing ...");
        ArrayList<Movie> lista = new ArrayList<>();
        try {
            String json_string = this.call(this.METHOD_GET_TOPRATED);
            Log.d("getToprated()",json_string);
            JSONObject json = new JSONObject(json_string);
            JSONArray results = json.getJSONArray("results");
            int count = results.length();
            for(int i = 0; i < count; i++) {
                JSONObject obj = results.getJSONObject(i);
                Movie m = new Movie(
                    obj.getInt("id"),
                    obj.getString("title"),
                    obj.getString("overview"),
                    obj.getString("poster_path"),
                        obj.getDouble("popularity")
                );
                lista.add(m);
            }
        } catch(Exception e) {
            e.printStackTrace();
        }
        return lista;
    }

    public ArrayList<Movie> getPopular() {
        Log.d("getPopular()","executing ...");
        ArrayList<Movie> lista = new ArrayList<>();
        try {
            String json_string = this.call(this.METHOD_GET_POPULAR);
            Log.d("getPopular()",json_string);
            JSONObject json = new JSONObject(json_string);
            JSONArray results = json.getJSONArray("results");
            int count = results.length();
            for(int i = 0; i < count; i++) {
                JSONObject obj = results.getJSONObject(i);
                Movie m = new Movie(
                        obj.getInt("id"),
                        obj.getString("title"),
                        obj.getString("overview"),
                        obj.getString("poster_path"),
                        obj.getDouble("popularity")
                );
                lista.add(m);
            }
        } catch(Exception e) {
            e.printStackTrace();
        }
        return lista;
    }

    public ArrayList<Movie> getSimilares(Movie movie) {
        Log.d("getSimilares()","executing ...");
        ArrayList<Movie> lista = new ArrayList<>();
        try {
            String method = this.METHOD_GET_SIMILARES + movie.getId()+ "/similar";
            Log.d("getSimilares()","method: " + method);
            String json_string = this.call(method);
            Log.d("getSimilares()",json_string);
            JSONObject json = new JSONObject(json_string);
            JSONArray results = json.getJSONArray("results");
            int count = results.length();
            for(int i = 0; i < count; i++) {
                JSONObject obj = results.getJSONObject(i);
                Movie m = new Movie(
                        obj.getInt("id"),
                        obj.getString("title"),
                        obj.getString("overview"),
                        obj.getString("poster_path"),
                        obj.getDouble("popularity")
                );
                lista.add(m);
            }
        } catch(Exception e) {
            e.printStackTrace();
        }
        return lista;
    }




    public boolean isFavorite(Movie movie) {
        db = banco.getReadableDatabase();
        Cursor cursor;
        String[] campos =  {CriarBanco.ID};

        cursor = db.query(banco.TABELA, campos, "codigo = " + movie.getId(), null, null, null, null, null);

        return cursor.moveToFirst();

    }

    public void addFavorite(Movie movie) {
        db = banco.getWritableDatabase();
        ContentValues valores;
        long resultado;
        valores = new ContentValues();
        valores.put(CriarBanco.CODIGO, movie.id);
        valores.put(CriarBanco.TITULO, movie.title);
        valores.put(CriarBanco.DESCRICAO, movie.overview);
        valores.put(CriarBanco.POPULARIDADE, movie.popularity);
        valores.put(CriarBanco.POSTER, movie.thumb);
        resultado = db.insert(CriarBanco.TABELA, null, valores);
        db.close();
    }

    public void deleteFavorite(Movie movie) {
        db = banco.getWritableDatabase();
        db.delete(CriarBanco.TABELA,"codigo = " + movie.getId(),null);
        db.close();
    }

    public ArrayList<Movie>  getFavorites() {
        ArrayList<Movie> lista = new ArrayList<>();
        Cursor cursor;
        String[] campos =  {CriarBanco.ID, CriarBanco.CODIGO,CriarBanco.TITULO, CriarBanco.DESCRICAO, CriarBanco.POPULARIDADE, CriarBanco.POSTER};
        db = banco.getReadableDatabase();
        cursor = db.query(banco.TABELA, campos, null, null, null, null, null, null);
        boolean hasItens = cursor.moveToFirst();
        if(cursor!=null && hasItens){
            do {

                lista.add(
                    new Movie(
                        cursor.getInt(1),
                        cursor.getString(2),
                        cursor.getString(3),
                        cursor.getString(5),
                        cursor.getDouble(4)
                    )
                );

            }while(cursor.moveToNext());
        }


        db.close();

        return lista;
    }

}
