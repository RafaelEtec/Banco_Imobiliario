package goulart.bancoimobiliario;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.sql.Array;
import java.util.ArrayList;
import java.util.List;

public class DadosAdapter extends ArrayAdapter<dadosJogadores> {
    Context mCtx;
    int listaLayoutRes;
    List<dadosJogadores> listaDados;
    SQLiteDatabase bancoImo;

    //Construtor da classe
    public DadosAdapter(Context mCtx, int listaLayoutRes, List<dadosJogadores> listaDados, SQLiteDatabase bancoImo) {
        super(mCtx, listaLayoutRes, listaDados);

        this.mCtx = mCtx;
        this.listaLayoutRes = listaLayoutRes;
        this.listaDados = listaDados;
        this.bancoImo= bancoImo;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        LayoutInflater inflater = LayoutInflater.from(mCtx);
        View view = inflater.inflate(listaLayoutRes, null);

        final dadosJogadores dadosjogadores = listaDados.get(position);

        TextView txtViewNome, txttViewTotem, txtViewDinheiro, txtViewDataEntrada;

        txtViewNome = view.findViewById(R.id.txtNomeViewJogador);
        txttViewTotem = view.findViewById(R.id.txtTotemViewJogador);
        txtViewDinheiro = view.findViewById(R.id.txtDinheiroViewJogador);
        txtViewDataEntrada = view.findViewById(R.id.txtEntradaviewJogador);

        txtViewNome.setText(dadosjogadores.getNome());
        txttViewTotem.setText(dadosjogadores.getTotem());
        txtViewDinheiro.setText(String.valueOf(dadosjogadores.getDinheiro()));
        txtViewDataEntrada.setText(dadosjogadores.getDataEntrada());

        Button btnExcluir, btnEditar;

        btnExcluir = view.findViewById(R.id.btnExcluirViewJogador);
        btnEditar = view.findViewById(R.id.btnEditarViewJogador);

        btnEditar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                alterarDados(dadosjogadores);
            }
        });

        btnExcluir.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(mCtx);
                builder.setTitle("Deseja excluir?");
                builder.setIcon(R.drawable.outline_cancel);
                builder.setPositiveButton("Sim", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                        String sql = "DELETE FROM dadosJogadores WHERE id = ?";

                        bancoImo.execSQL(sql, new Integer[]{dadosjogadores.getId()});
                        recarregarJogadoresDB();
                    }
                });
                builder.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        //somente vai voltar para tela.
                        recarregarJogadoresDB();

                    }
                });
                AlertDialog dialog = builder.create();
                dialog.show();
            }
        });

        return view;


    }

    public void alterarDados(final dadosJogadores dados) {

        final AlertDialog.Builder builder = new AlertDialog.Builder(mCtx);

        LayoutInflater inflater = LayoutInflater.from(mCtx);

        View view = inflater.inflate(R.layout.caixa_alterar_dados, null);
        builder.setView(view);

        final EditText txtEditarDados = view.findViewById(R.id.txtEditarDados);
        final EditText txtEditarDinheiro = view.findViewById(R.id.txtEditarDinheiro);
        final Spinner spnTotens = view.findViewById(R.id.spnTotens);

        txtEditarDados.setText(dados.getNome());
        txtEditarDinheiro.setText(String.valueOf(dados.getDinheiro()));

        //Criando o janela de diálogo
        final AlertDialog dialog = builder.create();
        //Mostrando a janela de diálogo
        dialog.show();

        view.findViewById(R.id.btnAlterarDados).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String nome = txtEditarDados.getText().toString().trim();
                String dinheiro = txtEditarDinheiro.getText().toString().trim();
                String totem = spnTotens.getSelectedItem().toString().trim();

                if (nome.isEmpty()) {
                    txtEditarDados.setError("Nome está em branco");
                    txtEditarDados.requestFocus();
                    return;
                }
                if (dinheiro.isEmpty()) {
                    txtEditarDinheiro.setError("Dinheiro está em branco");
                    txtEditarDinheiro.requestFocus();
                    return;
                }

                String sql = "UPDATE dadosJogadores SET nome = ?, totem = ?, dinheiro = ? WHERE id = ?";
                bancoImo.execSQL(sql,
                        new String[]{nome, totem, dinheiro, String.valueOf(dados.getId())});
                Toast.makeText(mCtx, "Dados alterados com sucesso!!!", Toast.LENGTH_LONG).show();

                recarregarJogadoresDB();

                dialog.dismiss();
            }
        });

    }

    //Realizar um select na tabela
    public void recarregarJogadoresDB() {
        Cursor cursorDados = bancoImo.rawQuery("SELECT * FROM dadosJogadores", null);
        if (cursorDados.moveToFirst()) {
            listaDados.clear();
            do {
                listaDados.add(new dadosJogadores(
                        cursorDados.getInt(0),
                        cursorDados.getString(1),
                        cursorDados.getString(2),
                        cursorDados.getString(3),
                        cursorDados.getDouble(4)
                ));
            } while (cursorDados.moveToNext());
        }
        cursorDados.close();
        notifyDataSetChanged();
    }
}