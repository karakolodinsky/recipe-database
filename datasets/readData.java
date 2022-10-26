package datasets;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.util.Random;
import java.util.random.*;

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

    public void readRecipes (Connection con) throws IOException, SQLException {
        //Connection con = DataBase.getCon();
        FileReader fr = new FileReader("datasets/test_recipe.csv"); // I'm running from recipe-database
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
            String steps = recipeParts[5];
            String desc = recipeParts[6];
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
            Random random = new Random();
            String password = String.valueOf(random.nextInt());
            String username = author.strip();
            PreparedStatement s = con.prepareStatement("SELECT username FROM netizen WHERE username=?;");
            s.setString(1, username);
            ResultSet user = s.executeQuery();
            if (!user.next()) {
                s = con.prepareStatement("INSERT INTO netizen VALUES (?, ?, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, ?);");
            String salt = DataBase.generateSalt(); 

            byte[] saltBytes = salt.getBytes();
    
            PBEKeySpec spec = new PBEKeySpec(password.toCharArray(), saltBytes, 1000, 128);
            try {
                SecretKeyFactory factory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA512");
                byte[] hashed =  factory.generateSecret(spec).getEncoded();
                String hashPsswrd = new String(hashed);
                s.setString(1, username);
                s.setString(2, hashPsswrd);
                s.executeUpdate();
            } catch (NoSuchAlgorithmException | InvalidKeySpecException e) {
                e.printStackTrace();
            } finally {
                spec.clearPassword();
            }
             
            }
            // parse and create categories
            int size = categories.length;
            for(int i = 0; i < size; i++){

                String currTag = categories[i];

                //check that element of categories is not "" or ", "
                if(currTag.equals("") || currTag.equals(", ") || currTag.equals(" ")){
                        break;
                }
                try {
                    PreparedStatement st = con.prepareStatement("SELECT categoryId FROM category WHERE name=?;");
                    st.setString(1, currTag.strip());
                    ResultSet rs = st.executeQuery();
                    int categoryId = -1;

                    // if category does not already exist, create it, else grab existing category's Id
                    if (!rs.next()) {
                        st = con.prepareStatement("Select max(categoryId) from category;");
                        rs = st.executeQuery();
                        categoryId = rs.getInt(1) + 1;
                        st = con.prepareStatement("Insert into category values('?','?');");
                        st.setInt(1, categoryId);
                        st.setString(2, currTag);
                        st.executeUpdate();
                    }
                    else{
                        categoryId = rs.getInt(1);
                    }

                    // add category to recipeCategory
                    st = con.prepareStatement("insert into recipeCategory values('?', '?');");
                    st.setInt(1, recipeId);
                    st.setInt(2, categoryId);

                } catch (SQLException e) {
                    JOptionPane.showMessageDialog(null, "Database statement error", "Database",
                            JOptionPane.ERROR_MESSAGE);
                }    
            }


            // parse and create ingredients
            size = ingredients.length;
            for(int i = 0; i < size; i++){

                String currIngred = ingredients[i];

                //check that element of categories is not "" or ", " or " "
                if(currIngred.equals("") || currIngred.equals(", ") || currIngred.equals(" ")){
                        break;
                }
                try {
                    PreparedStatement st = con.prepareStatement("SELECT ingredientId FROM ingredient WHERE name=?;");
                    st.setString(1, currIngred.strip());
                    ResultSet rs = st.executeQuery();
                    int ingredId = -1;

                    // if ingredient does not already exist, create it, else grab the existing ingred's ID
                    if (!rs.next()) {
                        st = con.prepareStatement("Select max(ingredientId) from ingredient;");
                        rs = st.executeQuery();
                        ingredId = rs.getInt(1) + 1;
                        st = con.prepareStatement("Insert into ingredient values('?','?');");
                        st.setInt(1, ingredId);
                        st.setString(2, currIngred);
                        st.executeUpdate();
                    }
                    else{
                        ingredId = rs.getInt(1);
                    }

                    // add ingredient to recipeRequires
                    st = con.prepareStatement("insert into recipeRequires values('?', '?', '?', '?');");
                    st.setInt(1, recipeId);
                    st.setInt(2, ingredId);
                    st.setString(3, "1");
                    st.setString(4, "grams");
                    st.executeUpdate();

                } catch (SQLException e) {
                    System.exit(0);
                }    
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
        
        readData rd = new readData();
        Connection con = rd.dbLogin(username, password);
        rd.readRecipes(con);
    }
    
}
