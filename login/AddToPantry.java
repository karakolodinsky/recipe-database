package login;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import net.proteanit.sql.DbUtils;
import java.sql.*;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.util.Locale;
import java.util.Properties;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import javax.swing.text.DateFormatter;
import javax.swing.text.AbstractDocument.Content;
import java.util.Date;

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

        JLabel iJLabel = new JLabel("Ingredient Name");
        iJLabel.setVisible(false);
        iJLabel.setBackground(new Color(181, 151, 207));
        iJLabel.setForeground(new Color(181, 151, 207));
        iJLabel.setFont(new Font("80er Teenie Demo", Font.BOLD, 20));
        iJLabel.setBounds(350, 50, 200, 40);
        contentPane.add(iJLabel);

        JLabel pJLabel = new JLabel("Purchase Date");
        pJLabel.setVisible(false);
        pJLabel.setBackground(new Color(181, 151, 207));
        pJLabel.setForeground(new Color(181, 151, 207));
        pJLabel.setFont(new Font("80er Teenie Demo", Font.BOLD, 20));
        pJLabel.setBounds(375, 100, 200, 40);
        contentPane.add(pJLabel);

        JLabel eJLabel = new JLabel("Expiration Date");
        eJLabel.setBackground(new Color(181, 151, 207));
        eJLabel.setVisible(false);
        eJLabel.setForeground(new Color(181, 151, 207));
        eJLabel.setFont(new Font("80er Teenie Demo", Font.BOLD, 20));
        eJLabel.setBounds(360, 150, 200, 40);
        contentPane.add(eJLabel);



        JLabel bqJLabel = new JLabel("Bought Quantity");
        bqJLabel.setBackground(new Color(181, 151, 207));
        bqJLabel.setVisible(false);
        bqJLabel.setForeground(new Color(181, 151, 207));
        bqJLabel.setFont(new Font("80er Teenie Demo", Font.BOLD, 20));
        bqJLabel.setBounds(350, 200, 200, 40);
        contentPane.add(bqJLabel);

        JLabel uJLabel = new JLabel("Unit of Measurement");
        uJLabel.setBackground(new Color(181, 151, 207));
        uJLabel.setVisible(false);
        uJLabel.setForeground(new Color(181, 151, 207));
        uJLabel.setFont(new Font("80er Teenie Demo", Font.BOLD, 20));
        uJLabel.setBounds(320, 250, 200, 40);
        contentPane.add(uJLabel);

        String[] unit = { "grams","ounces", "fl oz", "item name"};
        JComboBox<String> units = new JComboBox<String>(unit);
        units.setBounds(500, 250, 200, 30);
        units.setVisible(false);
        contentPane.add(units);
        


        JTextField SIngredientField = new JTextField();
        SIngredientField.setFont(new Font("Juice ITC", Font.PLAIN, 20));
        SIngredientField.setBounds(500, 50, 200, 30);
        SIngredientField.setColumns(10);
       SIngredientField.setVisible(false);
        SIngredientField.setEditable(false);
        contentPane.add(SIngredientField);

        // UtilDateModel model = new UtilDateModel();
        // model.setDate(2022,11,1);
        // Properties p = new Properties();
        // p.put("text.today", "Today");
        // p.put("text.month", "Month");
        // p.put("text.year", "Year");
        // DatePicker datePicker = new DatePicker();
        // datePicker.setBounds(500, 100, 200, 30);
        // datePicker.setVisible(false);
        DateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        DateFormatter df = new DateFormatter(format);
        JFormattedTextField dateField = new JFormattedTextField(df);
        dateField.setBounds(500, 100, 200, 30);
        dateField.setValue(new Date());
        dateField.setVisible(false);
        contentPane.add(dateField);

        DateFormat format2 = new SimpleDateFormat("yyyy-MM-dd");
        DateFormatter df2 = new DateFormatter(format2);
        JFormattedTextField dateField2 = new JFormattedTextField(df2);
        dateField2.setBounds(500, 150, 200, 30);
        dateField2.setValue(new Date());
        dateField2.setVisible(false);
        contentPane.add(dateField2);


        // UtilDateModel model2 = new UtilDateModel();
        // model2.setDate(2022,11,1);
        // Properties p2 = new Properties();
        // p2.put("text.today", "Today");
        // p2.put("text.month", "Month");
        // p2.put("text.year", "Year");
        // JDatePanelImpl datePanel2 = new JDatePanelImpl(model2, p2);
        // JDatePickerImpl datePicker2 = new JDatePickerImpl(datePanel2, new DateComponentFormatter());
        // datePicker2.setBounds(500, 150, 200, 30);
        // datePicker2.setVisible(false);
        // contentPane.add(datePicker2);

        JTextField QuantBoughtField = new JTextField();
        QuantBoughtField.setFont(new Font("Juice ITC", Font.PLAIN, 20));
        QuantBoughtField.setBounds(500, 200, 200, 30);
        QuantBoughtField.setColumns(10);
        QuantBoughtField.setVisible(false);
        contentPane.add(QuantBoughtField);

        // JTextField SIngredientField = new JTextField();
        // SIngredientField.setFont(new Font("Juice ITC", Font.PLAIN, 20));
        // SIngredientField.setBounds(500, 140, 200, 30);
        // SIngredientField.setColumns(10);
        // SIngredientField.setVisible(false);
        // SIngredientField.setEditable(false);
        // contentPane.add(SIngredientField);
        


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
        SearchTbl.setVisible(false);
        SearchTbl.setBounds(20, 135, 200, 195);
        SearchTbl.setFocusable(false);
        SearchTbl.setRowSelectionAllowed(true);
        SearchTbl.addMouseListener(new java.awt.event.MouseAdapter() {
                @Override
                public void mouseClicked(java.awt.event.MouseEvent evt) {
                    int row = SearchTbl.rowAtPoint(evt.getPoint());
                    String ing = (String) SearchTbl.getValueAt(row, 0);
                    SIngredientField.setText(ing);
                    SIngredientField.setVisible(true);
                    QuantBoughtField.setVisible(true);
                    iJLabel.setVisible(true);
                    uJLabel.setVisible(true);
                    bqJLabel.setVisible(true);
                    uJLabel.setVisible(true);
                    eJLabel.setVisible(true);
                    units.setVisible(true);
                    pJLabel.setVisible(true);
                    dateField.setVisible(true);
                    dateField2.setVisible(true);


                }
            });

        contentPane.add(SearchTbl);

        SearchTbl.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);

        // JScrollPane pane = new JScrollPane(SearchTbl);
        // contentPane.add(pane);
        

        // JScrollPane js = new JScrollPane(SearchTbl);
        // Dimension ingDim = new Dimension(200, 175 );
        // js.setPreferredSize( ingDim );
        // js.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        // contentPane.add(js);

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
                                        SearchTbl.setVisible(true);
                                        SearchTbl.setDefaultEditor(Object.class, null);
           
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

        JButton AddButton = new JButton("Add to Pantry");
        AddButton.setVisible(true);
        AddButton.setFont(new Font("80er Teenie Demo", Font.BOLD, 26));
        AddButton.setBounds(500, 350, 200, 50);
        AddButton.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                        if (SIngredientField.getText().equals("") && QuantBoughtField.getText().equals("")) {
                                label_errorText.setText("Nothing has been typed."); 
                                
           
                            } else {
           
                               try {
                                java.util.Date date = format.parse(dateField.getText());
                                System.out.println(date.toString());
                                java.sql.Date sqlStartDate = new java.sql.Date(date.getTime());
                                System.out.println(sqlStartDate.toString());  
                                java.util.Date date2 = format.parse(dateField2.getText());
                                java.sql.Date sqlStartDate2 = new java.sql.Date(date2.getTime()); 
                
                                if (DataBase.addtoPantry(SIngredientField.getText(), UserLogin.getUsername(), Integer.parseInt(QuantBoughtField.getText()),sqlStartDate, sqlStartDate2, Integer.parseInt(QuantBoughtField.getText()), String.valueOf(units.getSelectedItem()) ) != -1) {
                                        
                                        JOptionPane.showMessageDialog(contentPane, SIngredientField.getText()+ " added to "+ UserLogin.getUsername() + "'s pantry.", "Login",
                                        JOptionPane.INFORMATION_MESSAGE);

                    EventQueue.invokeLater(new Runnable() {
                        @Override
                        public void run() {
                            AddToPantry.this.dispose();
                            new Pantry();
}
                    });
                       } else {
                                   label_errorText.setText("");
                       }
                               } catch (HeadlessException e1) {
                                   // TODO Auto-generated catch block
                                   e1.printStackTrace();
                               } catch (IOException e1) {
                                   // TODO Auto-generated catch block
                                   e1.printStackTrace();
                               } catch (ParseException e1) {
                                // TODO Auto-generated catch block
                                e1.printStackTrace();
                        }
           
                       }
           
                   }
               });
               contentPane.add(AddButton);

               JButton backButton = new JButton("Return to Pantry");
        backButton.setFont(new Font("80er Teenie Demo", Font.BOLD, 15));
        backButton.setBounds(20, 5, 150, 30);
        contentPane.add(backButton);
        backButton.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                        dispose();
                        Pantry userHome = new Pantry();
                        userHome.setTitle("Pantry");
                        userHome.setVisible(true);
                    }
                
            });
        contentPane.add(backButton);


               

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