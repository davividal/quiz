package com.example.quiz;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Locale;
import java.util.Random;

public class MainActivity extends AppCompatActivity implements Response.Listener, Response.ErrorListener {
    private String ServiceUrl = "http://ec2-52-67-202-68.sa-east-1.compute.amazonaws.com/perguntas.php/%d";
    private ArrayList<Integer> Questions = new ArrayList<>();
    private String respostaCorreta;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Questions.add(1);
        Questions.add(2);
        Questions.add(3);
        Questions.add(4);
        Questions.add(5);
        setContentView(R.layout.activity_main);
        sortearPergunta();
    }

    private void sortearPergunta() {
        if (Questions.size() > 0) {
            Integer index = new Random().nextInt(Questions.size());
            Integer question = Questions.get(index);
            Questions.remove(Questions.indexOf(question));

            String url = String.format(new Locale("en", "us"), ServiceUrl, question);
            RequestQueue queue = CustomVolleyRequestQueue.getInstance(getApplicationContext()).getRequestQueue();

            CustomJSONObjectRequest request = new CustomJSONObjectRequest(url, new JSONObject(), this, this);
            request.setTag("QuizApp");
            queue.add(request);
        }
    }

    public void responder(View view) {
        RadioGroup alternativas = (RadioGroup) findViewById(R.id.alternativas);
        Integer resposta = alternativas.getCheckedRadioButtonId();

        if (respostaCorreta.equals(radioToString(resposta))) {
            Toast.makeText(this, "Resposta certa!", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "Resposta errada!", Toast.LENGTH_SHORT).show();
        }

        sortearPergunta();
    }

    private String radioToString(Integer id) {
        switch (id) {
            case R.id.alternativa_a:
                return "a";
            case R.id.alternativa_b:
                return "b";
            case R.id.alternativa_c:
                return "c";
            case R.id.alternativa_d:
                return "d";
        }
        return "";
    }

    @Override
    public void onErrorResponse(VolleyError error) {
        Toast.makeText(this, "Ocorreu um erro!", Toast.LENGTH_LONG).show();
    }

    @Override
    public void onResponse(Object response) {
        /**
         * {
         * "pergunta":"Como se chama o lago salgado, que é parte mais baixa da Terra, situado na Palestina?",
         * "a":"Mar Morto","b":"Mar Cáspio","c":"Mar Mediterrneo","d":"Mar do Caribe",
         * "resposta":"a"
         * }
         * */
        JSONObject objPergunta = (JSONObject) response;

        String rPergunta;
        String rAlternativaA;
        String rAlternativaB;
        String rAlternativaC;
        String rAlternativaD;

        try {
            rPergunta = objPergunta.getString("pergunta");
            rAlternativaA = objPergunta.getString("a");
            rAlternativaB = objPergunta.getString("b");
            rAlternativaC = objPergunta.getString("c");
            rAlternativaD = objPergunta.getString("d");
            respostaCorreta = objPergunta.getString("resposta");
        } catch (JSONException e) {
            Toast.makeText(this, "Ocorreu um erro ao parsear o JSON", Toast.LENGTH_SHORT).show();
            return;
        }

        EditText pergunta = (EditText) findViewById(R.id.texto_pergunta);
        RadioButton alternativaA = (RadioButton) findViewById(R.id.alternativa_a);
        RadioButton alternativaB = (RadioButton) findViewById(R.id.alternativa_b);
        RadioButton alternativaC = (RadioButton) findViewById(R.id.alternativa_c);
        RadioButton alternativaD = (RadioButton) findViewById(R.id.alternativa_d);
        ((RadioGroup) findViewById(R.id.alternativas)).clearCheck();

        pergunta.setText(rPergunta);
        alternativaA.setText(rAlternativaA);
        alternativaB.setText(rAlternativaB);
        alternativaC.setText(rAlternativaC);
        alternativaD.setText(rAlternativaD);
    }
}
