package upec.projetandroid20182019.sqlite;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;

import upec.projetandroid20182019.ExpenseActivity;
import upec.projetandroid20182019.entity.Expense;
import upec.projetandroid20182019.entity.Person;

public class PersonsBDD {

    private static final String TABLE_PERSONS= "persons";
    private static final String COL_ID = "pid";
    private static final int NUM_COL_ID = 0;
    private static final String COL_NAME = "name";
    private static final int NUM_COL_NAME = 1;
    private static final String COL_AID = "aid";
    private static final int NUM_COL_AID = 2;

    private SQLiteDatabase bdd;

    private SQLiteManager maBaseSQLite;

    public PersonsBDD(Context context){
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

    public long insertPerson(Person person){
        //Création d'un ContentValues (fonctionne comme une HashMap)
        ContentValues values = new ContentValues();
        //on lui ajoute une valeur associée à une clé (qui est le nom de la colonne dans laquelle on veut mettre la valeur)
        values.put(COL_NAME, person.getName());
        values.put(COL_AID, person.getAccount().getID());
        //on insère l'objet dans la BDD via le ContentValues
        return bdd.insert(TABLE_PERSONS, null, values);
    }

    public int updatePerson(int id, Person person){
        //La mise à jour d'un person dans la BDD fonctionne plus ou moins comme une insertion
        //il faut simplement préciser quel person on doit mettre à jour grâce à l'ID
        ContentValues values = new ContentValues();
        values.put(COL_NAME, person.getName());
        values.put(COL_AID, person.getAccount().getID());
        return bdd.update(TABLE_PERSONS, values, COL_ID + " = " +id, null);
    }

    public int removePersonWithID(int id){
        //Suppression d'un person de la BDD grâce à l'ID
        return bdd.delete(TABLE_PERSONS, COL_ID + " = " +id, null);
    }

    public Person getPersonWithName(String name){
        //Récupère dans un Cursor les valeurs correspondant à un person contenu dans la BDD (ici on sélectionne l'account grâce à son nom)
        Cursor c = bdd.query(TABLE_PERSONS, new String[] {COL_ID, COL_NAME}, COL_NAME + " LIKE \"" + name +"\"", null, null, null, null);
        return cursorToAccount(c);
    }

    public Person getPersonWithAID(int aid){
        //Récupère dans un Cursor les valeurs correspondant à un person contenu dans la BDD (ici on sélectionne l'account grâce à son aid)
        Cursor c = bdd.query(TABLE_PERSONS, new String[] {COL_ID, COL_NAME, COL_AID}, COL_AID + " = \"" + aid +"\"", null, null, null, null);
        return cursorToAccount(c);
    }

    //Cette méthode permet de convertir un cursor en un person
    private Person cursorToAccount(Cursor c){
        //si aucun élément n'a été retourné dans la requête, on renvoie null
        if (c.getCount() == 0)
            return null;

        //Sinon on se place sur le premier élément
        c.moveToFirst();
        //On crée un person
        Person person = new Person();
        //on lui affecte toutes les infos grâce aux infos contenues dans le Cursor
        person.setId(c.getInt(NUM_COL_ID));
        person.setName(c.getString(NUM_COL_NAME));
        person.setAid(c.getInt(NUM_COL_AID));
        //On ferme le cursor
        c.close();

        //On retourne l'account
        return person;
    }

    public int removePersonWithAID(int aid) {
        //Suppression d'un person de la BDD grâce à l'AID
        return bdd.delete(TABLE_PERSONS, COL_AID + " = " +aid, null);
    }

    public boolean isExist(){
        Cursor cursor = bdd.rawQuery("SELECT DISTINCT tbl_name FROM sqlite_master WHERE tbl_name = '"+TABLE_PERSONS+"'", null);
        if(cursor!=null) {
            if(cursor.getCount()>0) {
                cursor.close();
                return true;
            }
            cursor.close();
        }
        return false;
    }

    public Person getPersonWithID(int paidBy) {
        //Récupère dans un Cursor les valeurs correspondant à un person contenu dans la BDD (ici on sélectionne l'account grâce à son id)
        Cursor c = bdd.query(TABLE_PERSONS, new String[] {COL_ID, COL_NAME, COL_AID}, COL_ID + " = \"" + paidBy +"\"", null, null, null, null);
        return cursorToAccount(c);
    }

    public ArrayList<Person> getPersons() {
        ArrayList<Person> personsList = new ArrayList<>();
        //we used rawQuery(sql, selectionargs) for fetching all the accounts
        Cursor cursorPersons = bdd.rawQuery("SELECT * FROM persons WHERE aid = " + ExpenseActivity.a.getID() + ";", null);

        //if the cursor has some data
        if (cursorPersons.moveToFirst()) {
            //looping through all the records
            do {
                //pushing each record in the expenses list
                personsList.add(new Person(
                        cursorPersons.getInt(0),
                        cursorPersons.getString(1),
                        cursorPersons.getInt(2)
                ));
            } while (cursorPersons.moveToNext());
        }
        //closing the cursor
        cursorPersons.close();
        return personsList;
    }
}
