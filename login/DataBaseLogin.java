package login;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.*;
/**
 * UI-Interface for the database login
 *
 * @author Kara Kolodinsky
 * @author Ainsley Ross
 * @author ?
 */

public class DataBaseLogin extends JFrame{

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

    private JPanel contentPane;
    private JButton db_button_login;
    private JLabel db_label_username, db_label_password, db_label_icon, db_label_errorText;
    private JTextField textField_username;
    private JPasswordField db_passwordField_password;
    private Insets insets;

    public DataBaseLogin() {
        super("DataBaseLogin");
        // add db_icon to login gui
        // setIconImage(Toolkit.getDefaultToolkit().createImage().getScaledInstance("logos\\db_login_icon.png"));
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

    /**
     * main calls login --> login calls gui_layout to set up layout and check login using DataBase.java static functions
     */
    private void init(){
        contentPane = new JPanel();
        contentPane.setLayout(null);
        contentPane.setBounds(insets.left, insets.top, WIDTH_FRAME - insets.left - insets.right,
                HEIGHT_FRAME - insets.bottom - insets.top);

        db_label_username = new JLabel("Username");
        db_label_username.setFont(new Font("Tahoma", Font.PLAIN, 14));
        db_label_username.setBounds(120, 140, 70, 20);
        contentPane.add(db_label_username);

        db_label_password = new JLabel("Password");
        db_label_password.setFont(db_label_username.getFont());
        db_label_password.setBounds(db_label_username.getX(), db_label_username.getY() + 40,
                db_label_username.getWidth(), db_label_username.getHeight());
        contentPane.add(db_label_password);

        textField_username = new JTextField();
        textField_username.setBounds(db_label_username.getX() + db_label_username.getWidth() + 30,
                db_label_username.getY(), 120, db_label_username.getHeight());
        textField_username.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                db_passwordField_password.requestFocus();
            }
        });
        contentPane.add(textField_username);

        db_passwordField_password = new JPasswordField();
        db_passwordField_password.setBounds(textField_username.getX(), db_label_password.getY(),
                120, db_label_password.getHeight());
        db_passwordField_password.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                db_button_login.doClick();
            }
        });
        contentPane.add(db_passwordField_password);

        db_button_login = new JButton("Login");
        db_button_login.setBounds(textField_username.getX() + 20, db_label_username.getY() + 80, 80, 22);
        db_button_login.setFocusPainted(false);
        db_button_login.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                if(textField_username.getText().equals("") || String.valueOf(db_passwordField_password.getPassword()).equals("")) {

                    db_label_errorText.setText(errorText);

                } else {

                    db_label_errorText.setText("");
                    if( DataBase.verifyDBLogin(textField_username.getText(),
                            String.valueOf(db_passwordField_password.getPassword())) != -1 ) {

                        EventQueue.invokeLater(new Runnable() {
                            @Override
                            public void run() {
                                DataBaseLogin.this.dispose();
                                new UserLogin();
                            }
                        });

                    } else {
                        db_label_errorText.setText(errorText);
                    }

                }

            }
        });
        contentPane.add(db_button_login);

        db_label_icon = new JLabel(new ImageIcon("src\\icons\\Login_user_72.png"));
        db_label_icon.setBounds(textField_username.getX() + 20, textField_username.getY() - 100, 72, 72);
        contentPane.add(db_label_icon);

        db_label_errorText = new JLabel();
        db_label_errorText.setForeground(Color.RED);
        db_label_errorText.setBounds(db_button_login.getX() - 45, db_button_login.getY() + 30,
                170, 30);
        db_label_errorText.setFont(new Font("Tahoma", Font.PLAIN + Font.BOLD, 11));
        contentPane.add(db_label_errorText);

        setContentPane(contentPane);

    }

    public static void main(String[] args) {
        EventQueue.invokeLater(new Runnable() {

            @Override
            public void run() {

                new DataBaseLogin();

            }
        });

    }


}