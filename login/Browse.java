package login;
import javax.swing.*;
import java.awt.*;  
import java.awt.event.*;
import java.sql.*;  

/*
 * author: Teagan
 */

public class Browse extends JFrame {

    private String user;

    static private String search = "Name";

    static private String sort = "Name";

    static private String order = "Ascending";

    /**
     * login window width
     */
    public static final int WIDTH_FRAME = 960;

    /**
     * login window height
     */
    public static final int HEIGHT_FRAME = 960;

    //Initializing Components 
    JScrollPane pane = new JScrollPane();
    GridBagLayout bag = new GridBagLayout();
    GridBagConstraints bagConstraints = new GridBagConstraints();
    JScrollPane scroll = new JScrollPane();
    
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
        setLocation(getX(), getY());
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setBounds(0, 0, 1000, 600);
        setVisible(true);

        init();
    }  

    private void init (){
        setup();
        JButton home = returnHome();
        bagConstraints.gridx = 2;
        bagConstraints.gridy = 1;
        add(home, bagConstraints);
        JButton logout = logout();
        bagConstraints.gridx = 3;
        bagConstraints.gridy = 1;
        add(logout, bagConstraints);
    }

    private void setup () {
        setLayout(bag);
        bagConstraints.insets = new Insets(10, 10, 10, 10);
        JLabel search = new JLabel("Search: ");
        bagConstraints.gridx = 0;
        bagConstraints.gridy = 0;
        add(search, bagConstraints);

        tf1 = new JTextField(15);
        bagConstraints.gridx = 1;
        bagConstraints.gridy = 0;
        add(tf1, bagConstraints);

        JButton submit = new JButton("Submit");
        submit.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                submitClick();
                
            }

        });
        bagConstraints.gridx = 1;
        bagConstraints.gridy = 1;
        add(submit, bagConstraints);

        JMenuBar menu = new JMenuBar();
        JMenu searchBy = new JMenu("Search By:");
        menu.add(searchBy);
        bagConstraints.gridx = 2;
        bagConstraints.gridy = 0;
        add(menu, bagConstraints);
        ButtonGroup group = new ButtonGroup();
        JRadioButtonMenuItem rbMenuItem = new JRadioButtonMenuItem("Name");
        rbMenuItem.setSelected(true);
        searchClick(rbMenuItem);
        group.add(rbMenuItem);
        searchBy.add(rbMenuItem);
        rbMenuItem = new JRadioButtonMenuItem("Ingredient");
        searchClick(rbMenuItem);
        group.add(rbMenuItem);
        searchBy.add(rbMenuItem);
        rbMenuItem = new JRadioButtonMenuItem("Category");
        searchClick(rbMenuItem);
        group.add(rbMenuItem);
        searchBy.add(rbMenuItem);

        JMenuBar menu2 = new JMenuBar();
        JMenu sortBy = new JMenu("Sort By: ");
        menu2.add(sortBy);
        bagConstraints.gridx = 3;
        bagConstraints.gridy = 0;
        add(menu2, bagConstraints);
        ButtonGroup group2 = new ButtonGroup();
        JRadioButtonMenuItem rbMenuItem2 = new JRadioButtonMenuItem("Name");
        rbMenuItem2.setSelected(true);
        sortClick(rbMenuItem2);
        group2.add(rbMenuItem2);
        sortBy.add(rbMenuItem2);
        rbMenuItem2 = new JRadioButtonMenuItem("Rating");
        sortClick(rbMenuItem2);
        group2.add(rbMenuItem2);
        sortBy.add(rbMenuItem2);
        rbMenuItem2 = new JRadioButtonMenuItem("Date");
        sortClick(rbMenuItem2);
        group2.add(rbMenuItem2);
        sortBy.add(rbMenuItem2);

        JMenuBar menu3 = new JMenuBar();
        JMenu order = new JMenu("Order: ");
        menu3.add(order);
        ButtonGroup group3 = new ButtonGroup();
        JRadioButtonMenuItem rbMenuItem3 = new JRadioButtonMenuItem("Ascending");
        rbMenuItem3.setSelected(true);
        orderClick(rbMenuItem3);
        group3.add(rbMenuItem3);
        order.add(rbMenuItem3);
        rbMenuItem3 = new JRadioButtonMenuItem("Descending");
        orderClick(rbMenuItem3);
        group3.add(rbMenuItem3);
        order.add(rbMenuItem3);
        bagConstraints.gridx = 4;
        bagConstraints.gridy = 0;
        add(menu3, bagConstraints);
        bagConstraints.gridx = 0;
        bagConstraints.gridy = 3;
        add(scroll, bagConstraints);


        validate();

    }

    private void searchClick (JRadioButtonMenuItem m) {
        m.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                String text = m.getText();
                search = text;
            }

        });
    }

    private void sortClick (JRadioButtonMenuItem m) {
        m.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                String text = m.getText();
                sort = text;
            }

        });
    }

    private void orderClick (JRadioButtonMenuItem m) {
        m.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                String text = m.getText();
                order = text;
            }

        });
    }

    private void submitClick () {
        boolean exec = false;
        PreparedStatement ps;
        Connection con = DataBase.getCon();
        String searchVal = tf1.getText().toString().strip();
        boolean asc = order.equals("Ascending");
        try {
            // no search value -> just browsing
            if (searchVal.equals("")) {
                //sort on name
                if (sort.equals("Name")) {
                    if (asc) {
                        // ps = con.prepareStatement("SELECT r.name, r.recipeId, avg(n.rating) as avgrating " +
                        //                         "FROM recipe r LEFT JOIN netizen_creates n on r.recipeId=n.recipeId " +
                        //                         "group by r.recipeId, r.name ORDER BY r.name;");
                        ps = con.prepareStatement("SELECT * FROM recipepublic ORDER BY name;");
                    }
                    else {
                        ps = con.prepareStatement("SELECT * FROM recipepublic ORDER BY name DESC;");
                    }
                }
                //sort on date
                else if (sort.equals("Date")) {
                    if (asc) {
                        ps = con.prepareStatement("SELECT * FROM recipepublic ORDER BY date;");
                    }
                    else {
                        ps = con.prepareStatement("SELECT * FROM recipepublic ORDER BY date DESC;");
                    }
                }
                else { //sort on rating
                    if (asc) {
                        ps = con.prepareStatement("SELECT * FROM recipepublic ORDER BY avgrating;");
                    }
                    else {
                        ps = con.prepareStatement("SELECT * FROM recipepublic ORDER BY avgrating DESC;");
                    }
                }
            }
            // searched name of recipe
            else if (search.equals("Name")) {
                // sort by name
                if (sort.equals("Name")) {
                    if (asc) {
                        // ps = con.prepareStatement("SELECT r.name, r.recipeId, avg(n.rating) as avgrating " +
                        //                         "FROM recipe r LEFT JOIN netizen_creates n on r.recipeId=n.recipeId " +
                        //                         "WHERE name like ? GROUP BY r.recipeId, r.name ORDER BY r.name;");
                        ps = con.prepareStatement("SELECT * FROM recipepublic WHERE name LIKE ? ORDER BY name;");
                    }
                    else {
                        ps = con.prepareStatement("SELECT * FROM recipepublic WHERE name LIKE ? ORDER BY name DESC;");
                    }
                }
                //sort by date
                else if (sort.equals("Date")) {
                    if (asc) {
                        // ps = con.prepareStatement("SELECT r.name, r.recipeId, avg(n.rating) as avgrating " +
                        //                         "FROM recipe r LEFT JOIN netizen_creates n on r.recipeId=n.recipeId " +
                        //                         "WHERE name like ? GROUP BY r.recipeId, r.name ORDER BY r.date;");
                        ps = con.prepareStatement("SELECT * FROM recipepublic WHERE name LIKE ? ORDER BY date;");
                    }
                    else {
                        ps = con.prepareStatement("SELECT * FROM recipepublic WHERE name LIKE ? ORDER BY date DESC;");
                    }
                }
                else { //sort on rating
                    if (asc) {
                        ps = con.prepareStatement("SELECT * FROM recipepublic WHERE name LIKE ? ORDER BY avgrating;");
                    }
                    else {
                        ps = con.prepareStatement("SELECT * FROM recipepublic WHERE name LIKE ? ORDER BY avgrating;");
                    }
                }
                ps.setString(1, "%" + searchVal + "%");
                
                    

            } // searched for ingredien
            else if (search.equals("Ingredient")) {
                // sort by name
                if (sort.equals("Name")) {
                    if (asc) {
                        ps = con.prepareStatement("select * from recipepublic where recipeid in " + 
                                                "(select distinct recipeId from recipe_requires where ingredientid in " +
                                                    "(select ingredientid from ingredient where name like ?))" +
                                                "order by name;");
                    }
                    else {
                        ps = con.prepareStatement("select * from recipepublic where recipeid in " + 
                                                "(select distinct recipeId from recipe_requires where ingredientid in " +
                                                "(select ingredientid from ingredient where name like ?))" +
                                                "order by name DESC;");
                    }
                }
                //sort by date
                else if (sort.equals("Date")) {
                    if (asc) {
                        ps = con.prepareStatement("select * from recipepublic where recipeid in " + 
                                                "(select distinct recipeId from recipe_requires where ingredientid in " +
                                                "(select ingredientid from ingredient where name like ?))" +
                                                "order by date;");
                    }
                    else {
                        ps = con.prepareStatement("select * from recipepublic where recipeid in " + 
                                                "(select distinct recipeId from recipe_requires where ingredientid in " +
                                                "(select ingredientid from ingredient where name like ?))" +
                                                "order by date DESC;");
                    }
                }
                else { //sort on rating
                    if (asc) {
                        ps = con.prepareStatement("select * from recipepublic where recipeid in " + 
                                                "(select distinct recipeId from recipe_requires where ingredientid in " +
                                                "(select ingredientid from ingredient where name like ?))" +
                                                "order by avgrating;");
                    }
                    else {
                        ps = con.prepareStatement("select * from recipepublic where recipeid in " + 
                                                "(select distinct recipeId from recipe_requires where ingredientid in " +
                                                "(select ingredientid from ingredient where name like ?))" +
                                                "order by avgrating DESC;");
                    }
                }
                ps.setString(1, "%" + searchVal + "%");
            }
            else { // category
                if (sort.equals("Name")) {
                    if (asc) {
                        ps = con.prepareStatement("select * from recipepublic where recipeid in " + 
                                            "(select distinct recipeId from recipe_category where categoryid in " +
                                            "(select categoryid from category where categoryname like ?))" +
                                            "order by name;");
                    }
                    else {
                        ps = con.prepareStatement("select * from recipepublic where recipeid in " + 
                                            "(select distinct recipeId from recipe_category where categoryid in " +
                                            "(select categoryid from category where categoryname like ?))" +
                                            "order by name DESC;");
                    }
                }
                else if (sort.equals("Date")) {
                    if (asc) {
                        ps = con.prepareStatement("select * from recipepublic where recipeid in " + 
                                            "(select distinct recipeId from recipe_category where categoryid in " +
                                            "(select categoryid from category where categoryname like ?))" +
                                            "order by date;");
                    }
                    else {
                        ps = con.prepareStatement("select * from recipepublic where recipeid in " + 
                                            "(select distinct recipeId from recipe_category where categoryid in " +
                                            "(select categoryid from category where categoryname like ?))" +
                                            "order by date DESC;");
                    }
                }
                else {
                    if (asc) {
                        ps = con.prepareStatement("select * from recipepublic where recipeid in " + 
                                            "(select distinct recipeId from recipe_category where categoryid in " +
                                            "(select categoryid from category where categoryname like ?))" +
                                            "order by avgrating;");
                    }
                    else {
                        ps = con.prepareStatement("select * from recipepublic where recipeid in " + 
                                            "(select distinct recipeId from recipe_category where categoryid in " +
                                            "(select categoryid from category where categoryname like ?))" +
                                            "order by avgrating DESC;");
                    }
                }
                ps.setString(1, "%" + searchVal + "%");
            }

            exec = ps.execute();
            
            if (exec) {
                ResultSet rs = ps.getResultSet();
                if ((rs.isBeforeFirst())) {
                    new BrowseResult(user, rs);
                }

            }

        }  catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        
    }

    private JButton logout () {
        JButton btnNewButton = new JButton("Logout");
        btnNewButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                int a = JOptionPane.showConfirmDialog(btnNewButton, "Are you sure?");
                // JOptionPane.setRootFrame(null);
                if (a == JOptionPane.YES_OPTION) {
                    dispose();
                    UserLogin obj = new UserLogin();
                    obj.setTitle("Login");
                    obj.setVisible(true);
                } else {

                }
            }
        });
        return btnNewButton;
    }

    

    private JButton returnHome () {
        JButton home = new JButton("Home");
        home.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                EventQueue.invokeLater(new Runnable() {
                    @Override
                    public void run() {
                        Browse.this.dispose();
                        new UserHome();
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
