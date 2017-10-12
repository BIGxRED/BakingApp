package com.palarz.mike.bakingapp;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

/**
 * Created by mpala on 9/30/2017.
 */

public class RecipeAdapter extends RecyclerView.Adapter<RecipeAdapter.RecipeViewHolder> {

    public static final String EXTRA_RECIPE = "com.palarz.mike.bakingapp.recipe";

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
        protected TextView vName;


        public RecipeViewHolder(View v) {
            super(v);
            vName =  (TextView) v.findViewById(R.id.recipe_name);
            vName.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            Recipe clickedRecipe = mRecipeList.get(this.getAdapterPosition());
            Intent makingTheRecipeIntent = new Intent(mContext, MakingTheRecipeActivity.class);
            makingTheRecipeIntent.putExtra(EXTRA_RECIPE, clickedRecipe);
            mContext.startActivity(makingTheRecipeIntent);
        }
    }
}
