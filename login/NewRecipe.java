package login;

import java.awt.Color;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.*;
import javax.swing.border.EmptyBorder;

public class NewRecipe extends JFrame{

    private static final long serialVersionUID = 1;
    private JPanel contentPane;

    // standardized variables for buttons:
    private int strdButtonWidth = 150;
    private int strdButtonHeight = 40;
    private int strdFontSize = 10;
    private int borderSize = 20;

    // standardized variables for text-boxes:

    // Current user
    static private String currUser;

    /**
     * Launch the application.
     */
    public static void main(String[] args) {
        EventQueue.invokeLater(new Runnable() {
            public void run() {
                try {
                    NewRecipe frame = new NewRecipe();
                    frame.setVisible(true);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    public NewRecipe() {

    }

    public NewRecipe(String user) {
        currUser = user;
        // yoinked code:
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setBounds(450, 190, 1014, 597);
        setResizable(false);
        contentPane = new JPanel();
        contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
        setContentPane(contentPane);
        contentPane.setLayout(null);

        JTextField create = new JTextField("Create a new Recipe:");
        create.setBounds(50, 20, 300, 50);
        contentPane.add(create);

    }

}
