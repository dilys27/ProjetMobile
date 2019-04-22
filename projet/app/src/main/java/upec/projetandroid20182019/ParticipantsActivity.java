package upec.projetandroid20182019;

import android.content.Intent;
import android.support.constraint.ConstraintLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.Layout;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

import upec.projetandroid20182019.entity.Expense;
import upec.projetandroid20182019.entity.Person;
import upec.projetandroid20182019.sqlite.ExpensesBDD;
import upec.projetandroid20182019.sqlite.PersonsBDD;

public class ParticipantsActivity extends AppCompatActivity {

    private ArrayList<String> participants_names = new ArrayList<>();
    private ArrayList<String> participants_amount = new ArrayList<>();
    private Intent intent;
    private String amount;
    private EditText editText;
    private TextView textView;
    private ScrollView scrollView;
    private LinearLayout myLayout;
    private Button button;
    private Toolbar toolbar;
    private RecyclerView rv;
    private ExpensesBDD expensesBDD;
    private PersonsBDD personsBDD;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.participants);
        button = findViewById(R.id.btn_participant);
        scrollView = findViewById(R.id.scrollV);
        myLayout = findViewById(R.id.layout);
        rv = findViewById(R.id.recycle_view);
        rv.setLayoutManager(new LinearLayoutManager(this));
        getSupportActionBar().setTitle("Nouvelle dépense - Participants");

        intent = getIntent();
        amount = intent.getStringExtra(AddExpense.AMOUNT);
        editText = findViewById(R.id.editText);
        editText.setText(amount + " €");

//        expensesBDD = new ExpensesBDD(getApplicationContext());
//        expensesBDD.open();
//        personsBDD = new PersonsBDD(getApplicationContext());
//        personsBDD.open();

//        for (Person p : personsBDD.getPersons()) {
//            participants_names.add(p.getName());
//        }

        participants_names.add("a");
        participants_names.add("b");
        participants_names.add("c");
        participants_names.add("d");
        participants_names.add("e");
        participants_names.add("f");

        Float f = Float.valueOf(amount);


        Float equal = f / participants_names.size();
        participants_amount.clear();
        for (int i = 0; i < participants_names.size(); i++) {
            participants_amount.add(Float.toString(equal));
            Toast.makeText(getApplicationContext(), Float.toString(f) + " €", Toast.LENGTH_LONG).show();
        }

        // A mettre les noms et les montants de chaque personne
        rv.setAdapter(new MyAdapt(participants_names, participants_amount));

        //Ajouter les dépenses dans la database
        FloatingActionButton fab = findViewById(R.id.fab_create);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Float f = Float.valueOf(0);
//                for (int i = 0; i < participants_names.size(); i++) {
//                    participants_amount.add(Float.toString(equal));
//                    Toast.makeText(getApplicationContext(), Float.toString(equal) + " €", Toast.LENGTH_LONG).show();
//
//                }
                for (int i = 0; i < participants_amount.size(); i++) {
//                    System.out.print(participants_amount.get(i));
                    f = f + Float.valueOf(participants_amount.get(i));
                    Toast.makeText(getApplicationContext(), Float.toString(f) + " €", Toast.LENGTH_LONG).show();
                }
                if (f > Float.valueOf(amount) || f < Float.valueOf(amount)) {
                    Snackbar.make(view, "Vérifier le montant de chaque personne", Snackbar.LENGTH_LONG)
                            .setAction("Action", null).show();
                } else {
                    Snackbar.make(view, "Nouvelle dépense ajoutée", Snackbar.LENGTH_LONG)
                            .setAction("Action", null).show();
                    Intent intent = new Intent(getApplicationContext(), ExpenseActivity.class);
                    startActivity(intent);
                }
            }
        });
    }

    public void clear(View v) {
        final EditText editText_mon = (EditText) v;
//        if(!editText_mon.equals(""))
//        editText_mon.getText().clear();
//        else {
//        final int i = participants_amount.indexOf("");
//        Toast.makeText(getApplicationContext(), Integer.toString(i), Toast.LENGTH_LONG).show();
        editText_mon.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH || actionId == EditorInfo.IME_ACTION_DONE ||
                        event != null && event.getAction() == KeyEvent.ACTION_DOWN && event.getKeyCode() == KeyEvent.KEYCODE_ENTER) {
                    if (event == null || !event.isShiftPressed()) {
                        // the user is done typing.
                        String s = String.valueOf(editText_mon.getText());
                        participants_amount.remove(participants_amount.size() - 1);
                        participants_amount.add(s);
                        //besoin de la bd

                        return true; // consume.
                    }
                }
                return false;
            }
        });
//        }
    }


    public void checkPerson(final View check) {
        final CheckBox checkBox = (CheckBox) check;
//        final EditText editText = findViewById(R.id.editText_mon);
        if (!checkBox.isChecked()) {
            // A effacer le montant d'une personne besoin de la bd
            participants_names.remove(checkBox.getText());

            Float f = Float.valueOf(amount);
            Float equal = f / participants_names.size();
            participants_amount.clear();
            for (int i = 0; i < participants_names.size(); i++) {
                participants_amount.add(Float.toString(equal));
                Toast.makeText(getApplicationContext(), Float.toString(equal) + " € " + checkBox.getText(), Toast.LENGTH_LONG).show();

            }

        } else {
            participants_names.add((String) checkBox.getText());

            Float f = Float.valueOf(amount);
            Float equal = f / participants_names.size();
            participants_amount.clear();
            for (int i = 0; i < participants_names.size(); i++) {
                participants_amount.add(Float.toString(equal));
                Toast.makeText(getApplicationContext(), Float.toString(equal) + " €", Toast.LENGTH_LONG).show();
            }
        }

    }

    private void refreshView() {
        scrollView = findViewById(R.id.scrollV);
        scrollView.setVisibility(View.GONE);
        scrollView.setVisibility(View.VISIBLE);
    }

}


class ViewH extends RecyclerView.ViewHolder {
    ConstraintLayout c;

    public ViewH(ConstraintLayout c) {
        super(c);
        this.c = c;
    }
}

class MyAdapt extends RecyclerView.Adapter<ViewH> {

    ArrayList<String> data1;
    ArrayList<String> data2;

    public MyAdapt(ArrayList<String> data1, ArrayList<String> data2) {
        this.data1 = data1;
        this.data2 = data2;
    }

    @Override
    public ViewH onCreateViewHolder(ViewGroup parent, int i) {
        ConstraintLayout cl = (ConstraintLayout) LayoutInflater.from(parent.getContext()).inflate(R.layout.content_participants, parent, false);
        ViewH vh = new ViewH(cl);
        return vh;
    }

    @Override
    public void onBindViewHolder(ViewH holder, int position) {
        CheckBox checkBox = holder.c.findViewById(R.id.checkBox);
        EditText textView = holder.c.findViewById(R.id.editText_mon);
        textView.setText(data2.get(position) + " €");
        checkBox.setText(data1.get(position));
    }

    @Override
    public int getItemCount() {
        return data1.size();
    }
}
