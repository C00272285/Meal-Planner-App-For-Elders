package com.example.design;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.squareup.picasso.Picasso;
import java.util.List;

public class MenuAdapter extends RecyclerView.Adapter<MenuAdapter.ViewHolder>
{

    private Context context;
    private List<RecipeSearchResponse.Recipe> recipeList;

    public MenuAdapter(Context context, List<RecipeSearchResponse.Recipe> recipeList)
    {
        this.context = context;
        this.recipeList = recipeList;
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
    }

    @Override
    public int getItemCount()
    {
        return recipeList.size();
    }

    //getting the view ID's from the XML file for image and text
    public static class ViewHolder extends RecyclerView.ViewHolder
    {
        public ImageView imageViewItem;
        public TextView textViewTitle;

        public ViewHolder(View view) {
            super(view);
            imageViewItem = view.findViewById(R.id.imageView_menu_item);
            textViewTitle = view.findViewById(R.id.textView_menu_title);
        }
    }
}