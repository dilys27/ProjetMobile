package upec.projetandroid20182019.sqlite;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;

import upec.projetandroid20182019.ExpenseActivity;
import upec.projetandroid20182019.entity.Expense;

public class ExpensesBDD {

    private static final String TABLE_EXPENSE = "expenses";
    private static final String COL_ID = "eid";
    private static final int NUM_COL_ID = 0;
    private static final String COL_NAME = "name";
    private static final int NUM_COL_NAME = 1;
    private static final String COL_AMOUNT = "amount";
    private static final int NUM_COL_AMOUNT = 2;
    private static final String COL_DATE = "date";
    private static final int NUM_COL_DATE = 3;
    private static final String COL_PAIDBY = "paidby";
    private static final int NUM_COL_PAIDBY = 4;
    private static final String COL_AID = "aid";
    private static final int NUM_COL_AID = 5;

    private SQLiteDatabase bdd;

    private SQLiteManager maBaseSQLite;

    public ExpensesBDD(Context context){
        //On crée la BDD et sa table
        maBaseSQLite = new SQLiteManager(context);
    }

    public void open(){
        //on ouvre la BDD en écriture
        bdd = maBaseSQLite.getWritableDatabase();
    }

    public void close(){
        //on ferme l'accès à la BDD
        bdd.close();
    }

    public SQLiteDatabase getBDD(){
        return bdd;
    }

    public long insertExpense(Expense expense){
        //Création d'un ContentValues (fonctionne comme une HashMap)
        ContentValues values = new ContentValues();
        //on lui ajoute une valeur associée à une clé (qui est le nom de la colonne dans laquelle on veut mettre la valeur)
        values.put(COL_NAME, expense.getNom_depense());
        values.put(COL_AMOUNT, expense.getAmount());
        values.put(COL_DATE, expense.getDate());
        values.put(COL_PAIDBY, expense.getPaidBy());
        values.put(COL_AID, expense.getAid());
        //on insère l'objet dans la BDD via le ContentValues
        return bdd.insert(TABLE_EXPENSE, null, values);
    }

    public int updateExpense(int id, Expense expense){
        //La mise à jour d'un expense dans la BDD fonctionne plus ou moins comme une insertion
        //il faut simplement préciser quel expense on doit mettre à jour grâce à l'ID
        ContentValues values = new ContentValues();
        values.put(COL_NAME, expense.getNom_depense());
        values.put(COL_AMOUNT, expense.getAmount());
        values.put(COL_DATE, expense.getDate());
        values.put(COL_PAIDBY, expense.getPaidBy());
        values.put(COL_AID, expense.getAid());
        return bdd.update(TABLE_EXPENSE, values, COL_ID + " = " +id, null);
    }

    public int removeExpenseWithID(int id){
        //Suppression d'un expense de la BDD grâce à l'ID
        return bdd.delete(TABLE_EXPENSE, COL_ID + " = " +id, null);
    }

    public Expense getExpenseWithName(String name){
        //Récupère dans un Cursor les valeurs correspondant à un expense contenu dans la BDD (ici on sélectionne l'expense grâce à son nom)
        Cursor c = bdd.query(TABLE_EXPENSE, new String[] {COL_ID, COL_NAME, COL_AMOUNT, COL_DATE, COL_PAIDBY, COL_AID}, COL_NAME + " LIKE \"" + name +"\"", null, null, null, null);
        return cursorToExpense(c);
    }

    public Expense getExpenseWithAid(int aid){
        //Récupère dans un Cursor les valeurs correspondant à un expense contenu dans la BDD (ici on sélectionne l'expense grâce à son aid)
        Cursor c = bdd.query(TABLE_EXPENSE, new String[] {COL_ID, COL_NAME, COL_AMOUNT, COL_DATE, COL_PAIDBY, COL_AID}, COL_AID+ " = \"" + aid +"\"", null, null, null, null);
        return cursorToExpense(c);
    }

    //Cette méthode permet de convertir un cursor en un expense
    private Expense cursorToExpense(Cursor c){
        //si aucun élément n'a été retourné dans la requête, on renvoie null
        if (c.getCount() == 0)
            return null;

        //Sinon on se place sur le premier élément
        c.moveToFirst();
        //On crée un expense
        Expense expense = new Expense();
        //on lui affecte toutes les infos grâce aux infos contenues dans le Cursor
        expense.setID(c.getInt(NUM_COL_ID));
        expense.setNom_depense(c.getString(NUM_COL_NAME));
        expense.setAmount(c.getDouble(NUM_COL_AMOUNT));
        expense.setDate(c.getString(NUM_COL_DATE));
        expense.setPaidBy(c.getInt(NUM_COL_PAIDBY));
        expense.setAid(c.getInt(NUM_COL_AID));
        //On ferme le cursor
        c.close();

        //On retourne l'expense
        return expense;
    }

    public int removeExpenseWithAID(int aid) {
        //Suppression d'un expense de la BDD grâce à l'AID
        return bdd.delete(TABLE_EXPENSE, COL_AID + " = " +aid, null);
    }

    public boolean isExist(){
        Cursor cursor = bdd.rawQuery("SELECT DISTINCT tbl_name FROM sqlite_master WHERE tbl_name = '"+TABLE_EXPENSE+"'", null);
        if(cursor!=null) {
            if(cursor.getCount()>0) {
                cursor.close();
                return true;
            }
            cursor.close();
        }
        return false;
    }

    public ArrayList<Expense> getExpenses() {
        ArrayList<Expense> expensesList = new ArrayList<>();
        //we used rawQuery(sql, selectionargs) for fetching all the accounts
        Cursor cursorExpenses = bdd.rawQuery("SELECT * FROM expenses WHERE aid = " + ExpenseActivity.a.getID() + ";", null);

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
        return expensesList;
    }
}
