package com.example.design;
import android.annotation.SuppressLint;
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
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.menu_item, parent, false);
        return new ViewHolder(view);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        RecipeSearchResponse.Recipe recipe = recipeList.get(position);
        holder.textViewTitle.setText(recipe.title);
        Picasso.get().load(recipe.image).into(holder.imageViewItem);
        // if there is no cookingTime or ServingSize information available then display "not available"
        String readyInMinutesText = recipe.readyInMinutes > 0 ? recipe.readyInMinutes + " min" : "Not available";
        String servingsText = recipe.servings > 0 ? String.valueOf(recipe.servings) : "Not available";

        holder.CookingTime.setText("Cooking Time: " + readyInMinutesText);
        holder.ServingSize.setText("Servings: " + servingsText);


        holder.itemView.setOnClickListener(v -> {
            if (onMealClickListener != null) {
                onMealClickListener.onMealClick(recipe);
            }
        });
    }

    @Override
    public int getItemCount()
    {
        return recipeList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder
    {
        ImageView imageViewItem;
        TextView textViewTitle;
        TextView CookingTime;
        TextView ServingSize;
        Button btnAddToMain;

        public ViewHolder(View view) {
            super(view);
            imageViewItem = view.findViewById(R.id.imageView_menu_item);
            textViewTitle = view.findViewById(R.id.textView_menu_title);
            CookingTime = view.findViewById(R.id.servingTimeText);
            ServingSize = view.findViewById(R.id.servingSizeText);
            btnAddToMain = view.findViewById(R.id.btnAddToMain);


            btnAddToMain.setOnClickListener(v -> {
                int position = getAdapterPosition();
                if (position != RecyclerView.NO_POSITION)
                {
                    RecipeSearchResponse.Recipe recipe = recipeList.get(position);
                    if (context instanceof MenuActivity)
                    {
                        ((MenuActivity) context).showMealTimeDialog(recipe);
                    }
                }
            });
        }
    }

}