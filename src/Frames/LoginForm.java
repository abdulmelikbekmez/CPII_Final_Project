package Frames;

import Database.PartyActions;
import Database.PersonActions;
import Utils.Party;
import Utils.Person;
import Utils.Validation;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.sql.SQLException;

public class LoginForm extends MyFrame {
    private JPanel pnl_main;
    private JPanel pnl_name;
    private JPanel pnl_password;
    private JTextField txt_email;
    private JLabel lbl_email;
    private JLabel lbl_password;
    private JPasswordField txtp_password;
    private JButton btn_goBack;
    private JPanel pnl_action;
    private JButton btn_login;

    public LoginForm(){
        initFrame("Giriş",400,400,pnl_main);
    }

    @Override
    protected void initMenuBar() {

    }

    @Override
    protected void initComponents(){
        txt_email.addKeyListener(new KeyAdapter() {
            @Override
            public void keyReleased(KeyEvent e) {
                emailKeyEvent(e);
            }
        });

        btn_login.addActionListener(this::loginButtonAction);
    }

    @Override
    protected void initPopUp() {

    }

    private void emailKeyEvent(KeyEvent e){
        String email = txt_email.getText();
        if(!Validation.emailValidation(email)) {
            lbl_email.setForeground(Color.red);
            btn_login.setEnabled(false);
        }else{
            lbl_email.setForeground(Color.green);
            btn_login.setEnabled(true);
        }
    }

    private void loginButtonAction(ActionEvent e){
        String email, password ;

        email = txt_email.getText();
        password = new String(txtp_password.getPassword());
        try {
           Person person = new PersonActions().getByEmail(email);
           if(person == null){
               JOptionPane.showMessageDialog(getContentPane(), "Email Yanlış!!","Hata!", JOptionPane.ERROR_MESSAGE);
               txt_email.setText("");
               txtp_password.setText("");
           }else if(!person.getPassword().equals(password)){
              JOptionPane.showMessageDialog(getContentPane(), "Şifre Yanlış!!","Hata!", JOptionPane.ERROR_MESSAGE);
              txtp_password.setText("");
           }else{
               if (person.isAdmin()){
                   new AdminFrame(person).setVisible(true);
               }else if (person.isLeader()){
                   Party party = new PartyActions().getByPresidentId(person.getId());
                   new PartyFrame(party).setVisible(true);
               }else{
                   new SelectionFrame(person).setVisible(true);
               }
               dispose();
           }
        } catch (SQLException throwables) {
            JOptionPane.showMessageDialog(getContentPane(),throwables.getMessage(),"hata",JOptionPane.ERROR_MESSAGE);
        }

    }
}
