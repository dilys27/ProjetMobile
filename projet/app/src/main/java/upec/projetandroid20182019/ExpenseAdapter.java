package upec.projetandroid20182019;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

import upec.projetandroid20182019.entity.Expense;
import upec.projetandroid20182019.sqlite.AccountsBDD;
import upec.projetandroid20182019.sqlite.ExpensesBDD;
import upec.projetandroid20182019.sqlite.PersonsBDD;

public class ExpenseAdapter extends ArrayAdapter<Expense> {
    private Context mCtx;
    private int listLayoutRes;
    private ArrayList<Expense> expensesList;
    private SQLiteDatabase mDatabase;
    private AccountsBDD accountsBDD;
    private PersonsBDD personsBDD;
    private ExpensesBDD expensesBDD;

    public ExpenseAdapter(Context mCtx, int listLayoutRes, ArrayList<Expense> expensesList, SQLiteDatabase mDatabase, ExpensesBDD expensesBDD) {
        super(mCtx, listLayoutRes, expensesList);

        this.mCtx = mCtx;
        this.listLayoutRes = listLayoutRes;
        this.expensesList = expensesList;
        this.mDatabase = mDatabase;
        this.expensesBDD = expensesBDD;
        this.personsBDD = new PersonsBDD(mCtx);
        this.accountsBDD = new AccountsBDD(mCtx);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        LayoutInflater inflater = LayoutInflater.from(mCtx);
        View view = inflater.inflate(listLayoutRes, null);

        final Expense expense = expensesList.get(position);

        personsBDD.open();
        accountsBDD.open();

        TextView tvNom = view.findViewById(R.id.tvNom);
        TextView tvPaidBy = view.findViewById(R.id.tvPaidBy);
        TextView tvAmount = view.findViewById(R.id.tvAmount);
        TextView tvDate = view.findViewById(R.id.tvDate);

        tvNom.setText(expense.getNom_depense());
        //Récupère le nom par l'id de Person
        tvPaidBy.setText(personsBDD.getPersonWithID(expense.getPaidBy()).getName());
        //Récupère la devise par l'id de Account
        tvAmount.setText(expense.getAmount().toString()+" "+accountsBDD.getAccountWithID(expense.getAid()).getDevise());
        tvDate.setText(expense.getDate());

        Button buttonDelete = view.findViewById(R.id.delete);
        Button buttonEdit = view.findViewById(R.id.edit);

        //adding a clicklistener to button
        buttonEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                updateExpense(expense);
            }
        });

        //the delete operation
        buttonDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(mCtx);
                builder.setTitle("Are you sure?");
                builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        expensesBDD.removeExpenseWithID(expense.getId());
                        reloadExpensesFromDatabase();
                        personsBDD.close();
                        expensesBDD.close();
                        accountsBDD.close();
                        Intent intent = new Intent(mCtx.getApplicationContext(), ExpenseActivity.class);
                        mCtx.startActivity(intent);
                    }
                });
                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                });
                AlertDialog dialog = builder.create();
                dialog.show();
            }
        });

        return view;
    }

    private void updateExpense(final Expense expense) {
        final AlertDialog.Builder builder = new AlertDialog.Builder(mCtx);

        LayoutInflater inflater = LayoutInflater.from(mCtx);
        View view = inflater.inflate(R.layout.dialog_update_expense, null);
        builder.setView(view);


        final EditText editNom = view.findViewById(R.id.editNom);
        final EditText editAmount = view.findViewById(R.id.editAmount);
        final EditText editDate = view.findViewById(R.id.editDate);

        editNom.setText(expense.getNom_depense());
        editAmount.setText(expense.getAmount().toString());
        editDate.setText(expense.getDate());

        final AlertDialog dialog = builder.create();
        dialog.show();

        view.findViewById(R.id.Update).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String nom = editNom.getText().toString().trim();
                String amount = editAmount.getText().toString().trim();
                String date = editDate.getText().toString().trim();

                if (nom.isEmpty()) {
                    editNom.setError("Nom can't be blank");
                    editNom.requestFocus();
                    return;
                }

                if (amount.isEmpty()) {
                    editAmount.setError("Amount can't be blank");
                    editAmount.requestFocus();
                    return;
                }

                if (date.isEmpty()) {
                    editDate.setError("Date can't be blank");
                    editDate.requestFocus();
                    return;
                }

                expense.setNom_depense(nom);
                expense.setAmount(Double.parseDouble(amount));
                expense.setDate(date);
                expensesBDD.updateExpense(expense.getID(), expense);
                Toast.makeText(mCtx, "Expense Updated", Toast.LENGTH_SHORT).show();
                reloadExpensesFromDatabase();

                dialog.dismiss();
            }
        });
    }

    private void reloadExpensesFromDatabase() {
        Cursor cursorExpenses = mDatabase.rawQuery("SELECT * FROM expenses WHERE aid = "+ExpenseActivity.a.getID()+";", null);
        if (cursorExpenses.moveToFirst()) {
            expensesList.clear();
            do {
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
        cursorExpenses.close();
    }
}
