package upec.projetandroid20182019.entity;

import upec.projetandroid20182019.entity.Account;

public class Person {

    private Account account;
    private String name;
    private Double paid; //ce que la personne a payé
    private Double owed; //ce que la personne doit
    private int id;
    private int aid;

    public Person(Account account, String name) {
        this.account = account;
        this.aid = account.getID();
        this.name = name;
    }

    public Person(int id, String name, int aid) {
        this.id = id;
        this.aid = aid;
        this.name = name;
    }

    public Person() {
    }

    public Account getAccount() {
        return account;
    }

    public void setAccount(Account account) {
        this.account = account;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Double getPaid() {
        return paid;
    }

    public void setPaid(Double paid) {
        this.paid = paid;
    }

    public Double getOwed() {
        return owed;
    }

    public void setOwed(Double owed) {
        this.owed = owed;
    }

    public void setId(int anInt) {
        this.id = anInt;
    }

    public int getID() {
        return id;
    }

    public void setAid(int aid) {
        this.aid = aid;
    }

    public int getAid() {
        return aid;
    }

    @Override
    public String toString() {
        return "Name : "+name+"\nPaid : "+paid+"\nOwed : "+owed;
    }
}