package Database;

import Utils.Party;
import Utils.Person;

import javax.swing.*;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class PersonActions extends DatabaseAccess implements IDatabaseActions<Person>{
    @Override
    public void insert(Person data) throws SQLException {
        String query = "insert into person (firstname, lastname, email, password ,age, is_smoker) VALUES ('%s','%s','%s','%s',%d, %s)".formatted(data.getFirstname(), data.getLastname(), data.getEmail(), data.getPassword(), data.getAge(), data.isSmoker());
        updateDatabase(query);
    }

    @Override
    public void update(Person data) throws SQLException {
        String query = "update person set firstname='%s', lastname='%s', vote_status = %s, email='%s', is_admin = %s, voted_party = %s, password = '%s', is_leader = %s, age = %s, is_smoker = %s where id = %s".formatted(
                data.getFirstname(),
                data.getLastname(),
                data.isVoted(),
                data.getEmail(),
                data.isAdmin(),
                data.getVotedParty().getId(),
                data.getPassword(),
                data.isLeader(),
                data.getAge(),
                data.isSmoker(),
                data.getId()
                );
        updateDatabase(query);
    }

    public void updateLeaderStatus(Person person) throws SQLException {
        String query = "update person set is_leader="+person.isLeader()+" where id="+person.getId()+"";
        updateDatabase(query);
    }

    public void updateAdminStatus(Person person) throws SQLException {
        String query = "update person set is_admin="+person.isAdmin()+" where id="+person.getId()+"";
        updateDatabase(query);
    }


    @Override
    public List<Person> getList() throws SQLException {
        ArrayList<Person> personArrayList = new ArrayList<>();

        Connection c = getConnection();
        Statement s = c.createStatement();
        String query = "select * from person";
        ResultSet rs = s.executeQuery(query);
        while (rs.next()){
            personArrayList.add(getPerson(rs));
        }
        s.close();
        c.close();
        return personArrayList;
    }

    @Override
    public Person getById(int id) throws SQLException {
        Person person = null;
        Connection c = getConnection();
        Statement s = c.createStatement();
        String query = "select * from person where id="+id;
        ResultSet rs = s.executeQuery(query);
        while (rs.next()){
            person = getPerson(rs);
        }
        s.close();
        c.close();
        return person;
    }

    @Override
    public void delete(int id) throws SQLException {
        String query = "delete from person where id=" + id;
        updateDatabase(query);
    }


    public Person getByEmail(String email) throws SQLException {
        Person person = null;
        Connection c = getConnection();
        Statement s = c.createStatement();
        String query = "select * from person where email='"+email+"'";
        ResultSet rs = s.executeQuery(query);
        while (rs.next()){
            person = getPerson(rs);
        }
        s.close();
        c.close();
        return person;
    }

    public float getPercentage(Party party) throws SQLException {
        int voted = 0;
        int total = 0;
        Connection c = getConnection();
        Statement s = c.createStatement();
        String query = "select count(id) from person where voted_party=" + party.getId();
        ResultSet rs = s.executeQuery(query);
        while (rs.next()){
            voted = rs.getInt(1);
        }
        query = "select count(id) from person where vote_status = true";
        rs = s.executeQuery(query);
        while (rs.next()){
            total = rs.getInt(1);
        }
        return 100 * ((float)voted/total);
    }

    private Person getPerson(ResultSet rs){
        Person person = null;
            try {
                person = new Person(
                        rs.getInt("id"),
                        rs.getInt("age"),
                        rs.getString("firstname"),
                        rs.getString("lastname"),
                        rs.getString("email"),
                        rs.getString("password"),
                        rs.getBoolean("vote_status"),
                        rs.getBoolean("is_admin"),
                        rs.getBoolean("is_leader"),
                        rs.getBoolean("is_smoker"),
                        rs.getInt("voted_party")
                );
            } catch (SQLException throwables) {
                JOptionPane.showMessageDialog(null, throwables.getMessage(), "Hata", JOptionPane.ERROR_MESSAGE);
            }
            return person;
    }
}
