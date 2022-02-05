package com.acpereiraz.blocknotes;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;

public class Main2Activity extends AppCompatActivity {

    com.google.android.material.floatingactionbutton.FloatingActionButton salvarNota;
    public static EditText campoTexto;
    com.google.android.material.textfield.TextInputEditText campoArquivo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);

        salvarNota = (com.google.android.material.floatingactionbutton.FloatingActionButton) findViewById(R.id.abrirEditor);
        campoTexto = (EditText) findViewById(R.id.campoTexto);
        campoArquivo = (com.google.android.material.textfield.TextInputEditText) findViewById(R.id.campoArquivo);
        Intent intent2 = getIntent();
        Bundle b = intent2.getExtras();
        String arq = (String) b.get("Arquivo");
        String nArq = (String) b.get("Nome");
        campoTexto.setText(arq);
        campoArquivo.setText(nArq);

        salvarNota.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String textoInteiro = campoTexto.getText().toString();
                String nomeArquivo = campoArquivo.getText().toString();
                nomeArquivo = nomeArquivo+".txt";

                if (campoArquivo.getText().toString().length() < 1){

                    String semNomeArquivo = "Insira um nome para o arquivo a ser salvo!";
                    Context contexto = getApplicationContext();
                    Toast alerta = Toast.makeText(contexto, semNomeArquivo, Toast.LENGTH_SHORT);
                    alerta.show();

                }else if (textoInteiro != ""){

                    campoTexto.setText("");
                    campoTexto.findFocus();
                    if (!storageState()){

                    }else {
                        salvarNoSdcard(getApplicationContext(), nomeArquivo, textoInteiro);
                        MainActivity.Files.add(nomeArquivo);
                        MainActivity.notFound.notifyDataSetChanged();
                        finish();
                    }
                }
            }
        });

    }

    public boolean storageState() {
        if (Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState())) {
            return true;
        }
        return false;
    }



    public static File getDocumentsDir(Context context){
        String dir = "BlockNotes";
        File file = new File(context.getExternalFilesDir(Environment.DIRECTORY_DOCUMENTS), dir);
        file.mkdir();
        return file;
    }




    public void salvarNoSdcard(Context context, String nomeArquivo, String texto){
        try {
            File arquivo = new File(getDocumentsDir(context), nomeArquivo);
            FileOutputStream saida = new FileOutputStream(arquivo);
            saida.write(texto.getBytes());
            saida.close();
        }catch (IOException e){
            e.printStackTrace();
        }

    }
}
