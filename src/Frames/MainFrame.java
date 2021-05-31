package Frames;

import javax.swing.*;

public class MainFrame extends MyFrame {
    private JButton btn_login;
    private JButton btn_exit;
    private JButton btn_register;
    private JPanel pnl_main;

    public MainFrame(){
        initFrame("Ana Sayfa", 800,600, pnl_main);
    }

    @Override
    protected void initMenuBar() {

    }

    @Override
    protected void initComponents(){
        btn_register.addActionListener(e -> {
            RegisterFrame frame = new RegisterFrame();
            frame.setVisible(true);
            dispose();
        });

        btn_login.addActionListener(e -> {
            LoginForm frame = new LoginForm();
            frame.setVisible(true);
            dispose();
        });

        btn_exit.addActionListener(e -> dispose());
    }

    @Override
    protected void initPopUp() {

    }

    public static void main(String[] args) {
        JFrame frame = new MainFrame();
        frame.setVisible(true);
    }
}
