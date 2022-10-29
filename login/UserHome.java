package login;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.imageio.ImageIO;
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

    /** private JPanel contentPane; */

    private static final int BUTTON_WIDTH = 150;
    private static final int BUTTON_HEIGHT = 50;
    private static final int FONT_SIZE = 10;
    private static final int BORDER = 20;

    private static final int LOGOUT_BUTTON_WIDTH = 80;
    private static final int LOGOUT_BUTTON_HEIGHT = 30;

    /** */
    private static final int LOWER_BUTTON = 125;


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

        btnNewButton.setFont(new Font("Tahoma", Font.PLAIN, FONT_SIZE));

        btnNewButton.setFont(new Font("Tahoma", Font.PLAIN, FONT_SIZE));

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

        //btnNewButton.setBounds(20, 20, 200, 30);

        contentPane.add(btnNewButton);
        JButton browseButton = new JButton("Browse Recipes");
        browseButton.setForeground(new Color(0, 0, 0));
        browseButton.setBackground(UIManager.getColor("Button.disabledForeground"));
        browseButton.setFont(new Font("Tahoma", Font.PLAIN, FONT_SIZE));
        browseButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                UserHome.this.dispose();
                new Browse(user);
                //browse.setTitle("Browse Recipes");
                //browse.setVisible(true);
            }
        });
        contentPane.add(browseButton);

        JButton pantryButton = new JButton("My Pantry");
        pantryButton.setForeground(new Color(0, 0, 0));
        pantryButton.setBackground(UIManager.getColor("Button.disabledForeground"));
        pantryButton.setFont(new Font("Tahoma", Font.PLAIN, FONT_SIZE));
        pantryButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                    dispose();
                    Pantry pan = new Pantry();
                    pan.setTitle("My Pantry");
                        pan.setVisible(true);
                }
            
        });
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
        newRecipeButton.setFont(new Font("Tahoma", Font.PLAIN, FONT_SIZE));
        //newRecipeButton.setBounds(247, 320, 491, 114);
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


        JButton editRecipeButton = new JButton("View My Recipes");
        editRecipeButton.setBackground(UIManager.getColor("Button.disabledForeground"));
        editRecipeButton.setFont(new Font("Tahoma", Font.PLAIN, FONT_SIZE));
        //editRecipeButton.setBounds(247, 320, 491, 114);
        editRecipeButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                //functionality
                ResultSet rs = DataBase.getUserRecipes(netizenUsername);
                myRecipes newR;
                try {
                        newR = new myRecipes(netizenUsername, rs);
                        newR.setTitle("Create a recipe:");
                        newR.setVisible(true);
                } catch (SQLException e1) {
                        // TODO Auto-generated catch block
                        e1.printStackTrace();
                }
            }
        });
        contentPane.add(editRecipeButton);

        /** Create a new category */
        JButton newCategoryButton = new JButton("Create a Category");
        newCategoryButton.setForeground(new Color(0, 0, 0));
        newCategoryButton.setBackground(UIManager.getColor("Button.disabledForeground"));
        newCategoryButton.setFont(new Font("Tahoma", Font.PLAIN, FONT_SIZE));
        newCategoryButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                UserHome.this.dispose();
                NewCategory newR = new NewCategory(user);
            }
        });
        contentPane.add(newCategoryButton);

        // JButton searchRecipeButton = new JButton("Search for Recipe");
        // searchRecipeButton.setBackground(UIManager.getColor("Button.disabledForeground"));
        // searchRecipeButton.setFont(new Font("Tahoma", Font.PLAIN, strdFontSize));
        // //editRecipeButton.setBounds(247, 320, 491, 114);
        // searchRecipeButton.setBounds((20 + (2 * (strdButtonWidth + borderSize))), 320, strdButtonWidth, strdButtonHeight);
        // searchRecipeButton.addActionListener(new ActionListener() {
        //     public void actionPerformed(ActionEvent e) {
        //         //functionality
        //     }
        // });
        // contentPane.add(searchRecipeButton);

        /* JButton makeRecipeButton = new JButton("Bake/Cook Recipe");
        makeRecipeButton.setBackground(UIManager.getColor("Button.disabledForeground"));
        makeRecipeButton.setFont(new Font("Tahoma", Font.PLAIN, strdFontSize));
        //editRecipeButton.setBounds(247, 320, 491, 114);
        makeRecipeButton.setBounds((20 + (3 * (strdButtonWidth + borderSize))), 320, strdButtonWidth, strdButtonHeight);
        makeRecipeButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                dispose();
                Cook cook = new Cook();
                cook.setTitle("Choose Recipe to cook");
                cook.setVisible(true);
            }
        });
        contentPane.add(makeRecipeButton);*/

        /** Welcome message */
        String welcomeText = "<html>Welcome, " + UserLogin.getUsername() + "<br/>Select an action:<html/>";
        System.out.println(welcomeText);
        JLabel welcomeLabel = new JLabel(welcomeText);
        //welcome.setMinimumSize(new Dimension(500, 50));
        welcomeLabel.setFont(new Font("80er Teenie Demo", Font.ITALIC, 20));
        /* To center a component: (int) Math.floor(WIDTH_FRAME/2.5)*/
        welcomeLabel.setBounds((2 * BORDER), (2 * -BORDER), 500, 200);
        contentPane.add(welcomeLabel);

        /** Cooking mama */
        try{
            BufferedImage myPicture = ImageIO.read(new File("./login/mama.jpg"));
            JLabel picLabel = new JLabel(new ImageIcon(myPicture));
            picLabel.setBounds((int) Math.floor(WIDTH_FRAME/3), 200, 310, 359);
            add(picLabel);
        } catch (IOException e) { e.printStackTrace(); }

        /** Set button locations */
        btnNewButton.setBounds((WIDTH_FRAME - (6 * BORDER)), (2 * BORDER), LOGOUT_BUTTON_WIDTH, LOGOUT_BUTTON_HEIGHT);  //logout
        browseButton.setBounds(BORDER, LOWER_BUTTON, BUTTON_WIDTH, BUTTON_HEIGHT);
        editRecipeButton.setBounds((BORDER + BUTTON_WIDTH + BORDER), LOWER_BUTTON, BUTTON_WIDTH, BUTTON_HEIGHT);
        newRecipeButton.setBounds((BORDER + (2 * (BUTTON_WIDTH + BORDER))), LOWER_BUTTON, BUTTON_WIDTH, BUTTON_HEIGHT);
        pantryButton.setBounds((BORDER + (3 * (BUTTON_WIDTH + BORDER))), LOWER_BUTTON, BUTTON_WIDTH, BUTTON_HEIGHT);
        newCategoryButton.setBounds((BORDER + (4 * (BUTTON_WIDTH + BORDER))), LOWER_BUTTON, BUTTON_WIDTH, BUTTON_HEIGHT);




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
