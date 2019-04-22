package upec.projetandroid20182019.entity;


import java.util.ArrayList;

public class Account {

    private int id;
    private String titre;
    private String description;
    private String devise;
    private ArrayList<Person> participants;


    public Account(String titre, String description, String devise, ArrayList<Person> participants) {
        this.titre = titre;
        this.description = description;
        this.devise = devise;
        this.participants = participants;
    }

    public Account(int id, String titre, String description, String devise) {
        this.id = id;
        this.titre = titre;
        this.description = description;
        this.devise = devise;
    }

    public Account() {
        participants = new ArrayList<>();
    }

    public String getTitre() {
        return titre;
    }

    public void setTitre(String titre) {
        this.titre = titre;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getDevise() {
        return devise;
    }

    public void setDevise(String devise) {
        this.devise = devise;
    }

    public ArrayList<Person> getParticipants() {
        return participants;
    }

    public void setParticipants(ArrayList<Person> participants) {
        this.participants = participants;
    }

    @Override
    public String toString() {
        String s = "Account{" +
                "titre='" + titre + '\'' +
                ", description='" + description + '\'' +
                ", devise=" + devise +
                ", content_participants=" + participants;
        for(Person p : participants){
            s += ", "+p.getName();
        }
        s += '}';
        return s;
    }

    public void setID(int id) {
        this.id = id;
    }

    public int getID() {
        return id;
    }
}
