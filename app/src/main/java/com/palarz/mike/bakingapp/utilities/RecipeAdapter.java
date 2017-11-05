package com.palarz.mike.bakingapp.utilities;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.palarz.mike.bakingapp.R;
import com.palarz.mike.bakingapp.activities.RecipeDetails;
import com.palarz.mike.bakingapp.data.Recipe;
import com.squareup.picasso.Picasso;

import java.util.List;
import java.util.Random;

/**
 * Created by mpala on 9/30/2017.
 */

public class RecipeAdapter extends RecyclerView.Adapter<RecipeAdapter.RecipeViewHolder> {

    private static final String TAG = RecipeAdapter.class.getSimpleName();

    public static final String EXTRA_RECIPE = "com.palarz.mike.bakingapp.recipe";

    public static final int[] RECIPE_IMAGES = {
            R.drawable.brownies,
            R.drawable.cheesecake,
            R.drawable.nutella,
            R.drawable.yellow_cake,
            R.drawable.random_recipe1,
            R.drawable.random_recipe2,
            R.drawable.random_recipe3,
            R.drawable.random_recipe4,
            R.drawable.random_recipe5,
            R.drawable.random_recipe6,
            R.drawable.random_recipe7,
            R.drawable.random_recipe8
    };

    public static final int INDEX_BROWNIES = 0;
    public static final int INDEX_CHEESECAKE = 1;
    public static final int INDEX_NUTELLA = 2;
    public static final int INDEX_YELLOW_CAKE = 3;
    public static final int INDEX_RANDOM_BEGINNING = 4;
    public static final int IMAGE_RESOURCES_LENGTH = RECIPE_IMAGES.length;

    private List<Recipe> mRecipeList;
    private Context mContext;

    public RecipeAdapter(Context context, List<Recipe> recipeList) {
        this.mRecipeList = recipeList;
        this.mContext = context;
    }

    @Override
    public int getItemCount() {
        if (mRecipeList == null){
            return 0;
        }
        return mRecipeList.size();
    }

    @Override
    public void onBindViewHolder(RecipeViewHolder recipeViewHolder, int position) {
        Recipe recipe = mRecipeList.get(position);

        recipeViewHolder.vName.setText(recipe.getName());

        // If the image property of the Recipe is empty in any way, we'll simply load a random
        // recipe image from drawables
        if  (recipe.getImage() == null || recipe.getImage() == "" || recipe.getImage().isEmpty()){
            int imageResource = getImageResource(recipe);
            recipe.setImage(String.valueOf(imageResource));
            mRecipeList.set(position, recipe);

            Picasso.with(mContext)
                    .load(Integer.parseInt(recipe.getImage()))
                    .placeholder(R.drawable.hourglass)
                    .into(recipeViewHolder.vImage);
        }

        // Otherwise, we'll load the image into our CardView
        else {
            Picasso.with(mContext)
                    .load(recipe.getImage())
                    .placeholder(R.drawable.hourglass)
                    .error(getImageResource(recipe))
                    .into(recipeViewHolder.vImage);
        }
    }

    @Override
    public RecipeViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        View itemView = LayoutInflater
                .from(viewGroup.getContext())
                .inflate(R.layout.recipe_list_item, viewGroup, false);

        return new RecipeViewHolder(itemView);
    }

    public void swapRecipes(List<Recipe> newRecipes){
        mRecipeList = newRecipes;
        notifyDataSetChanged();
    }


    public class RecipeViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public TextView vName;
        public ImageView vImage;

        public RecipeViewHolder(View view) {
            super(view);
            vName = view.findViewById(R.id.recipe_list_item_name);
            vImage = view.findViewById(R.id.recipe_list_item_image);
            view.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            Recipe clickedRecipe = mRecipeList.get(this.getAdapterPosition());
            Intent makingTheRecipeIntent = new Intent(mContext, RecipeDetails.class);
            makingTheRecipeIntent.putExtra(EXTRA_RECIPE, clickedRecipe);
            mContext.startActivity(makingTheRecipeIntent);
        }
    }

    /*
    The purpose of this function is to generate a random drawable ID in case the Recipe object
    does not have anything specified for the JSON "image" key. This function has taken into account
    that some of the Recipes are known based on the current state of the JSON data. Therefore,
    a drawable ID is only created for the random drawables that have been provided.
     */
    public int getRandomImageResource(){
        /*
        This version of nextInt() allows us to place a range for the int value that is returned -
        the argument for nextInt() is exclusive of the random number that will be generated.
        Therefore, nextInt() will generate a random int within 0 inclusive and the argument value
        exclusive. We than add INDEX_RANDOM_BEGINNING to randomIndex to make sure that the returned
        value is always at least INDEX_RANDOM_BEGINNING.
         */
        int randomIndex = new Random().nextInt(IMAGE_RESOURCES_LENGTH - INDEX_RANDOM_BEGINNING) + INDEX_RANDOM_BEGINNING;

        return RECIPE_IMAGES[randomIndex];
    }

    public int getImageResource(Recipe recipe){
        String recipeName = recipe.getName();

        switch (recipeName){
            case Recipe.RECIPE_BROWNIES:
                return RECIPE_IMAGES[INDEX_BROWNIES];
            case Recipe.RECIPE_CHEESECAKE:
                return RECIPE_IMAGES[INDEX_CHEESECAKE];
            case Recipe.RECIPE_NUTELLA_PIE:
                return RECIPE_IMAGES[INDEX_NUTELLA];
            case Recipe.RECIPE_YELLOW_CAKE:
                return RECIPE_IMAGES[INDEX_YELLOW_CAKE];
            default:
                return getRandomImageResource();
        }
    }
}
