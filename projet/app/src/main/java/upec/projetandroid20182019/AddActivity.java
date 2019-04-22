package upec.projetandroid20182019;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.Toast;

import upec.projetandroid20182019.entity.Account;
import upec.projetandroid20182019.entity.Person;
import upec.projetandroid20182019.sqlite.AccountsBDD;
import upec.projetandroid20182019.sqlite.PersonsBDD;

public class AddActivity extends AppCompatActivity {

    private LinearLayout myLayout;
    private ScrollView scrollView;
    private TextInputEditText til_titre, til_desc, til_devise;
    private Button btn_participant, btn_create;
    private Toolbar toolbar;
    private Account account;
    private AccountsBDD accountsBDD;
    private PersonsBDD personsBDD;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add);

        toolbar = findViewById(R.id.toolbar);
        til_titre = findViewById(R.id.titre);
        til_desc = findViewById(R.id.desc);
        til_devise = findViewById(R.id.devise);

        myLayout = findViewById(R.id.layout);
        scrollView = findViewById(R.id.scrollV);

        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Nouveau Account");

        accountsBDD = new AccountsBDD(this);
        accountsBDD.open();
        personsBDD = new PersonsBDD(this);
        personsBDD.open();
        account = new Account();

        btn_participant = findViewById(R.id.btn_participant);
        btn_participant.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                addPerson(account);
            }
        });

        btn_create = findViewById(R.id.btn_create);
        btn_create.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                createAccount(account);

            }
        });
    }

    private void addPerson(final Account account) {
        LayoutInflater layoutInflater =
                (LayoutInflater) getBaseContext().getSystemService(AddActivity.LAYOUT_INFLATER_SERVICE);
        final View addView = layoutInflater.inflate(R.layout.add_part, null);

        final TextInputEditText til_participant = addView.findViewById(R.id.participant);

        final Button buttonRemove = (Button) addView.findViewById(R.id.remove);
        final Button btn_add_person = addView.findViewById(R.id.btn_add_person);

        btn_add_person.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String name = til_participant.getText().toString().trim();
                final Person person = new Person(account, name);
                account.getParticipants().add(person);
                Toast.makeText(getApplicationContext(), "Ce participant "+person.getName()+" a bien été ajouté !", Toast.LENGTH_LONG).show();
                Log.d("APP", "Ce participant "+person.getName()+" a bien été ajouté !");
            }
        });

        final View.OnClickListener thisListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String name = til_participant.getText().toString().trim();
                final Person person = new Person(account, name);
                account.getParticipants().remove(person);
                Toast.makeText(getApplicationContext(), "Ce participant "+person.getName()+" a bien été supprimé !", Toast.LENGTH_LONG).show();
                Log.d("APP", "Ce participant "+person.getName()+" a bien été supprimé !");
                ((LinearLayout) addView.getParent()).removeView(addView);
            }
        };

        buttonRemove.setOnClickListener(thisListener);
        myLayout.addView(addView);
    }

    private void createAccount(Account account) {
        String titre = til_titre.getText().toString().trim();
        String description = til_desc.getText().toString().trim();
        String devise = til_devise.getText().toString().trim();

        if(titre.isEmpty()) {
            Toast.makeText(getApplicationContext(), "Le titre doit contenir au moins un caractère !", Toast.LENGTH_LONG).show();
        }else if(account.getParticipants().size() == 0){
            Toast.makeText(getApplicationContext(), "L'account doit contenir au moins un participant dont vous !", Toast.LENGTH_LONG).show();
        }else {
            if(description.isEmpty())
                description="Pas de description";
            account.setTitre(titre);
            account.setDescription(description);
            account.setDevise(devise);
            accountsBDD.insertAccount(account);
            account = accountsBDD.getAccountWithTitre(account.getTitre()); //récupération de l'account créé dans la bd
            Log.d("APP", "Account ID : "+account.getID());
            for (Person person : account.getParticipants()){
                person.setAid(account.getID());
                personsBDD.insertPerson(person);
            }
            accountsBDD.close();
            personsBDD.close();
            Toast.makeText(getApplicationContext(), "Cet account a bien été créé !", Toast.LENGTH_LONG).show();
            Intent intent = new Intent(getApplicationContext(), IndividualActivity.class);
            startActivity(intent);
            finish();
        }

    }
}
