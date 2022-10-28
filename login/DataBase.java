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
import java.util.ArrayList;
import java.util.Base64;
import java.util.Calendar;
// import java.util.Date;
import java.sql.Date;

import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import javax.swing.JOptionPane;

public class DataBase {

    private static String db_username;
    private static String db_password;
    private static String user;
    protected static Connection con;
    public static final int ITERATIONS = 1000;
    // PBEWith<digest>And<encryption> Parameters for use with the PBEWith<digest>And<encryption> algorithm.
    // HmacSHA512 Key generator for use with the HmacSHA512 algorithm
    private static final String ALGORITHM = "PBKDF2WithHmacSHA512";


    /**
     * Returns the current database connection; if none exists, a new connection is made
     * @return      RIT CS Database connection
     */
    public static Connection getCon() {
        if (con == null){
            Connection conn = getConnect();
            con = conn;
        }
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
        Connection conn = DataBase.getCon();

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
                    user = username;
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
        Connection conn = DataBase.getCon();

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

    public static int addtoPantry(String item, String user, int quantity, Date exp, Date purch, int qbought, String unit) throws IOException {
        Connection conn = DataBase.getCon();

        try {
                String ingID = "";
                int qOLD = 0;
                int bqOLD = 0;
                PreparedStatement st0 = (PreparedStatement) conn
                    .prepareStatement("SELECT ingredientid from ingredient where name = ?;");
                st0.setString(1, item);
                ResultSet rs0 = st0.executeQuery();
                while (rs0.next()) {
                         ingID = rs0.getString("ingredientID");
                        System.out.println(ingID + "\n");
                        System.out.println(ingID + "\n");
                      }
                      PreparedStatement st1 = (PreparedStatement) conn
                      .prepareStatement("SELECT ingredientid from in_pantry where ingredientid = ? and username = ? and purchasedate = ?");
                      st1.setInt(1,Integer.parseInt(ingID));
                      st1.setString(2, user);
                      st1.setDate(3, (java.sql.Date) purch);
                      ResultSet rs1 = st1.executeQuery();
                      boolean in_pantry = true;
                      if (!rs1.isBeforeFirst() ) {    
                        in_pantry = false;
                      } 

                      if (in_pantry){
                        PreparedStatement st3 = (PreparedStatement) conn
                        .prepareStatement("Select quantitycurr, quantitybought from in_pantry where username = ? and ingredientid = ?");
                        st3.setString(1, user);
                        st3.setInt(2, Integer.parseInt(ingID));
                        ResultSet rs3 = st3.executeQuery();
                        while (rs3.next()) {
                                        qOLD = rs3.getInt(1);
                                        bqOLD = rs3.getInt(2);
                                }
                        PreparedStatement st2;
                        if (unit != "other"){
                            st2 = (PreparedStatement) conn
                            .prepareStatement("UPDATE in_pantry SET quantitycurr = ?, quantitybought = ?, purchasedate = ? , expirationdate = ?, unit = ? WHERE username = ? and ingredientid = ?;  ");
                            st2.setDate(3, (java.sql.Date) purch);
                            st2.setInt(1, quantity + qOLD);
                            st2.setInt(2, qbought+ bqOLD);
                            st2.setDate(4, (java.sql.Date) exp);
                                st2.setString(5, unit);   
                        }
                        else {
                            st2 = (PreparedStatement) conn
                            .prepareStatement("UPDATE in_pantry SET quantitycurr = ?, quantitybought = ?, purchasedate = ? , expirationdate = ?, unit = '' WHERE username = ? and ingredientid = ?;  ");
                            st2.setDate(3, (java.sql.Date) purch);
                            st2.setInt(1, quantity + qOLD);
                            st2.setInt(2, qbought+ bqOLD);
                            st2.setDate(4, (java.sql.Date) exp);
                        }
                        st2.setString(6, user);
                        st2.setInt(7, Integer.parseInt(ingID));

                       int rs2 = st2.executeUpdate();

                        if(rs2 == 1){
                                return 1;
                            }
                      }
                      else{
                      
          
                PreparedStatement st = (PreparedStatement) conn
                .prepareStatement("INSERT INTO in_pantry VALUES (?,?,?,?,?,?,?);");
                st.setString(1, user);
            st.setInt(2, Integer.parseInt(ingID));
            st.setDate(3, (java.sql.Date) purch);
            st.setInt(4, qbought);
            st.setInt(5, quantity);
            st.setDate(6, (java.sql.Date) exp);
            if (unit != "other"){
                st.setString(7, unit);   
        }
                else {
                        String emp = "";
                st.setString(7, emp);
        }
            int rs = st.executeUpdate();
            if(rs == 1){
                return 1;
            }
           }
        } catch (SQLException e) {
                

                // print SQL exception information
                printSQLException(e);
            }

        return -1;

    }

    public static int SearchIngredient(String ingredient) throws IOException {
        Connection conn = DataBase.getCon();

        try {
            PreparedStatement st = (PreparedStatement) conn
                    .prepareStatement("SELECT ingredientid, name FROM ingredient where name = ?");
                    st.setString(1, ingredient);
            System.out.println(st);
            ResultSet rs = st.executeQuery();
            while(rs.next()){
                System.out.println(rs.getInt("ingredientid"));
                return rs.getInt("ingredientid");
            }
        } catch (SQLException e) {

                // print SQL exception information
                printSQLException(e);
            }

        return -1;

    }

    public static ResultSet GetIngredients (String ingredient) throws IOException {
        Connection conn = DataBase.getCon();

        try {
            PreparedStatement st = (PreparedStatement) conn
                    .prepareStatement("SELECT name FROM ingredient WHERE name LIKE '" + ingredient + "%' order by name");
                //     st.setString(1, ingredient);
            System.out.println(st);
            ResultSet rs = st.executeQuery();
            return rs;
        } catch (SQLException e) {

                // print SQL exception information
                printSQLException(e);
            }

        return null;

    }

    public static ResultSet GetCategories (String category) throws IOException {
        Connection conn = DataBase.getCon();

        try {
            PreparedStatement st = (PreparedStatement) conn
                    .prepareStatement("SELECT categoryname FROM category WHERE categoryname LIKE '%" + category + "%'");
                //     st.setString(1, ingredient);
            System.out.println(st);
            ResultSet rs = st.executeQuery();
            return rs;
        } catch (SQLException e) {

                // print SQL exception information
                printSQLException(e);
            }

        return null;

    }

    public static Integer GetCategoryByName(String name) throws IOException {
        Connection conn = DataBase.getCon();

        int ret = -1;
        try{
            PreparedStatement st = (PreparedStatement) conn
                .prepareStatement("SELECT categoryId FROM category WHERE categoryname LIKE '%" + name + "%'");
            System.out.println(st);
            ResultSet exists = st.executeQuery();
            while (exists.next()){
                ret = exists.getInt(1);
                return ret;
            }
        }
        catch(SQLException e){
            printSQLException(e);
        }
        return ret;
    }

    public static ResultSet GetPantry (String user) throws IOException {
        Connection conn = DataBase.getCon();

        try {
            PreparedStatement st = (PreparedStatement) conn
                    .prepareStatement("SELECT i.name, p.purchasedate, p.expirationdate, p.quantitycurr, p.quantitybought, p.unit from ingredient i, in_pantry p where i.ingredientid = p.ingredientid and p.username = ? order by name");
                st.setString(1, user);
            System.out.println(st);
            ResultSet rs = st.executeQuery();
            return rs;
        } catch (SQLException e) {

                // print SQL exception information
                printSQLException(e);
            }

        return null;

    }

    // STUB
    // todo on make-bake-cook branch
    public static int cookRecipe (int recipeID, int quant) {
        Connection conn = DataBase.getCon();
        try{
            PreparedStatement st = (PreparedStatement)  conn
                    .prepareStatement("SELECT COUNT(*) AS ingredientCount FROM recipe_requires WHERE recipeid=?");
            st.setInt(1, recipeID);
            System.out.println(st);
            ResultSet rs = st.executeQuery();
            rs.next();


            // first loop to see if we have enough of all ingredients
            // if we don't have enough of even 1 then exit
            PreparedStatement stFirstCheck = (PreparedStatement)  conn
                    .prepareStatement("SELECT ingredientid, quantity FROM recipe_requires WHERE recipeid=?",
                            ResultSet.TYPE_SCROLL_SENSITIVE,
                            ResultSet.CONCUR_UPDATABLE);
            stFirstCheck.setInt(1, recipeID);
            System.out.println(stFirstCheck);
            ResultSet rsFirstCheck = stFirstCheck.executeQuery();

            int ingredientCount = rs.getInt("ingredientCount");
            for(int i = 0; i < ingredientCount; i++) {
                if (rsFirstCheck.next()) {
                    PreparedStatement st2 = (PreparedStatement) conn
                            .prepareStatement("SELECT purchasedate, ingredientid, quantitycurr, unit FROM in_pantry WHERE ingredientid=? AND username=? " +
                                            "group by purchasedate, ingredientid, quantitycurr, unit, expirationdate ORDER BY expirationdate ASC;", ResultSet.TYPE_SCROLL_SENSITIVE,
                                    ResultSet.CONCUR_UPDATABLE);
                    st2.setInt(1, rsFirstCheck.getInt("ingredientid"));
                    st2.setString(2, user);
                    boolean rscheck = st2.execute();
                    if (rscheck) {
                        ResultSet rs2 = st2.getResultSet();
                        if (!rs2.isBeforeFirst()) {
                            return -1;
                        } else {
                            rs2.next();
                            int quantityLeft = rsFirstCheck.getInt("quantity");
                            rs2.previous();
                            boolean seen = false;
                            while (rs2.next() && quantityLeft > 0) {
                                // loop through all of one ingredient in pantry
                                seen = false;
                                int currentQuantity = rs2.getInt("quantitycurr");

                                if (currentQuantity <= quantityLeft) {
                                    //calculate qtyLeft
                                    quantityLeft = quantityLeft - currentQuantity;
                                    seen = true;

                                }
                                if (currentQuantity > quantityLeft && !seen) {
                                    // you have enough break while
                                    quantityLeft = 0;
                                }
                            }
                            if (quantityLeft > 0){
                                // never enough of one of the ingredients so return -1
                                return -1;
                            }
                        }
                    }
                }
            }









            PreparedStatement st1 = (PreparedStatement)  conn
                    .prepareStatement("SELECT ingredientid, quantity FROM recipe_requires WHERE recipeid=?",
                            ResultSet.TYPE_SCROLL_SENSITIVE,
                            ResultSet.CONCUR_UPDATABLE);
            st1.setInt(1, recipeID);
            System.out.println(st1);
            ResultSet rs1 = st1.executeQuery();

            // boolean notenough = false;
            // int quantityInPantry = 0;
            for(int i = 0; i < ingredientCount; i++){
                if(rs1.next()){
//                    if(notenough){
//                        rs1.previous();
//                    }
//                    if(!notenough){
//                        quantityInPantry = rs1.getInt("quantity");
//                    }
                    PreparedStatement st2 = (PreparedStatement)  conn
                            .prepareStatement("SELECT purchasedate, ingredientid, quantitycurr, unit FROM in_pantry WHERE ingredientid=? AND username=? " +
                                    "group by purchasedate, ingredientid, quantitycurr, unit, expirationdate ORDER BY expirationdate ASC;", ResultSet.TYPE_SCROLL_SENSITIVE,
                                    ResultSet.CONCUR_UPDATABLE);
                    st2.setInt(1, rs1.getInt("ingredientid"));
                    st2.setString(2, user);
                    boolean rscheck = st2.execute();
                    if(rscheck){
                        ResultSet rs2 = st2.getResultSet();
                        if(!rs2.isBeforeFirst()){
                            return -1;
                        }
                        else{
                            rs2.next();
                            int ingredientID = rs2.getInt("ingredientid");
                            //String username = rs2.getString("username");
                            ArrayList<Date> toDelete = new ArrayList<Date>();
                            int quantityLeft = rs1.getInt("quantity");
                            rs2.previous();
                            boolean seen = false;
                            while(rs2.next() && quantityLeft > 0){
                                // loop through all of one ingredient in pantry
                                seen = false;
                                Date purchaseDate = rs2.getDate("purchasedate");
                                int currentQuantity = rs2.getInt("quantitycurr");

                                if(currentQuantity <= quantityLeft){
                                    // add purchase date to list
                                    //calculate qtyLeft
                                    toDelete.add(purchaseDate);
                                    quantityLeft = quantityLeft - currentQuantity;
                                    seen = true;

                                }
                                 if (currentQuantity > quantityLeft && !seen){
                                    // decrement quantityLeft
                                    // update quantitycurr
                                    // set quantityLeft = 0 or just subtract so we can exit while loop
                                    currentQuantity = currentQuantity - quantityLeft;
                                     if(currentQuantity == 0){
                                         toDelete.add(purchaseDate);
                                     }
                                    //if currqty == 0 -> delete it
                                    quantityLeft = 0;
                                    PreparedStatement stUpdQty = (PreparedStatement)  conn
                                            .prepareStatement("UPDATE in_pantry SET quantitycurr=? " +
                                                    "WHERE ingredientid=? AND username=? AND purchasedate=?;");
                                    stUpdQty.setInt(1, currentQuantity);
                                    stUpdQty.setInt(2, ingredientID);
                                    stUpdQty.setString(3, user);
                                    stUpdQty.setDate(4, purchaseDate);
                                    int rsUpdQty = stUpdQty.executeUpdate();
                                }
                            }
                            if(quantityLeft <= 0){
                                // delete everything from list
                                for(int j = 0; j < toDelete.size(); j++){
                                    // get each purchaseDate in list to delete
                                    Date thisPurchaseDate = toDelete.get(j);

                                    PreparedStatement stDelete = (PreparedStatement)  conn
                                            .prepareStatement("DELETE FROM in_pantry WHERE ingredientid=? AND" +
                                                    " username=? AND purchasedate=?;");
                                    stDelete.setInt(1, ingredientID);
                                    stDelete.setString(2, user);
                                    stDelete.setDate(3, thisPurchaseDate);
                                    int rsDelete = stDelete.executeUpdate();
                                }
                            }
                            else{
                                // don't delete everything because you never had enough
                                return -1;
                            }


//                            if(rs2.next()){
//                                if(rs2.getInt("quantitycurr") < rs1.getInt("quantity")){
//                                    i--;
//                                    notenough = true;
//                                    PreparedStatement st3 = (PreparedStatement)  conn
//                                            .prepareStatement("UPDATE in_pantry SET quantitycurr=0 WHERE ingredientid=?; " +
//                                                    "DELETE FROM in_pantry WHERE quantitycurr=0");
//                                    st3.setInt(1, rs2.getInt("ingredientid"));
//                                    int rs3 = st3.executeUpdate();
//                                    quantityInPantry = quantityInPantry - rs2.getInt("quantitycurr");
//                                }
//                                else{
//                                    quantityInPantry = rs2.getInt("quantitycurr") - rs1.getInt("quantity");
//                                    PreparedStatement st4 = (PreparedStatement)  conn
//                                            .prepareStatement("UPDATE in_pantry SET quantitycurr=? WHERE ingredientid=?");
//                                    st4.setInt(1, quantityInPantry);
//                                    st4.setInt(2, rs2.getInt("ingredientid"));
//                                    int rs4 = st4.executeUpdate();
//                                }
//                            }
                        }

                        }
                    }
            }
            return 0;
        } catch (SQLException e) {

            // print SQL exception information
            printSQLException(e);
        }
        return 0;
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


    /**
     * Creates a recipe with information passed from the UI
     * @param steps             String <= 5000 chars long
     * @param description       String <= 200 chars long
     * @param cooktime          Positive integer
     * @param servings          Integer 1-12
     * @param difficulty        Integer 1-5
     * @param name              String <= 50 chars long
     * @return                  New recipe's ID
     */
    public static int createRecipe(String steps, String description, Integer cooktime,
                                    Integer servings, Integer difficulty, String name){
        String username = UserLogin.getUsername();
        Connection conn = DataBase.getCon();
        try{
            int newid = (getMaxRecipeId() + 1);
            //recipe: (recipeid, author, steps, description, cooktime, servings, difficulty, name, date)
            PreparedStatement st = (PreparedStatement) conn
                    .prepareStatement("INSERT INTO recipe (RECIPEID, AUTHOR, STEPS, DESCRIPTION, COOKTIME, SERVINGS, " +
                                        "DIFFICULTY, NAME, DATE) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?);");
            st.setInt(1, newid);
            st.setString(2, username);
            st.setString(3, steps);
            st.setString(4, description);
            st.setInt(5, cooktime);
            st.setInt(6, servings);
            st.setInt(7, difficulty);
            st.setString(8, name);
            st.setDate(9, java.sql.Date.valueOf(java.time.LocalDate.now()));

            int rs = st.executeUpdate();
            if(rs == 1){
                return newid;
            }
        }
        catch (SQLException e) {
            printSQLException(e);
        }
        return -1;                  // try failed
    }

   
    /**
     * Deletes a recipe as long as no one has cooked/reviewed it yet
     * @param recipeId the recipe to be deleted
     * @return 1 if the recipe was deleted, 0 otherwise
     */
    public static int DeleteRecipe(Integer recipeId){

        try {
            ResultSet rs = null;
            PreparedStatement st = con.prepareStatement("SELECT recipeId FROM netizen_creates WHERE recipeId=?;");
            st.setString(1, String.valueOf(recipeId));
            boolean exists = st.execute();
            if (!exists) { 
                st = con.prepareStatement("DELETE FROM recipe WHERE recipeid=?;");
                st.setString(1, String.valueOf(recipeId));
                st.executeUpdate();
                st = con.prepareStatement("DELETE FROM recipe_category WHERE recipeid=?");
                st.setString(1, String.valueOf(recipeId));
                st.executeUpdate();
                st = con.prepareStatement("DELETE FROM recipe_requires WHERE recipeid=?");
                st.setString(1, String.valueOf(recipeId));
                st.executeUpdate();
                return 1;
            }
        }
        catch (SQLException e){
            printSQLException(e);
        }
        return 0;
    }


    /**
     * Categorize a recipe with the given categories
     * @param categoryString the string of categories
     * @param recipeId the recipe to add these tags to
     */
    public static void categorizeRecipe(String categoryString, Integer recipeId){

        // make the categories a list of strings
        String[] categories = categoryString.split(",");

        int size = categories.length;
        for(int i = 0; i < size; i++){

            String currTag = categories[i];

            //check that element of categories is not "" or ", " or " "
            if(!(currTag.equals("") || currTag.equals(", ") || currTag.equals(" "))){
            
                try {
                    ResultSet rs = null;
                    PreparedStatement st = con.prepareStatement("SELECT categoryId FROM category WHERE categoryname=?;");
                    st.setString(1, currTag.strip());
                    boolean exists = st.execute();
                    int categoryId = -1;
                    if (exists) {
                        // if category does not already exist, create it, else grab existing category's Id
                        rs = st.getResultSet();
                        if (!rs.isBeforeFirst()) {
                            st = con.prepareStatement("Select max(categoryId) from category;");
                            rs = st.executeQuery();
                            rs.next();
                            categoryId = rs.getInt(1) + 1;
                            st = con.prepareStatement("Insert into category values(?, ?);");
                            st.setInt(1, categoryId);
                            st.setString(2, currTag);
                            st.executeUpdate();
                        }
                        else{
                            rs.next();
                            categoryId = rs.getInt(1);
                        }
                    }

                    // add category to recipeCategory
                    st = con.prepareStatement("insert into recipe_category values(?, ?);");
                    st.setInt(1, recipeId);
                    st.setInt(2, categoryId);
                    st.executeUpdate();

                } catch (SQLException e) {
                    System.exit(0);
                } 
            }       
        }
    }


    /**
     * Gets the current maximum recipeid value from the recipe table
     * @return              RecipeID on success, -1 on fail
     */
    public static int getMaxRecipeId (){
        Connection conn = DataBase.getCon();
        int newId;
        try{
            PreparedStatement id = (PreparedStatement) conn .prepareStatement("SELECT MAX(R.RECIPEID) " +
                                                                                "FROM RECIPE AS R;");
            ResultSet idEx = id.executeQuery();
            while (idEx.next()){
                return idEx.getInt(1);
            }
        }
        catch (SQLException e) {
            e.printStackTrace();
        }
        System.out.println("Select max failed");
        return -1;
    }


public static int deleteFromPantry(String username, String item) throws IOException {
        Connection conn = DataBase.getCon();

        try {
                int ingID = 0;
                PreparedStatement st0 = (PreparedStatement) conn
                    .prepareStatement("SELECT ingredientid from ingredient where name = ? ");
                st0.setString(1, item);
                ResultSet rs0 = st0.executeQuery();
                while (rs0.next()) {
                         ingID = rs0.getInt("ingredientID");
                        }
            PreparedStatement st = (PreparedStatement) conn
                    .prepareStatement("DELETE FROM in_pantry WHERE username = ? and ingredientid = ?"); 
            st.setString(1, username);
            st.setInt(2, ingID);
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


    /**
     * Adds all the arraylist's ingredients to the SQL recipe_requires table
     * @param recipeID      ID number for recipe in database (Foreign Key)
     * @param ingredients   Array of all ingredients for the recipe
     * @return              1 on success, -1 on fail
     */
    public static int recipeRequires(int recipeID, ArrayList<Ingredient> ingredients){
        Connection conn = getCon();
        Integer ingID = null;
        try{
            for (Ingredient i : ingredients){
                /** get the ID for the current ingredient */
                PreparedStatement st0 = (PreparedStatement) conn
                        .prepareStatement("SELECT ingredientid from ingredient where name = ? ");
                st0.setString(1, i.getName());
                ResultSet rs0 = st0.executeQuery();
                while (rs0.next()) {
                    ingID = rs0.getInt("ingredientID");
                }

                if (ingID != null) {
                    PreparedStatement st = (PreparedStatement) conn
                            .prepareStatement("INSERT INTO recipe_requires (RECIPEID, INGREDIENTID, QUANTITY, UNIT) "
                                    + "VALUES (?, ?, ?, ?)" );
                    st.setInt(1, recipeID);
                    st.setInt(2, ingID);
                    st.setInt(3, i.getQuantity());
                    if (i.getUnit() != null){
                        st.setString(4, i.getUnit());
                    }

                    int rs = st.executeUpdate();
                    if (rs == 1) {
                        return 1;
                    }
                }
            }
        }
        catch (SQLException throwables) {
            throwables.printStackTrace();
            return -1;
        }
        return -1;
    }


    public static int leaveReview(String user, int quant, int stars, String revtext, int recipeID){
        Connection conn = getCon();
        try{
                if(DataBase.cookRecipe(recipeID, quant) != -1){
                        PreparedStatement st = (PreparedStatement) conn
                .prepareStatement("INSERT INTO netizen_creates values (?,?, now(), ?,?,? );");
                st.setString(1, user);
                st.setInt(2, recipeID);
                st.setInt(3, stars);
                st.setString(4, revtext);
                st.setInt(5, quant);
                }
                else return -1;
        } 
        



         catch (SQLException throwables) {
             throwables.printStackTrace();
             return -1;
         }
         return -1;
    }
//     public static int leaveReview(String user, int quant, int stars, String revtext, int recipeID){
//         Connection conn = getCon();
//         try{
//                 ArrayList<Integer> list = new ArrayList<>();
//                 ArrayList<Integer> list2 = new ArrayList<>();
//                 ArrayList<String> list3 = new ArrayList<>();
//                 PreparedStatement st0 = (PreparedStatement) conn
//                 .prepareStatement("SELECT ingredientid, quantity, unit from recipe_req where recipeid = ? ");
//         st0.setInt(1, recipeID);
//         ResultSet rs0 = st0.executeQuery();
//         while (rs0.next()) {
//             list.add(rs0.getInt("ingredientID"));
//             list2.add(rs0.getInt("quantity"));
//             list3.add(rs0.getString("unit"));
//         }
//         for (int i =0 ; i< list.size() ;i++){
//                 PreparedStatement st1 = (PreparedStatement) conn
//                 .prepareStatement("SELECT ingredientid, quantitycurr, unit from in_pantry where user = ? ");
//                 st1.setString(1, user);
//                 ResultSet rs1 = st1.executeQuery();
//                 while (rs1.next()) {
//         } 
        



//         }
//         catch (SQLException throwables) {
//             throwables.printStackTrace();
//             return -1;
//         }
//         return -1;
//     }


public static ResultSet getUserRecipes (String user){
        Connection conn = DataBase.getCon();
        try{
            PreparedStatement ps = (PreparedStatement) conn .prepareStatement("SELECT name, recipeid from recipe where author = ?");
            ps.setString(1, user);
            ResultSet rs = ps.executeQuery();
            return rs;
        }
        catch (SQLException e) {
            e.printStackTrace();
        }
        System.out.println("Select max failed");
        return null;
    }


    /**
     * Gets all ingredients belonging to a selected recipe
     * @param recipeID      the recipes ID number / foreign key to recipe in recipe_requires
     * @return              ResultSet of ingredients' recipe-specific name, quantity, unit
     */
    public static ResultSet getIngredients(int recipeID){
        Connection conn = getCon();
        try{
            PreparedStatement st = (PreparedStatement) conn
                    .prepareStatement("SELECT i.name, r.quantity, r.unit "
                            + "FROM recipe_requires AS r, ingredient AS i "
                            + "WHERE i.ingredientid IN ( "
                            + "SELECT r.ingredientid "
                            + "WHERE r.recipeid = ? "
                            + ") "
                            + "AND i.ingredientid = r.ingredientid;");
            st.setInt(1, recipeID);
            ResultSet rs = st.executeQuery();
            return rs;
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return null;
    }


    /**
     * Gets all categories belonging to a selected recipe
     * @param recipeID      the recipes ID number / foreign key to recipe in recipe_requires
     * @return              ResultSet of categories' name
     */
    public static ResultSet getCategories(int recipeID){
        Connection conn = getCon();
        try{
            PreparedStatement st = (PreparedStatement) conn
                    .prepareStatement("SELECT c.categoryname "
                            + "FROM category AS c, recipe_category as r "
                            + "WHERE c.categoryid = r.categoryid "
                            + "AND c.categoryid IN ( "
                            + "SELECT r.categoryid "
                            + "WHERE r.recipeid = ?);");
            st.setInt(1, recipeID);
            ResultSet rs = st.executeQuery();
            return rs;
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return null;
    }


    /**
     *
     * @param name
     */
    public static boolean newCategory(String name) {
        Connection conn = getCon();
        try {
            int catID = - 1;
            /** get the ID for the current category */
            PreparedStatement st0 = (PreparedStatement) conn
                    .prepareStatement("SELECT MAX(categoryid) from category");
            ResultSet rs0 = st0.executeQuery();
            while (rs0.next()) {
                catID = rs0.getInt(1);
            }

            if (catID > 0){
                if (-1 != GetCategoryByName(name)) {
                    return false;
                }
                int newCatID = catID += 1;
                PreparedStatement st = (PreparedStatement) conn
                        .prepareStatement("INSERT INTO category (categoryid, categoryname) VALUES (?, ?);");
                st.setInt(1, newCatID);
                st.setString(2, name);
                st.execute();
                return true;
            }

        }
        catch (SQLException | IOException throwables) {
            throwables.printStackTrace();
        }
        return false;
    }

}

