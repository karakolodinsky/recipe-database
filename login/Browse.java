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
    public static final int WIDTH_FRAME = 800;

    /**
     * login window height
     */
    public static final int HEIGHT_FRAME = 800;

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

    private void setup () {
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
        bagConstraints.gridx = 5;
        bagConstraints.gridy = 0;
        add(submit, bagConstraints);

        JMenuBar menu = new JMenuBar();
        JMenu searchBy = new JMenu("Search By:");
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

        JMenuBar menu2 = new JMenuBar();
        JMenu sortBy = new JMenu("Sort By: ");
        JMenuItem m21 = new JMenuItem("Rating");
        JMenuItem m22 = new JMenuItem("Date");
        JMenuItem m23 = new JMenuItem("Name");
        menuClick(m21, sortBy);
        menuClick(m22, sortBy);
        menuClick(m23, sortBy);
        sortBy.add(m23);
        sortBy.add(m22);
        sortBy.add(m21);
        menu2.add(sortBy);
        bagConstraints.gridx = 3;
        bagConstraints.gridy = 0;
        add(menu2, bagConstraints);

        JMenuBar menu3 = new JMenuBar();
        JMenu order = new JMenu("Order: ");
        JMenuItem m31 = new JMenuItem("Ascending");
        JMenuItem m32 = new JMenuItem("Descending");
        menuClick(m31, sortBy);
        menuClick(m32, sortBy);
        order.add(m32);
        order.add(m31);
        menu3.add(order);
        bagConstraints.gridx = 4;
        bagConstraints.gridy = 0;
        add(menu3, bagConstraints);


        validate();

    }

    private void menuClick (JMenuItem m, JMenu menu) {
        m.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                String text = m.getText();
                menu.setText(text); 
            }

        });
    }

    public void submitClick (JButton submit, JMenu search, JMenu sort, JMenu order) {
        String searchChoice = search.getText();
        if (searchChoice.equals("Sort by:") || searchChoice.equals("Name:")){
            searchChoice = "name";
        }
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
