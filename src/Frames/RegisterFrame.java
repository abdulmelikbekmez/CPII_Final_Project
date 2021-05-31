package Frames;

import Database.PersonActions;
import Utils.InputField;
import Utils.Person;
import Utils.Validation;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.sql.SQLException;

public class RegisterFrame extends MyFrame {
    private JPanel pnl_main;
    private JPanel pnl_name;
    private JPanel pnl_lastname;
    private JPanel pnl_email;
    private JPanel pnl_password;
    private JPanel pnl_age;
    private JPanel pnl_button;
    private JTextField txt_lastname;
    private JTextField txt_name;
    private JTextField txt_email;
    private JPasswordField txtp_password;
    private JSpinner spn_age;
    private JLabel lbl_name;
    private JLabel lbl_lastname;
    private JLabel lbl_email;
    private JLabel lbl_password;
    private JButton btn_addPerson;
    private JLabel lbl_age;
    private JButton btn_back;
    private JCheckBox chbox_isCigaretteUser;

    public RegisterFrame(){
        initFrame("Kaydol",400,400,pnl_main);
    }

    @Override
    protected void initMenuBar() {

    }

    @Override
    protected void initComponents(){
        // Set Name To Input Fields
        System.out.println(InputField.EMAIL.name());
        txt_email.setName(InputField.EMAIL.name());
        txtp_password.setName(InputField.PASSWORD.name());
        txt_name.setName(InputField.TEXT.name());
        txt_lastname.setName(InputField.TEXT.name());

        // Add EventListener to all Input Fields
        for(Component component : pnl_main.getComponents()){
           if(component instanceof JPanel pnl) {
              for (Component c :  pnl.getComponents()){
                  if(c instanceof JTextField txt){
                      txt.addKeyListener(new KeyAdapter() {
                          @Override
                          public void keyReleased(KeyEvent e) {
                              textFieldValidationRegister(e);
                          }
                      });
                  }
              }
           }
        }

        btn_addPerson.addActionListener(e -> btnActionEvent(e));

        btn_back.addActionListener(e -> goBack());
    }

    @Override
    protected void initPopUp() {
    }

    private void textFieldValidationRegister(KeyEvent e){
        btn_addPerson.setEnabled(Validation.formValidation(pnl_main,e));
    }

    private void btnActionEvent(ActionEvent a){
        String name,lastname,email,password;
        int age;
        boolean isSmoker;
        name = txt_name.getText();
        lastname = txt_lastname.getText();
        email = txt_email.getText();
        password = new String(txtp_password.getPassword());
        age = (int) spn_age.getValue();
        isSmoker = chbox_isCigaretteUser.isSelected();
        try {
            new PersonActions().insert(new Person(name, lastname, email, password, age, isSmoker));
            goBack();
        } catch (SQLException throwables) {
            JOptionPane.showMessageDialog(getContentPane(),throwables.getMessage(), "Beklenmedik Bir Hata Oluştu",JOptionPane.ERROR_MESSAGE);
        }
    }

    private void goBack() {
        new MainFrame().setVisible(true);
        dispose();
    }
}
