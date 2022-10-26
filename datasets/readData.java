package datasets;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Connection;

import javax.swing.JOptionPane;

import login.DataBase;

public class readData {

    readData () {

    }

    public void readRecipes () throws IOException {
        Connection con = DataBase.getConnect();
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
            String steps = recipeParts[5];
            String desc = recipeParts[6];
            String[] ingredients = recipeParts[7].split("'");


            // String description = recipe[9];
            // recipe requires entity
            // String ingredients = recipe[10];
            // String n_ingred = recipe[11];


            // store author, name, description, difficulty, steps, servings, and cooktime in the recipe
            try{

                PreparedStatement st = con.prepareStatement("insert into recipe(author, name, description, difficulty, steps, servings, cooktime) values ('?', '?', '?', '?', '?', '?', '?')");
                st.setString(1, author);
                st.setString(2, name);
                st.setString(3, desc);
                st.setString(4, String.valueOf(3));
                st.setString(5, steps);
                st.setString(6, String.valueOf(2));
                st.setString(7, String.valueOf(cookTime));
                st.executeQuery();
            }
            catch (SQLException e){
                JOptionPane.showMessageDialog(null, "Database statement error", "Database",
                            JOptionPane.ERROR_MESSAGE);
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
                    PreparedStatement st = login.DataBase.con.prepareStatement("SELECT categoryId FROM category WHERE name=?");
                    st.setString(1, currTag.strip());
                    ResultSet rs = st.executeQuery();
                    int categoryId = -1;

                    // if category does not already exist, create it, else grab existing category's Id
                    if (!rs.next()) {
                        st = login.DataBase.con.prepareStatement("Select max(categoryId) from category");
                        rs = st.executeQuery();
                        categoryId = rs.getInt(1) + 1;
                        st = login.DataBase.con.prepareStatement("Insert into category values('?','?'");
                        st.setString(1, String.valueOf(categoryId));
                        st.setString(2, currTag);
                        rs = st.executeQuery();
                    }
                    else{
                        categoryId = rs.getInt(1);
                    }

                    // add category to recipeCategory
                    st = login.DataBase.con.prepareStatement("insert into recipeCategory values('?', '?'");
                    st.setString(1, String.valueOf(recipeId));
                    st.setString(2, String.valueOf(categoryId));

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
                    PreparedStatement st = login.DataBase.con.prepareStatement("SELECT ingredientId FROM ingredient WHERE name=?");
                    st.setString(1, currIngred.strip());
                    ResultSet rs = st.executeQuery();
                    int ingredId = -1;

                    // if ingredient does not already exist, create it, else grab the existing ingred's ID
                    if (!rs.next()) {
                        st = login.DataBase.con.prepareStatement("Select max(ingredientId) from ingredient");
                        rs = st.executeQuery();
                        ingredId = rs.getInt(1) + 1;
                        st = login.DataBase.con.prepareStatement("Insert into ingredient values('?','?'");
                        st.setString(1, String.valueOf(ingredId));
                        st.setString(2, currIngred);
                        st.executeUpdate();
                    }
                    else{
                        ingredId = rs.getInt(1);
                    }

                    // add ingredient to recipeRequires
                    st = login.DataBase.con.prepareStatement("insert into recipeRequires values('?', '?', '?', '?'");
                    st.setString(1, String.valueOf(recipeId));
                    st.setString(2, String.valueOf(ingredId));
                    st.setString(3, "1");
                    st.setString(4, "grams");
                    st.executeUpdate();

                } catch (SQLException e) {
                    JOptionPane.showMessageDialog(null, "Database statement error", "Database",
                            JOptionPane.ERROR_MESSAGE);
                }    
            } 
            
        }

        br.close();
        fr.close();
    }

    public static void main(String[] args) throws IOException {
        // make connection to database
        readData rd = new readData();
        rd.readRecipes();
    }
    
}
