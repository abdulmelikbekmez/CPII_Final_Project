package Frames;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public abstract class MyFrame extends JFrame implements ActionListener {

    static Color theme = Color.GRAY;
    private JMenuItem exitItem, defaultThemeItem, pinkThemeItem, orangeThemeItem, greenThemeItem, colorPickerItem;
    private ButtonGroup colorGroup;
    protected JMenu settingsMenu;

    public void initFrame(String title, int width, int height, JPanel pnl_main){
        setTitle(title);
        setSize(width, height);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setContentPane(pnl_main);
        getContentPane().setBackground(theme);
        defaultMenuBar();
        initMenuBar();
        initPopUp();
        initComponents();
    }

    protected void addExitSession() {
        JMenuItem exitSession = new JMenuItem("Hesaptan çıkış yap");
        exitSession.addActionListener(e -> {
            new MainFrame().setVisible(true);
            dispose();
        });

        settingsMenu.add(exitSession);
    }

    private void defaultMenuBar(){
        JMenuBar menuBar = new JMenuBar();

        settingsMenu = new JMenu("Ayarlar");
        JMenu themeMenu = new JMenu("Tema Seç");

        exitItem = new JMenuItem("Çıkış");
        exitItem.setAccelerator(KeyStroke.getKeyStroke((char) KeyEvent.VK_Q));

        defaultThemeItem = new JRadioButtonMenuItem("Normal");
        pinkThemeItem = new JRadioButtonMenuItem("Pembe");
        orangeThemeItem = new JRadioButtonMenuItem("Turuncu");
        greenThemeItem = new JRadioButtonMenuItem("Yeşil");

        colorPickerItem = new JMenuItem("Renk Seç");

        defaultThemeItem.addActionListener(this);
        pinkThemeItem.addActionListener(this);
        orangeThemeItem.addActionListener(this);
        greenThemeItem.addActionListener(this);
        colorPickerItem.addActionListener(this);

        exitItem.addActionListener(e -> dispose());

        colorGroup = new ButtonGroup();
        colorGroup.add(defaultThemeItem);
        colorGroup.add(pinkThemeItem);
        colorGroup.add(orangeThemeItem);
        colorGroup.add(greenThemeItem);

        themeMenu.add(defaultThemeItem);
        themeMenu.addSeparator();
        themeMenu.add(pinkThemeItem);
        themeMenu.add(orangeThemeItem);
        themeMenu.add(greenThemeItem);
        themeMenu.addSeparator();
        themeMenu.add(colorPickerItem);

        settingsMenu.add(exitItem);

        menuBar.add(settingsMenu);
        menuBar.add(themeMenu);

        setJMenuBar(menuBar);
    }

    protected abstract void initMenuBar();


    @Override
    public void actionPerformed(ActionEvent e) {
        Color color = Color.gray;
        if(e.getSource() == pinkThemeItem){
            color = Color.pink;
        }else if(e.getSource() == orangeThemeItem){
            color = Color.orange;
        }else if(e.getSource() == greenThemeItem){
            color = Color.green;
        }else if(e.getSource() == colorPickerItem){
            color = JColorChooser.showDialog(null,"Tema Seç",theme);
            colorGroup.clearSelection();
        }
        theme = color;
        getContentPane().setBackground(color);

    }

    protected abstract void initComponents();

    protected abstract void initPopUp();


}
