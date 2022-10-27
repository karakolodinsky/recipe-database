package login;

import javax.swing.*;
import java.awt.*;
import java.sql.ResultSet;
import java.sql.SQLException; 

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

        panel.add(Box.createVerticalGlue());
        formatRecipe();
        this.getContentPane().add(scrollPane);
        setContentPane(scrollPane);
    }

    private void formatRecipe () throws SQLException {
        String avg = "";
        String[] info = btnText.split("Avg Rating: ");
        if (info.length > 1) {
            avg = info[1];
        }
        JButton recipe = new JButton("recipe");
        panel.add(recipe);

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