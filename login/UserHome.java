package login;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.ResultSet;

import javax.swing.*;
import javax.swing.border.EmptyBorder;

/**
 * UI-Interface for the User-home; offers buttons that link to database-functionality
 *
 * @author Kara Kolodinsky
 * @author Ainsley Ross
 * @author Teagan Nester
 * @author Caitlyn Cyrek
 * @author Serene Wood
 */

public class UserHome extends JFrame {

    private static final long serialVersionUID = 1;

    /**
     * Currently logged in user's username
     */
    private static String netizenUsername;

    //private JPanel contentPane;
    private int strdButtonWidth = 150;
    private int strdButtonHeight = 40;
    private int strdFontSize = 10;
    private int borderSize = 20;

    // netizen primary key, and recipe foreign key (pointing to recipe creator)
    static private String user;


    /**
     * UserHome window width
     */
    public static int WIDTH_FRAME = 960;

    /**
     * UserHome window height
     */
    public static int HEIGHT_FRAME = 960;

    /**
     * frame edge
     */
    public static Insets INSETS;

    //user = userSes;

    private JMenuBar menuBar_menubar;
    private JPanel contentPane;

    public UserHome(){
        super("UserHome");
        setResizable(false);
        setLayout(null);
        setSize(WIDTH_FRAME, HEIGHT_FRAME);
        setLocationRelativeTo(null);
        setLocation(getX() - 80, getY() - 80);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setVisible(true);

        init();
    }

    private void init() {
        netizenUsername = UserLogin.getUsername();                  //current user's UN

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setBounds(0, 0, 1000, 600);
        setResizable(false);
        contentPane = new JPanel();
        contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
        setContentPane(contentPane);
        contentPane.setLayout(null);
        JButton btnNewButton = new JButton("Logout");
        btnNewButton.setForeground(new Color(0, 0, 0));
        btnNewButton.setBackground(UIManager.getColor("Button.disabledForeground"));

        btnNewButton.setFont(new Font("Tahoma", Font.PLAIN, strdFontSize));

        btnNewButton.setFont(new Font("Tahoma", Font.PLAIN, 15));

        btnNewButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                int a = JOptionPane.showConfirmDialog(btnNewButton, "Are you sure?");
                // JOptionPane.setRootFrame(null);
                if (a == JOptionPane.YES_OPTION) {
                    dispose();
                    UserLogin obj = new UserLogin();
                    obj.setTitle("Login");
                    obj.setVisible(true);
                } else {

                }
            }
        });

        //btnNewButton.setBounds(247, 118, 491, 114);
        btnNewButton.setBounds(20, 10, strdButtonWidth, strdButtonHeight);

        btnNewButton.setBounds(20, 20, 200, 30);

        contentPane.add(btnNewButton);
        JButton browseButton = new JButton("Browse Recipes");
        browseButton.setForeground(new Color(0, 0, 0));
        browseButton.setBackground(UIManager.getColor("Button.disabledForeground"));
        browseButton.setFont(new Font("Tahoma", Font.PLAIN, 15));
        browseButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                UserHome.this.dispose();
                new Browse(user);
                //browse.setTitle("Browse Recipes");
                //browse.setVisible(true);
            }
        });
        browseButton.setBounds(20, 120, 200, 30);
        contentPane.add(browseButton);

        JButton pantryButton = new JButton("My Pantry");
        pantryButton.setForeground(new Color(0, 0, 0));
        pantryButton.setBackground(UIManager.getColor("Button.disabledForeground"));
        pantryButton.setFont(new Font("Tahoma", Font.PLAIN, 15));
        pantryButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                    dispose();
                    Pantry pan = new Pantry();
                    pan.setTitle("My Pantry");
                        pan.setVisible(true);
                }
            
        });
        pantryButton.setBounds(20, 160, 200, 30);
        contentPane.add(pantryButton);
        // JButton button = new JButton("Change-password\r\n");
        // button.setBackground(UIManager.getColor("Button.disabledForeground"));
        // button.addActionListener(new ActionListener() {
        //   public void actionPerformed(ActionEvent e) {
        // ChangePassword bo = new ChangePassword(userSes);
        // bo.setTitle("Change Password");
        // bo.setVisible(true);


        //button.setFont(new Font("Tahoma", Font.PLAIN, strdFontSize));
        //button.setBounds(247, 320, 491, 114);
        //button.setBounds(20, 55, strdButtonWidth, strdButtonHeight);
        //contentPane.add(button);

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
                //System.out.print("Current max recipeID" + DataBase.getMaxRecipeId());                 DEBUG
                //JOptionPane.showMessageDialog(btnNewButton, "You have successfully logged in");
            }
        });
        contentPane.add(newRecipeButton);


        JButton editRecipeButton = new JButton("Edit Existing Recipe");
        editRecipeButton.setBackground(UIManager.getColor("Button.disabledForeground"));
        editRecipeButton.setFont(new Font("Tahoma", Font.PLAIN, strdFontSize));
        //editRecipeButton.setBounds(247, 320, 491, 114);
        editRecipeButton.setBounds((20 + strdButtonWidth + borderSize), 320, strdButtonWidth, strdButtonHeight);
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
        searchRecipeButton.setBounds((20 + (2 * (strdButtonWidth + borderSize))), 320, strdButtonWidth, strdButtonHeight);
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
        makeRecipeButton.setBounds((20 + (3 * (strdButtonWidth + borderSize))), 320, strdButtonWidth, strdButtonHeight);
        makeRecipeButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                //functionality todo on make-bake-cook branch
                dispose();
                Cook cook = new Cook();
                cook.setTitle("Choose Recipe to cook");
                cook.setVisible(true);
            }
        });
        contentPane.add(makeRecipeButton);
    }





         //   }
        // });
        // button.setFont(new Font("Tahoma", Font.PLAIN, 35));
        // button.setBounds(247, 320, 491, 114);
        // contentPane.add(button);
        





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

}
