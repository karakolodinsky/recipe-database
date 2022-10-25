package datasets;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.swing.JOptionPane;

import login.DataBase;

public class readData {

    readData () {

    }

    public void readRecipes () throws IOException {
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

            String steps = recipeParts[5];
            // String description = recipe[9];
            // recipe requires entity
            // create ingredients if they don't already exist
            // String ingedients = recipe[10];
            // String n_ingred = recipe[11];

            // insert steps to parse and create categories
            String[] categories = recipeParts[1].split("'");
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

                     // if category does not already exist, create it
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

            // insert steps to parse steps
            
            
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
