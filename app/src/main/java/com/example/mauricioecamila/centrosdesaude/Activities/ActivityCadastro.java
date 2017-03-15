package com.example.mauricioecamila.centrosdesaude.Activities;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.mauricioecamila.centrosdesaude.Conexao;
import com.example.mauricioecamila.centrosdesaude.R;

public class ActivityCadastro extends AppCompatActivity {

    private EditText cadNome;
    private EditText cadSenha;
    private EditText cadEmail;

    private Button botaoCadastrar;
    private String url = "";
    private String parametros = "";

    private Button botaoCancelar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cadastro);

        //Typeface custom_font = Typeface.createFromAsset(getAssets(),"assets/Lato-Light.ttf");

        cadNome = (EditText) findViewById(R.id.cadNome);
        cadEmail = (EditText) findViewById(R.id.cadEmail);
        cadSenha = (EditText)findViewById(R.id.cadSenha);
        botaoCancelar = (Button)findViewById(R.id.botaoCancelar);
        botaoCadastrar = (Button)findViewById(R.id.botaoCadastrar);

        botaoCadastrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Verifica o estado da rede e conexão
                ConnectivityManager connMgr = (ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);
                NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
                //Se o estado da rede for diferente de nulo e a rede estiver conectada, irá executar
                if(networkInfo != null && networkInfo.isConnected()){
                    String nome = cadNome.getText().toString();
                    String email = cadEmail.getText().toString();
                    String senha = cadSenha.getText().toString();

                    //Verifica se há algo no email e senha
                    if(email.isEmpty() || senha.isEmpty() || nome.isEmpty()){
                        Toast.makeText(getApplicationContext(), "Nenhum campo pode estar vazio", Toast.LENGTH_LONG).show();
                    }
                    else{
                        //Criar a URL
                        url = "http://192.168.0.31:8090/login/registrar.php";
                        //url = "http://localhost:8090/login/logar.php";
                        parametros = "?nome=" + nome + "email=" + email + "&senha=" + senha;
                        new SolicitaDados().execute(url);
                    }

                }
                else{
                    Toast.makeText(getApplicationContext(), "Nenhuma conexão foi detectada", Toast.LENGTH_LONG).show();
                }
            }
           });

        botaoCancelar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent voltaLogin = new Intent(ActivityCadastro.this, ActivityLogin.class);
                startActivity(voltaLogin);
            }
        });

    }

    public class SolicitaDados extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... urls) {
            // params comes from the execute() call: params[0] is the url.
            return Conexao.postDados(urls[0],parametros);
            //} catch (IOException e) {
            //    return "Unable to download the requested page.";
            //}
        }

        // onPostExecute displays the results of the AsyncTask.
        @Override
        protected void onPostExecute(String resultado) {

            if(resultado.contains("email_erro")){
                Toast.makeText(getApplicationContext(),"Este email já está cadastrado", Toast.LENGTH_SHORT).show();
            }
            else if(resultado.contains("registro_ok")){
                Toast.makeText(getApplicationContext(),"Cadastro concluído com sucesso", Toast.LENGTH_SHORT).show();
                Intent abrePrincipal = new Intent(ActivityCadastro.this, ActivityLogin.class);
                startActivity(abrePrincipal);
            }
            else{
                Toast.makeText(getApplicationContext(),"Ocorreu um erro ao cadastrar", Toast.LENGTH_SHORT).show();
            }
        }
    }//SolicitaDados

    //Se sair da tela vai fechar
    @Override
    protected void onPause() {
        super.onPause();
        //finish();
    }
}
