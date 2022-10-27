package login;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.*;
import java.awt.*;
import java.sql.ResultSet;
import java.sql.SQLException; 

public class BrowseResult extends JFrame {

    private String user;
    private JScrollPane contentPane;
    public static final int WIDTH_FRAME = 1000;
    public static final int HEIGHT_FRAME = 600;
    private JPanel panel;
    private ResultSet rs;


    public BrowseResult (String user, ResultSet rs) throws SQLException {
        super("Browse Results");
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
        panel = new JPanel(new GridLayout(0,5, 10, 15));
        contentPane = new JScrollPane(panel,
        JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
        JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        addResult(rs);
        setContentPane(contentPane);
    }

    private void addResult (ResultSet rs) throws SQLException {
        while (rs.next()) {
            String name = rs.getString("name");
            int recipeId = rs.getInt("recipeId");
            int rating = rs.getInt("avgrating");
            if (rating != 0) {
                name = name + " Avg Rating: " + String.valueOf(rating);
            }
            JButton recipe = new JButton(name);
            recipe.setName(String.valueOf(recipeId));
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
