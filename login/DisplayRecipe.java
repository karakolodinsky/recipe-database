package login;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException; 
/**
 * UI-Interface for the display recipe
 *
 * @author Teagan Nester
 * @author Serene Wood
 * @author ?
 */

public class DisplayRecipe extends JFrame {

    private String user;
    private static int recipeId;
    private String btnText;
    private JScrollPane contentPane;
    public static final int WIDTH_FRAME = 1200;
    public static final int HEIGHT_FRAME = 600;
    private int TEXT_BOX_WIDTH = 150;
    private int TEXT_BOX_HEIGHT = 30;
    private JPanel panel;
    private ResultSet rs;
    private JButton cookButton;
    private JPanel ingredientContainer;
    private JPanel descriptionContainer;
    private JPanel categoriesContainer;

    /** standardized variables for containers: */
    private int CONTAINER_HEIGHT = 500;
    private int CONTAINER_WIDTH = 400;
    private int HALF_CONTAINER_HEIGHT = 250;
    //private int SMALL_CONTAINER_HEIGHT = ;


    public DisplayRecipe (String user, int recipeId, String btnText) throws SQLException {
        super("Recipe Information");
        this.user = user;
        this.recipeId = recipeId;
        this.btnText = btnText;
        setResizable(false);

        setSize(WIDTH_FRAME, HEIGHT_FRAME);
        setLocationRelativeTo(null);
        setLocation(getX() - 80, getY() - 80);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);

        setVisible(true);

        init ();

    }

    private void init () throws SQLException {
        panel = new JPanel();
        panel.setLayout(new FlowLayout());
        panel.setBorder(new EmptyBorder(5, 5, 5, 5));
        JScrollPane scrollPane = new JScrollPane(panel);

        //headerContainer = new JPanel();
        //categoriesContainer.setAlignmentX(Component.LEFT_ALIGNMENT);
        //categoriesContainer.setPreferredSize(new Dimension(CONTAINER_WIDTH, HALF_CONTAINER_HEIGHT));
        //categoriesContainer.setMaximumSize(new Dimension(CONTAINER_WIDTH, HALF_CONTAINER_HEIGHT));

        ingredientContainer = new JPanel();
        ingredientContainer.setAlignmentX(Component.LEFT_ALIGNMENT);
        ingredientContainer.setPreferredSize(new Dimension(CONTAINER_WIDTH, HALF_CONTAINER_HEIGHT));
        ingredientContainer.setMaximumSize(new Dimension(CONTAINER_WIDTH, HALF_CONTAINER_HEIGHT));

        categoriesContainer = new JPanel();
        categoriesContainer.setAlignmentX(Component.LEFT_ALIGNMENT);
        categoriesContainer.setPreferredSize(new Dimension(CONTAINER_WIDTH, HALF_CONTAINER_HEIGHT));
        categoriesContainer.setMaximumSize(new Dimension(CONTAINER_WIDTH, HALF_CONTAINER_HEIGHT));

        descriptionContainer = new JPanel();
        descriptionContainer.setAlignmentX(Component.LEFT_ALIGNMENT);
        descriptionContainer.setPreferredSize(new Dimension(CONTAINER_WIDTH, CONTAINER_HEIGHT));
        descriptionContainer.setMaximumSize(new Dimension(CONTAINER_WIDTH, CONTAINER_HEIGHT));

        panel.add(descriptionContainer);
        panel.add(categoriesContainer);
        panel.add(ingredientContainer);

        //panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setAlignmentY(TOP_ALIGNMENT);

        panel.add(Box.createVerticalGlue());
        RecipeIngredients();
        formatRecipe();
        RecipeTags();
        this.getContentPane().add(scrollPane);
        setContentPane(scrollPane);
    }

    private void formatRecipe () throws SQLException {
        Connection con = DataBase.getCon();
        String avg = "";
        String[] info = btnText.split("Avg Rating: ");
        if (info.length > 1) {
            avg = info[1];
        }
        cookButton = new JButton("cook/make/bake");
        cookButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        descriptionContainer.add(cookButton);
        cookButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                //functionality for make-bake-cook
                // will need to pass in recipeid to to Database.cookRecipe(int recipeid)
                int x = DataBase.cookRecipe(recipeId, 1);
                if(x == -1){
                    JOptionPane.showMessageDialog(new JFrame(), "out of ingredients!",
                            "yo", JOptionPane.ERROR_MESSAGE);
                }
                else {
                        EventQueue.invokeLater(new Runnable() {
                                @Override
                                public void run() {
                                    new Review();
                                }
                            });
                }
            }
        });
        JLabel label = new JLabel(info[0]);
        label.setAlignmentX(Component.CENTER_ALIGNMENT);
        descriptionContainer.add(label);
        if (!(avg.equals(""))) {
            label = new JLabel("Average Rating: " + avg);
            label.setAlignmentX(Component.CENTER_ALIGNMENT);
            descriptionContainer.add(label);
        }
        PreparedStatement ps = con.prepareStatement("SELECT cooktime, steps, description, servings, difficulty, date " +
                                "FROM recipe WHERE recipeId=?;");
        ps.setInt(1, recipeId);
        ResultSet rs = ps.executeQuery();
        rs.next();
        String date = rs.getDate("date").toString();
        labelMaker("Uploaded On: " + date);
        int diff = rs.getInt("difficulty");
        String diffString = difficulty(diff);
        labelMaker("Difficulty: " + diffString);
        int time = rs.getInt("cooktime");
        labelMaker("Cook Time: " + time);
        int serv = rs.getInt("servings");
        labelMaker("Servings: " + serv);

        JPanel div = new JPanel();
        div.setSize(new Dimension(900, 50));
        panel.add(div);

        String desc = rs.getString("description");
        textArea("Description:\n" + desc);
        String steps = rs.getString("steps");
        textArea("Steps:\n " + steps);


        validate();
    }


    /**
     * Adds recipe's "tags": categories
     */
    private void RecipeTags(){
        String categorysList = "<html>Categories:<br/><br/>";

        try{
            ResultSet categoryList = DataBase.getCategories(recipeId);
            if (categoryList != null){
                while (categoryList.next()) {
                    categorysList += categoryList.getString(1);
                    categorysList += ", <br/>";
                }
                categorysList += "<html/>";
                JLabel comp = new JLabel(categorysList);
                System.out.println(categorysList);
                categoriesContainer.add(comp);
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    /**
     * Pulls all ingredients for the current recipe and displays them
     */
    private void RecipeIngredients(){
        String ingredientsList = "<html>Ingredients:<br/><br/>";
        try{
            ResultSet categoryList = DataBase.getIngredients(recipeId);
            //SELECT r.ingredientid, r.quantity, r.unit
            if (categoryList != null){
                while (categoryList.next()){
                    ingredientsList += categoryList.getString(1);
                    ingredientsList += (categoryList.getString(3));
                    ingredientsList += categoryList.getInt(2);
                    ingredientsList += ", <br/>"; //ingredientsList += ", <br/>";             // newlines
                }
                ingredientsList += "<html/>";
                JLabel comp = new JLabel(ingredientsList);
                System.out.println(ingredientsList);
                ingredientContainer.add(comp);
            }
            else{
                System.out.println("No ingredients");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        //textArea(ingredientsList);
    }

    private void labelMaker (String text) {
        JLabel label = new JLabel(text);
        label.setPreferredSize(new Dimension(TEXT_BOX_WIDTH, TEXT_BOX_HEIGHT));
        label.setAlignmentX(Component.CENTER_ALIGNMENT);
        descriptionContainer.add(label);
    }

    private void textArea (String text) {
        JTextArea textArea = new JTextArea(text);
        //textArea.setColumns(300);
        textArea.setLineWrap(true);
        textArea.setWrapStyleWord(true);


        textArea.setEditable(false);  
        textArea.setCursor(null);  
        textArea.setOpaque(false);  
        textArea.setFocusable(false);
        textArea.setLineWrap(true);
        textArea.setWrapStyleWord(true);
        textArea.setAlignmentX(CENTER_ALIGNMENT);
        descriptionContainer.add(textArea);
        descriptionContainer.add(textArea);
    }

    private String difficulty (int diff) {
        String diffString = "";
        if (diff == 1) {
            diffString = "Easy";
        }
        else if (diff == 2) {
            diffString = "Easy-Medium";
        }
        else if (diff == 3) {
            diffString = "Medium";
        }
        else if (diff == 4) {
            diffString = "Medium-Hard";
        }
        else {
            diffString = "Hard";
        }
        return diffString;
            
    }

    public static void main(String[] args) {
        EventQueue.invokeLater(new Runnable() {
            public void run() {
                try {
                    new BrowseResult("test", null);
                } catch (SQLException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
        });
    }

    public static int getRecipeId() {
            return recipeId;
    }
        
}