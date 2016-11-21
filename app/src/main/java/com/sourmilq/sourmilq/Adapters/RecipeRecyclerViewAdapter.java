package com.sourmilq.sourmilq.Adapters;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.sourmilq.sourmilq.DataModel.Model;
import com.sourmilq.sourmilq.DataModel.Recipe;
import com.sourmilq.sourmilq.R;
import com.sourmilq.sourmilq.RecipeDetailActivity;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Observable;
import java.util.Observer;

/**
 * Created by ajanthan on 15-11-26.
 */
public class RecipeRecyclerViewAdapter extends RecyclerView.Adapter<RecipeRecyclerViewAdapter.RecipeViewHolder> implements Observer {

    private LayoutInflater mInflate;
    private ArrayList<Recipe> recipes = new ArrayList<Recipe>();
    private Context mContext;
    private int currentPostion;
    private Model model;

    public RecipeRecyclerViewAdapter(Context context, ArrayList<Recipe> articles) {
        mInflate = LayoutInflater.from(context);

        if(articles!=null){
            this.recipes =articles;
        }
        mContext = context;
        model = Model.getInstance(context);
        model.addObserver(this);
    }

    @Override
    public RecipeRecyclerViewAdapter.RecipeViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mInflate.inflate(R.layout.recipe_card, parent, false);
        RecipeViewHolder holder = new RecipeViewHolder(view, mContext);
        return holder;
    }

    @Override
    public void onBindViewHolder(RecipeViewHolder holder, int position) {
        holder.tvTitle.setText(recipes.get(position).getTitle());
//        holder.tvPerparationTime.setText(recipes.get(position).getText()+"");
        Picasso.with(mContext)
                .load(recipes.get(position).getImageUrl())
                .into(holder.ivImage);
        currentPostion = position;
    }

    @Override
    public int getItemCount() {
        return recipes.size();
    }

    @Override
    public void onDetachedFromRecyclerView(RecyclerView recyclerView) {
        super.onDetachedFromRecyclerView(recyclerView);
    }

    public int getCurrentPostion() {
        return currentPostion;
    }

    public ArrayList<Recipe> getRecipes(){
        return recipes;
    }

    @Override
    public void update(Observable observable, Object data) {
        recipes = model.getRecipes();
        notifyDataSetChanged();
    }

    class RecipeViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private TextView tvTitle;
        private TextView tvPerparationTime;
        private ImageView ivImage;
        private Context context;
        private SharedPreferences prefs;

        public RecipeViewHolder(View itemView, Context context) {
            super(itemView);
            tvTitle = (TextView) itemView.findViewById(R.id.title);
//            tvPerparationTime = (TextView) itemView.findViewById(R.id.preparationTime);
            ivImage = (ImageView) itemView.findViewById(R.id.logo);
            this.context = context;
            prefs = context.getSharedPreferences("article", Context.MODE_PRIVATE);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            Intent i = new Intent(context, RecipeDetailActivity.class);
            i.putExtra("recipe", recipes.get(getLayoutPosition()));
            context.startActivity(i);
        }
    }
}
