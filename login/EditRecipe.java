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
    private JTextField add;
    private JTextField delete;
    private JTextField deleteCat;
    private JTextField addCat;
    private JTextField recipeName;
    private JTextField cookTime;
    private JTextField servings;
    private JTextArea description;
    private JTextArea step;


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
        submit();
        deleteRec();
        this.getContentPane().add(scrollPane);
        setContentPane(scrollPane);
    }

    private void deleteRec () {
        JButton delete = new JButton("Delete Recipe");
        panel.add(delete);
        delete.addActionListener(new ActionListener () {

            @Override
            public void actionPerformed(ActionEvent e) {
                DataBase.DeleteRecipe(recipeId);
                EditRecipe.this.dispose();
            }

        });
    }

    private void submit() {
        JButton submit = new JButton ("Submit");
        panel.add(submit);
        submit.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                String name = recipeName.getText().strip();
                int time = Integer.parseInt(cookTime.getText().strip());
                int serv = Integer.parseInt(servings.getText());
                String steps = step.getText();
                if (steps.length() > 5000) {
                    steps.substring(0, 4999);
                }
                String desc = description.getText();
                if (desc.length() > 500) {
                    desc.substring(0, 499);
                }

                try {
                    PreparedStatement ps = DataBase.getCon().prepareStatement("UPDATE recipe SET steps=?, description=?, cooktime=?, " +
                                                                "servings=?, difficulty=?, name=? WHERE recipeid=?;");
                    ps.setString(1, steps);
                    ps.setString(2, desc);
                    ps.setInt(3, time);
                    ps.setInt(4, serv);
                    ps.setString(6, name);
                    ps.setInt(5, newDiff);
                    ps.setInt(7, recipeId);
                    ps.executeUpdate();
                    EditRecipe.this.dispose();
                } catch (SQLException e1) {
                    // TODO Auto-generated catch block
                    e1.printStackTrace();
                }          
                
            }
            
        });
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
        recipeName = label;
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
        newDiff = diff;
        diffMenu(diff);
        int time = rs.getInt("cooktime");
        labelLabel = new JLabel("Cook Time (Minutes):");
        panel.add(labelLabel);
        cookTime = labelMaker(String.valueOf(time));
        int serv = rs.getInt("servings");
        labelLabel = new JLabel("Servings:");
        panel.add(labelLabel);
        servings = labelMaker(String.valueOf(serv));
        String desc = rs.getString("description");
        labelLabel = new JLabel("Description:");
        panel.add(labelLabel);
        description = textArea(desc);
        String steps = rs.getString("steps");
        labelLabel = new JLabel("Steps:");
        panel.add(labelLabel);
        step = textArea(steps);
        labelLabel = new JLabel("Ingredients [ Name | Quantity | Units ]");
        panel.add(labelLabel);
        displayIngred();
        addIngBtn();
        deleteIngBtn();

        displayCategories();
        addCategoryBtn();



        validate();
    }

    private void addIngBtn () {
        JButton addIng = new JButton("Add Ingredient:");
        addIng.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    String text = add.getText().strip();
                    if (text != null || !text.equals("")) {
                        int ingredientId = addIngredient(text);
                        new addIngredientPopup(ingredientId, recipeId);
                    }
                } catch (IOException e1) {
                    // TODO Auto-generated catch block
                    e1.printStackTrace();
                }
                
            }

        });
        panel.add(addIng);
        add = textField();
    }

    private void deleteIngBtn () {
        JButton deleteIng = new JButton("Delete Ingredient:");
        deleteIng.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                
                deleteIngredient(delete.getText().strip());
                
            }

        });
        panel.add(deleteIng);
        delete = textField();
    }

    private void deleteIngredient (String name) {
        Connection con = DataBase.getCon();
        try {
            PreparedStatement st = (PreparedStatement) con
                    .prepareStatement("SELECT ingredientid, name FROM ingredient where name = ?");
            st.setString(1, name);
            st.execute();
            ResultSet rs = st.getResultSet();
            if (rs.isBeforeFirst()) {
                rs.next();
                int ingredientId = rs.getInt("ingredientid");
                PreparedStatement ps = con.prepareStatement(
                "DELETE FROM recipe_requires WHERE recipeid=? AND ingredientid=?;");
                ps.setInt(1, recipeId);
                ps.setInt(2, ingredientId);
                ps.executeUpdate();
            }
        } catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    private void deleteCategory (String name) {
        Connection con = DataBase.getCon();
        try {
            PreparedStatement st = (PreparedStatement) con
                    .prepareStatement("SELECT categoryid FROM category where categoryname = ?");
            st.setString(1, name);
            st.execute();
            ResultSet rs = st.getResultSet();
            rs.next();
            int categoryid = rs.getInt("categoryid");
            st = (PreparedStatement) con
                    .prepareStatement("SELECT categoryid FROM recipe_category where categoryid = ?");
            st.setInt(1, categoryid);
            st.execute();
            rs = st.getResultSet();
            if (rs.isBeforeFirst()) {
                rs.next();
                int categoryId = rs.getInt("categoryid");
                PreparedStatement ps = con.prepareStatement(
                "DELETE FROM recipe_category WHERE recipeid=? AND categoryid=?;");
                ps.setInt(1, recipeId);
                ps.setInt(2, categoryId);
                ps.executeUpdate();
            }
        } catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    

    private JTextField labelMaker (String text) {
        JTextField label = new JTextField(text);
        label.setAlignmentX(Component.LEFT_ALIGNMENT);
        label.setEditable(true);
        panel.add(label);
        return label;
    }

    private JTextArea textArea (String text) {
        JTextArea textArea = new JTextArea(text);
        textArea.setEditable(true);   
        textArea.setOpaque(true);  
        textArea.setLineWrap(true);
        textArea.setWrapStyleWord(true);
        textArea.setAlignmentX(LEFT_ALIGNMENT);
        panel.add(textArea);
        return textArea;
    }

    private JTextField textField () {
        JTextField textArea = new JTextField();
        textArea.setEditable(true);   
        textArea.setOpaque(true);  
        textArea.setAlignmentX(LEFT_ALIGNMENT);
        panel.add(textArea);
        return textArea;
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
            newDiff = 2;
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
    

    public static int addIngredient(String ingredient) throws IOException {
        Connection conn = DataBase.getCon();

        try {
            PreparedStatement st = (PreparedStatement) conn
                    .prepareStatement("SELECT ingredientid, name FROM ingredient where name = ?");
                    st.setString(1, ingredient);
            System.out.println(st);
            st.execute();
            ResultSet rs = st.getResultSet();
            int ingredientid = 0;
            if (rs.isBeforeFirst()) {
                if (rs.next()){
                    ingredientid = rs.getInt("ingredientid");
                    return ingredientid;
                }
            }

            else {
                st = conn.prepareStatement("SELECT MAX(ingredientid) as max FROM ingredient;");
                rs = st.executeQuery();
                rs.next();
                ingredientid = rs.getInt(1) + 1;
                st = conn.prepareStatement("INSERT INTO ingredient VALUES(?, ?);");
                st.setInt(1, ingredientid);
                st.setString(2, ingredient);
                st.executeUpdate();
                return ingredientid;
                
            }
            
            
        } catch (SQLException e) {
            }
        return 0;

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
            ps.execute();
            ResultSet rs = ps.getResultSet();
            if (rs.isBeforeFirst()) {
                JLabel labelLabel = new JLabel("Categories:");
                panel.add(labelLabel);
                panel.add(SearchTbl);
                JButton btn = new JButton("Delete Category:");
                panel.add(btn);
                btn.addActionListener(new ActionListener() {

                    @Override
                    public void actionPerformed(ActionEvent e) {
                        String text = deleteCat.getText().strip();
                        if (text != null && !text.equals("")) {
                            deleteCategory(text);
                        }
                        
                    }});
                deleteCat = textField();
                
            }
        } catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        try {
            ps = DataBase.getCon().prepareStatement("SELECT c.categoryname FROM recipe_category rc, category c " +
                                                "WHERE rc.categoryid=c.categoryid AND rc.recipeid=?;");
            ps.setInt(1, recipeId);
            ps.execute();
            ResultSet rs = ps.getResultSet();
                
            SearchTbl.setVisible(true);
            //panel.add(SearchTbl);
            SearchTbl.setModel(DbUtils.resultSetToTableModel(rs));
        } catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    private void addCategoryBtn () {
        JButton btn = new JButton("Add Category:");
        panel.add(btn);
        btn.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                String text =addCat.getText().strip();
                if (text != null && !text.equals("")) {
                    try {
                        addCategory(text);
                    } catch (IOException e1) {
                        // TODO Auto-generated catch block
                        e1.printStackTrace();
                    }
                }
                
            }});
        addCat = textField();
    }

    private int addCategory(String text) throws IOException {
        Connection conn = DataBase.getCon();

        try {
            PreparedStatement st = (PreparedStatement) conn
                    .prepareStatement("SELECT categoryid FROM category where categoryname = ?;");
                    st.setString(1, text);
            System.out.println(st);
            st.execute();
            ResultSet rs = st.getResultSet();
            int categoryid = 0;
            if (rs.isBeforeFirst()) {
                if (rs.next()){
                    categoryid = rs.getInt("categoryid");
                }
            }

            else {
                st = conn.prepareStatement("SELECT MAX(categoryid) as max FROM category;");
                rs = st.executeQuery();
                rs.next();
                categoryid = rs.getInt(1) + 1;
                st = conn.prepareStatement("INSERT INTO category VALUES(?, ?);");
                st.setInt(1, categoryid);
                st.setString(2, text);
                st.executeUpdate();
                
            }
            st = conn.prepareStatement("INSERT INTO recipe_category VALUES (?, ?);");
            st.setInt(1, recipeId);
            st.setInt(2, categoryid);
            st.executeUpdate();
            
            
        } catch (SQLException e) {
            }
        return 0;

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
