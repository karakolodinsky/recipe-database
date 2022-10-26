package login;

import com.jcraft.jsch.JSch;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.Session;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.spec.InvalidKeySpecException;
import java.sql.*;
import java.util.Base64;
import java.util.Date;

import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import javax.swing.JOptionPane;

public class DataBase {

    private static String db_username;
    private static String db_password;
    protected static Connection con;
    public static final int ITERATIONS = 1000;
    // PBEWith<digest>And<encryption> Parameters for use with the PBEWith<digest>And<encryption> algorithm.
    // HmacSHA512 Key generator for use with the HmacSHA512 algorithm
    private static final String ALGORITHM = "PBKDF2WithHmacSHA512"; 

    public static Connection getCon() {
        return con;
    }

    /** 
     * @return salt for user
     * @throws IOException
     */
    public static String generateSalt() throws IOException {

        SecureRandom RANDOM = new SecureRandom();
        byte[] salt = new byte[16];
        RANDOM.nextBytes(salt);
        String s64 =  Base64.getEncoder().encodeToString(salt);
        byte[] s = s64.getBytes("UTF-8");
        return s.toString();
      }

      public static byte[] hashHelper (byte[] saltBytes, String password) {
        boolean invalid = true;
        while (invalid) {
            PBEKeySpec spec = new PBEKeySpec(password.toCharArray(), saltBytes, ITERATIONS, 128);
            try {
                SecretKeyFactory factory = SecretKeyFactory.getInstance(ALGORITHM);
                byte[] hashed =  factory.generateSecret(spec).getEncoded();
                try {
                    hashed = new String(hashed).getBytes("UTF-8");
                    return hashed;
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
            } catch (NoSuchAlgorithmException | InvalidKeySpecException e) {
                e.printStackTrace();
            } finally {
                spec.clearPassword();
            }
        }
        return null;
      }

    /** Takes user's plaintext password and hashes it
     * @param password plaintext password
     * @return hashed password
     */
    public static String hashPassword (String password, String salt) {

        byte[] saltBytes = salt.getBytes();

        byte[] hashed = hashHelper(saltBytes, password);
        String hashPsswrd = new String(hashed);
        return hashPsswrd;
      }

    public static int getSSH(){

        try {
            // connect to ssh
            Class.forName("org.postgresql.Driver");
            JSch jsch = new JSch();

            Session session = jsch.getSession(db_username, "starbug.cs.rit.edu", 22);
            session.setPassword(db_password);
            session.setConfig("StrictHostKeyChecking", "no");
            session.connect();
            session.setPortForwardingL(1001, "localhost", 5432);

        }
        catch (JSchException | ClassNotFoundException ssh){
            JOptionPane.showMessageDialog(null, "ssh error " + db_username + " " + db_password, "SSH",
                    JOptionPane.ERROR_MESSAGE);
            return -1;
        }
        return 1;
    }
    /**
     * SSH using credentials and then connect to DB
     * @return Connection
     */
    public static Connection getConnect() {

        Connection conn = null;
        try {
            // now connect to DB
            String dbUrl = "jdbc:postgresql://localhost:1001/p32002_31";
            conn = DriverManager.getConnection(dbUrl, db_username, db_password);
            con = conn;

        }
        catch (SQLException e) {

            JOptionPane.showMessageDialog(null, "Database connection error " + db_username + " " + db_password, "Database",
                    JOptionPane.ERROR_MESSAGE);

        }

        return conn;
    }


    /**
     *
     * @param input_db_username
     * @param input_db_password
     * @return if database credentials work, return 1, else return -1
     */
    public static int verifyDBLogin(String input_db_username, String input_db_password) {

        db_username = input_db_username;
        db_password = input_db_password;
        if(getSSH() == -1){
            return -1;
        }
        Connection conn = DataBase.getConnect();
        if(conn != null){
            return 1;
        }
        return -1;
    }


    /**
     *
     * @param username
     * @param password
     * @return if the user exists, it returns the user id.
     */
    public static int verifyLogin(String username, String password) {
        Connection conn = DataBase.getConnect();

        try {
            PreparedStatement st = (PreparedStatement) conn
                    .prepareStatement("Select username, passwordhash, salt from netizen where username=? and passwordhash=?;");

            st.setString(1, username);
            st.setString(2, password);
            ResultSet rs = st.executeQuery();
            if (rs.next()) {
                //String hashed = rs.getString("passwordhash");
                //String salt = rs.getString("salt");
                //String hashPass = hashPassword(password, salt);
                //if (hashPass.equals(hashed)) {
                    PreparedStatement st1 = (PreparedStatement) conn.prepareStatement("UPDATE netizen SET lastaccessdate = CURRENT_TIMESTAMP where username = ?");
                    st1.setString(1, username);
                    st1.executeUpdate();
                    return 1;
                    //update most recent access date
                }
                
            //}
        } catch (SQLException e) {

            JOptionPane.showMessageDialog(null, "Database statement error", "Database",
                    JOptionPane.ERROR_MESSAGE);

        }

        return -1;

    }

    public static int createUser(String username, String password) throws IOException {
        Connection conn = DataBase.getConnect();

        try {
            PreparedStatement st = (PreparedStatement) conn
                    .prepareStatement("INSERT INTO netizen VALUES (?, ?, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, ?);");
            //String salt = generateSalt(); 
            //String hashPsswrd = hashPassword(password, salt);   
            st.setString(1, username);
            st.setString(2, password);
            st.setString(3, "");
            System.out.println(st);
            int rs = st.executeUpdate();
            if(rs == 1){
                return 1;
            }
        } catch (SQLException e) {

                // print SQL exception information
                printSQLException(e);
            }

        return -1;

    }

    public static void printSQLException(SQLException ex) {
        for (Throwable e: ex) {
            if (e instanceof SQLException) {
                e.printStackTrace(System.err);
                System.err.println("SQLState: " + ((SQLException) e).getSQLState());
                System.err.println("Error Code: " + ((SQLException) e).getErrorCode());
                System.err.println("Message: " + e.getMessage());
                Throwable t = ex.getCause();
                while (t != null) {
                    System.out.println("Cause: " + t);
                    t = t.getCause();
                }
            }
        }
}


}

