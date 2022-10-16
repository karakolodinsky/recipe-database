package login;

import java.awt.Color;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.UIManager;
import javax.swing.border.EmptyBorder;

public class UserHome extends JFrame {

    private static final long serialVersionUID = 1;
    private JPanel contentPane;

    private int strdButtonWidth = 150;
    private int strdButtonHeight = 40;
    private int strdFontSize = 10;
    private int borderSize = 20;

    // netizen primary key, and recipe foreign key (pointing to recipe creator)
    static private String user;


    /**
     * Launch the application.
     */
    public static void main(String[] args) {
        EventQueue.invokeLater(new Runnable() {
            public void run() {
                try {
                    UserHome frame = new UserHome();
                    frame.setVisible(true);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    public UserHome() {

    }

    /**
     * Create the frame.
     */
    public UserHome(String userSes) {

        user = userSes;

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setBounds(450, 190, 1014, 597);
        setResizable(false);
        contentPane = new JPanel();
        contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
        setContentPane(contentPane);
        contentPane.setLayout(null);
        JButton btnNewButton = new JButton("Logout");
        btnNewButton.setForeground(new Color(0, 0, 0));
        btnNewButton.setBackground(UIManager.getColor("Button.disabledForeground"));
        btnNewButton.setFont(new Font("Tahoma", Font.PLAIN, strdFontSize));
        btnNewButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                int a = JOptionPane.showConfirmDialog(btnNewButton, "Are you sure?");
                // JOptionPane.setRootFrame(null);
                if (a == JOptionPane.YES_OPTION) {
                    dispose();
                    UserLogin obj = new UserLogin();
                    obj.setTitle("Student-Login");
                    obj.setVisible(true);
                }
                else { 
                
                }
            }
        });
        //btnNewButton.setBounds(247, 118, 491, 114);
        btnNewButton.setBounds(20, 10, strdButtonWidth, strdButtonHeight);
        contentPane.add(btnNewButton);
        JButton button = new JButton("Change-password\r\n");
        button.setBackground(UIManager.getColor("Button.disabledForeground"));
        button.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                // ChangePassword bo = new ChangePassword(userSes);
                // bo.setTitle("Change Password");
                // bo.setVisible(true);
            }
        });
        button.setFont(new Font("Tahoma", Font.PLAIN, strdFontSize));
        //button.setBounds(247, 320, 491, 114);
        button.setBounds(20, 55, strdButtonWidth, strdButtonHeight);
        contentPane.add(button);

        JButton newRecipeButton = new JButton("Create new Recipe");
        newRecipeButton.setBackground(UIManager.getColor("Button.disabledForeground"));
        newRecipeButton.setFont(new Font("Tahoma", Font.PLAIN, strdFontSize));
        //newRecipeButton.setBounds(247, 320, 491, 114);
        newRecipeButton.setBounds(20, 320, strdButtonWidth, strdButtonHeight);
        newRecipeButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                //UserHome ah = new UserHome(userName);
                NewRecipe newR = new NewRecipe(user);
                newR.setTitle("Create a recipe:");
                newR.setVisible(true);
                //JOptionPane.showMessageDialog(btnNewButton, "You have successfully logged in");
            }
        });
        contentPane.add(newRecipeButton);


        JButton editRecipeButton = new JButton("Edit Existing Recipe");
        editRecipeButton.setBackground(UIManager.getColor("Button.disabledForeground"));
        editRecipeButton.setFont(new Font("Tahoma", Font.PLAIN, strdFontSize));
        //editRecipeButton.setBounds(247, 320, 491, 114);
        editRecipeButton.setBounds((20+strdButtonWidth+borderSize), 320, strdButtonWidth, strdButtonHeight);
        editRecipeButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                //functionality
            }
        });
        contentPane.add(editRecipeButton);

        JButton searchRecipeButton = new JButton("Search for Recipe");
        searchRecipeButton.setBackground(UIManager.getColor("Button.disabledForeground"));
        searchRecipeButton.setFont(new Font("Tahoma", Font.PLAIN, strdFontSize));
        //editRecipeButton.setBounds(247, 320, 491, 114);
        searchRecipeButton.setBounds((20+ (2 * (strdButtonWidth+borderSize))), 320, strdButtonWidth, strdButtonHeight);
        searchRecipeButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                //functionality
            }
        });
        contentPane.add(searchRecipeButton);

        JButton makeRecipeButton = new JButton("Bake/Cook Recipe");
        makeRecipeButton.setBackground(UIManager.getColor("Button.disabledForeground"));
        makeRecipeButton.setFont(new Font("Tahoma", Font.PLAIN, strdFontSize));
        //editRecipeButton.setBounds(247, 320, 491, 114);
        makeRecipeButton.setBounds((20+ (3 * (strdButtonWidth+borderSize))), 320, strdButtonWidth, strdButtonHeight);
        makeRecipeButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                //functionality
            }
        });
        contentPane.add(makeRecipeButton);

    }


}
