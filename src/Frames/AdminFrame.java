package Frames;

import Database.PartyActions;
import Database.PersonActions;
import Utils.Party;
import Utils.Person;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.sql.SQLException;
import java.util.List;

public class AdminFrame extends MyFrame {
    private JTabbedPane tpane_main;
    private JTable tbl_persons;
    private JPanel pnl_selectionControl;
    private JPanel pnl_addParty;
    private JPanel pnl_adminManagement;
    private JPanel pnl_main;
    private JTextField txt_partyName;
    private JComboBox cmbbox_presidentCandidate;
    private JButton btn_emblem;
    private JPanel pnl_addParty_name;
    private JPanel pnl_addParty_president;
    private JPanel pnl_addParty_emblem;
    private JLabel lbl_partyName;
    private JLabel lbl_president;
    private JLabel lbl_emblem;
    private JButton btn_addParty;
    private JPanel pnl_addParty_add;
    private JSplitPane spane_adminManagement;
    private JPanel pnl_adminManagement_list;
    private JPanel pnl_adminManagement_add;
    private JList lst_admins;
    private JButton btn_removeAdmin;
    private JComboBox cmbbox_adminCandidate;
    private JButton btn_addAdmin;
    private JButton btn_removePerson;
    private JScrollPane spane_table;

    // Data Variables
    private List<Person> personArrayList;
    private String imagePath;
    private DefaultListModel<Person> adminListModel;
    private DefaultTableModel personTableModel;
    private Person loggedInAdmin;


    public AdminFrame(Person loggedInAdmin){
        this.loggedInAdmin = loggedInAdmin;
        initFrame("Admin Paneli",800,600,pnl_main);
    }

    @Override
    protected void initMenuBar() {
        JMenuItem goToSelection = new JMenuItem("Seçim Menüsüne Git");
        goToSelection.addActionListener(e -> {
            new SelectionFrame(loggedInAdmin).setVisible(true);
            dispose();
        });
       settingsMenu.add(goToSelection);

       addExitSession();
    }

    @Override
    protected void initComponents() {

        // Variable and Datatypes initializes
        fetchPersonArrayList();
        adminListModel = new DefaultListModel<>();
        personTableModel = new DefaultTableModel();
        updateDataTypes();
        lst_admins.setModel(adminListModel);
        tbl_persons.setModel(personTableModel);

        // Event Handlers
        btn_emblem.addActionListener(e -> emblemButtonAction(e));

        btn_addParty.addActionListener(e -> addPartyAction(e));

        txt_partyName.addKeyListener(new KeyAdapter() {
            @Override
            public void keyReleased(KeyEvent e) {
                partyTextKeyEvent(e);
            }
        });

        cmbbox_presidentCandidate.addActionListener(e -> comboBoxActionEvent(e));

        btn_addAdmin.addActionListener(e -> addAdminAction(e));

        btn_removeAdmin.addActionListener(e -> removeAdminAction(e));

        btn_removePerson.addActionListener(e -> removePersonActionEvent(e));
    }

    @Override
    protected void initPopUp() {
        JPopupMenu popupMenu = new JPopupMenu();

        JMenuItem updateTableWithAdmin = new JMenuItem("Sadece Adminleri Göster");
        updateTableWithAdmin.addActionListener(e -> updatePersonTable(true));

        JMenuItem updateTableWithoutAdmin = new JMenuItem("Admin Dışındakileri Göster");
        updateTableWithoutAdmin.addActionListener(e -> updatePersonTable(false));

        JMenuItem updateTableDefault = new JMenuItem("Bütün Bireyleri Göster");
        updateTableDefault.addActionListener(e -> updatePersonTable());

        popupMenu.add(updateTableWithAdmin);
        popupMenu.add(updateTableWithoutAdmin);
        popupMenu.add(updateTableDefault);

        spane_table.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseReleased(MouseEvent e) {
                if (SwingUtilities.isRightMouseButton(e)){
                    popupMenu.show(spane_table, e.getX(), e.getY());
                }
            }
        });

        tbl_persons.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseReleased(MouseEvent e) {
                if (SwingUtilities.isRightMouseButton(e)){
                    popupMenu.show(tbl_persons, e.getX(), e.getY());
                }
            }
        });


    }

    // Events
    private void emblemButtonAction(ActionEvent e){
        int res;
        JFileChooser chooser = new JFileChooser(".");
        res = chooser.showOpenDialog(null);
        if(res == JFileChooser.APPROVE_OPTION){
            File imageFile = chooser.getSelectedFile();
            String path = imageFile.getName();
            if(path.endsWith(".png")){
                ImageIcon imageIcon = new ImageIcon(path);
                Image image = imageIcon.getImage();
                Image resizedImage = image.getScaledInstance(200,200,Image.SCALE_SMOOTH);
                lbl_emblem.setIcon(new ImageIcon(resizedImage));
                imagePath = path;
                System.out.println(isTextValid());
                if (isTextValid()){
                    btn_addParty.setEnabled(true);
                }
            }else{
                JOptionPane.showMessageDialog(getContentPane(),"Lütfen png uzantılı resim seçiniz...", "Hata!",JOptionPane.ERROR_MESSAGE);
                lbl_emblem.setIcon(null);
                imagePath = null;
                btn_addParty.setEnabled(false);
            }
        }

    }

    private void partyTextKeyEvent(KeyEvent e){
        btn_addParty.setEnabled(isTextValid() && imagePath != null && isComboBoxValid());
    }

    private void addPartyAction(ActionEvent e){
        Person person = (Person) cmbbox_presidentCandidate.getSelectedItem();
        Party party = new Party(txt_partyName.getText(), imagePath, ((Person) cmbbox_presidentCandidate.getSelectedItem()).getId());
        try {
            new PartyActions().insert(party);
            person.setIsLeader(true);
            new PersonActions().updateLeaderStatus(person);
            updateDataTypes();
        } catch (SQLException throwables) {
            JOptionPane.showMessageDialog(getContentPane(),throwables.getMessage(),"Hata!",JOptionPane.ERROR_MESSAGE);
        }
    }

    private void addAdminAction(ActionEvent e){
        Person person = (Person) cmbbox_adminCandidate.getSelectedItem();
        person.setIsAdmin(true);
        try {
            new PersonActions().updateAdminStatus(person);
            JOptionPane.showMessageDialog(getContentPane(), "Başarı ile admin yapıldı", "Başarılı", JOptionPane.INFORMATION_MESSAGE);
            updateDataTypes();
        } catch (SQLException throwables) {
            JOptionPane.showMessageDialog(getContentPane(),throwables.getMessage(),"Hata", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void removeAdminAction(ActionEvent e){
        Person person = (Person) lst_admins.getSelectedValue();
        if(person == null){
            JOptionPane.showMessageDialog(getContentPane(), "Lütfen önce birey seçiniz", "Hata", JOptionPane.ERROR_MESSAGE);
            return;
        }
        person.setIsAdmin(false);
        try {
            new PersonActions().updateAdminStatus(person);
            JOptionPane.showMessageDialog(getContentPane(), "Başarı ile adminlikten çıkarıldı", "Başarılı", JOptionPane.INFORMATION_MESSAGE);
            updateDataTypes();
        } catch (SQLException throwables) {
            JOptionPane.showMessageDialog(getContentPane(), throwables.getMessage(),"Hata", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void comboBoxActionEvent(ActionEvent e){
        btn_addParty.setEnabled(isTextValid() && imagePath != null && isComboBoxValid());
    }

    private void removePersonActionEvent(ActionEvent e){
        int row = tbl_persons.getSelectedRow();
        if(row == -1){
            JOptionPane.showMessageDialog(getContentPane(), "Lütfen önce birey seçiniz...", "Hata", JOptionPane.ERROR_MESSAGE);
            return;
        }
        Object id =  personTableModel.getValueAt(row,0);
        Person person = personArrayList.get(row);
        if(person.isLeader() || person.isAdmin()){
            //TODO : bu durumu hallet
            JOptionPane.showMessageDialog(getContentPane(), "Admin veya Lider!!", "Hata", JOptionPane.ERROR_MESSAGE);
            return;
        }
        try {
            new PersonActions().delete(Integer.parseInt((String) id));
            personArrayList.remove(row);
            updateDataTypes();
        } catch (SQLException throwables) {
            JOptionPane.showMessageDialog(getContentPane(), throwables.getMessage(), "Hata", JOptionPane.ERROR_MESSAGE);
        }
    }

    // Other Methods

    private void updateDataTypes(){
        updateAdminCandidateCombobox();
        updatePresidentCandidateCombobox();
        updateAdminList();
        updatePersonTable();
    }

    private void fetchPersonArrayList(){
        try {
            personArrayList = new PersonActions().getList();
        } catch (SQLException throwables) {
            System.out.println(throwables.getMessage());
        }
    }

    private void updatePresidentCandidateCombobox(){
        cmbbox_presidentCandidate.removeAllItems();
        for (Person person: personArrayList){
            if(!person.isLeader() && !person.isAdmin()){
                cmbbox_presidentCandidate.addItem(person);
            }
        }
    }

    private void updateAdminCandidateCombobox(){
        cmbbox_adminCandidate.removeAllItems();
        for (Person person: personArrayList){
            if(!person.isAdmin() && !person.isLeader()){
                cmbbox_adminCandidate.addItem(person);
            }
        }
    }

    private void updateAdminList(){
        adminListModel.removeAllElements();

        for (Person person: personArrayList){
            if(person.isAdmin()){
                adminListModel.addElement(person);
            }
        }
    }

    private void updatePersonTable(){
        int rowCount = personTableModel.getRowCount();
        for (int i = rowCount - 1; i >=0 ; i--) {
            personTableModel.removeRow(i);
        }
        personTableModel.setColumnIdentifiers(new String[]{"id","İsim", "Soyisim", "Seçim Durumu", "Email", "Admin Durumu","Seçimi", "Parti Lideri mi", "Yaş"});
        for (Person person: personArrayList){
            addItemToTable(person);
        }
    }

    private void updatePersonTable(boolean isAdmin){
        int rowCount = personTableModel.getRowCount();
        for (int i = rowCount - 1; i >=0 ; i--) {
            personTableModel.removeRow(i);
        }
        personTableModel.setColumnIdentifiers(new String[]{"id","İsim", "Soyisim", "Seçim Durumu", "Email", "Admin Durumu","Seçimi", "Parti Lideri mi", "Yaş"});
        for (Person person: personArrayList){
            if (person.isAdmin() == isAdmin){
                addItemToTable(person);
            }
        }
    }


    private void addItemToTable(Person person){
        personTableModel.addRow(
                new String[]{
                        String.valueOf(person.getId()),
                        person.getFirstname(),
                        person.getLastname(),
                        person.isVoted() ? "Yaptı" : "Yapmadı",
                        person.getEmail(),
                        person.isAdmin() ? "admin" : "admin değil",
                        person.getVotedParty() == null ? "-" : String.valueOf(person.getVotedParty()),
                        person.isLeader() ? "Evet" : "Hayır",
                        String.valueOf(person.getAge())
                });
    }

    private boolean isTextValid(){
        if(txt_partyName.getText().isEmpty() || txt_partyName.getText().length() <= 6){
            lbl_partyName.setForeground(Color.red);
            return false;
        }else{
            lbl_partyName.setForeground(Color.GREEN);
            return true;
        }
    }

    private boolean isComboBoxValid(){
        return cmbbox_presidentCandidate.getSelectedItem() instanceof Person;
    }
}
