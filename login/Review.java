package login;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.plaf.basic.BasicArrowButton;
import javax.swing.text.LabelView;
import javax.swing.text.MaskFormatter;

public class Review extends JFrame {

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
    private static int recipeId;
    private static double scaleQuant;



    public Review(int recipeId, double scaleQuant){
        super("Review");
        setResizable(false);
        setLayout(null);
        setSize(WIDTH_FRAME, HEIGHT_FRAME);
        setLocationRelativeTo(null);
        setLocation(getX() - 80, getY() - 80);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setVisible(true);
        Review.recipeId = recipeId;
        Review.scaleQuant = scaleQuant;

        init();
    }

    private void init(){
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setBounds(0, 0, 1000, 600);
        setResizable(false);
        contentPane = new JPanel();
        setContentPane(contentPane);
        contentPane.setLayout(null);


        JLabel rev = new JLabel("Review Recipe");
        rev.setBounds(400,0,500,50);
        rev.setFont(new Font("Calibri", Font.BOLD, 26));
        contentPane.add(rev);
        
        JTextArea reviewtxt = new JTextArea();
        reviewtxt.setBounds(300,100,400,200);
        reviewtxt.setFont(new Font("Calibri", Font.BOLD, 26));
        reviewtxt.setLineWrap(true);
        contentPane.add(reviewtxt);

        JLabel starss = new JLabel("Rating");
        starss.setBounds(350,325,100,30);
        starss.setFont(new Font("Calibri", Font.BOLD, 26));
        contentPane.add(starss);

        Integer[] stars = { 0, 1, 2, 3, 4, 5};
        JComboBox<Integer> units = new JComboBox<Integer>(stars);
        units.setBounds(475, 325, 50, 30);
        contentPane.add(units);


        JButton revbtn = new JButton("Leave Review");
        revbtn.setBounds(400,450,200,100);
        revbtn.setFont(new Font("Calibri", Font.BOLD, 26));
        revbtn.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    // todo
                    String reviewText = reviewtxt.getText();
                    int starRating = units.getItemAt(units.getSelectedIndex());
                    int returnValue = DataBase.leaveReview(starRating, reviewText, recipeId, scaleQuant);
                    Review.this.dispose();
           }
       });
       contentPane.add(revbtn);


        
        setContentPane(contentPane);


    }

    /**
     * Launch the application.
     */
    public static void main(String[] args) {
        EventQueue.invokeLater(new Runnable() {
            public void run() {
                try {
                    Review frame = new Review(recipeId, scaleQuant);
                    frame.setVisible(true);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }); 

        }


        protected MaskFormatter createFormatter(String s) {
                MaskFormatter formatter = null;
                try {
                    formatter = new MaskFormatter(s);
                } catch (java.text.ParseException exc) {
                    System.err.println("formatter is bad: " + exc.getMessage());
                    System.exit(-1);
                }
                return formatter;
            }
}