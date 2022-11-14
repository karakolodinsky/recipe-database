package login;
import javax.swing.*;
import java.awt.*;  
import java.awt.event.*;
import java.sql.*;  

public class Recommendation extends JFrame{

    static private String user;

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
        Recommendation.user = user; 
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
        contentPane.setLayout(new BoxLayout(contentPane, BoxLayout.Y_AXIS));
        contentPane.add(Box.createRigidArea(new Dimension(960,50)));
        contentPane.add(topRate());
        contentPane.add(Box.createRigidArea(space));
        contentPane.add(returnHome());
        contentPane.add(Box.createRigidArea(space));
        contentPane.add(logout());
        contentPane.add(Box.createRigidArea(new Dimension(960,50)));
        setContentPane(contentPane);
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
