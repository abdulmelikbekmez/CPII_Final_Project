package Utils;

import Database.PersonActions;

import javax.swing.*;
import java.sql.SQLException;

public class Party {
    private int id;
    private String name;
    private String emblem_url;
    private int president;

    public Party(int id, String name, String emblem_url, int president) {
        this.id = id;
        this.name = name;
        this.emblem_url = emblem_url;
        this.president = president;
    }


    public Party(String name, String emblem_url, int president) {
        this.name = name;
        this.emblem_url = emblem_url;
        this.president = president;
    }

    @Override
    public String toString(){
        return name.toUpperCase();
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmblemUrl() {
        return emblem_url;
    }

    public void setEmblemUrl(String emblem_url) {
        this.emblem_url = emblem_url;
    }

    public Person getPresident() {
        try {
            return new PersonActions().getById(president);
        } catch (SQLException throwables) {
            JOptionPane.showMessageDialog(null, throwables.getMessage(), "Hata", JOptionPane.ERROR_MESSAGE);
            return null;
        }
    }

    public void setPresident(int president) {
        this.president = president;
    }

}
