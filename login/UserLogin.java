package login;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.text.AbstractDocument.Content;


public class UserLogin extends JFrame {

    private static final long serialVersionUID = 1L;

    /**
     * login window width
     */
    public static final int WIDTH_FRAME = 540;

    /**
     * login window height
     */
    public static final int HEIGHT_FRAME = 360;

    /**
     * wrong password or username error message
     */
    private final String errorText = "Wrong username or password";


    private JTextField usernameField;
    private JPasswordField passwordField;
    private JButton btnNewButton;
    private JLabel label_errorText;
    private JPanel contentPane;
    private Insets insets;


    public UserLogin() {
        super("UserLogin");
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

        usernameField = new JTextField();
        usernameField.setFont(new Font("Juice ITC", Font.PLAIN, 32));
        usernameField.setBounds(481, 170, 281, 68);
        contentPane.add(usernameField);
        usernameField.setColumns(10);

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
            @Override
            public void actionPerformed(ActionEvent e) {

                if (usernameField.getText().equals("") || String.valueOf(passwordField.getPassword()).equals("")) {
                    label_errorText.setText(errorText);

                } else {

                    label_errorText.setText("");
                    if (DataBase.verifyLogin(usernameField.getText(),
                            String.valueOf(passwordField.getPassword())) != -1) {

						/*JOptionPane.showMessageDialog(contentPane, "Login successful. Welcome", "Login",
								JOptionPane.INFORMATION_MESSAGE);*/

                        EventQueue.invokeLater(new Runnable() {
                            @Override
                            public void run() {
                                UserLogin.this.dispose();
                                new UserHome("user");
                            }
                        });

                    } else {
                        label_errorText.setText(errorText);
                    }

                }

            }
        });
        contentPane.add(btnNewButton);

        label_errorText = new JLabel();
        label_errorText.setForeground(Color.RED);
        label_errorText.setBounds(btnNewButton.getX() - 45, btnNewButton.getY() + 30,
                170, 30);
        label_errorText.setFont(new Font("Tahoma", Font.PLAIN + Font.BOLD, 11));
        contentPane.add(label_errorText);

        setContentPane(contentPane);

    }
    public static void main(String[] args) {
        EventQueue.invokeLater(new Runnable() {

            @Override
            public void run() {

                new UserLogin();

            }
        });

    }
}