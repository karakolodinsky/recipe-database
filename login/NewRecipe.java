package login;

import net.proteanit.sql.DbUtils;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.*;
import java.awt.*;
import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import javax.swing.*;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.text.NumberFormatter;

/**
 * UI-Interface for the New-Recipe functionality
 *
 * @author Serene Wood
 */

public class NewRecipe extends JFrame{

    /** Java Swing vars */
    private static final long serialVersionUID = 1;
    private JPanel contentPane;

    /** standardized variables for buttons: */
    private int strdButtonWidth = 150;
    private int strdButtonHeight = 40;
    private int strdFontSize = 10;
    private int borderSize = 20;

    /** standardized variables for text-boxes: */
    private int TEXT_BOX_WIDTH = 100;
    private int COMBO_BOX_WIDTH = 100;
    private int COMBO_BOX_HEIGHT = 50;

    private int SMALL_TEXT_BOX_WIDTH = 30;
    //private int SMALL_BOX_HEIGHT =20;

    /** Current user: */
    static private String currUser;

    /** SQL Recipe-Table Variables: */
    String name;            // <= 500 chars
    String author;
    String description;     // <= 200 chars
    String steps;           // <= 5000 chars
    Integer cooktime;
    Integer servings;
    Integer difficulty;

    /** Ingredient  */
    ArrayList<Ingredient> ingredients = new ArrayList<>();
    static String currIngredientStr;
    //private JScrollPane IngredientScroll;



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

    /**
     * Unused constructor
     */
    public NewRecipe() {

    }

    /**
     * Main NewRecipe Constructor
     * @param user  Current user's username'
     */
    public NewRecipe(String user) {
        currUser = user;                        // unnecessary
        author = UserLogin.getUsername();

        /** yoinked code: **/
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setBounds(450, 190, 1014, 597);
        setResizable(false);
        contentPane = new JPanel();
        contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
        setContentPane(contentPane);
        contentPane.setLayout(new FlowLayout());

        /** Enter Recipe Name Label & Text-box **/
        JLabel rNameLabel = new JLabel("Enter Recipe Name:");
        JTextField rNameTxt = new JTextField(TEXT_BOX_WIDTH);
        contentPane.add(rNameLabel);
        contentPane.add(rNameTxt);

        /** Enter Recipe Description Label & Text-box **/
        JLabel rDescLabel = new JLabel("Enter Recipe Description:");
        JTextField rDescTxt = new JTextField(TEXT_BOX_WIDTH);
        contentPane.add(rDescLabel);
        contentPane.add(rDescTxt);

        /** Enter Recipe Steps Label & Textbox **/
        JLabel rStepsLabel = new JLabel("Enter Recipe Steps:");
        JTextField rStepsTxt = new JTextField(TEXT_BOX_WIDTH);
        contentPane.add(rStepsLabel);
        contentPane.add(rStepsTxt);

        /** Cooktime Formatter (Integers must be >= 0) **/
        NumberFormat format = NumberFormat.getInstance();
        NumberFormatter formatter = new NumberFormatter(format);
        formatter.setValueClass(Integer.class);
        formatter.setMinimum(0);
        formatter.setMaximum(Integer.MAX_VALUE);
        formatter.setAllowsInvalid(false);

        /** Enter Recipe Cooktime Label & Text-box **/
        JLabel rCookTimeLabel = new JLabel("Enter Recipe Cooktime(in minutes):");
        JFormattedTextField rCookTimeInt = new JFormattedTextField(formatter);
        rCookTimeInt.setColumns(TEXT_BOX_WIDTH);
        contentPane.add(rCookTimeLabel);
        contentPane.add(rCookTimeInt);

        /** Enter Recipe Difficulty Label & Drop-down **/
        JLabel rDifficultyLabel = new JLabel("Select Recipe Difficulty:");
        Integer[] diffChoices = {1, 2, 3, 4, 5};
        JComboBox rDiffTime = new JComboBox(diffChoices);
        rDiffTime.setSize(COMBO_BOX_WIDTH, COMBO_BOX_HEIGHT);
        contentPane.add(rDifficultyLabel);
        contentPane.add(rDiffTime);

        /** Enter Recipe Servings Label & Drop-down **/
        JLabel rServingsLable = new JLabel("Select Recipe Servings:");
        Integer[] ServingsChoices = {1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12};
        JComboBox rServings = new JComboBox(ServingsChoices);
        rServings.setSize(COMBO_BOX_WIDTH, COMBO_BOX_HEIGHT);
        contentPane.add(rServingsLable);
        contentPane.add(rServings);

        /** Enter-Button calls DataBase.createRecipe() **/
        JButton enter = new JButton("Enter");
        enter.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // don't judge my shitty != null checks
                /** Check for all fields filled */
                if (rNameTxt.getText() != null && rStepsTxt.getText()!= null && rCookTimeInt.getValue()!= null &&
                        rServings.getItemAt(rServings.getSelectedIndex())!= null &&
                        rDiffTime.getItemAt(rDiffTime.getSelectedIndex())!= null && rDescTxt.getText() != null){

                    /** Check for acceptable str length */
                    if (rNameTxt.getText().length() < 500 && rStepsTxt.getText().length() < 5000 &&
                            rDescTxt.getText().length() <= 200){
                        /* Pull & save entered values: */
                        name = rNameTxt.getText();
                        System.out.println("set var name as: " + name);
                        description = rDescTxt.getText();
                        System.out.println("set description as: " + description);
                        steps = rStepsTxt.getText();
                        System.out.println("set steps as: " + steps);
                        difficulty = (int) rDiffTime.getItemAt(rDiffTime.getSelectedIndex());
                        System.out.println("set difficulty as: " + difficulty);
                        servings = (int) rServings.getItemAt(rServings.getSelectedIndex());
                        System.out.println("set servings as: " + servings);
                        cooktime = (Integer) rCookTimeInt.getValue();
                        System.out.println("set cooktime as: " + cooktime + " minutes");

                        System.out.println("The author of "+name+" is "+author);

                        /** Create recipe in SQL database; do not run until RecipeId is auto-generated **/
                        int newId = DataBase.createRecipe(steps, description, cooktime, servings, difficulty, name);
                        if (newId == -1){
                            System.out.println("ERROR: FAILED TO ADD NEW RECIPE TO DATABASE");
                        }
                        else{
                            NewRecipe.this.dispose();
                        }
                    }

                    /** Error message pop-up for when string input exceed limits */
                    else{       // strings too long
                        JFrame errorPopup = new JFrame("Error");
                        errorPopup.setSize(350,175);
                        PopupFactory pop = new PopupFactory();
                        Popup p = pop.getPopup(errorPopup, new JPanel(), 180, 100);
                        errorPopup.add(new JLabel("<html>Surpassed character limit: <br/>Recipe name must be " +
                                "less than 500 characters <br/>Recipe steps must be less than 5000 characters<br/>" +
                                "Recipe Description must be less than 200 characters<html/>"));
                        errorPopup.show();
                        p.show();
                    }
                }

                /** Error message pop-up for field values are unfilled */
                else{       // Empty fields
                    JFrame errorPopup = new JFrame("Error");
                    errorPopup.setSize(325,100);
                    PopupFactory pop = new PopupFactory();
                    Popup p = pop.getPopup(errorPopup, new JPanel(), 180, 100);
                    errorPopup.add(new JLabel("Fill out all fields"));
                    errorPopup.show();
                    p.show();
                }
            }
        });

        JPanel ingredientContainer = new JPanel();
        ingredientContainer.setAlignmentX(Component.LEFT_ALIGNMENT);
        ingredientContainer.setPreferredSize(new Dimension(1014, 100));
        ingredientContainer.setMaximumSize(new Dimension(1014, 100));

        /** Enter Recipe Steps Label & Textbox **/
        JLabel ingSelectLabel = new JLabel("Search for Ingredient:");
        JTextField ingNameTxt = new JTextField(20);
        //contentPane.add(ingSelectLabel);
        //contentPane.add(ingNameTxt);
        ingredientContainer.add(ingSelectLabel);
        ingredientContainer.add(ingNameTxt);

        JTable IngredientButtons = new JTable();
        //IngredientButtons.setVisible(false);
        JScrollPane IngredientScroll = new JScrollPane(IngredientButtons);
        JButton ingSearch = new JButton("Search");
        //contentPane.add(ingSearch);
        ingredientContainer.add(ingSearch);
        ingSearch.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try{
                    if (ingNameTxt.getText() != null) {
                        ResultSet ingredientsList = DataBase.GetIngredients(ingNameTxt.getText());
                        while (ingredientsList.next()){
                            IngredientButtons.setModel(DbUtils.resultSetToTableModel(ingredientsList));
                        }
                    }
                    else{
                        ResultSet ingredientsList =DataBase.GetIngredients("");
                        while (ingredientsList.next()){
                            IngredientButtons.setModel(DbUtils.resultSetToTableModel(ingredientsList));
                        }
                    }
                } catch (IOException | SQLException ioException) { ioException.printStackTrace(); }
            }
        });

        Dimension ingDim = new Dimension(400, 200 );
        IngredientScroll.setPreferredSize( ingDim );
        //JScrollPane IngredientScroll = new JScrollPane(IngredientButtons);
        IngredientScroll.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        //IngredientScroll.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        //IngredientScroll.setPreferredSize(new Dimension(SMALL_TEXT_BOX_WIDTH, COMBO_BOX_HEIGHT));
        //IngredientScroll.setSize(new Dimension(SMALL_TEXT_BOX_WIDTH, COMBO_BOX_HEIGHT));
        //contentPane.add(IngredientScroll);
        //ingredientContainer.add(IngredientScroll);

        JList<Ingredient> curIngredients = new JList<Ingredient>();
        curIngredients.setPreferredSize( ingDim );

        JLabel ingUnitLabel = new JLabel("Units (optional):");
        JTextField ingUnitTxt = new JTextField(10);
        //contentPane.add(ingUnitLabel);
        //contentPane.add(ingUnitTxt);
        ingredientContainer.add(ingUnitLabel);
        ingredientContainer.add(ingUnitTxt);

        JLabel ingQuantityLabel = new JLabel("Quantity:");
        JFormattedTextField ingQuantityTxt = new JFormattedTextField(formatter);
        ingQuantityTxt.setColumns(10);
        //contentPane.add(ingQuantityLabel);
        //contentPane.add(ingQuantityTxt);
        ingredientContainer.add(ingQuantityLabel);
        ingredientContainer.add(ingQuantityTxt);


        contentPane.add(ingredientContainer);
        contentPane.add(IngredientScroll);
        contentPane.add(curIngredients);
        contentPane.add(enter);
        contentPane.setVisible(true);
    }


    /**
     * Adds an ingredient button to save the current ingredient
     * @param txt
     * @return
     */
    private static JButton newIngredientButton (String txt){
        JButton nb = new JButton(txt);
        nb.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                currIngredientStr = txt;
            }
    });
        return nb;
    }

}
