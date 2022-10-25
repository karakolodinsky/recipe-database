package login;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.*;
import javax.swing.border.EmptyBorder;

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
        contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
        setContentPane(contentPane);
        contentPane.setLayout(null);
        JButton addButton = new JButton("Add to Pantry");
        addButton.setForeground(new Color(0, 0, 0));
        addButton.setBackground(UIManager.getColor("Button.disabledForeground"));
        addButton.setFont(new Font("Tahoma", Font.PLAIN, 15));
        addButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                    AddToPantry Add = new AddToPantry();
                    //browse.setTitle("Browse Recipes");
                    Add.setVisible(true);
                }
            
        });
        addButton.setBounds(20, 120, 200, 30);
        contentPane.add(addButton);
        



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