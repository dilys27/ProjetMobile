package upec.projetandroid20182019;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.constraint.ConstraintLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.nio.channels.FileLock;
import java.util.ArrayList;
import java.util.Calendar;

public class AddExpense extends AppCompatActivity {

    public static final String AMOUNT = "";

    private DatePickerDialog.OnDateSetListener date_listener;
    private LinearLayout myLayout;
    private TextInputEditText date_activity;
    private TextInputEditText montant;
    private TextInputEditText titre_act;
    private TextInputEditText date_act;
    private TextInputEditText person_act;
    private Toolbar toolbar;
    private Button btn_participants;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_expense);

        toolbar = findViewById(R.id.toolbar);
        date_activity = findViewById(R.id.date_activity);
        montant = findViewById(R.id.montant);
        myLayout = findViewById(R.id.layout);
        titre_act = findViewById(R.id.titre);
        date_act = findViewById(R.id.date_activity);
        person_act = findViewById(R.id.person);
        btn_participants= findViewById(R.id.btn_participant);


        btn_participants.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String amount = montant.getText().toString().trim();
                final String titre = titre_act.getText().toString().trim();
                final String date = date_act.getText().toString().trim();
                final String person = person_act.getText().toString().trim();
                if (amount.isEmpty() || titre.isEmpty() || date.isEmpty() || person.isEmpty()) {
                    Toast.makeText(getApplicationContext(), "Aucun de ces champ ne peut être vide!", Toast.LENGTH_LONG).show();
            }else {
                    Intent intent = new Intent(getApplicationContext(), ParticipantsActivity.class);
                    intent.putExtra(AMOUNT, amount);
                    startActivity(intent);
                }
            }
        });


        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Nouvelle dépense");

        date_listener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                month = month + 1;
                String date = dayOfMonth + "/" + month + "/" + year;
                date_activity.setText(date);
            }
        };

        date_activity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Calendar c = Calendar.getInstance();
                int year = c.get(Calendar.YEAR);
                int month = c.get(Calendar.MONTH);
                int day = c.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog dialog = new DatePickerDialog(AddExpense.this, AlertDialog.THEME_HOLO_DARK, date_listener, year, month, day);
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                dialog.show();

            }
        });



    }

}

