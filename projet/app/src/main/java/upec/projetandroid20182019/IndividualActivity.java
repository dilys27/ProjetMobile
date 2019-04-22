package upec.projetandroid20182019;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;

import upec.projetandroid20182019.entity.Account;
import upec.projetandroid20182019.sqlite.AccountsBDD;

public class IndividualActivity extends AppCompatActivity {

    private FloatingActionButton fab_add;
    ArrayList<Account> accountList; //liste des comptes créés par l'utilisateur
    AccountAdapter adapter;
    private ListView listView;
    private AccountsBDD accountsBDD;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_individual);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Listes des comptes");

        accountList = new ArrayList<>();
        listView = findViewById(R.id.listView);

        accountsBDD = new AccountsBDD(this);
        accountsBDD.open();

        ArrayList<Account> accountList = new ArrayList<>();

        showAccountsFromDatabase();

        fab_add = findViewById(R.id.fab_add);
        fab_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), AddActivity.class);
                startActivity(intent);
                accountsBDD.close();
                finish();
            }
        });

    }

    private void showAccountsFromDatabase() {
        //we used rawQuery(sql, selectionargs) for fetching all the accounts
        Cursor cursorAccounts = accountsBDD.getBDD().rawQuery("SELECT * FROM accounts", null);

        //if the cursor has some data
        if (cursorAccounts.moveToFirst()) {
            //looping through all the records
            do {
                //pushing each record in the accounts list
                accountList.add(new Account(
                        cursorAccounts.getInt(0),
                        cursorAccounts.getString(1),
                        cursorAccounts.getString(2),
                        cursorAccounts.getString(3)
                        ));
            } while (cursorAccounts.moveToNext());
        }
        //closing the cursor
        cursorAccounts.close();

        //creating the adapter object
        adapter = new AccountAdapter(this, R.layout.list_accounts, accountList, accountsBDD.getBDD(), accountsBDD);

        //adding the adapter to listview
        listView.setAdapter(adapter);
    }
}
