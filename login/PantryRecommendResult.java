package login;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList; 

public class PantryRecommendResult extends JFrame {

    //private String user;
    private JScrollPane contentPane;
    public static final int WIDTH_FRAME = 1200;
    public static final int HEIGHT_FRAME = 600;
    private String user;
    private JPanel panel;
    private ArrayList<Integer> recipeids;


    public PantryRecommendResult (String user, ArrayList<Integer> recipes) throws SQLException {
        super("Browse Results");
        this.user = user;
        recipeids = recipes;
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
        addResult(recipeids);
        setContentPane(contentPane);
    }
    
    private void recipeClick (JButton recipe) {
        int recipeId = Integer.parseInt(recipe.getName());
        String btnText = recipe.getText();
        recipe.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    new DisplayRecipe(user, recipeId, btnText);
                } catch (SQLException e1) {
                    e1.printStackTrace();
                }
                
            }

        });
    }

    private void addResult (ArrayList<Integer> recipeids) throws SQLException {
        Connection con = DataBase.getCon();
        int size = recipeids.size();
        for (int i = 0; i < size; i++) {
            PreparedStatement ps = con.prepareStatement("SELECT * FROM recipepublic WHERE recipeid = ?;");
            ps.setInt(1, recipeids.get(i));
            ResultSet rs = ps.executeQuery();
            rs.next();
            String name = rs.getString("name");
            int recipeId = rs.getInt("recipeId");
            int rating = rs.getInt("avgrating");
            if (rating != 0) {
                name = name + " Avg Rating: " + String.valueOf(rating);
            }
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
                    e.printStackTrace();
                }
            }
        });
    }


        
}
