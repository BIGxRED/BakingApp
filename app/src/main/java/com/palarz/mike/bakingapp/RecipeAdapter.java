package com.palarz.mike.bakingapp;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.List;
import java.util.Random;

/**
 * Created by mpala on 9/30/2017.
 */

public class RecipeAdapter extends RecyclerView.Adapter<RecipeAdapter.RecipeViewHolder> {

    private static final String TAG = RecipeAdapter.class.getSimpleName();

    public static final String EXTRA_RECIPE = "com.palarz.mike.bakingapp.recipe";

    public static final int[] IMAGE_RESOURCES = new int[]{R.drawable.recipe1,
                                                            R.drawable.recipe2,
                                                            R.drawable.recipe3,
                                                            R.drawable.recipe4,
                                                            R.drawable.recipe5,
                                                            R.drawable.recipe6,
                                                            R.drawable.recipe7,
                                                            R.drawable.recipe8};

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

        //TODO: Maybe find a placeholder image for when the image is attempted to being loaded?
        Picasso.with(mContext)
                .load(recipe.getImage())
                .error(getRandomImageResource())
                .into(recipeViewHolder.vImage);
    }

    @Override
    public RecipeViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
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
            Intent makingTheRecipeIntent = new Intent(mContext, RecipeDetailsActivity.class);
            makingTheRecipeIntent.putExtra(EXTRA_RECIPE, clickedRecipe);
            mContext.startActivity(makingTheRecipeIntent);
        }
    }

    public int getRandomImageResource(){
        int randomIndex = new Random().nextInt(IMAGE_RESOURCES.length);
        Log.d(TAG, "Result of random index: " + randomIndex);

        return IMAGE_RESOURCES[randomIndex];
    }
}
