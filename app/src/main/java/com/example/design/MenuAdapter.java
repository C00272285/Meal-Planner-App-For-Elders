package com.example.design;

import android.app.AlertDialog;
import android.content.Context;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.squareup.picasso.Picasso;
import java.util.List;

public class MenuAdapter extends RecyclerView.Adapter<MenuAdapter.ViewHolder>
{

    private final Context context;
    private final List<RecipeSearchResponse.Recipe> recipeList;
    private final RecipeListener listener;


    public MenuAdapter(Context context, List<RecipeSearchResponse.Recipe> recipeList, RecipeListener listener)
    {
        this.context = context;
        this.recipeList = recipeList;
        this.listener = listener;
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType)
    {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.menu_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position)
    {
        RecipeSearchResponse.Recipe recipe = recipeList.get(position);
        holder.textViewTitle.setText(recipe.title);
        Picasso.get().load(recipe.image).into(holder.imageViewItem);

        holder.btnAddToMain.setOnClickListener(v -> showMealTimeDialog(recipe.title));
    }



    @Override
    public int getItemCount() {
        return recipeList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder
    {
        public ImageView imageViewItem;
        public TextView textViewTitle;
        public Button btnAddToMain;

        public ViewHolder(View view)
        {
            super(view);
            imageViewItem = view.findViewById(R.id.imageView_menu_item);
            textViewTitle = view.findViewById(R.id.textView_menu_title);
            btnAddToMain = view.findViewById(R.id.btnAddToMain);
        }
    }

    public interface RecipeListener
    {
        void onRecipeSelected(String recipeName, String mealTime);
    }

    private void showMealTimeDialog(String recipeName)
    {
        final String[] mealTimes = {"Breakfast", "Lunch", "Dinner"};
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("Add to Meal")
                .setItems(mealTimes, (dialog, which) ->
                {
                    String mealTime = mealTimes[which];
                    if(listener != null) {
                        listener.onRecipeSelected(recipeName, mealTime);
                    }
                });
        AlertDialog dialog = builder.create();
        dialog.show();
    }

}
