package upec.projetandroid20182019.entity;

import java.util.ArrayList;
import java.util.Date;

public class Expense {

    private int id;
    private String nom_depense;
    private Double amount; //montant
    private String date;
    private int paidBy; //par qui
    private ArrayList<Integer> forWho; //pour qui
    private int aid;

    public Expense(String nom_depense, Double amount, String date, int paidBy, ArrayList<Integer> forWho, int aid) {
        this.nom_depense = nom_depense;
        this.amount = amount;
        this.date = date;
        this.paidBy = paidBy;
        this.forWho = forWho;
        this.aid = aid;
    }

    public Expense(int id, String nom_depense, Double amount, String date, int paidBy, int aid){
        this.id = id;
        this.nom_depense = nom_depense;
        this.amount = amount;
        this.date = date;
        this.paidBy = paidBy;
        this.aid = aid;
    }

    public Expense() {
    }

    public String getNom_depense() {
        return nom_depense;
    }

    public void setNom_depense(String nom_depense) {
        this.nom_depense = nom_depense;
    }

    public Double getAmount() {
        return amount;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public int getPaidBy() {
        return paidBy;
    }

    public void setPaidBy(int paidBy) {
        this.paidBy = paidBy;
    }

    public ArrayList<Integer> getForWho() {
        return forWho;
    }

    public void setForWho(ArrayList<Integer> forWho) {
        this.forWho = forWho;
    }

    public void setID(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public int getAid() {
        return aid;
    }

    public void setAid(int aid) {
        this.aid = aid;
    }

    public int getID() {
        return id;
    }

    @Override
    public String toString() {
        String s = "Nom dépense : "+nom_depense+"\nMontant : "+amount+"\nDate : "+date.toString()+"\nPayé par : "+paidBy+"\nPour qui : ";
        for(int i : forWho){
            s += i;
        }
        return s;
    }

}
