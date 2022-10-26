package login;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.*;
import java.awt.*;
import java.text.NumberFormat;
import javax.swing.*;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.text.NumberFormatter;

public class NewRecipe extends JFrame{

    private static final long serialVersionUID = 1;
    private JPanel contentPane;

    // standardized variables for buttons:
    private int strdButtonWidth = 150;
    private int strdButtonHeight = 40;
    private int strdFontSize = 10;
    private int borderSize = 20;

    // standardized variables for text-boxes:
    private int TEXT_BOX_WIDTH = 100;
    private int COMBO_BOX_WIDTH = 100;
    private int COMBO_BOX_HEIGHT = 50;

    // Current user
    static private String currUser;

    // SQL variables:
    String name;            // <= 500 chars
    String author;
    String steps;           // <= 5000 chars
    Integer cooktime;
    Integer servings;
    Integer diffuiculty;


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
        //solve s = new solve();
        currUser = user;

        author = UserLogin.getUsername();

        // yoinked code:
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setBounds(450, 190, 1014, 597);
        setResizable(false);
        contentPane = new JPanel();
        contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
        setContentPane(contentPane);
        contentPane.setLayout(new FlowLayout());

        //JTextField create = new JTextField("Create a new Recipe:", 20);
        JLabel create = new JLabel("Create a new recipe");
        contentPane.add(create);

        JLabel rNameLabel = new JLabel("Enter Recipe Name:");
        JTextField rNameTxt = new JTextField(TEXT_BOX_WIDTH);
        contentPane.add(rNameLabel);
        contentPane.add(rNameTxt);

        /**
         * ADD RECIPE DESCRIPTION INPUT: VARCHAR(200)
         */

        JLabel rStepsLabel = new JLabel("Enter Recipe Steps:");
        JTextField rStepsTxt = new JTextField(TEXT_BOX_WIDTH);
        contentPane.add(rStepsLabel);
        contentPane.add(rStepsTxt);

        NumberFormat format = NumberFormat.getInstance();
        NumberFormatter formatter = new NumberFormatter(format);
        formatter.setValueClass(Integer.class);
        formatter.setMinimum(0);
        formatter.setMaximum(Integer.MAX_VALUE);
        formatter.setAllowsInvalid(false);

        JLabel rCookTimeLabel = new JLabel("Enter Recipe Cooktime(in minutes):");
        JFormattedTextField rCookTimeInt = new JFormattedTextField(formatter);
        rCookTimeInt.setColumns(TEXT_BOX_WIDTH);

        contentPane.add(rCookTimeLabel);
        contentPane.add(rCookTimeInt);

        JLabel rDifficultyLabel = new JLabel("Select Recipe Difficulty:");
        Integer[] diffChoices = {1, 2, 3, 4, 5};
        JComboBox rDiffTime = new JComboBox(diffChoices);
        rDiffTime.setSize(COMBO_BOX_WIDTH, COMBO_BOX_HEIGHT);
        contentPane.add(rDifficultyLabel);
        contentPane.add(rDiffTime);

        JLabel rServingsLable = new JLabel("Select Recipe Servings:");
        Integer[] ServingsChoices = {1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12};
        JComboBox rServings = new JComboBox(ServingsChoices);
        rServings.setSize(COMBO_BOX_WIDTH, COMBO_BOX_HEIGHT);
        contentPane.add(rServingsLable);
        contentPane.add(rServings);

        JButton enter = new JButton("Enter");
        contentPane.add(enter);

        enter.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // String name;            // <= 500 chars
                //    String author;
                //    String steps;           // <= 5000 chars
                //    int cooktime;
                //    int servings;
                //    int diffuiculty;
                if (rNameTxt.getText() != null && rStepsTxt.getText()!= null && rCookTimeInt.getValue()!= null &&
                        rServings.getItemAt(rServings.getSelectedIndex())!= null &&
                        rDiffTime.getItemAt(rDiffTime.getSelectedIndex())!= null){
                    if (rNameTxt.getText().length() < 500 && rStepsTxt.getText().length() < 5000){
                        name = rNameTxt.getText();
                        System.out.println("set var name as: " + name);
                        /**
                         * ADD RECIPE DESCRIPTION VARIABLE
                         */
                        steps = rStepsTxt.getText();
                        System.out.println("set steps as: " + steps);
                        diffuiculty = (int) rDiffTime.getItemAt(rDiffTime.getSelectedIndex());
                        System.out.println("set difficulty as: " + diffuiculty);
                        servings = (int) rServings.getItemAt(rServings.getSelectedIndex());
                        System.out.println("set servings as: " + servings);
                        cooktime = (Integer) rCookTimeInt.getValue();
                        System.out.println("set cooktime as: " + cooktime + " minutes");

                        System.out.println("The author of "+name+" is "+author);
                        NewRecipe.this.dispose();
                    }
                    else{                                                       // strings too long
                        JFrame errorPopup = new JFrame("Error");
                        errorPopup.setSize(325,100);
                        PopupFactory pop = new PopupFactory();
                        Popup p = pop.getPopup(errorPopup, new JPanel(), 180, 100);
                        errorPopup.add(new JLabel("<html>Surpassed character limit: <br/>Recipe name must be " +
                                "less than 500 characters <br/>Recipe steps must be less than 5000 characters<html/>"));
                        errorPopup.show();
                        p.show();
                    }
                }
                else{                                                           // Empty fields
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

        contentPane.setVisible(true);

    }

}
