package repobanhang;

import java.awt.Component;
import javax.swing.JOptionPane;

public class JOPane {
    public static void showMessageDialog(Component parent, String content){
        JOptionPane.showMessageDialog(parent, content, "Meo Meo Store", JOptionPane.INFORMATION_MESSAGE);
    }
     public static boolean showConfirmDialog(Component parent, String content){
        int choose = JOptionPane.showConfirmDialog(parent, content, "Meo Meo Store",
                     JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
        return choose == JOptionPane.YES_OPTION;
    }
     public static void showErrorDialog(Component parent, String content, String title){
        JOptionPane.showMessageDialog(parent, content, title, JOptionPane.ERROR_MESSAGE);
    }
}
