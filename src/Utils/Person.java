package Utils;

import Database.PartyActions;

import javax.swing.*;
import java.sql.SQLException;

public class Person {
    private int id, age;
    private String firstname, lastname, email, password;
    private boolean vote_status;
    private boolean is_admin;
    private boolean is_leader;

    public boolean isSmoker() {
        return is_smoker;
    }

    public void setIsSmoker(boolean is_smoker) {
        this.is_smoker = is_smoker;
    }

    private boolean is_smoker;
    private int voted_party;

    public Person(int id, int age, String firstname, String lastname, String email, String password, boolean vote_status, boolean is_admin, boolean is_leader, boolean is_smoker, int voted_party) {
        this.id = id;
        this.age = age;
        this.firstname = firstname;
        this.lastname = lastname;
        this.email = email;
        this.password = password;
        this.vote_status = vote_status;
        this.is_admin = is_admin;
        this.is_leader = is_leader;
        this.is_smoker = is_smoker;
        this.voted_party = voted_party;
    }

    public Person(String firstname, String lastname, String email, String password, int age, boolean is_smoker){
        this.firstname = firstname;
        this.lastname = lastname;
        this.email = email;
        this.password = password;
        this.age = age;
        this.is_smoker = is_smoker;
        vote_status = false;
        is_admin = false;
        is_leader = false;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }

    public void setLastname(String lastname) {
        this.lastname = lastname;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setIsVoted(boolean vote_status) {
        this.vote_status = vote_status;
    }

    public void setIsAdmin(boolean is_admin) {
        this.is_admin = is_admin;
    }

    public void setIsLeader(boolean is_leader) {
        this.is_leader = is_leader;
    }

    public void setVotedParty(int voted_party) {
        this.voted_party = voted_party;
    }

    public String getFirstname() {
        return firstname;
    }

    public String getLastname() {
        return lastname;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }

    public int getAge() {
        return age;
    }

    public Party getVotedParty() {
        try {
            return new PartyActions().getById(voted_party);
        } catch (SQLException throwables) {
            JOptionPane.showMessageDialog(null, throwables.getMessage(), "Hata", JOptionPane.ERROR_MESSAGE);
            return null;
        }
    }

    public int getId() {
        return id;
    }

    public boolean isVoted() {
        return vote_status;
    }

    public boolean isAdmin() {
        return is_admin;
    }

    public boolean isLeader() {
        return is_leader;
    }

    public String toString(){
        return firstname + " " + lastname;
    }
}
