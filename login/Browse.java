package login;
import javax.swing.*;
import java.awt.*;  
import java.awt.event.*;
import java.sql.*;  

public class Browse extends JFrame {

    static private String netizenUsername;

    static private String user;

    /**
     * login window width
     */
    public static final int WIDTH_FRAME = 540;

    /**
     * login window height
     */
    public static final int HEIGHT_FRAME = 360;

    //Initializing Components  
    GridBagLayout bag = new GridBagLayout();
    GridBagConstraints bagConstraints = new GridBagConstraints();
    JLabel lb, lb1, lb2, lb3, lb4, lb5;  
    JTextField tf1, tf2, tf3, tf4, tf5;  
    JButton btn;  
    //Creating Constructor for initializing JFrame components  

    public Browse(String user) {  
        //Providing Title  
        super("Browse"); 
        this.user = user; 
        setResizable(false);
        setSize(WIDTH_FRAME, HEIGHT_FRAME);
        setLocationRelativeTo(null);
        setLocation(getX() - 80, getY() - 80);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setVisible(true);

        init();
    }  

    private void init (){
        setup();
        JButton home = returnHome();
    }

    private GridBagConstraints setup () {
        setLayout(bag);
        bagConstraints.insets = new Insets(10, 10, 10, 10);
        JLabel search = new JLabel("Search: ");
        bagConstraints.gridx = 0;
        bagConstraints.gridy = 0;
        add(search, bagConstraints);

        JTextField tf1 = new JTextField(15);
        bagConstraints.gridx = 1;
        bagConstraints.gridy = 0;
        add(tf1, bagConstraints);

        JButton submit = new JButton("Submit");
        bagConstraints.gridx = 1;
        bagConstraints.gridy = 1;
        add(submit, bagConstraints);

        JMenuBar menu = new JMenuBar();
        JMenu searchBy = new JMenu("Search By");
        JMenuItem m1 = new JMenuItem("Name");
        JMenuItem m2 = new JMenuItem("Ingredient");
        JMenuItem m3 = new JMenuItem("Category");
        menuClick(m1, searchBy);
        menuClick(m2, searchBy);
        menuClick(m3, searchBy);
        searchBy.add(m3);
        searchBy.add(m2);
        searchBy.add(m1);
        menu.add(searchBy);
        bagConstraints.gridx = 2;
        bagConstraints.gridy = 0;
        add(menu, bagConstraints);


        validate();

        return bagConstraints;
    }

    private void menuClick (JMenuItem m, JMenu searchBy) {
        m.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                String text = m.getText();
                searchBy.setText(text); 
            }

        });
    }

    private JButton returnHome () {
        JButton home = new JButton("Home");
        home.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                EventQueue.invokeLater(new Runnable() {
                    @Override
                    public void run() {
                        Browse.this.dispose();
                        new UserHome(user);
                    }
                });
            }
        });
        return home;
    }

    //Running Constructor  
    public static void main(String args[]) {  
        EventQueue.invokeLater(new Runnable() {
            public void run() {
                new Browse("test");
            }
        }); 
    }

    
}
