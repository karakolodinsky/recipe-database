package login;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JPanel;
import javax.swing.JRadioButtonMenuItem;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

public class addIngredientPopup extends JFrame{

    private int ingredientId;
    JPanel panel;
    JTextArea quantity;
    String unit = "";
    int recipeId;
    //String user;

    public addIngredientPopup (int ingredientId, int recipeId) {
        super("Edit Ingredients");
        this.ingredientId = ingredientId;
        this.recipeId = recipeId;
        //this.user = user;
        setResizable(false);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setVisible(true);

        init();
    }
    
    private void init () {
        panel = new JPanel();
        JScrollPane scrollPane = new JScrollPane(panel);
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setAlignmentX(CENTER_ALIGNMENT);
        panel.add(Box.createVerticalGlue());
        add();
        JButton btn = new JButton("Submit");
        btn.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                submit();
                addIngredientPopup.this.dispose();
                
            }});

        this.getContentPane().add(scrollPane);
        setContentPane(scrollPane);
    }

    private void add () {
        try {
            PreparedStatement ps = DataBase.getCon().prepareStatement("SELECT name FROM ingredient WHERE ingredientid=?;");
            ps.setInt(1, ingredientId);
            ResultSet rs = ps.executeQuery();
            String name = rs.getString("name");
            JLabel label = new JLabel(name);
            panel.add(label);
            label = new JLabel("Quantity:");
            panel.add(label);
            quantity = textArea("");
            unitMenu();
        } catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

    }

    private void submit () {
        String qty = quantity.getText();
        if (!qty.equals("")) {
            int qtyInt = Integer.parseInt(qty);
            try {
                PreparedStatement ps = DataBase.getCon().prepareStatement(
                "INSERT INTO recipe_requires VALUES(?, ?, ?, ?);");
                ps.setInt(1, recipeId);
                ps.setInt(2, ingredientId);
                ps.setInt(3, qtyInt);
                ps.setString(4, unit);
                ps.executeUpdate();
            } catch (SQLException e) {
                e.printStackTrace();
            }

        }
    }

    private void unitMenu () {
        JMenuBar menu = new JMenuBar();
        JMenu unit = new JMenu("Unit (Optional)");
        menu.add(unit);
        panel.add(menu);
        ButtonGroup group = new ButtonGroup();
        JRadioButtonMenuItem rbMenuItem = new JRadioButtonMenuItem("Easy");
        unitClick(rbMenuItem);
        group.add(rbMenuItem);
        unit.add(rbMenuItem);
        rbMenuItem = new JRadioButtonMenuItem("oz");
        unitClick(rbMenuItem);
        group.add(rbMenuItem);
        unit.add(rbMenuItem);
        rbMenuItem = new JRadioButtonMenuItem("fl oz");
        unitClick(rbMenuItem);
        group.add(rbMenuItem);
        unit.add(rbMenuItem);
        rbMenuItem = new JRadioButtonMenuItem("grams");
        unitClick(rbMenuItem);
        group.add(rbMenuItem);
        unit.add(rbMenuItem);
    }

    private void unitClick (JRadioButtonMenuItem m) {
        m.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                String text = m.getText();
                if (text.equals("oz")) {
                    unit = "oz";
                }
                else if (text.equals("fl oz")) {
                    unit = "fl oz";
                }
                else if (text.equals("grams")) {
                    unit = "grams";
                }
                
            }

        });
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
    
}
