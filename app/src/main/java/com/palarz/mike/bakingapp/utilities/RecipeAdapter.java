/*
The following code is the property and sole work of Mike Palarz, a student at Udacity.
 */

package com.palarz.mike.bakingapp.utilities;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.palarz.mike.bakingapp.R;
import com.palarz.mike.bakingapp.activities.RecipeDetails;
import com.palarz.mike.bakingapp.model.Recipe;
import com.squareup.picasso.Picasso;

import java.util.List;
import java.util.Random;

import butterknife.BindView;
import butterknife.ButterKnife;
import timber.log.Timber;

public class RecipeAdapter extends RecyclerView.Adapter<RecipeAdapter.RecipeViewHolder> {

    public static final String EXTRA_RECIPE_ID = "com.palarz.mike.bakingapp.recipe_id";

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
        if  (recipe.getImage() == null || recipe.getImage().isEmpty()){
            int imageResource = Utilities.getImageResource(recipe);
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
                    .error(Utilities.getImageResource(recipe))
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
        @BindView(R.id.recipe_list_item_name) public TextView vName;
        @BindView(R.id.recipe_list_item_image) public ImageView vImage;

        public RecipeViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
            view.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            Recipe clickedRecipe = mRecipeList.get(this.getAdapterPosition());
            int recipeID = clickedRecipe.getID();
            Intent makingTheRecipeIntent = new Intent(mContext, RecipeDetails.class);
            makingTheRecipeIntent.putExtra(EXTRA_RECIPE_ID, recipeID);
            mContext.startActivity(makingTheRecipeIntent);
        }
    }

}
