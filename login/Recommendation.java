package login;
import javax.swing.*;
import java.awt.*;  
import java.awt.event.*;
import java.sql.*;  

public class Recommendation extends JFrame{

    private String user;

    /**
     * login window width
     */
    public static final int WIDTH_FRAME = 960;

    /**
     * login window height
     */
    public static final int HEIGHT_FRAME = 960;

    //Creating Constructor for initializing JFrame components 
    
    public Recommendation (String user) {
        super("Recommendation"); 
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
        JPanel contentPane = new JPanel();
        Dimension space = new Dimension(960,20);
        contentPane.setBackground(new Color(196,239,255,255));
        contentPane.setLayout(new BoxLayout(contentPane, BoxLayout.Y_AXIS));
        contentPane.add(Box.createRigidArea(new Dimension(960,150)));
        contentPane.add(topRate());
        contentPane.add(Box.createRigidArea(space));
        contentPane.add(mostRecent());
        contentPane.add(Box.createRigidArea(space));
        contentPane.add(inPantry());
        contentPane.add(Box.createRigidArea(space));
        contentPane.add(recommend());
        contentPane.add(Box.createRigidArea(space));
        contentPane.add(returnHome());
        contentPane.add(Box.createRigidArea(space));
        contentPane.add(logout());
        //contentPane.add(Box.createRigidArea(new Dimension(960,80)));
        setContentPane(contentPane);
    }

    private JButton mostRecent () {
        JButton btn = new JButton("Most Recent");
        btn.addActionListener(new ActionListener(){

            @Override
            public void actionPerformed(ActionEvent e) {
                Connection con = DataBase.getCon();
                try {
                    PreparedStatement ps = con.prepareStatement("SELECT * FROM recipepublic ORDER BY date DESC LIMIT 50;");
                    boolean exec = ps.execute();

                    if (exec) {
                        ResultSet rs = ps.getResultSet();
                        if ((rs.isBeforeFirst())) {
                            new BrowseResult(user, rs);
                        }
        
                    }
                } catch (SQLException exception) {
                    exception.printStackTrace();
                }
                
            }});
        btn.setBackground(new Color (255,213,237,255));
        btn.setAlignmentX(CENTER_ALIGNMENT);
        return btn;
    }

    private JButton inPantry () {
        JButton btn = new JButton("In the Pantry");
        btn.addActionListener(new ActionListener(){

            @Override
            public void actionPerformed(ActionEvent e) {
                Connection con = DataBase.getCon();
                try {
                    PreparedStatement ps = con.prepareStatement("");
                    boolean exec = ps.execute();

                    if (exec) {
                        ResultSet rs = ps.getResultSet();
                        if ((rs.isBeforeFirst())) {
                            new BrowseResult(user, rs);
                        }
        
                    }
                } catch (SQLException exception) {
                    exception.printStackTrace();
                }
                
            }});
        btn.setAlignmentX(CENTER_ALIGNMENT);
        btn.setBackground(new Color (255,213,237,255));
        return btn;
    }

    private JButton recommend () {
        JButton btn = new JButton("Recommended");
        btn.addActionListener(new ActionListener(){

            @Override
            public void actionPerformed(ActionEvent e) {
                Connection con = DataBase.getCon();
                try {
                    PreparedStatement ps = con.prepareStatement("SELECT * FROM recipepublic WHERE recipeid IN (SELECT DISTINCT nc2.recipeid FROM " +
                                            "(SELECT * FROM netizen_creates WHERE username = ?) AS nc1 " +
                                            "JOIN netizen_creates AS nc2 ON (nc1.recipeid = nc2.recipeid) WHERE nc2.username != ?) " +
                                            "ORDER BY avgrating DESC;");
                    ps.setString(1, user);
                    ps.setString(2, user);
                    boolean exec = ps.execute();

                    if (exec) {
                        ResultSet rs = ps.getResultSet();
                        if ((rs.isBeforeFirst())) {
                            new BrowseResult(user, rs);
                        }
        
                    }
                } catch (SQLException exception) {
                    exception.printStackTrace();
                }
                
            }});
        btn.setBackground(new Color (255,213,237,255));
        btn.setAlignmentX(CENTER_ALIGNMENT);
        return btn;
    }



    private JButton topRate () {
        JButton btn = new JButton("Top Rated");
        btn.addActionListener(new ActionListener(){

            @Override
            public void actionPerformed(ActionEvent e) {
                Connection con = DataBase.getCon();
                try {
                    PreparedStatement ps = con.prepareStatement("SELECT * FROM recipepublic WHERE avgrating is not NULL "
                                            + "ORDER BY avgrating DESC LIMIT 50;");
                    boolean exec = ps.execute();

                    if (exec) {
                        ResultSet rs = ps.getResultSet();
                        if ((rs.isBeforeFirst())) {
                            new BrowseResult(user, rs);
                        }
        
                    }
                } catch (SQLException exception) {
                    exception.printStackTrace();
                }
                
            }});
        btn.setAlignmentX(CENTER_ALIGNMENT);
        btn.setBackground(new Color (255,213,237,255));
        return btn;
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
        btnNewButton.setAlignmentX(CENTER_ALIGNMENT);
        btnNewButton.setBackground(new Color (255,213,237,255));
        //btnNewButton.setAlignmentY(CENTER_ALIGNMENT);
        return btnNewButton;
    }

    

    private JButton returnHome () {
        JButton home = new JButton("Home");
        home.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                EventQueue.invokeLater(new Runnable() {
                    @Override
                    public void run() {
                        Recommendation.this.dispose();
                        new UserHome();
                    }
                });
            }
        });
        home.setAlignmentX(CENTER_ALIGNMENT);
        home.setBackground(new Color (255,213,237,255));
        //home.setAlignmentY(CENTER_ALIGNMENT);
        return home;
    }

    //Running Constructor  
    public static void main(String args[]) {  
        EventQueue.invokeLater(new Runnable() {
            public void run() {
                new Recommendation("test");
            }
        }); 
    }
    
}
