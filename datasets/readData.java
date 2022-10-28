package datasets;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.sql.Connection;
import java.sql.Date;
import java.sql.DriverManager;
import java.util.Random;
//import java.util.random.*;

import javax.annotation.processing.Filer;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import javax.swing.JOptionPane;

import com.jcraft.jsch.JSch;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.Session;

import login.DataBase;

public class readData {

    readData () {
        
    }

    private void readReviews (Connection con) throws IOException, SQLException {
        FileReader fr = new FileReader("datasets/RAW_interactions.csv");
        BufferedReader br = new BufferedReader(fr);
        String line = br.readLine();
        while ((line = br.readLine()) != null) {
            String[] reviewParts = line.split(",");
            String dateForm = formatDate(reviewParts[2]);
            Date date = Date.valueOf(dateForm);
            int rating = Integer.parseInt(reviewParts[3]);
            String review = reviewParts[4];
            if (review.length() >= 500) {
                review = review.substring(0, 499);
            }
            if (rating < 6 && rating > 0) {
                // get random user from database
                PreparedStatement ps = con.prepareStatement("SELECT * FROM netizen ORDER BY RANDOM() LIMIT 1;");
                ResultSet rs = ps.executeQuery();
                rs.next();
                String user = rs.getString("username");
                // get random recipe from database
                ps = con.prepareStatement("SELECT * FROM recipe ORDER BY RANDOM() LIMIT 1;");
                rs = ps.executeQuery();
                rs.next();
                int recipeId = rs.getInt("recipeId");
                Date uploaded = rs.getDate("date");
                boolean timingGood = date.after(uploaded);
                if (!timingGood) {
                    date = uploaded;
                }
                //fix user account creation date
                ps = con.prepareStatement("UPDATE netizen SET creationdate=? where username=?;");
                Timestamp creationDate = new Timestamp(date.getTime());
                ps.setTimestamp(1, creationDate);
                ps.setString(2, user);
                ps.executeUpdate();

                // make their review
                ps = con.prepareStatement("INSERT into netizen_creates VALUES (?, ?, ?, ?, ?, ?);");
                ps.setString(1, user);
                ps.setInt(2, recipeId);
                ps.setDate(3, date);
                ps.setInt(4, rating);
                ps.setString(5, review);
                Random rand = new Random();
                int serv = rand.nextInt(10);
                ps.setInt(6, serv);
                ps.executeUpdate();
            }
        }

        br.close();
        fr.close();
    }

    private void readRecipes (Connection con) throws IOException, SQLException {
        //Connection con = DataBase.getCon();
        FileReader fr = new FileReader("datasets/RAW_recipes.csv"); // I'm running from recipe-database
        BufferedReader br = new BufferedReader(fr);
        String line = br.readLine();
        while ((line = br.readLine()) != null) {
            String[] recipeParts = line.split("[\\[\\]]");
            String[] recipe = recipeParts[0].split(",");
            String name = recipe[0];
            int recipeId = Integer.parseInt(recipe[1]);
            int cookTime = Integer.parseInt(recipe[2]);
            String author = recipe[3];
            String[] categories = recipeParts[1].split("'");   // check these receipeParts numbers
            String steps = recipeParts[5].substring(1);
            if (steps.length() >= 5000) {
                steps = steps.substring(0, 4999);
            }
            String desc = recipeParts[6];
            if (desc.length() == 0) {
                desc = "";
            }
            else {
                desc = recipeParts[6].substring(2);
            }
            if (desc.length() >= 200) {
                desc = desc.substring(0, 199);
            }
            String[] ingredients = recipeParts[7].split("'");


            // String description = recipe[9];
            // recipe requires entity
            // String ingredients = recipe[10];
            // String n_ingred = recipe[11];


            // store recipeId, author, steps, description, cooktime, servings, difficulty, name in the recipe
            PreparedStatement st1 = con.prepareStatement("insert into recipe values (?, ?, ?, ?, ?, ?, ?, ?);");
            st1.setInt(1, 1);
            st1.setInt(1, recipeId);
            //st1.setString(2, "Teagan");
            st1.setString(2, author);
            //st1.setString(8, "Mush");
            st1.setString(8, name);
            //st1.setString(4, "Mushy");
            st1.setString(4, desc);
            //st1.setInt(7, 3);
            st1.setInt(7, 3); //difficulty = Medium
            //st1.setString(3, "Take indredients and MUSH");
            st1.setString(3, steps);
            //st1.setInt(6, 1);
            st1.setInt(6, 1); //servings
            //st1.setInt(5, 5);
            st1.setInt(5, cookTime);
            st1.executeUpdate();

            // create user from author if they don't already exist
            String password = "password";
            String username = author.strip();
            PreparedStatement s = con.prepareStatement("SELECT username FROM netizen WHERE username=?;");
            s.setString(1, username);
            ResultSet user = s.executeQuery();
            if (!user.next()) {
                s = con.prepareStatement("INSERT INTO netizen VALUES (?, ?, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, ?);");
                // String salt = DataBase.generateSalt(); 

                // byte[] saltBytes = salt.getBytes();
    
                //     PBEKeySpec spec = new PBEKeySpec(password.toCharArray(), saltBytes, 1000, 128);
                // try {
                //     SecretKeyFactory factory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA512");
                //     byte[] hashed =  factory.generateSecret(spec).getEncoded();
                //     hashed = new String(hashed).getBytes("UTF-8");
                //     String hashPsswrd = new String(hashed);
                    s.setString(1, username);
                    s.setString(2, password);
                    s.setString(3, "");
                    s.executeUpdate();
                // } catch (NoSuchAlgorithmException | InvalidKeySpecException e) {
                //     e.printStackTrace();
                // } finally {
                //     spec.clearPassword();
                // }
             
            }
            // parse and create categories
            int size = categories.length;
            for(int i = 0; i < size; i++){

                String currTag = categories[i];

                //check that element of categories is not "" or ", "
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


            // parse and create ingredients
            size = ingredients.length;
            for(int i = 0; i < size; i++){

                String currIngred = ingredients[i];

                //check that element of ingredients is not "" or ", " or " "
                if(!(currIngred.equals("") || currIngred.equals(", ") || currIngred.equals(" "))){

                    try {
                        ResultSet rs = null;
                        PreparedStatement st = con.prepareStatement("SELECT ingredientId FROM ingredient WHERE name=?;");
                        st.setString(1, currIngred.strip());
                        boolean exists = st.execute();
                        int ingredId = -1;
                        if (exists) {
                            rs = st.getResultSet();
                            // if ingredient does not already exist, create it, else grab the existing ingred's ID
                            if (!rs.isBeforeFirst()) {
                                st = con.prepareStatement("Select max(ingredientId) from ingredient;");
                                rs = st.executeQuery();
                                rs.next();
                                ingredId = rs.getInt(1) + 1;
                                st = con.prepareStatement("Insert into ingredient values(?, ?);");
                                st.setInt(1, ingredId);
                                st.setString(2, currIngred);
                                st.executeUpdate();
                            }
                            else{
                                rs.next();
                                ingredId = rs.getInt(1);
                            }
                        }

                        // add ingredient to recipeRequires
                        st = con.prepareStatement("insert into recipe_requires values(?, ?, ?, ?);");
                        st.setInt(1, recipeId);
                        st.setInt(2, ingredId);
                        st.setInt(3, 1);
                        st.setString(4, "");
                        st.executeUpdate();

                    } catch (SQLException e) {
                        System.exit(0);
                    }  
                }  
            } 
            
        }

        br.close();
        fr.close();
    }

    private String formatDate (String text) {
        String[] d = text.split("/");
        String year = d[2];
        String month = d[0];
        String day = d[1];
        if (day.length() == 1) {
            day = "0" + day;
        }
        if (month.length() == 1) {
            month = "0" + month;
        }
        String dateForm = year + "-" + month + "-" + day;
        return dateForm;
    }

    private void getDates (Connection con) throws IOException {
        FileReader fr = new FileReader("datasets/RAW_recipes.csv"); // I'm running from recipe-database
        BufferedReader br = new BufferedReader(fr);
        String line = br.readLine();
        while ((line = br.readLine()) != null) {
            String[] recipeParts = line.split("[\\[\\]]");
            String[] recipe = recipeParts[0].split(",");
            int recipeId = Integer.parseInt(recipe[1]);
            String dateForm = formatDate(recipe[4]);
            
            Date date = Date.valueOf(dateForm);

            ResultSet rs = null;
            try {
                PreparedStatement st = con.prepareStatement("SELECT recipeId FROM recipe WHERE recipeId=?;");
                st.setInt(1, recipeId);
                boolean exists = st.execute();
                if (exists) {
                    rs = st.getResultSet();
                    rs.next();
                    st = con.prepareStatement("UPDATE recipe set date=? where recipeId=?;");
                    st.setDate(1, date);
                    st.setInt(2, recipeId);
                    st.executeUpdate();
                }
            } catch (SQLException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }

        br.close();
        fr.close();
    }

    private Connection dbLogin (String username, String password) {
        try {
            // connect to ssh
            Class.forName("org.postgresql.Driver");
            JSch jsch = new JSch();

            Session session = jsch.getSession(username, "starbug.cs.rit.edu", 22);
            session.setPassword(password);
            session.setConfig("StrictHostKeyChecking", "no");
            session.connect();
            session.setPortForwardingL(1001, "localhost", 5432);

        }
        catch (JSchException | ClassNotFoundException ssh){
            return null;
        }

        try {
            // now connect to DB
            String dbUrl = "jdbc:postgresql://localhost:1001/p32002_31";
            Connection con = DriverManager.getConnection(dbUrl, username, password);
            return con;

        }
        catch (SQLException e) {
            return null;
        }

    }

    public static void main(String[] args) throws IOException, SQLException {
        // make connection to database
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        System.out.println("Username: ");
 
        // Reading data using readLine
        String username = reader.readLine().trim();
        System.out.println("Password: ");
        String password = reader.readLine().trim();
        
        // Uncomment the below to read in recipe data
        // readData rd = new readData();
        // Connection con = rd.dbLogin(username, password);
        // //rd.readRecipes(con);
        // rd.getDates(con);
        // System.exit(1);


        // Uncomment the below to read in the recipe review data
        readData rd = new readData();
        Connection con = rd.dbLogin(username, password);
        //rd.readRecipes(con);
        //rd.getDates(con);
        rd.readReviews(con);
        System.exit(1);
    }
    
}
