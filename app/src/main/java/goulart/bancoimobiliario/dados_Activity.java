package goulart.bancoimobiliario;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

public class dados_Activity extends AppCompatActivity{

    List<dadosJogadores> dadosList;
    DadosAdapter dadosAdapter;
    SQLiteDatabase bancoImo;
    ListView listViewDados;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dados_layout);

        listViewDados = findViewById(R.id.listarDadosView);

        dadosList = new ArrayList<>();

        bancoImo = openOrCreateDatabase(MainActivity.Banco_Imobiliario, MODE_PRIVATE, null);

        visualizarDadosDatabase();
    }

    private void visualizarDadosDatabase() {
        Cursor cursorDados = bancoImo.rawQuery("SELECT * FROM dadosJogadores", null);

        if (cursorDados.moveToFirst()) {
            do {
                dadosList.add(new dadosJogadores(
                        cursorDados.getInt(0),
                        cursorDados.getString(1),
                        cursorDados.getString(2),
                        cursorDados.getString(3),
                        cursorDados.getDouble(4)
                ));
            } while (cursorDados.moveToNext());
        }
        cursorDados.close();

        dadosAdapter = new DadosAdapter(this, R.layout.lista_view_jogadores, dadosList, bancoImo);

        listViewDados.setAdapter(dadosAdapter);
    }
}