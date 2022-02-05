package com.acpereiraz.blocknotes;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private com.google.android.material.floatingactionbutton.FloatingActionButton button1;
    private ListView Notas;
    public static ArrayList<String> Files = new ArrayList<String>();
    static ArrayAdapter<String> notFound = null;
    public String caminho = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        Notas = (ListView) findViewById(R.id.Notas);
        notFound = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, Files);
        Notas.setAdapter(notFound);

        caminho = com.acpereiraz.blocknotes.Main2Activity.getDocumentsDir(getApplicationContext()).toString();

        getFiles(Files, caminho);

        Notas.setOnItemClickListener(new AdapterView.OnItemClickListener(){
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String arquivo = Files.get(position);
                Files.remove(position);
                abrirArquivo(caminho, arquivo);
            }
        });

        Notas.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                String arquivo = Files.get(position);
                Files.remove(position);
                notFound.notifyDataSetChanged();
                deletarArquivo(caminho, arquivo);
                return true;
            }
        });

        //Criar botão e setar a ação de clique
        button1 = (com.google.android.material.floatingactionbutton.FloatingActionButton) findViewById(R.id.abrirEditor);
        button1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                abrirAct(null, null);
            }
        });
    }

    public void deletarArquivo(String caminho, String nomeArquivo){
        try {
            File arquivo = new File(caminho, nomeArquivo);
            arquivo.delete();
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public static String convertStreamToString(InputStream is) throws Exception{
        BufferedReader reader = new BufferedReader(new InputStreamReader(is));
        StringBuilder sb = new StringBuilder();
        String line = null;
        while ((line = reader.readLine()) != null) {
            sb.append(line).append("\n");
        }
        reader.close();
        return sb.toString();
    }

    public void abrirArquivo(String caminho, String nomeArquivo){
        try {
            File arquivo = new File(caminho, nomeArquivo);
            FileInputStream entrada = new FileInputStream(arquivo);
            String file = convertStreamToString(entrada);
            entrada.close();
            nomeArquivo = nomeArquivo.replace(".txt", "");
            abrirAct(file, nomeArquivo);
        }catch(Exception e){
            e.printStackTrace();
        }
    }

    public void getFiles(ArrayList array, String caminho){

        File dir = new File(caminho);
        File[] files = dir.listFiles();
        for (int x = 0; x < files.length; x++)
        {
            String fileName = files[x].getName();
            if (checkFiles(array, caminho, fileName)) {
                array.add(fileName);
            }
        }

    }

    public boolean checkFiles(ArrayList array, String caminho, String fileName){

        for (int i=0; i < array.size(); i++){
            if (array.get(i) == fileName){
                return false;
            }
        }
        return true;
    }

    //Metodo responsável por iniciar a outra activity
    public void abrirAct(String file, String nomeArquivo){
        Intent intent = new Intent(this, Main2Activity.class);
        intent.putExtra("Arquivo", file);
        intent.putExtra("Nome", nomeArquivo);
        startActivity(intent);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
