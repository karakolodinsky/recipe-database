package login;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.sql.ResultSet;

import javax.swing.*;
import javax.swing.border.EmptyBorder;

import net.proteanit.sql.DbUtils;

public class Pantry extends JFrame {

    private static final long serialVersionUID = 1;
    /**
     * UserHome window width
     */
    public static int WIDTH_FRAME = 960;

    /**
     * UserHome window height
     */
    public static int HEIGHT_FRAME = 960;

    /**
     * frame edge
     */
    public static Insets INSETS;

    private JMenuBar menuBar_menubar;
    private JPanel contentPane;

    public Pantry(){
        super("User Pantry");
        setResizable(false);
        setLayout(null);
        setSize(WIDTH_FRAME, HEIGHT_FRAME);
        setLocationRelativeTo(null);
        setLocation(getX() - 80, getY() - 80);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setVisible(true);

        init();
    }

    private void init(){
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setBounds(0, 0, 1000, 600);
        setResizable(false);
        contentPane = new JPanel();
        contentPane.setBackground(new Color(168, 240, 224));
        contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
        setContentPane(contentPane);
        contentPane.setLayout(null);


        JLabel addJLabel = new JLabel(UserLogin.getUsername()+"'s Pantry");
        addJLabel.setBackground(new Color(181, 151, 207));
        addJLabel.setForeground(new Color(181, 151, 207));
        addJLabel.setFont(new Font("80er Teenie Demo", Font.BOLD, 30));
        addJLabel.setBounds(500, 25, 200, 40);
        contentPane.add(addJLabel);


        JButton addButton = new JButton("Add to Pantry");
        addButton.setFont(new Font("80er Teenie Demo", Font.BOLD, 26));
        addButton.setBounds(20, 20, 200, 50);
        contentPane.add(addButton);
        addButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                    AddToPantry Add = new AddToPantry();
                    Add.setVisible(true);
                }
            
        });
        contentPane.add(addButton);

        JButton updButton = new JButton("Update Pantry");
        updButton.setFont(new Font("80er Teenie Demo", Font.BOLD, 26));
        updButton.setBounds(750,500 , 200, 50);
        contentPane.add(updButton);

        JLabel iJLabel = new JLabel("Ingredient Name");
        iJLabel.setBackground(new Color(181, 151, 207));
        iJLabel.setForeground(new Color(181, 151, 207));
        iJLabel.setFont(new Font("80er Teenie Demo", Font.BOLD, 20));
        iJLabel.setBounds(100, 90, 200, 40);
        contentPane.add(iJLabel);

        JLabel pJLabel = new JLabel("Purchase Date");
        pJLabel.setBackground(new Color(181, 151, 207));
        pJLabel.setForeground(new Color(181, 151, 207));
        pJLabel.setFont(new Font("80er Teenie Demo", Font.BOLD, 20));
        pJLabel.setBounds(250, 90, 200, 40);
        contentPane.add(pJLabel);

        JLabel eJLabel = new JLabel("Expiration Date");
        eJLabel.setBackground(new Color(181, 151, 207));
        eJLabel.setForeground(new Color(181, 151, 207));
        eJLabel.setFont(new Font("80er Teenie Demo", Font.BOLD, 20));
        eJLabel.setBounds(375, 90, 200, 40);
        contentPane.add(eJLabel);

        JLabel qJLabel = new JLabel("Current Quantity");
        qJLabel.setBackground(new Color(181, 151, 207));
        qJLabel.setForeground(new Color(181, 151, 207));
        qJLabel.setFont(new Font("80er Teenie Demo", Font.BOLD, 20));
        qJLabel.setBounds(515, 90, 200, 40);
        contentPane.add(qJLabel);

        JLabel bqJLabel = new JLabel("Bought Quantity");
        bqJLabel.setBackground(new Color(181, 151, 207));
        bqJLabel.setForeground(new Color(181, 151, 207));
        bqJLabel.setFont(new Font("80er Teenie Demo", Font.BOLD, 20));
        bqJLabel.setBounds(675, 90, 200, 40);
        contentPane.add(bqJLabel);

        JLabel uJLabel = new JLabel("Unit");
        uJLabel.setBackground(new Color(181, 151, 207));
        uJLabel.setForeground(new Color(181, 151, 207));
        uJLabel.setFont(new Font("80er Teenie Demo", Font.BOLD, 20));
        uJLabel.setBounds(825, 90, 200, 40);
        contentPane.add(uJLabel);




        JTable PantTbl = new JTable();
        PantTbl.setBounds(100, 125, 775, 350);
        String user = UserLogin.getUsername();
        ResultSet rs;
        try {
                rs = DataBase.GetPantry(user);
                PantTbl.setModel(DbUtils.resultSetToTableModel(rs));
        } catch (IOException e1) {
                // TODO Auto-generated catch block
                e1.printStackTrace();
        }

        
        contentPane.add(PantTbl);
        


        

        setContentPane(contentPane);


    }

    /**
     * Launch the application.
     */
    public static void main(String[] args) {
        EventQueue.invokeLater(new Runnable() {
            public void run() {
                try {
                    Pantry frame = new Pantry();
                    frame.setVisible(true);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

}