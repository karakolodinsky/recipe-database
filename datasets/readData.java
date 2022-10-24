package datasets;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

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

            // // insert steps to parse and create categories
            String[] categories = recipeParts[1].split("'");
            int size = categories.length;
            //check that element of categories is not "" or ", "
            // if category does not already exist, create it
            // add category to recipe_category

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
