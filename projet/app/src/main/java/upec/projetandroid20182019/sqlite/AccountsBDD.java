package upec.projetandroid20182019.sqlite;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import upec.projetandroid20182019.entity.Account;

public class AccountsBDD {

    private static final String TABLE_ACCOUNT = "accounts";
    private static final String COL_ID = "aid";
    private static final int NUM_COL_ID = 0;
    private static final String COL_TITRE = "titre";
    private static final int NUM_COL_TITRE = 1;
    private static final String COL_DESCRIPTION = "description";
    private static final int NUM_COL_DESCRIPTION = 2;
    private static final String COL_DEVISE = "devise";
    private static final int NUM_COL_DEVISE = 3;

    private SQLiteDatabase bdd;

    private SQLiteManager maBaseSQLite;

    public AccountsBDD(Context context){
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

    public long insertAccount(Account account){
        //Création d'un ContentValues (fonctionne comme une HashMap)
        ContentValues values = new ContentValues();
        //on lui ajoute une valeur associée à une clé (qui est le nom de la colonne dans laquelle on veut mettre la valeur)
        values.put(COL_TITRE, account.getTitre());
        values.put(COL_DESCRIPTION, account.getDescription());
        values.put(COL_DEVISE, account.getDevise());
        //on insère l'objet dans la BDD via le ContentValues
        return bdd.insert(TABLE_ACCOUNT, null, values);
    }

    public int updateAccount(int id, Account account){
        //La mise à jour d'un account dans la BDD fonctionne plus ou moins comme une insertion
        //il faut simplement préciser quel account on doit mettre à jour grâce à l'ID
        ContentValues values = new ContentValues();
        values.put(COL_TITRE, account.getTitre());
        values.put(COL_DESCRIPTION, account.getDescription());
        values.put(COL_DEVISE, account.getDevise());
        return bdd.update(TABLE_ACCOUNT, values, COL_ID + " = " +id, null);
    }

    public int removeAccountWithID(int id){
        //Suppression d'un account de la BDD grâce à l'ID
        return bdd.delete(TABLE_ACCOUNT, COL_ID + " = " +id, null);
    }

    public Account getAccountWithTitre(String titre){
        //Récupère dans un Cursor les valeurs correspondant à un account contenu dans la BDD (ici on sélectionne l'account grâce à son titre)
        Cursor c = bdd.query(TABLE_ACCOUNT, new String[] {COL_ID, COL_TITRE, COL_DESCRIPTION, COL_DEVISE}, COL_TITRE + " LIKE \"" + titre +"\"", null, null, null, null);
        return cursorToAccount(c);
    }

    //Cette méthode permet de convertir un cursor en un account
    private Account cursorToAccount(Cursor c){
        //si aucun élément n'a été retourné dans la requête, on renvoie null
        if (c.getCount() == 0)
            return null;

        //Sinon on se place sur le premier élément
        c.moveToFirst();
        //On créé un account
        Account account = new Account();
        //on lui affecte toutes les infos grâce aux infos contenues dans le Cursor
        account.setID(c.getInt(NUM_COL_ID));
        account.setTitre(c.getString(NUM_COL_TITRE));
        account.setDescription(c.getString(NUM_COL_DESCRIPTION));
        account.setDevise(c.getString(NUM_COL_DEVISE));
        //On ferme le cursor
        c.close();

        //On retourne l'account
        return account;
    }

    public Account getAccountWithID(int aid) {
        //Récupère dans un Cursor les valeurs correspondant à un account contenu dans la BDD (ici on sélectionne l'account grâce à son titre)
        Cursor c = bdd.query(TABLE_ACCOUNT, new String[] {COL_ID, COL_TITRE, COL_DESCRIPTION, COL_DEVISE}, COL_ID + " LIKE \"" + aid +"\"", null, null, null, null);
        return cursorToAccount(c);
    }
}
