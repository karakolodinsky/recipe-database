package login;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;

import net.proteanit.sql.DbUtils;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.Vector;
/**
 * UI-Interface for the edit recipe
 *
 * @author Teagan Nester
 * @author ?
 */

public class EditRecipe extends JFrame {

    private String user;
    private int recipeId;
    private String btnText;
    private JScrollPane contentPane;
    public static final int WIDTH_FRAME = 1200;
    public static final int HEIGHT_FRAME = 600;
    private JPanel panel;
    private ResultSet rs;
    private int newDiff;


    public EditRecipe (String user, int recipeId, String btnText) throws Exception {
        super("Edit Recipe");
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

    private void init () throws Exception {
        panel = new JPanel();
        JScrollPane scrollPane = new JScrollPane(panel);

        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setAlignmentY(TOP_ALIGNMENT);

        panel.add(Box.createVerticalGlue());
        formatRecipe();
        JButton home = returnHome();
        panel.add(home);
        this.getContentPane().add(scrollPane);
        setContentPane(scrollPane);
    }

    private void formatRecipe () throws Exception {
        Connection con = DataBase.getCon();
        String avg = "";
        String[] info = btnText.split("Avg Rating: ");
        if (info.length > 1) {
            avg = info[1];
        }
        JTextField label = new JTextField(info[0]);
        label.setEditable(true);
        label.setAlignmentX(Component.CENTER_ALIGNMENT);
        panel.add(label);
        if (!(avg.equals(""))) {
            JLabel labelLabel = new JLabel("Average Rating: " + avg);
            labelLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
            panel.add(label);
        }
        PreparedStatement ps = con.prepareStatement("SELECT cooktime, steps, description, servings, difficulty, date " +
                                "FROM recipe WHERE recipeId=?;");
        ps.setInt(1, recipeId);
        ResultSet rs = ps.executeQuery();
        rs.next();
        String date = rs.getDate("date").toString();
        JLabel labelLabel = new JLabel("Uploaded On: " + date);
        panel.add(labelLabel);
        int diff = rs.getInt("difficulty");
        diffMenu(diff);
        int time = rs.getInt("cooktime");
        labelLabel = new JLabel("Cook Time (Minutes):");
        panel.add(labelLabel);
        labelMaker(String.valueOf(time));
        int serv = rs.getInt("servings");
        labelLabel = new JLabel("Servings:");
        panel.add(labelLabel);
        labelMaker(String.valueOf(serv));
        String desc = rs.getString("description");
        labelLabel = new JLabel("Description:");
        panel.add(labelLabel);
        textArea(desc);
        String steps = rs.getString("steps");
        labelLabel = new JLabel("Steps:");
        panel.add(labelLabel);
        textArea(steps);
        labelLabel = new JLabel("Ingredients [ Name | Quantity | Units ]");
        panel.add(labelLabel);
        displayIngred();
        displayCategories();



        validate();
    }

    private void labelMaker (String text) {
        JTextField label = new JTextField(text);
        label.setAlignmentX(Component.LEFT_ALIGNMENT);
        label.setEditable(true);
        panel.add(label);
    }

    private void textArea (String text) {
        JTextArea textArea = new JTextArea(text);
        textArea.setEditable(true);   
        textArea.setOpaque(true);  
        textArea.setLineWrap(true);
        textArea.setWrapStyleWord(true);
        textArea.setAlignmentX(LEFT_ALIGNMENT);
        panel.add(textArea);
    }

    private JButton returnHome () {
        JButton home = new JButton("Home");
        home.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                EventQueue.invokeLater(new Runnable() {
                    @Override
                    public void run() {
                        EditRecipe.this.dispose();
                        new UserHome();
                    }
                });
            }
        });
        return home;
    }

    private void diffMenu (int diff) {
        String diffString = difficulty(diff);
        JMenuBar menu = new JMenuBar();
        JMenu difficult = new JMenu("Difficulty");
        menu.add(difficult);
        panel.add(menu);
        ButtonGroup group = new ButtonGroup();
        JRadioButtonMenuItem rbMenuItem = new JRadioButtonMenuItem("Easy");
        if (diff == 1) {
            rbMenuItem.setSelected(true);
        }
        diffClick(rbMenuItem);
        group.add(rbMenuItem);
        difficult.add(rbMenuItem);
        rbMenuItem = new JRadioButtonMenuItem("Easy-Medium");
        if (diff == 2) {
            rbMenuItem.setSelected(true);
        }
        diffClick(rbMenuItem);
        group.add(rbMenuItem);
        difficult.add(rbMenuItem);
        rbMenuItem = new JRadioButtonMenuItem("Medium");
        if (diff == 3) {
            rbMenuItem.setSelected(true);
        }
        diffClick(rbMenuItem);
        group.add(rbMenuItem);
        difficult.add(rbMenuItem);
        rbMenuItem = new JRadioButtonMenuItem("Medium-Hard");
        if (diff == 4) {
            rbMenuItem.setSelected(true);
        }
        diffClick(rbMenuItem);
        group.add(rbMenuItem);
        difficult.add(rbMenuItem);
        rbMenuItem = new JRadioButtonMenuItem("Hard");
        if (diff == 5) {
            rbMenuItem.setSelected(true);
        }
        diffClick(rbMenuItem);
        group.add(rbMenuItem);
        difficult.add(rbMenuItem);
    }

    private void diffClick (JRadioButtonMenuItem m) {
        m.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                String text = m.getText();
                String newDiffText = text;
                if (newDiffText.equals("Easy")) {
                    newDiff = 1;
                }
                else if (newDiffText.equals("Easy-Medium")) {
                    newDiff = 2;
                }
                else if (newDiffText.equals("Medium")) {
                    newDiff = 3;
                }
                else if (newDiffText.equals("Medium-Hard")) {
                    newDiff = 4;
                }
                else {
                    newDiff = 5;
                }
            }

        });
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

    private void displayIngred () {
        JTable SearchTbl = new JTable();
        //SearchTbl.setBounds(0, 0, 100, 100);
        SearchTbl.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
        SearchTbl.setDefaultEditor(Object.class, null);
        SearchTbl.setAlignmentX(LEFT_ALIGNMENT);
        PreparedStatement ps;
        try {
            ps = DataBase.getCon().prepareStatement("SELECT i.name, rr.quantity, rr.unit FROM recipe_requires rr, ingredient i " +
                                            "WHERE rr.ingredientid=i.ingredientid AND rr.recipeid=?;");
            ps.setInt(1, recipeId);
            ResultSet rs = ps.executeQuery();
            SearchTbl.setModel(DbUtils.resultSetToTableModel(rs));
        } catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
            
        SearchTbl.setVisible(true);
        SearchTbl.setRowSelectionAllowed(true);
        SearchTbl.addMouseListener(new java.awt.event.MouseAdapter() {
                @Override
                public void mouseClicked(java.awt.event.MouseEvent evt) {
                    int row = SearchTbl.rowAtPoint(evt.getPoint());
                    String ing = (String) SearchTbl.getValueAt(row, 0);
                }
            });
        //helper.add(SearchTbl);
        panel.add(SearchTbl);
    }
    

    public static void addIngredient(String ingredient) throws IOException {
        Connection conn = DataBase.getConnect();

        try {
            PreparedStatement st = (PreparedStatement) conn
                    .prepareStatement("SELECT ingredientid, name FROM ingredient where name = ?");
                    st.setString(1, ingredient);
            System.out.println(st);
            boolean exec = st.execute();
            ResultSet rs = st.getResultSet();
            int ingredientid = 0;
            if (rs.isBeforeFirst()) {
                while(rs.next()){
                    ingredientid = rs.getInt("ingredientid");
                    // add to display
                }
            }

            else {
                st = conn.prepareStatement("SELECT MAX(ingredientid) as max FROM ingredient;");
                rs = st.executeQuery();
                rs.next();
                ingredientid = rs.getInt(1) + 1;

                //add to display
            }
            st = conn.prepareStatement("INSERT INTO ingredient VALUES(?, ?);");
            st.setInt(1, ingredientid);
            st.setString(2, ingredient);
            st.executeUpdate();
            
        } catch (SQLException e) {
            }

    }

    private void displayCategories () {
        JTable SearchTbl = new JTable();
        SearchTbl.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
        SearchTbl.setDefaultEditor(Object.class, null);
        SearchTbl.setAlignmentX(LEFT_ALIGNMENT);
        PreparedStatement ps;
        try {
            ps = DataBase.getCon().prepareStatement("SELECT c.categoryname FROM recipe_category rc, category c " +
                                            "WHERE rc.categoryid=c.categoryid AND rc.recipeid=?;");
            ps.setInt(1, recipeId);
            ResultSet rs = ps.executeQuery();
            SearchTbl.setModel(DbUtils.resultSetToTableModel(rs));
            if (rs.next()) {
                JLabel labelLabel = new JLabel("Categories:");
                panel.add(labelLabel);
            }
        } catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
            
        SearchTbl.setVisible(true);
        SearchTbl.setRowSelectionAllowed(true);
        SearchTbl.addMouseListener(new java.awt.event.MouseAdapter() {
                @Override
                public void mouseClicked(java.awt.event.MouseEvent evt) {
                    int row = SearchTbl.rowAtPoint(evt.getPoint());
                    String ing = (String) SearchTbl.getValueAt(row, 0);
                }
            });
        //helper.add(SearchTbl);
        panel.add(SearchTbl);
    }

    public static void main(String[] args) {
        EventQueue.invokeLater(new Runnable() {
            public void run() {
                try {
                    new EditRecipe("test", 1, "str");
                } catch (SQLException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                } catch (Exception e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
        });
    }


        
}
