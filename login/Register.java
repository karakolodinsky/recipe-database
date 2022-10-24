package login;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;

import javax.swing.*;
import javax.swing.border.EmptyBorder;

public class Register extends JFrame {

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
    private JTextField usernameField;
    private JPasswordField passwordField;
    private JLabel label_errorText;
    private JButton btnNewButton;
    private JButton btnNewButton2;



    public Register(){
        super("Register");
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
        setContentPane(contentPane);
        contentPane.setLayout(null);


        JButton btnNewButton = new JButton("Return to login");

        btnNewButton.setBackground(UIManager.getColor("Button.disabledForeground"));
        btnNewButton.setFont(new Font("Tahoma", Font.PLAIN, 15));
        btnNewButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                int a = JOptionPane.showConfirmDialog(btnNewButton, "Are you sure?");
                // JOptionPane.setRootFrame(null);
                if (a == JOptionPane.YES_OPTION) {
                    dispose();
                    UserLogin obj = new UserLogin();
                    obj.setTitle("Login");
                    obj.setVisible(true);
                }
                else {

                }
            }
        });

        
        btnNewButton.setBounds(0, 0, 150, 100);
        contentPane.add(btnNewButton);


        label_errorText = new JLabel();
        label_errorText.setForeground(Color.RED);
        label_errorText.setBounds(btnNewButton.getX() - 45, btnNewButton.getY() + 30,
                170, 30);
        label_errorText.setFont(new Font("Tahoma", Font.PLAIN + Font.BOLD, 11));
        contentPane.add(label_errorText);
        usernameField = new JTextField();
        usernameField.setFont(new Font("Juice ITC", Font.PLAIN, 32));
        usernameField.setBounds(481, 170, 281, 68);
        contentPane.add(usernameField);
        usernameField.setColumns(10);

        JLabel lblUsername = new JLabel("username");
        lblUsername.setBackground(new Color(181, 151, 207));
        lblUsername.setForeground(new Color(181, 151, 207));
        lblUsername.setFont(new Font("80er Teenie Demo", Font.BOLD, 31));
        lblUsername.setBounds(250, 166, 193, 52);
        contentPane.add(lblUsername);

        JLabel lblREG = new JLabel("REGISTER A NEW ACCOUNT");
        lblREG.setForeground(new Color(181, 151, 207));
        lblREG.setBackground(new Color(181, 151, 207));
        lblREG.setFont(new Font("80er Teenie Demo", Font.BOLD, 31));
        lblREG.setBounds(300, 0, 800, 52);
        contentPane.add(lblREG);


        JLabel lblPassword = new JLabel("password");
        lblPassword.setForeground(new Color(181, 151, 207));
        lblPassword.setBackground(new Color(181, 151, 207));
        lblPassword.setFont(new Font("80er Teenie Demo", Font.BOLD, 31));
        lblPassword.setBounds(250, 286, 193, 52);
        contentPane.add(lblPassword);

        passwordField = new JPasswordField();
        passwordField.setFont(new Font("Juice ITC", Font.PLAIN, 32));
        passwordField.setBounds(481, 286, 281, 68);
        contentPane.add(passwordField);


        btnNewButton2 = new JButton("register");
        btnNewButton2.setFont(new Font("80er Teenie Demo", Font.BOLD, 26));
        btnNewButton2.setBounds(545, 392, 162, 73);
        btnNewButton2.addActionListener(new ActionListener() {
             @Override
             public void actionPerformed(ActionEvent e) {

                 if (usernameField.getText().equals("") || String.valueOf(passwordField.getPassword()).equals("")) {
                     label_errorText.setText("Nothing has been typed.");

                 } else {

                     label_errorText.setText("");
                    try {
                        if (DataBase.createUser(usernameField.getText(),
                                String.valueOf(passwordField.getPassword())) != -1) {

                        	JOptionPane.showMessageDialog(contentPane, "Register successful. Welcome", "Login",
                        			JOptionPane.INFORMATION_MESSAGE);

                            EventQueue.invokeLater(new Runnable() {
                                @Override
                                public void run() {
                                    Register.this.dispose();
                                    new UserHome("user");
}
                            });

            } else {
                        label_errorText.setText("Username is already taken.");
                        JOptionPane.showMessageDialog(contentPane, "Username is taken.", "Error",
                        			JOptionPane.INFORMATION_MESSAGE);
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
    contentPane.add(btnNewButton2);
    }

    /**
     * Launch the application.
     */
    public static void main(String[] args) {
        EventQueue.invokeLater(new Runnable() {
            public void run() {
                try {
                    Register frame = new Register();
                    frame.setVisible(true);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }); 

        }
}