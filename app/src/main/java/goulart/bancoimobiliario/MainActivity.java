package goulart.bancoimobiliario;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    public static final String Banco_Imobiliario = "dbImobiliario.db";

    TextView lblJogadores;
    EditText txtNomeJogador, txtDinheiroJogador;
    Spinner spnTotens;

    Button btnAdicionarJogador;

    //Declarando a variavel que terá todos os comandos do SQLite
    SQLiteDatabase bancoImo;

    //Create Database, Table
    //Insert, Select, Update, Delete

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        lblJogadores = findViewById(R.id.lblVisualizaDados);
        txtNomeJogador = findViewById(R.id.txtNomeNovoJogador);
        txtDinheiroJogador = findViewById(R.id.txtNovoDinheiroJogador);
        spnTotens = findViewById(R.id.spnTotens);

        btnAdicionarJogador = findViewById(R.id.btnAdicionarJogador);

        btnAdicionarJogador.setOnClickListener(this);
        lblJogadores.setOnClickListener(this);

        bancoImo = openOrCreateDatabase(Banco_Imobiliario, MODE_PRIVATE, null);

        criarTabelaBanco();
    }

    private boolean verificarEntrada(String nome, String dinheiro) {
        if (nome.isEmpty()) {
            txtNomeJogador.setError("Por favor entre com o Nome");
            txtNomeJogador.requestFocus();
            return false;
        }

        if (dinheiro.isEmpty()) {
            txtDinheiroJogador.setError("Por favor entre com o Dinheiro");
            txtDinheiroJogador.requestFocus();
            return false;
        }
        return true;
    }

    private void AdicionarJogador() {

        String nomeJog = txtNomeJogador.getText().toString().trim();
        String dinheiroJog = txtDinheiroJogador.getText().toString().trim();
        String totemJog = spnTotens.getSelectedItem().toString();

        // obtendo o horário atual para data de inclusão
        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
        String dataEntrada = simpleDateFormat.format(calendar.getTime());

        //validando entrada
        if (verificarEntrada(nomeJog, dinheiroJog)) {

            String insertSQL = "INSERT INTO dadosJogadores (" +
                    "nome, " +
                    "totem, " +
                    "dataEntrada," +
                    "dinheiro)" +
                    "VALUES(?, ?, ?, ?);";

            // usando o mesmo método execsql para inserir valores
            // desta vez tem dois parâmetros
            // primeiro é a string sql e segundo são os parâmetros que devem ser vinculados à consulta

            bancoImo.execSQL(insertSQL, new String[]{nomeJog, totemJog, dataEntrada, dinheiroJog});

            Toast.makeText(getApplicationContext(), "Jogador adicionado com sucesso!", Toast.LENGTH_SHORT).show();

            limparCadastro();
        }
    }

    public void limparCadastro() {
        txtNomeJogador.setText("");
        txtDinheiroJogador.setText("");
        txtNomeJogador.requestFocus();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btnAdicionarJogador:
                AdicionarJogador();
                break;
            case R.id.lblVisualizaDados:
                startActivity(new Intent(getApplicationContext(), dados_Activity.class));
                break;
        }
    }

    private void criarTabelaBanco() {
        bancoImo.execSQL(
                "CREATE TABLE IF NOT EXISTS dadosJogadores (" +
                        "id integer PRIMARY KEY AUTOINCREMENT," +
                        "nome varchar(200) NOT NULL," +
                        "totem varchar(200) NOT NULL," +
                        "dataEntrada datetime NOT NULL," +
                        "dinheiro double NOT NULL);"
        );
    }
}