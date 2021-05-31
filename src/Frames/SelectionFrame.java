package Frames;

import Database.PartyActions;
import Database.PersonActions;
import Utils.InputField;
import Utils.Party;
import Utils.Person;
import Utils.Validation;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.sql.SQLException;
import java.util.List;

public class SelectionFrame extends MyFrame {
    private JPanel pnl_main;
    private JTabbedPane tabbedPane1;
    private JPanel pnl_updateProfile;
    private JPanel pnl_makeSelection;
    private JTextField txt_firtsname;
    private JTextField txt_lastname;
    private JTextField txt_email;
    private JPasswordField txtp_password;
    private JSpinner spn_age;
    private JCheckBox chbox_isSmoker;
    private JLabel lbl_firstname;
    private JPanel pnl_updateProfile_name;
    private JLabel lbl_latname;
    private JPanel pnl_updateProfile_lastname;
    private JPanel pnl_updateProfile_email;
    private JLabel lbl_email;
    private JPanel pnl_updateProfile_password;
    private JLabel lbl_password;
    private JPanel pnl_updateProfile_age;
    private JLabel lbl_age;
    private JPanel pnl_updateProfile_smoker;
    private JButton btn_update;
    private JPanel pnl_updateProfile_update;

    private Person loggedInPerson;
    private List<Person> personList;
    private List<Party> partyList;
    private ButtonGroup group;
    private JRadioButton selectedRadioButton;

    public SelectionFrame(Person loggedInPerson){
        fetchPersonList();
        fetchPartyList();
        this.loggedInPerson = loggedInPerson;
        initFrame("Ana Sayfa",1024, 768,pnl_main);
    }

    @Override
    protected void initMenuBar() {
        if (loggedInPerson.isAdmin()){
            JMenuItem goToAdminMenu = new JMenuItem("Admin Paneline Git");
            goToAdminMenu.addActionListener(e -> {
                new AdminFrame(loggedInPerson).setVisible(true);
                dispose();
            });
            settingsMenu.add(goToAdminMenu);
        }else if(loggedInPerson.isLeader()){
            JMenuItem goToPartyMenu = new JMenuItem("Parti Paneline Git");
            goToPartyMenu.addActionListener(e -> {
                try {
                    new PartyFrame(new PartyActions().getByPresidentId(loggedInPerson.getId())).setVisible(true);
                    dispose();
                } catch (SQLException throwables) {
                    JOptionPane.showMessageDialog(getContentPane(), throwables.getMessage(), "Hata", JOptionPane.ERROR_MESSAGE);
                }
            });
            settingsMenu.add(goToPartyMenu);
        }

        addExitSession();
    }

    @Override
    protected void initComponents() {
        // Set name to Input Fields
        txt_email.setName(InputField.EMAIL.name());
        txt_firtsname.setName(InputField.TEXT.name());
        txt_lastname.setName(InputField.TEXT.name());
        txtp_password.setName(InputField.PASSWORD.name());

        initSelectionScreen();
        initUpdateScreen();
        for (Component component: pnl_updateProfile.getComponents()){
            if (component instanceof JPanel pnl_sub){
                for (Component c: pnl_sub.getComponents()){
                    if (c instanceof JTextField txt){
                        txt.addKeyListener(new KeyAdapter() {
                            @Override
                            public void keyReleased(KeyEvent e) {
                                textFieldValidation(e);
                            }
                        });
                    }
                }
            }
        }

        btn_update.addActionListener(e -> updateButtonAction(e));
    }

    private void textFieldValidation(KeyEvent e){
        btn_update.setEnabled(Validation.formValidation(pnl_updateProfile,e));
    }


    private void updateButtonAction(ActionEvent e ){
        loggedInPerson.setFirstname(txt_firtsname.getText());
        loggedInPerson.setLastname(txt_lastname.getText());
        loggedInPerson.setEmail(txt_email.getText());
        loggedInPerson.setPassword(new String(txtp_password.getPassword()));
        loggedInPerson.setAge((Integer) spn_age.getValue());
        loggedInPerson.setIsSmoker(chbox_isSmoker.isSelected());

        try {
            new PersonActions().update(loggedInPerson);
            JOptionPane.showMessageDialog(getContentPane(), "Güncelleme Başarılı!!", "Başarılı",JOptionPane.INFORMATION_MESSAGE);
        } catch (SQLException throwables) {
            JOptionPane.showMessageDialog(getContentPane(), throwables.getMessage(),"Hata",JOptionPane.ERROR_MESSAGE);
        }
    }

    @Override
    protected void initPopUp() {

    }

    private void initSelectionScreen(){
        if (loggedInPerson.isVoted()){
            afterSelectionScreen();
            return;
        }

        group = new ButtonGroup();
        JButton btn = new JButton("Seçim Yap");
        btn.setEnabled(false);
        JPanel wrapperPanel = new JPanel();
        for(Party party: partyList){
            JPanel panel = new JPanel();
            panel.setLayout(new BoxLayout(panel,BoxLayout.Y_AXIS));
            JLabel presidentName = new JLabel(String.valueOf(party.getPresident()));
            JRadioButton radioButton = new JRadioButton(party.getName());
            radioButton.setName(party.getName());
            radioButton.addActionListener(e -> {
                selectedRadioButton = (JRadioButton) e.getSource();
                btn.setEnabled(true);
            } );
            group.add(radioButton);
            ImageIcon imageIcon = new ImageIcon(party.getEmblemUrl());
            Image image = imageIcon.getImage();
            Image resizedImage = image.getScaledInstance(100,100,Image.SCALE_SMOOTH);
            JLabel label = new JLabel(new ImageIcon(resizedImage));

            panel.add(presidentName);
            panel.add(label);
            panel.add(radioButton);

            wrapperPanel.add(panel);
        }
        pnl_makeSelection.add(wrapperPanel, BorderLayout.CENTER);
        group.clearSelection();
        btn.addActionListener(e -> makeSelectionAction(e));
        pnl_makeSelection.add(btn, BorderLayout.SOUTH);
    }

    private void initUpdateScreen(){
        txt_firtsname.setText(loggedInPerson.getFirstname());
        txt_lastname.setText(loggedInPerson.getLastname());
        txt_email.setText(loggedInPerson.getEmail());
        chbox_isSmoker.setSelected(loggedInPerson.isSmoker());
        spn_age.setValue(loggedInPerson.getAge());
        txtp_password.setText(loggedInPerson.getPassword());
    }

    private void makeSelectionAction(ActionEvent e) {
        Party selectedParty = null;
        for (Party party: partyList){
            if (party.getName().equals(selectedRadioButton.getName())){
               selectedParty = party;
            }
        }
        if(selectedParty != null){
            loggedInPerson.setVotedParty(selectedParty.getId());
            loggedInPerson.setIsVoted(true);
            try {
                new PersonActions().update(loggedInPerson);
                JOptionPane.showMessageDialog(getContentPane(), "Başarı ile seçim yapılmıştır", "Başarılı",JOptionPane.INFORMATION_MESSAGE);
                pnl_makeSelection.removeAll();
                fetchPersonList();
                afterSelectionScreen();
            } catch (SQLException throwables) {
                JOptionPane.showMessageDialog(getContentPane(), throwables.getMessage(), "Hata", JOptionPane.ERROR_MESSAGE);
            }
        }else{
            System.out.println("beklenmedik bir hata olustu!!!");
        }

    }

    private void afterSelectionScreen(){
        JPanel wrapperPanel = new JPanel();
        for (Party party: partyList){
            JPanel panel = new JPanel();
            panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));

            JLabel presidentName = new JLabel(String.valueOf(party.getPresident()));

            ImageIcon imageIcon = new ImageIcon(party.getEmblemUrl());
            Image image = imageIcon.getImage();
            Image resizedImage =image.getScaledInstance(100,100, Image.SCALE_SMOOTH);
            JLabel imageLabel = new JLabel(new ImageIcon(resizedImage));

            JLabel persentage = new JLabel("%"+ getPersentage(party));

            JLabel partyName =new JLabel(party.getName());

            panel.add(presidentName);
            panel.add(imageLabel);
            panel.add(partyName);
            panel.add(persentage);

            wrapperPanel.add(panel);
        }
        pnl_makeSelection.add(wrapperPanel, BorderLayout.CENTER);
    }

    private void fetchPersonList() {
        try {
            personList = new PersonActions().getList();
        } catch (SQLException throwables) {
            JOptionPane.showMessageDialog(getContentPane(), throwables.getMessage(), "Hata", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void fetchPartyList(){
        try {
            partyList = new PartyActions().getList();
        } catch (SQLException throwables) {
            JOptionPane.showMessageDialog(getContentPane(), throwables.getMessage(), "Hata", JOptionPane.ERROR_MESSAGE);
        }
    }

    private float getPersentage(Party party){
        float persentage = 0;
        try {
            persentage = new PersonActions().getPercentage(party);
        } catch (SQLException throwables) {
            JOptionPane.showMessageDialog(getContentPane(), throwables.getMessage(), "Hata", JOptionPane.ERROR_MESSAGE);
        }
        return persentage;
    }

}
