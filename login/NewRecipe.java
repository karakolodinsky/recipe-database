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

    /** standardized variables for containers: */
    private int CONTAINER_HEIGHT = 50;
    private int CONTAINER_WIDTH = 1014;

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
    ArrayList<String> ingredientsStrings = new ArrayList<>();
    static String currIngredientStr;
    //private JScrollPane IngredientScroll;

    /** Category */
    ArrayList<String> categoriesString = new ArrayList<>();
    static String currCategoryStr;



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
                        //System.out.println(newId);
                        //System.exit(-1);
                        if (newId == -1){
                            System.out.println("ERROR: FAILED TO ADD NEW RECIPE TO DATABASE");
                        }
                        else{
                            DataBase.recipeRequires(newId, ingredients);
                            if (categoriesString.size() > 0){
                                String catStr = "";
                                for (String categ : categoriesString){
                                    catStr += (", " + categ);
                                }
                                DataBase.categorizeRecipe(catStr, newId);
                                NewRecipe.this.dispose();
                            }
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

        /** Container for ingredient buttons, labels, textboxes */
        JPanel ingredientContainer = new JPanel();
        ingredientContainer.setAlignmentX(Component.LEFT_ALIGNMENT);
        ingredientContainer.setPreferredSize(new Dimension(CONTAINER_WIDTH, CONTAINER_HEIGHT));
        ingredientContainer.setMaximumSize(new Dimension(CONTAINER_WIDTH, CONTAINER_HEIGHT));

        /** Enter Recipe Steps Label & Textbox **/
        JLabel ingSelectLabel = new JLabel("Search for Ingredient:");
        JTextField ingNameTxt = new JTextField(20);
        //contentPane.add(ingSelectLabel);
        //contentPane.add(ingNameTxt);
        ingredientContainer.add(ingSelectLabel);
        ingredientContainer.add(ingNameTxt);

        /** stores ingredient buttons in table */
        JTable IngredientButtons = new JTable();
        IngredientButtons.addMouseListener(new java.awt.event.MouseAdapter(){
            @Override
            public void mouseClicked(java.awt.event.MouseEvent evt){
                int row = IngredientButtons.rowAtPoint(evt.getPoint());
                String ing = (String) IngredientButtons.getValueAt(row, 0);
                currIngredientStr = ing;
            }
        });

        /** Scroll-wheel view of ingredients */
        JScrollPane IngredientScroll = new JScrollPane(IngredientButtons);

        /** Ingredient JTable buttons: */
        JButton ingSearch = new JButton("Search");
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

        /** Ingredient scroll attributes */
        Dimension ingDim = new Dimension(400, 200 );
        IngredientScroll.setPreferredSize( ingDim );
        IngredientScroll.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);

        /** List of ingredients added to recipe */
        JList<String> curIngredients = new JList<String>();
        curIngredients.setListData(ingredientsStrings.toArray(new String[ingredientsStrings.size()]));
        curIngredients.setPreferredSize( ingDim );

        /** Enter Ingredient Units Label & Text-box **/
        JLabel ingUnitLabel = new JLabel("Units (optional):");
        JTextField ingUnitTxt = new JTextField(10);
        ingredientContainer.add(ingUnitLabel);
        ingredientContainer.add(ingUnitTxt);

        /** Enter Ingredient Quantity Label & Text-box **/
        JLabel ingQuantityLabel = new JLabel("Quantity:");
        JFormattedTextField ingQuantityTxt = new JFormattedTextField(formatter);
        ingQuantityTxt.setColumns(10);
        //contentPane.add(ingQuantityLabel);
        //contentPane.add(ingQuantityTxt);
        ingredientContainer.add(ingQuantityLabel);
        ingredientContainer.add(ingQuantityTxt);

        /** Add-Ingredient button **/
        JButton addIngredient = new JButton("Add Ingredient");
        addIngredient.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (ingQuantityTxt.getValue() != null && currIngredientStr != null){
                    Ingredient newIngredient;
                    if (ingUnitTxt.getText() != null){
                        newIngredient = new Ingredient (currIngredientStr,
                                (Integer) ingQuantityTxt.getValue(), ingUnitTxt.getText());
                    }
                    else {
                        newIngredient = new Ingredient(currIngredientStr,
                                (Integer) ingQuantityTxt.getValue(), null);
                    }
                    ingredients.add(newIngredient);
                    ingredientsStrings.add(currIngredientStr);
                    curIngredients.setListData(ingredientsStrings.toArray(new String[ingredientsStrings.size()]));
                    System.out.println("New Ingredient: " + newIngredient.toStr());
                }
            }});

        /** Container for category buttons, labels, textboxes */
        JPanel categoryContainer = new JPanel();
        categoryContainer.setAlignmentX(Component.LEFT_ALIGNMENT);
        categoryContainer.setPreferredSize(new Dimension(CONTAINER_WIDTH, CONTAINER_HEIGHT));
        categoryContainer.setMaximumSize(new Dimension(CONTAINER_WIDTH, CONTAINER_HEIGHT));

        /** Category JTable buttons: */
        JTable CategoryButtons = new JTable();
        CategoryButtons.addMouseListener(new java.awt.event.MouseAdapter(){
            @Override
            public void mouseClicked(java.awt.event.MouseEvent evt){
                int row = CategoryButtons.rowAtPoint(evt.getPoint());
                String ing = (String) CategoryButtons.getValueAt(row, 0);
                currCategoryStr = ing;
            }
        });

        /** List of categories added to recipe */
        JList<String> curCategories = new JList<String>();
        curCategories.setListData(ingredientsStrings.toArray(new String[categoriesString.size()]));
        curCategories.setPreferredSize( ingDim );

        JButton addCategory = new JButton("Add Category");
        addCategory.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (currCategoryStr != null){
                    categoriesString.add(currCategoryStr);
                    curCategories.setListData(categoriesString.toArray(new String[categoriesString.size()]));
                    System.out.println("New Category: " + currCategoryStr);
                }
            }});


        /** Scroll-wheel view of ingredients */
        JScrollPane CategoryScroll = new JScrollPane(CategoryButtons);
        JButton catSearch = new JButton("Search");
        categoryContainer.add(catSearch);


        /** Add components in correct order: **/
        contentPane.add(ingredientContainer);
        contentPane.add(addIngredient);
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
