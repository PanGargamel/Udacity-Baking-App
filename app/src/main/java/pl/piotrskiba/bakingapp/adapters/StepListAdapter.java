package pl.piotrskiba.bakingapp.adapters;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import pl.piotrskiba.bakingapp.MainActivity;
import pl.piotrskiba.bakingapp.R;
import pl.piotrskiba.bakingapp.models.Step;

import static android.support.v4.content.res.TypedArrayUtils.getString;

public class StepListAdapter extends RecyclerView.Adapter<StepListAdapter.StepViewHolder> {

    Context context;
    List<Step> steps;
    int selectedItem = 0;

    final private StepListItemClickListener mOnClickListener;


    public StepListAdapter(Context context, List<Step> steps, StepListItemClickListener listener){
        this.context = context;
        this.steps = steps;
        this.mOnClickListener = listener;
    }

    @NonNull
    @Override
    public StepViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        View view = inflater.inflate(R.layout.step_list_item, parent, false);
        StepViewHolder viewHolder = new StepViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull StepViewHolder holder, int position) {
        String title = steps.get(position).getShortDescription();
        String text = String.format(context.getString(R.string.step_format), position+1, title);
        holder.stepTitleTextView.setText(text);

        if(position == selectedItem){
            holder.itemView.setBackgroundColor(context.getResources().getColor(R.color.colorAccent));
        }
        else{
            holder.itemView.setBackgroundColor(Color.rgb(255, 255, 255));
        }
    }

    @Override
    public int getItemCount() {
        if(steps == null)
            return 0;
        else
            return steps.size();
    }

    class StepViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        @BindView(R.id.tv_step_title)
        TextView stepTitleTextView;

        public StepViewHolder(View itemView) {
            super(itemView);

            ButterKnife.bind(this, itemView);

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            mOnClickListener.onClick(getAdapterPosition());
            selectedItem = getAdapterPosition();
            notifyDataSetChanged();
        }
    }

    public interface StepListItemClickListener{
        void onClick(int index);
    }
}
