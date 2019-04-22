package upec.projetandroid20182019;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import upec.projetandroid20182019.mode_connectee.AccueilMembre;
import upec.projetandroid20182019.mode_connectee.LoginActivity;
import upec.projetandroid20182019.mode_connectee.RegisterActivity;
import upec.projetandroid20182019.mode_connectee.SessionManager;

public class MainActivity extends AppCompatActivity {

    private Button btn_independant, btn_connecte;
    private SessionManager sessionManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getSupportActionBar().hide();
        /*sessionManager = new SessionManager(this);
        if(sessionManager.isLogged()){
            Intent intent = new Intent(this, AccueilMembre.class);
            startActivity(intent);
            finish();
        }*/

        btn_independant = findViewById(R.id.btn_independant); //mode indépendant
        btn_connecte = findViewById(R.id.btn_connecte); //mode connecté

        btn_independant.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), IndividualActivity.class);
                startActivity(intent);
            }
        });

        btn_connecte.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                startActivity(intent);
            }
        });
    }
}
