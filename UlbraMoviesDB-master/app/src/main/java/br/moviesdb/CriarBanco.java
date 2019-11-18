package br.moviesdb;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class CriarBanco extends SQLiteOpenHelper {

    public static final String NOME_BANCO = "movies.db";
    public static final String TABELA = "favoritos";
    public static final String ID = "_id";
    public static final String CODIGO = "codigo";
    public static final String TITULO = "titulo";
    public static final String POSTER = "poster";
    public static final String DESCRICAO = "descricao";
    public static final String POPULARIDADE = "popularidade";
    public static final int VERSAO = 1;

    public CriarBanco(Context context){
        super(context, NOME_BANCO,null,VERSAO);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String sql = "CREATE TABLE "+TABELA+"("
                + ID + " integer primary key autoincrement,"
                + CODIGO + " integer,"
                + TITULO + " text,"
                + POSTER + " text,"
                + DESCRICAO + " text,"
                + POPULARIDADE + " float"
                +")";
        db.execSQL(sql);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1) {
        db.execSQL("DROP TABLE IF EXISTS " + TABELA);
        onCreate(db);
    }
}
