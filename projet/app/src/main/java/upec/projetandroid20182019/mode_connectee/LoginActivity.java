package upec.projetandroid20182019.mode_connectee;

import android.content.Intent;
import android.os.Handler;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.android.volley.RequestQueue;

import upec.projetandroid20182019.R;
import upec.projetandroid20182019.myrequest.MyRequest;

public class LoginActivity extends AppCompatActivity {

    private TextInputLayout til_pseudo, til_password;
    private Button btn_send, btn_register;
    private RequestQueue queue;
    private MyRequest request;
    private ProgressBar pb_loader;
    private Handler handler;
    private SessionManager sessionManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        //Récupération du message de l'inscription
        Intent intent = getIntent();
        if(intent.hasExtra("REGISTER")){
            Toast.makeText(this, intent.getStringExtra("REGISTER"), Toast.LENGTH_SHORT).show();
        }

        til_pseudo = findViewById(R.id.til_pseudo_log);
        til_password = findViewById(R.id.til_password_log);
        btn_send = findViewById(R.id.btn_send);
        pb_loader = findViewById(R.id.pb_loader);

        queue = VolleySingleton.getInstance(this).getRequestQueue();
        request = new MyRequest(this, queue);
        handler = new Handler();
        sessionManager = new SessionManager(this);
        if(sessionManager.isLogged()){
            Intent i = new Intent(this, AccueilMembre.class);
            startActivity(i);
            finish();
        }

        btn_send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String pseudo = til_pseudo.getEditText().getText().toString().trim();
                final String password = til_password.getEditText().getText().toString().trim();
                pb_loader.setVisibility(View.VISIBLE);
                if(pseudo.length() > 0 && password.length() > 0) {
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            request.connection(pseudo, password, new MyRequest.LoginCallBack() {
                                @Override
                                public void onSuccess(String id, String pseudo) {
                                    pb_loader.setVisibility(View.GONE);
                                    sessionManager.insertUser(id, pseudo);
                                    Intent intent = new Intent(getApplicationContext(), AccueilMembre.class);
                                    startActivity(intent);
                                    finish();
                                }

                                @Override
                                public void onError(String message) {
                                    pb_loader.setVisibility(View.GONE);
                                    Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();
                                }
                            });
                        }
                    }, 1000);
                }else{
                    Toast.makeText(getApplicationContext(), "Veuillez remplir tous les champs", Toast.LENGTH_SHORT).show();
                }
            }
        });

        btn_register = findViewById(R.id.btn_register);
        btn_register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), RegisterActivity.class);
                startActivity(intent);
            }
        });
    }
}
