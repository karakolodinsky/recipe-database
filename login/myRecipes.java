package login;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.ResultSet;
import java.sql.SQLException; 

public class myRecipes extends JFrame {

    private String user;
    private JScrollPane contentPane;
    public static final int WIDTH_FRAME = 1200;
    public static final int HEIGHT_FRAME = 600;
    private JPanel panel;
    private ResultSet rs;


    public myRecipes (String user, ResultSet rs) throws SQLException {
        super("myRecipes");
        this.user = user;
        this.rs = rs;
        setResizable(false);
        setLayout(null);
        setSize(WIDTH_FRAME, HEIGHT_FRAME);
        setLocationRelativeTo(null);
        setLocation(getX() - 80, getY() - 80);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setVisible(true);

        init ();

    }

    private void init () throws SQLException {
        panel = new JPanel(new GridLayout(0, 3, 10, 10));
        contentPane = new JScrollPane(panel,
        JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
        JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        addResult(rs);
        setContentPane(contentPane);
    }
    
    private void recipeClick (JButton recipe) {
        int recipeId = Integer.parseInt(recipe.getName());
        String btnText = recipe.getText();
        recipe.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    new EditRecipe(user, recipeId, btnText);
                    myRecipes.this.dispose();
                } catch (Exception e1) {
                    // TODO Auto-generated catch block
                    e1.printStackTrace();
                }
                
            }

        });
    }

    private void addResult (ResultSet rs) throws SQLException {
        while (rs.next()) {
            String name = rs.getString("name");
            int recipeId = rs.getInt("recipeId");
            JButton recipe = new JButton(name);
            recipe.setName(String.valueOf(recipeId));
            recipeClick(recipe);
            recipe.setSize(10, 30);
            panel.add(recipe);
        }
        validate();
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


        
}
