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
import java.sql.Connection;
import java.sql.Date;
import java.sql.DriverManager;
import java.util.Random;
//import java.util.random.*;

import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import javax.swing.JOptionPane;

//import org.postgresql.util.OSUtil;

import com.jcraft.jsch.JSch;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.Session;

import login.DataBase;

public class readingmore {

        readingmore () {

                
        
    }

    public void readpant (Connection con) throws IOException, SQLException {
        // Connection con = DataBase.getCon();
        FileReader fr = new FileReader("datasets/inpantry.csv"); // I'm running from recipe-database
        BufferedReader br = new BufferedReader(fr);
        String name = "";
        String line = br.readLine();
        while ((line = br.readLine()) != null) {
            String unit;
            String[] recipeParts = line.split("[\\[\\]]");
            String[] recipe = recipeParts[0].split(",");
            int name1 = Integer.parseInt(recipe[0]);
            int ingredientId = Integer.parseInt(recipe[1]);
            int quantitybought = Integer.parseInt(recipe[3]);
            int currentquantity = Integer.parseInt(recipe[4]);
            int unit1 = Integer.parseInt(recipe[6]);
            if (unit1 == 0){
                 unit = "fl oz";
            }
            else if (unit1 == 1){
                 unit = "oz";
            } else if (unit1 == 2){
             unit = "grams"; }
            else {
                 unit = "unit name";
            }
            if (currentquantity> quantitybought){
                currentquantity = quantitybought;
            }

            if (currentquantity == 0 || quantitybought == 0){
                currentquantity = 33;
                quantitybought = 33;
            }
            PreparedStatement st8 = con.prepareStatement("SELECT username FROM netizen ORDER BY RANDOM() LIMIT 1");
            ResultSet rs = st8.executeQuery();
            if (rs.next()){
                
            name = rs.getString("username");

            }

            PreparedStatement st9 = con.prepareStatement("SELECT ingredientid FROM ingredient ORDER BY RANDOM() LIMIT 1");
            ResultSet rs9 = st9.executeQuery();
            if (rs9.next()){
                
            ingredientId = rs9.getInt("ingredientid");

            }



            // store recipeId, author, steps, description, cooktime, servings, difficulty, name in the recipe
            PreparedStatement st1 = con.prepareStatement("insert into in_pantry values (?, ?, now(), ?, ?, now(), ?);");
            st1.setString(1, name);
            //st1.setString(2, "Teagan");
            st1.setInt(2, ingredientId);
            //st1.setString(8, "Mush");
            //st1.setString(4, "Mushy");
            st1.setInt(3, quantitybought);
            //st1.setInt(7, 3);
            st1.setInt(4, currentquantity); //difficulty = Medium
            
        //st1.setString(3, "Take indredients and MUSH");
            //st1.setInt(6, 1);
            st1.setString(5, unit); //servings
            //st1.setInt(5, 5);
            st1.executeUpdate();
            System.out.println("query :^)");

        }

        br.close();
        fr.close();
    }

    private static void getDates (Connection con) throws IOException {
        FileReader fr = new FileReader("datasets/RAW_recipes.csv"); // I'm running from recipe-database
        BufferedReader br = new BufferedReader(fr);
        String line = br.readLine();
        while ((line = br.readLine()) != null) {
            String[] recipeParts = line.split("[\\[\\]]");
            String[] recipe = recipeParts[0].split(",");
            int recipeId = Integer.parseInt(recipe[1]);
            String[] d = recipe[4].split("/");
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

    private static Connection dbLogin (String username, String password) {
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
        
        readingmore rd = new readingmore();
        Connection con = dbLogin(username, password);
        //rd.readRecipes(con);
        rd.readpant(con);
        // getDates(con);
        System.exit(1);
    }
    
}

