package com.example.design;
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

public class MenuAdapter extends RecyclerView.Adapter<MenuAdapter.ViewHolder> {

    private final List<RecipeSearchResponse.Recipe> recipeList;
    private final RecipeListener recipeListener;
    private OnMealClickListener onMealClickListener;

    public MenuAdapter(List<RecipeSearchResponse.Recipe> recipeList, RecipeListener recipeListener) {
        this.recipeList = recipeList;
        this.recipeListener = recipeListener;
    }

    public void setOnMealClickListener(OnMealClickListener onMealClickListener) {
        this.onMealClickListener = onMealClickListener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.menu_item, parent, false);
        return new ViewHolder(view); // Pass the listener within onBindViewHolder instead
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        RecipeSearchResponse.Recipe recipe = recipeList.get(position);
        holder.textViewTitle.setText(recipe.title);
        Picasso.get().load(recipe.image).into(holder.imageViewItem);

        // Here we set the click listener and ensure we reference the correct item from recipeList
        holder.itemView.setOnClickListener(v -> {
            if (onMealClickListener != null) {
                onMealClickListener.onMealClick(recipeList.get(holder.getAdapterPosition()));
            }
        });

        holder.btnAddToMain.setOnClickListener(v -> {
            if (recipeListener != null) {
                recipeListener.onRecipeSelected(recipe.title, "");
            }
        });
    }

    @Override
    public int getItemCount() {
        return recipeList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView imageViewItem;
        TextView textViewTitle;
        Button btnAddToMain;

        public ViewHolder(View view) {
            super(view);
            imageViewItem = view.findViewById(R.id.imageView_menu_item);
            textViewTitle = view.findViewById(R.id.textView_menu_title);
            btnAddToMain = view.findViewById(R.id.btnAddToMain);
        }
    }

    public interface OnMealClickListener {
        void onMealClick(RecipeSearchResponse.Recipe recipe);
    }

    public interface RecipeListener {
        void onRecipeSelected(String recipeName, String mealTime);
    }
}
