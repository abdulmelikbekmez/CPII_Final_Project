package Database;

import Utils.Party;

import javax.swing.*;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class PartyActions extends DatabaseAccess implements IDatabaseActions<Party>{

    @Override
    public void insert(Party data) throws SQLException {
        String query = "insert into party (name, president, emblem_url) VALUES ('"+data.getName()+"',"+data.getPresident().getId()+",'"+data.getEmblemUrl()+"')";
        updateDatabase(query);
    }

    @Override
    public void update(Party data) throws SQLException {
        String query = "update party set name='%s', president = %s , emblem_url='%s' where id = %s".formatted(
                data.getName(),
                data.getPresident().getId(),
                data.getEmblemUrl(),
                data.getId()
        );
        updateDatabase(query);
    }

    @Override
    public List<Party> getList() throws SQLException {
        ArrayList<Party> partyArrayList = new ArrayList<>();

        Connection c = getConnection();
        Statement s = c.createStatement();
        String query = "select * from party";
        ResultSet rs = s.executeQuery(query);
        while (rs.next()){
            partyArrayList.add(getParty(rs));
        }
        s.close();
        c.close();

        return partyArrayList;
    }

    @Override
    public Party getById(int id) throws SQLException {
        Party party = null;
        Connection c = getConnection();
        Statement s = c.createStatement();
        String query = "select * from party where id="+id;
        ResultSet rs = s.executeQuery(query);
        while (rs.next()){
            party = getParty(rs);
        }
        s.close();
        c.close();
        return party;
    }

    public Party getByPresidentId(int id)throws SQLException{
        Party party = null;
        Connection c = getConnection();
        Statement s = c.createStatement();
        String query = "select * from party where president=" + id;
        ResultSet rs = s.executeQuery(query);
        while (rs.next()){
            party = getParty(rs);
        }
        s.close();
        c.close();
        return party;
    }

    @Override
    public void delete(int id) throws SQLException {
        String query = "delete from person where id=" + id;
        updateDatabase(query);
    }

    private Party getParty(ResultSet rs){
        Party party = null;
        try {
            party = new Party(
                    rs.getInt("id"),
                    rs.getString("name"),
                    rs.getString("emblem_url"),
                    rs.getInt("president")
            );
        } catch (SQLException throwables) {
            JOptionPane.showMessageDialog(null, throwables.getMessage(), "Hata", JOptionPane.ERROR_MESSAGE);
        }
        return party;
    }
}
