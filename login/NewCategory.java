package login;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.SQLException;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import javax.swing.*;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.text.NumberFormatter;


public class NewCategory extends JFrame {

    /** standardized variables for buttons: */
    private static final int strdButtonWidth = 150;
    private static final int strdButtonHeight = 40;
    private static final int strdFontSize = 10;
    private static final int borderSize = 20;

    /** standardized variables for text-boxes: */
    private static final int TEXT_BOX_WIDTH = 100;
    private static final int COMBO_BOX_WIDTH = 100;
    private static final int COMBO_BOX_HEIGHT = 50;

    private static final int SMALL_TEXT_BOX_WIDTH = 30;
    //private int SMALL_BOX_HEIGHT =20;

    /** standardized variables for containers: */
    private static final int CONTAINER_HEIGHT = 60;
    private static final int CONTAINER_WIDTH = 1014;

    /** NewCategory vars */
    String author;
    private static JPanel contentPane;
    static String currCategoryStr;
    private static String user;

    /**
     * Launch the application.
     */
    public static void main(String[] args) {
        EventQueue.invokeLater(new Runnable() {
            public void run() {
                try {
                    NewCategory frame = new NewCategory();
                    frame.setVisible(true);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    public NewCategory(){

    }


    /**
     * Main NewRecipe Constructor
     */
    public NewCategory(String user) {
        author = UserLogin.getUsername();
        this.user = user;
        //try {
        //    NewCategory frame = new NewCategory();
        //    frame.setVisible(true);
        //} catch (Exception e) {
        //    e.printStackTrace();
        //}

        /** yoinked code: **/
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setBounds(450, 190, 1014, 650);
        //contentPane.setBounds(450, 190, 1014, 650);
        setResizable(false);
        contentPane = new JPanel();
        contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
        setContentPane(contentPane);
        contentPane.setLayout(new FlowLayout());
        contentPane.setVisible(true);

        setVisible(true);
        init();
    }

    private void init(){
        //contentPane = new JPanel();

        /** Enter Category Name Label & Text-box **/
        JLabel cNameLabel = new JLabel("Enter Category Name:");
        JTextField cNameTxt = new JTextField(TEXT_BOX_WIDTH);
        contentPane.add(cNameLabel);
        contentPane.add(cNameTxt);

        /** Result text */
        JLabel resultLable = new JLabel("");

        /** Add category button */
        JButton nb = new JButton("Add Category");
        nb.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (cNameTxt.getText() != null){
                    currCategoryStr = cNameTxt.getText();
                    boolean added = DataBase.newCategory(currCategoryStr);
                    if (added){
                        resultLable.setText("Added category " + cNameTxt.getText() + " to the database");
                        cNameTxt.setText("");
                    }
                    if (!added){
                        resultLable.setText("Category " + cNameTxt.getText() + " is already in the database");
                    }
                }
                else{
                    JFrame errorPopup = new JFrame("Error");
                    errorPopup.setSize(325,100);
                    PopupFactory pop = new PopupFactory();
                    Popup p = pop.getPopup(errorPopup, new JPanel(), 180, 100);
                    errorPopup.add(new JLabel("<html>Fill out all fields<html/>"));
                    errorPopup.show();
                    p.show();
                }
            }
        });

        JButton homeButton = new JButton("Back to home");
        homeButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                NewCategory.this.dispose();
                UserHome newR = new UserHome();
            }
        });


        contentPane.add(nb);
        contentPane.add(homeButton);
        resultLable.setMinimumSize(new Dimension(900, 50));
        contentPane.add(resultLable);
    }
}
