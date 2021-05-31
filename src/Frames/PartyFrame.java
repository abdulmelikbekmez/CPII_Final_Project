package Frames;

import Database.PartyActions;
import Database.PersonActions;
import Utils.Party;
import Utils.Person;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.io.File;
import java.sql.SQLException;

public class PartyFrame extends MyFrame {
    private JPanel pnl_main;
    private JTabbedPane tpane_main;
    private JPanel pnl_patyManagement;
    private JPanel pnl_updateParty;
    private JTextField txt_name;
    private JButton btn_updateEmblem;
    private JPanel pnl_updateParty_name;
    private JLabel lbl_name;
    private JPanel pnl_updateParty_emblem;
    private JLabel lbl_emblemImage;
    private JButton btn_update;
    private JPanel pnl_updatePary_president;
    private JComboBox cmbbox_president;
    private JLabel lbl_president;
    private JLabel lbl_status;

    Party party;

    public PartyFrame(Party party){
        this.party = party;
        initFrame(party + " Durum Ekranı",800,600,pnl_main);
    }

    @Override
    protected void initMenuBar() {
        JMenuItem goToSelection = new JMenuItem("Seçim Menüsüne Git");
        goToSelection.addActionListener(e -> {
            new SelectionFrame(party.getPresident()).setVisible(true);
            dispose();
        });

        settingsMenu.add(goToSelection);
        addExitSession();

    }

    @Override
    protected void initComponents() {
        txt_name.setText(party.getName());
        String status = null;
        try {
            status = "%" + new PersonActions().getPercentage(party) + " oy oranınız var!!";
        } catch (SQLException throwables) {
            JOptionPane.showMessageDialog(getContentPane(),throwables.getMessage(),"Hata",JOptionPane.ERROR_MESSAGE);
        }
        lbl_status.setText(status);

        ImageIcon imageIcon = new ImageIcon(party.getEmblemUrl());
        Image image = imageIcon.getImage();
        Image resizedImage = image.getScaledInstance(100,100, Image.SCALE_SMOOTH);
        lbl_emblemImage.setIcon(new ImageIcon(resizedImage));

        cmbbox_president.addItem(party.getPresident());
        try {
            for (Person person: new PersonActions().getList()){
                if (!person.isLeader() && !person.isAdmin()){
                    cmbbox_president.addItem(person);
                }
            }
        } catch (SQLException throwables) {
            System.out.println(throwables.getMessage());
        }

        // Events
        btn_updateEmblem.addActionListener(e ->updateEmblemAction(e));

        btn_update.addActionListener(e -> updatePartyAction(e));
    }

    private void updatePartyAction(ActionEvent e) {
        Person newPresident = (Person) cmbbox_president.getSelectedItem();
        party.setName(txt_name.getText());
        party.setPresident(newPresident.getId());
        try {
            new PartyActions().update(party);
        } catch (SQLException throwables) {
            JOptionPane.showMessageDialog(getContentPane(), throwables.getMessage(), "Hata",JOptionPane.ERROR_MESSAGE);
        }
    }

    private void updateEmblemAction(ActionEvent e){
        int res;
        JFileChooser chooser = new JFileChooser(".");
        res = chooser.showOpenDialog(null);
        if (res == JFileChooser.APPROVE_OPTION){
            File imageFile = chooser.getSelectedFile();
            String path = imageFile.getName();
            if (path.endsWith(".png")){
                ImageIcon imageIcon = new ImageIcon(path);
                Image image = imageIcon.getImage();
                Image resizedImage = image.getScaledInstance(200,200,Image.SCALE_SMOOTH);
                lbl_emblemImage.setIcon(new ImageIcon(resizedImage));
                party.setEmblemUrl(path);
            }else{
                JOptionPane.showMessageDialog(getContentPane(), "Lütfen png uzantılı resim seçiniz","Hata", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    @Override
    protected void initPopUp() {

    }
}
