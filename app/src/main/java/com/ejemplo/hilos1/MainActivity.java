package com.ejemplo.hilos1;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    Button button1;
    Button button2;
    Button button3;
    Button button4;
    TextView txtContador;
    EditText edtNumero;
    ProgressBar progressBar1;
    ProgressBar progressBar2;
    int i;
    ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        button1 = (Button) findViewById(R.id.button1);
        button2 = (Button) findViewById(R.id.button2);
        button3 = (Button) findViewById(R.id.button3);
        button4 = (Button) findViewById(R.id.button4);
        txtContador = (TextView) findViewById(R.id.textView);
        edtNumero = (EditText) findViewById(R.id.edtNumero);
        progressBar1 = (ProgressBar) findViewById(R.id.progressBar1);


        button1.setEnabled(false);
        button2.setEnabled(false);
        button1.setOnClickListener(this);
        button2.setOnClickListener(this);
        button3.setOnClickListener(this);
        button4.setOnClickListener(this);
    }

    private void UnSegundo() {

        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onClick(View v) {
        int i;
        switch (v.getId()) {

            case R.id.button1:
                for (i = 1; i <= 10; i++) {
                    UnSegundo();
                }
                Toast.makeText(getApplicationContext(), "Segundos:" + i, Toast.LENGTH_LONG).show();
                break;
            case R.id.button2:
                for (i = 1; i <= 10; i++) {
                    UnSegundo();
                }
                Toast.makeText(getApplicationContext(), "Segundos:" + i, Toast.LENGTH_LONG).show();
                break;
            case R.id.button3:
                if (edtNumero.getText().toString().isEmpty()){
                    Toast.makeText(this, "No se ha ingresado ni un numero", Toast.LENGTH_SHORT).show();
                }
                else {
                    hilos(Integer.parseInt(edtNumero.getText().toString()));
                }

                break;
            case R.id.button4:
                if (edtNumero.getText().toString().isEmpty()){
                    Toast.makeText(this, "No se ha ingresado ni un numero", Toast.LENGTH_SHORT).show();
                }
                else{
                    AsyncTarea asyncTarea = new AsyncTarea();
                    asyncTarea.execute(Integer.parseInt(edtNumero.getText().toString()));
                }

                break;
            default:
                break;
        }

    }

    public void hilos(final int numero) {

        new Thread(new Runnable() {
            @Override
            public void run() {
                i =0;
                progressBar1.setMax(numero*10);
                progressBar1.setProgress(0);
                while(i<=numero-1){
                    UnSegundo();
                    i++;
                // Para generar un error con la UI Thread
               //Toast.makeText(getBaseContext(), "Segundos:5", Toast.LENGTH_LONG).show();

                runOnUiThread(new Runnable() {

                    @Override
                    public void run() {
                        progressBar1.setProgress(i*10);

                        txtContador.setText(""+i);
                        //Toast.makeText(getBaseContext(), "Segundos:5", Toast.LENGTH_LONG).show();
                    }

                });

                }

            }
        }).start();
    }

    private class  AsyncTarea extends AsyncTask<Integer, Integer,Boolean>{
        @Override
        protected void onPreExecute() {
            super.onPreExecute();

        }

        @Override
        protected Boolean doInBackground(Integer... params) {
            progressBar1.setMax(params[0]*10);
            for (int i=1; i<=params[0]; i++){
                UnSegundo();

                publishProgress(i*10);

                if (isCancelled()){
                    break;
                }
            }
            return true;
        }


        @Override
        protected void onProgressUpdate(Integer... values) {
            super.onProgressUpdate(values);

            //Actualizar la barra de progreso
            progressBar1.setProgress(values[0].intValue());
            txtContador.setText(""+((values[0].intValue())/10));
        }

        @Override
        protected void onPostExecute(Boolean aVoid) {
            //super.onPostExecute(aVoid);

            if (aVoid){
                Toast.makeText(getApplicationContext(),"Tarea finaliza AsyncTask",Toast.LENGTH_SHORT).show();
            }
        }


        @Override
        protected void onCancelled() {
            super.onCancelled();

            Toast.makeText(getApplicationContext(),"Tarea NO finaliza AsyncTask",Toast.LENGTH_SHORT).show();

        }


    }
}
