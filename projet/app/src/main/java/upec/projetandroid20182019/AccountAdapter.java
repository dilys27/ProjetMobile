package upec.projetandroid20182019;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

import upec.projetandroid20182019.entity.Account;
import upec.projetandroid20182019.entity.Expense;
import upec.projetandroid20182019.sqlite.AccountsBDD;
import upec.projetandroid20182019.sqlite.ExpensesBDD;
import upec.projetandroid20182019.sqlite.PersonsBDD;

public class AccountAdapter extends ArrayAdapter<Account> {
    private Context mCtx;
    private int listLayoutRes;
    private ArrayList<Account> accountsList;
    private SQLiteDatabase mDatabase;
    private AccountsBDD accountsBDD;
    private PersonsBDD personsBDD;
    private ExpensesBDD expensesBDD;
//    public static final String ACCOUNT = "";


    public AccountAdapter(Context mCtx, int listLayoutRes, ArrayList<Account> accountsList, SQLiteDatabase mDatabase, AccountsBDD accountsBDD) {
        super(mCtx, listLayoutRes, accountsList);

        this.mCtx = mCtx;
        this.listLayoutRes = listLayoutRes;
        this.accountsList = accountsList;
        this.mDatabase = mDatabase;
        this.accountsBDD = accountsBDD;
        this.personsBDD = new PersonsBDD(mCtx);
        this.expensesBDD = new ExpensesBDD(mCtx);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        LayoutInflater inflater = LayoutInflater.from(mCtx);
        View view = inflater.inflate(listLayoutRes, null);

        final Account account = accountsList.get(position);

        TextView tvTitre = view.findViewById(R.id.tvTitre);
        TextView tvDesc = view.findViewById(R.id.tvDesc);

        tvTitre.setText(account.getTitre());
        tvDesc.setText(account.getDescription());

        Button buttonDelete = view.findViewById(R.id.btnDelete);
        Button buttonEdit = view.findViewById(R.id.btnEdit);
        Button buttonDetail = view.findViewById(R.id.btnDetail);

        //adding a clicklistener to button
        buttonEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                updateAccount(account);
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
                        personsBDD.open();
                        expensesBDD.open();
                        if(expensesBDD.isExist()) {
                            expensesBDD.removeExpenseWithAID(account.getID());
                            Log.d("APP", "Les dépenses ont bien été supprimés !");
                        }
                        if(personsBDD.isExist()) {
                            personsBDD.removePersonWithAID(account.getID());
                            Log.d("APP", "Les participants ont bien été supprimés !");
                        }
                        accountsBDD.removeAccountWithID(account.getID());
                        reloadAccountsFromDatabase();
                        personsBDD.close();
                        expensesBDD.close();
                        Intent intent = new Intent(mCtx.getApplicationContext(), IndividualActivity.class);
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

        buttonDetail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                final EditText editTitre = v.findViewById(R.id.editTitre);
//                String account1 = editTitre.getText().toString().trim();
                Intent intent = new Intent(mCtx.getApplicationContext(), ExpenseActivity.class);
//                intent.putExtra(ACCOUNT, account1);
                mCtx.startActivity(intent);
                ExpenseActivity.a = account;
            }
        });

        return view;
    }

    private void updateAccount(final Account account) {
        final AlertDialog.Builder builder = new AlertDialog.Builder(mCtx);

        LayoutInflater inflater = LayoutInflater.from(mCtx);
        View view = inflater.inflate(R.layout.dialog_update_account, null);
        builder.setView(view);


        final EditText editTitre = view.findViewById(R.id.editTitre);
        final EditText editDesc = view.findViewById(R.id.editDesc);
        final EditText editDevise = view.findViewById(R.id.editDevise);

        editTitre.setText(account.getTitre());
        editDesc.setText(account.getDescription());
        editDevise.setText(account.getDevise());

        final AlertDialog dialog = builder.create();
        dialog.show();

        view.findViewById(R.id.btnUpdate).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String titre = editTitre.getText().toString().trim();
                String desc = editDesc.getText().toString().trim();
                String devise = editDevise.getText().toString().trim();

                if (titre.isEmpty()) {
                    editTitre.setError("Titre can't be blank");
                    editTitre.requestFocus();
                    return;
                }

                if (devise.isEmpty()) {
                    editDevise.setError("Devise can't be blank");
                    editDevise.requestFocus();
                    return;
                }

                account.setTitre(titre);
                account.setDescription(desc);
                account.setDevise(devise);
                accountsBDD.updateAccount(account.getID(), account);
                Toast.makeText(mCtx, "Account Updated", Toast.LENGTH_SHORT).show();
                reloadAccountsFromDatabase();

                dialog.dismiss();

                Intent intent = new Intent(mCtx.getApplicationContext(), IndividualActivity.class);
                mCtx.startActivity(intent);
            }
        });
    }

    private void reloadAccountsFromDatabase() {
        Cursor cursorAccounts = mDatabase.rawQuery("SELECT * FROM accounts", null);
        if (cursorAccounts.moveToFirst()) {
            accountsList.clear();
            do {
                accountsList.add(new Account(
                        cursorAccounts.getInt(0),
                        cursorAccounts.getString(1),
                        cursorAccounts.getString(2),
                        cursorAccounts.getString(3)
                ));
            } while (cursorAccounts.moveToNext());
        }
        cursorAccounts.close();
    }
}
