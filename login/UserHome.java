package login;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.*;
import javax.swing.border.EmptyBorder;

public class UserHome extends JFrame {

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

    public UserHome(String usersess){
        super("UserHome");
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
        contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
        setContentPane(contentPane);
        contentPane.setLayout(null);
        JButton btnNewButton = new JButton("Logout");
        btnNewButton.setForeground(new Color(0, 0, 0));
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
        btnNewButton.setBounds(20, 20, 200, 30);
        contentPane.add(btnNewButton);
        JButton browseButton = new JButton("Browse Recipes");
        browseButton.setForeground(new Color(0, 0, 0));
        browseButton.setBackground(UIManager.getColor("Button.disabledForeground"));
        browseButton.setFont(new Font("Tahoma", Font.PLAIN, 15));
        browseButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                    dispose();
                    Browse browse = new Browse();
                    //browse.setTitle("Browse Recipes");
                    browse.setVisible(true);
                }
            
        });
        browseButton.setBounds(20, 120, 200, 30);
        contentPane.add(browseButton);
        // JButton button = new JButton("Change-password\r\n");
        // button.setBackground(UIManager.getColor("Button.disabledForeground"));
        // button.addActionListener(new ActionListener() {
         //   public void actionPerformed(ActionEvent e) {
                // ChangePassword bo = new ChangePassword(userSes);
                // bo.setTitle("Change Password");
                // bo.setVisible(true);

         //   }
        // });
        // button.setFont(new Font("Tahoma", Font.PLAIN, 35));
        // button.setBounds(247, 320, 491, 114);
        // contentPane.add(button);
        



    }

    /**
     * Launch the application.
     */
    public static void main(String[] args) {
        EventQueue.invokeLater(new Runnable() {
            public void run() {
                try {
                    UserHome frame = new UserHome("user");
                    frame.setVisible(true);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

}