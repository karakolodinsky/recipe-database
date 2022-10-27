package login;

import javax.swing.*;
import java.awt.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
/**
 * UI-Interface for the display recipe
 *
 * @author Teagan Nester
 * @author ?
 */

public class DisplayRecipe extends JFrame {

    private String user;
    private int recipeId;
    private String btnText;
    private JScrollPane contentPane;
    public static final int WIDTH_FRAME = 1200;
    public static final int HEIGHT_FRAME = 600;
    private JPanel panel;
    private ResultSet rs;


    public DisplayRecipe (String user, int recipeId, String btnText) throws SQLException {
        super("Recipe Information");
        this.user = user;
        this.recipeId = recipeId;
        this.btnText = btnText;
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
        panel = new JPanel();
        JScrollPane scrollPane = new JScrollPane(panel);

        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setAlignmentY(TOP_ALIGNMENT);

        panel.add(Box.createVerticalGlue());
        formatRecipe();
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
        JLabel label = new JLabel(info[0]);
        label.setAlignmentX(Component.CENTER_ALIGNMENT);
        panel.add(label);
        if (!(avg.equals(""))) {
            label = new JLabel("Average Rating: " + avg);
            label.setAlignmentX(Component.CENTER_ALIGNMENT);
            panel.add(label);
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
        String desc = rs.getString("description");
        textArea("Description:\n" + desc);
        String steps = rs.getString("steps");
        textArea("Steps:\n " + steps);


        validate();
    }

    private void labelMaker (String text) {
        JLabel label = new JLabel(text);
        label.setAlignmentX(Component.CENTER_ALIGNMENT);
        panel.add(label);
    }

    private void textArea (String text) {
        JTextArea textArea = new JTextArea(text);
        textArea.setEditable(false);  
        textArea.setCursor(null);  
        textArea.setOpaque(false);  
        textArea.setFocusable(false);
        textArea.setLineWrap(true);
        textArea.setWrapStyleWord(true);
        textArea.setAlignmentX(CENTER_ALIGNMENT);
        panel.add(textArea);
        panel.add(textArea);
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


        
}