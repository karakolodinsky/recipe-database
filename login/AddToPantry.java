package login;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import net.proteanit.sql.DbUtils;
import java.sql.*;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.text.AbstractDocument.Content;


public class AddToPantry extends JFrame {

    private static final long serialVersionUID = 1L;

    /**
     * login window width
     */
    public static final int WIDTH_FRAME = 540;

    /**
     * login window height
     */
    public static final int HEIGHT_FRAME = 360;


    private JTextField ingredientJLabel;
    private JButton btnNewButton;
    private JButton RegisterButton;
    private JLabel label_errorText;
    private JPanel contentPane;
    private Insets insets;


    public AddToPantry() {
        super("Add To Pantry");
        setResizable(false);
        setLayout(null);
        setSize(WIDTH_FRAME, HEIGHT_FRAME);
        setLocationRelativeTo(null);
        setLocation(getX() - 80, getY() - 80);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setVisible(true);

        insets = this.getInsets();

        init();

    }

    private void init() {

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setBounds(50, 50, 750, 450);
        setResizable(false);
        contentPane = new JPanel();
        contentPane.setBackground(new Color(168, 240, 224));
        contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
        setContentPane(contentPane);
        contentPane.setLayout(null);


        JTextField IngredientField = new JTextField();
        IngredientField.setFont(new Font("Juice ITC", Font.PLAIN, 20));
        IngredientField.setBounds(20, 100, 200, 30);
        contentPane.add(IngredientField);
        IngredientField.setColumns(10);

        JLabel ingredientJLabel = new JLabel("Ingredient");
        ingredientJLabel.setBackground(new Color(181, 151, 207));
        ingredientJLabel.setForeground(new Color(181, 151, 207));
        ingredientJLabel.setFont(new Font("80er Teenie Demo", Font.BOLD, 20));
        ingredientJLabel.setBounds(30, 50, 200, 40);
        contentPane.add(ingredientJLabel);

        JLabel addJLabel = new JLabel("Add to Pantry");
        addJLabel.setBackground(new Color(181, 151, 207));
        addJLabel.setForeground(new Color(181, 151, 207));
        addJLabel.setFont(new Font("80er Teenie Demo", Font.BOLD, 30));
        addJLabel.setBounds(300, 10, 200, 40);
        contentPane.add(addJLabel);

        label_errorText = new JLabel();
        label_errorText.setForeground(Color.RED);
        label_errorText.setBounds(20, 320, 170, 30);
        label_errorText.setFont(new Font("Tahoma", Font.PLAIN + Font.BOLD, 11));
        contentPane.add(label_errorText);


        JTable SearchTbl = new JTable();
        SearchTbl.setBounds(20, 150, 200, 40);
        contentPane.add(SearchTbl);

        JScrollPane js=new JScrollPane(SearchTbl);
        js.setVisible(true);
        add(js);

        JButton SearchButton = new JButton("Search");
        SearchButton.setFont(new Font("80er Teenie Demo", Font.BOLD, 26));
        SearchButton.setBounds(20, 350, 162, 50);
        contentPane.add(SearchButton);
        SearchButton.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                        if (IngredientField.getText().equals("")) {
                                label_errorText.setText("Nothing has been typed.");
           
                            } else {
           
                               try {
                                System.out.println(IngredientField.getText());
                                        ResultSet rs = DataBase.GetIngredients(IngredientField.getText());
                                   if (rs != null) {
                                        SearchTbl.setModel(DbUtils.resultSetToTableModel(rs));
                                        label_errorText.setText("");
           
                       } else {
                                   label_errorText.setText("Ingredient not found.");
                       }
                               } catch (HeadlessException e1) {
                                   // TODO Auto-generated catch block
                                   e1.printStackTrace();
                               } catch (IOException e1) {
                                   // TODO Auto-generated catch block
                                   e1.printStackTrace();
                               }
           
                       }
           
                   }
               });

               

        setContentPane(contentPane);

    }
    public static void main(String[] args) {
        EventQueue.invokeLater(new Runnable() {

            @Override
            public void run() {

                new AddToPantry();

            }
        });

    }
}