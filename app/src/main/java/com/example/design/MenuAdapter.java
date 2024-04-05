package com.example.design;
import android.annotation.SuppressLint;
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
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType)
    {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.menu_item, parent, false);
        return new ViewHolder(view); // Pass the listener within onBindViewHolder instead
    }

    @SuppressLint("DefaultLocale")
    @Override
    public void onBindViewHolder(ViewHolder holder, int position)
    {
        RecipeSearchResponse.Recipe recipe = recipeList.get(position);
        holder.textViewTitle.setText(recipe.title);
        Picasso.get().load(recipe.image).into(holder.imageViewItem);
        holder.cookingTimeText.setText(String.format("Cooking time: %d min", recipe.cookingTime));
        holder.servingSizeText.setText(String.format("Serves: %d", recipe.servingSize));


        holder.itemView.setOnClickListener(v ->
        {
            if (onMealClickListener != null)
            {
                onMealClickListener.onMealClick(recipeList.get(holder.getAdapterPosition()));
            }
        });

        holder.btnAddToMain.setOnClickListener(v -> showMealTimeDialog(holder.itemView.getContext(), recipe));
    }

    @Override
    public int getItemCount() {
        return recipeList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView imageViewItem;
        TextView textViewTitle, cookingTimeText, servingSizeText;
        Button btnAddToMain;

        public ViewHolder(View view) {
            super(view);
            imageViewItem = view.findViewById(R.id.imageView_menu_item);
            textViewTitle = view.findViewById(R.id.textView_menu_title);
            btnAddToMain = view.findViewById(R.id.btnAddToMain);
            cookingTimeText = view.findViewById(R.id.servingTimeText);
            servingSizeText = view.findViewById(R.id.servingSizeText);
        }
    }


    private void showMealTimeDialog(Context context, RecipeSearchResponse.Recipe recipe) {
        final String[] mealTimes = {"Breakfast", "Lunch", "Dinner"};
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("Choose Meal Time")
                .setItems(mealTimes, (dialog, which) -> {
                    String selectedMealTime = mealTimes[which];
                    // Notify the activity (or whoever is listening) of the selected meal time
                    if (recipeListener != null) {
                        recipeListener.onRecipeSelected(recipe.title, selectedMealTime);
                    }
                });
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    public interface OnMealClickListener {
        void onMealClick(RecipeSearchResponse.Recipe recipe);
    }

    public interface RecipeListener {
        void onRecipeSelected(String recipeName, String mealTime);
    }
}