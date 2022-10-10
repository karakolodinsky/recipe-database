package login;

import java.awt.Color;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;
import javax.swing.text.AbstractDocument.Content;

import com.jcraft.jsch.*;

public class UserLogin extends JFrame {

    private static final long serialVersionUID = 1L;
    private JTextField textField;
    private JPasswordField passwordField;
    private JButton btnNewButton;
    private JLabel label;
    private JPanel contentPane;
    String usernameSQL = "ktk4111";
    String passwordSQL = "tunaSalad103!";
    String tunnelHost = "starbug.cs.rit.edu";
    String psqlHost = "localhost";
    int psqlPort = 5432; 
    int tunnelPort = 1001;
    String dbUrl = "jdbc:postgresql://localhost:" + tunnelPort + "/" + "p32002_31";

    /**
     * Launch the application.
     */
    public static void main(String[] args) {
        EventQueue.invokeLater(new Runnable() {
            public void run() {
                try {
                        Class.forName("org.postgresql.Driver");  
                        
                        String tunnelHost = "starbug.cs.rit.edu";
                        String psqlHost = "localhost";
                        int psqlPort = 5432; 
                        int tunnelPort = 1001;
                        String usernameSQL = "ktk4111";
                        String passwordSQL = "tunaSalad103!"; 
                        JSch jsch = new JSch();

                        Session session = jsch.getSession(usernameSQL, tunnelHost, 22);
                        session.setPassword(passwordSQL);
                        session.setConfig("StrictHostKeyChecking", "no");
                        session.connect();

                        System.out.println("Connected Successfully");
                        session.setPortForwardingL(tunnelPort, psqlHost, psqlPort);
                        System.out.println("Port Forwarded");

                        System.out.println("connect DB success");
                    UserLogin frame = new UserLogin();
                    frame.setVisible(true);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    /**
     * Create the frame.
     */
    public UserLogin() {
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setBounds(0, 0, 1000, 600);
        setResizable(false);
        contentPane = new JPanel();
        contentPane.setBackground(new Color(168, 240, 224));
        contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
        setContentPane(contentPane);
        contentPane.setLayout(null);

        JLabel lblNewLabel = new JLabel("too many chefs in the kitchen");
        lblNewLabel.setForeground(new Color(181, 151, 207));
        lblNewLabel.setFont(new Font("80er Teenie Demo", Font.BOLD, 60));
        lblNewLabel.setBounds(100, 0, 1000, 70);
        contentPane.add(lblNewLabel);

        JLabel lblNewLabelcred = new JLabel("<html>for RIT CSCI-330<br/>by team error 418</html>");
        lblNewLabelcred.setForeground(new Color(181, 151, 207));
        lblNewLabelcred.setFont(new Font("80er Teenie Demo", Font.BOLD, 20));
        lblNewLabelcred.setBounds(100, 400, 1000, 70);
        contentPane.add(lblNewLabelcred);

        textField = new JTextField();
        textField.setFont(new Font("Juice ITC", Font.PLAIN, 32));
        textField.setBounds(481, 170, 281, 68);
        contentPane.add(textField);
        textField.setColumns(10);

        passwordField = new JPasswordField();
        passwordField.setFont(new Font("Juice ITC", Font.PLAIN, 32));
        passwordField.setBounds(481, 286, 281, 68);
        contentPane.add(passwordField);

        JLabel lblUsername = new JLabel("username");
        lblUsername.setBackground(new Color(181, 151, 207));
        lblUsername.setForeground(new Color(181, 151, 207));
        lblUsername.setFont(new Font("80er Teenie Demo", Font.BOLD, 31));
        lblUsername.setBounds(250, 166, 193, 52);
        contentPane.add(lblUsername);

        JLabel lblPassword = new JLabel("password");
        lblPassword.setForeground(new Color(181, 151, 207));
        lblPassword.setBackground(new Color(181, 151, 207));
        lblPassword.setFont(new Font("80er Teenie Demo", Font.BOLD, 31));
        lblPassword.setBounds(250, 286, 193, 52);
        contentPane.add(lblPassword);

        btnNewButton = new JButton("login");
        btnNewButton.setFont(new Font("80er Teenie Demo", Font.BOLD, 26));
        btnNewButton.setBounds(545, 392, 162, 73);
        btnNewButton.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                String userName = textField.getText();
                String password = passwordField.getText();
                try {
                        Connection con = DriverManager.getConnection(dbUrl, usernameSQL, passwordSQL);
                    PreparedStatement st = (PreparedStatement) con
                        .prepareStatement("Select username, passwordhash from netizen where username=? and passwordhash=?");

                    st.setString(1, userName);
                    st.setString(2, password);
                    ResultSet rs = st.executeQuery();
                    if (rs.next()) {
                        dispose();
                        UserHome ah = new UserHome(userName);
                        ah.setTitle("Welcome");
                        ah.setVisible(true);
                        JOptionPane.showMessageDialog(btnNewButton, "You have successfully logged in");
                    } else {
                        JOptionPane.showMessageDialog(btnNewButton, "Wrong Username & Password");
                    }
                } catch (SQLException sqlException) {
                    sqlException.printStackTrace();
                } 
                }
        });

        contentPane.add(btnNewButton);

        label = new JLabel("");
        label.setBounds(0, 0, 1008, 562);
        contentPane.add(label);
    }}
