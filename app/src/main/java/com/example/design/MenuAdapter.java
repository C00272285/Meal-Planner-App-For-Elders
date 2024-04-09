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
    private final Context context;
    private final List<RecipeSearchResponse.Recipe> recipeList;
    private final OnMealClickListener onMealClickListener;

    public interface OnMealClickListener
    {
        void onMealClick(RecipeSearchResponse.Recipe recipe);
    }

    public MenuAdapter(Context context, List<RecipeSearchResponse.Recipe> recipeList, OnMealClickListener onMealClickListener)
    {
        this.context = context;
        this.recipeList = recipeList;
        this.onMealClickListener = onMealClickListener;
    }
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType)
    {
        View view = LayoutInflater.from(context).inflate(R.layout.menu_item, parent, false);
        return new ViewHolder(view);
    }
    @Override
    public void onBindViewHolder(ViewHolder holder, int position)
    {
        RecipeSearchResponse.Recipe recipe = recipeList.get(position);
        holder.textViewTitle.setText(recipe.title);
        Picasso.get().load(recipe.image).into(holder.imageViewItem);

        holder.itemView.setOnClickListener(v -> {
            if (onMealClickListener != null)
            {
                onMealClickListener.onMealClick(recipe);
            }
        });
    }
    @Override
    public int getItemCount()
    {
        return recipeList.size();
    }
    public static class ViewHolder extends RecyclerView.ViewHolder
    {
        ImageView imageViewItem;
        TextView textViewTitle;

        public ViewHolder(View view)
        {
            super(view);
            imageViewItem = view.findViewById(R.id.imageView_menu_item);
            textViewTitle = view.findViewById(R.id.textView_menu_title);
        }
    }
}
