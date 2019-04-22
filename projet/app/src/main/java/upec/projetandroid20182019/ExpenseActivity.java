package upec.projetandroid20182019;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ListView;

import java.util.ArrayList;

import upec.projetandroid20182019.entity.Account;
import upec.projetandroid20182019.entity.Expense;
import upec.projetandroid20182019.mode_connectee.LoginActivity;
import upec.projetandroid20182019.sqlite.AccountsBDD;
import upec.projetandroid20182019.sqlite.ExpensesBDD;

public class ExpenseActivity extends AppCompatActivity {

    public static Account a;
    ArrayList<Expense> expensesList; //liste des dépenses créés par l'utilisateur
    ExpenseAdapter adapter;
    private ListView listView;
    private ExpensesBDD expensesBDD;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_expense);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Liste des dépenses");

        expensesList = new ArrayList<>();
        listView = findViewById(R.id.lv);

        expensesBDD = new ExpensesBDD(this);
        expensesBDD.open();
        if(expensesBDD.isExist()) {
            showExpensesFromDatabase();
        }
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab_add);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), AddExpense.class);
                startActivity(intent);
                expensesBDD.close();
                finish();
            }
        });
    }

    private void showExpensesFromDatabase() {
        //we used rawQuery(sql, selectionargs) for fetching all the accounts
        Cursor cursorExpenses = expensesBDD.getBDD().rawQuery("SELECT * FROM expenses WHERE aid = "+a.getID()+";", null);

        //if the cursor has some data
        if (cursorExpenses.moveToFirst()) {
            //looping through all the records
            do {
                //pushing each record in the expenses list
                expensesList.add(new Expense(
                        cursorExpenses.getInt(0),
                        cursorExpenses.getString(1),
                        cursorExpenses.getDouble(2),
                        cursorExpenses.getString(3),
                        cursorExpenses.getInt(4),
                        cursorExpenses.getInt(5)
                ));
            } while (cursorExpenses.moveToNext());
        }
        //closing the cursor
        cursorExpenses.close();

        //creating the adapter object
        adapter = new ExpenseAdapter(this, R.layout.list_expenses, expensesList, expensesBDD.getBDD(), expensesBDD);

        //adding the adapter to listview
        listView.setAdapter(adapter);
    }

}
