package Utils;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Validation {

    private static final Pattern emailPattern = Pattern.compile("^[\\w\\d._%+-]+@[\\w.-]+\\.[a-z]{2,6}$");
    // TODO : write text regex
    private final Pattern textPattern = Pattern.compile("");
    // TODO : write password validation
    private final Pattern passwordPattern = Pattern.compile("");

    public static boolean emailValidation(String email) {
        Matcher matcher = emailPattern.matcher(email);
        return matcher.find();
    }

    private boolean textValidation(String text) {
        Matcher matcher = textPattern.matcher(text);
        return matcher.find();
    }

    private boolean passwordValidation(String password) {
        Matcher matcher = passwordPattern.matcher(password);
        return matcher.find();
    }

    public static boolean formValidation(JPanel pnl_main, KeyEvent e) {
        boolean res = true;

        JTextField current = (JTextField) e.getSource();
        JPanel parent = (JPanel) current.getParent();
        JLabel label = null;

        for (Component component : parent.getComponents()) {
            if (component instanceof JLabel lbl) {
                label = lbl;
            }
        }

        for (Component component : pnl_main.getComponents()) {
            if (component instanceof JPanel pnl_sub) {
                for (Component c : pnl_sub.getComponents()) {
                    if (c instanceof JTextField txt) {
                        if (!isInputFieldValid(txt)) {
                            res = false;
                        }
                    }
                }
            }
        }

        if (label != null) {
            labelValidationAction(label, isInputFieldValid(current));
        }
        return res;
    }

    private static boolean isInputFieldValid(JTextField txt) {
        if (txt.getName().equals(InputField.EMAIL.name())) {
            return emailValidation(txt.getText());
        } else if (txt.getName().equals(InputField.TEXT.name()) || txt.getName().equals(InputField.PASSWORD.name())) {
            return !txt.getText().isEmpty();
        }
        return true;
    }

    public static void labelValidationAction(JLabel label, boolean condition) {
        if (condition) {
            label.setForeground(new Color(0, 114, 0));
        } else {
            label.setForeground(Color.red);
        }
    }
}
