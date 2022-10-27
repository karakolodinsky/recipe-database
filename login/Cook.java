package login;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;

public class Cook extends JFrame implements ActionListener{

    //Initializing Components
    JLabel lb, lb1, lb2, lb3, lb4, lb5;
    JTextField tf1, tf2, tf3, tf4, tf5;
    JButton btn;
    //Creating Constructor for initializing JFrame components
    Cook() {
        //Providing Title
        super("Browse");
        lb5 = new JLabel("Search:");
        lb5.setBounds(20, 20, 100, 20);
        tf5 = new JTextField(20);
        tf5.setBounds(130, 20, 200, 20);
        btn = new JButton("Submit");
        btn.setBounds(50, 50, 100, 20);
        btn.addActionListener(this);
        setVisible(true);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(500, 500);
        lb1 = new JLabel("U_Name:");
        lb1.setBounds(20, 120, 100, 20);
        tf1 = new JTextField(50);
        tf1.setBounds(130, 120, 200, 20);
        setLayout(null);
        //Add components to the JFrame
        add(lb5);
        add(tf5);
        add(btn);
        add(lb1);
        add(tf1);
        //Set TextField Editable False
        tf1.setEditable(false);
    }
    public void actionPerformed(ActionEvent e) {
        //Create DataBase Coonection and Fetching Records
        try {
            String str = tf5.getText();
            PreparedStatement st = DataBase.con.prepareStatement("select * from recipe");
            st.setString(1, str);
            //Excuting Query
            ResultSet rs = st.executeQuery();
            if (rs.next()) {
                String s = rs.getString(1);
                String s1 = rs.getString(2);
                String s2 = rs.getString(3);
                String s3 = rs.getString(4);
                //Sets Records in TextFields.
                tf1.setText(s);
                tf2.setText(s1);
                tf3.setText(s2);
                tf4.setText(s3);
            } else {
                JOptionPane.showMessageDialog(null, "Name not Found");
            }
            //Create Exception Handler
        } catch (Exception ex) {
            System.out.println(ex);
        }
    }
    //Running Constructor
    public static void main(String args[]) {
        EventQueue.invokeLater(new Runnable() {
            public void run() {
                try {
                    Browse frame = new Browse();
                    frame.setVisible(true);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }


}
