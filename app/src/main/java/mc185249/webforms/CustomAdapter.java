package mc185249.webforms;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by jn185090 on 6/8/2016.
 */
public class CustomAdapter extends RecyclerView.Adapter<CustomAdapter.ViewHolder> {

    private Context mContext;
    private ArrayList<Information> data;
    private LayoutInflater inflater;
    private int previousPosition = 0;

    public CustomAdapter(Context context,ArrayList<Information>data) {
        this.mContext = context;
        this.data = data;
        inflater = LayoutInflater.from(context);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.list_item_row,parent,false);
        ViewHolder holder = new ViewHolder(view);
        holder.imageView = (ImageView) view.findViewById(R.id.img_row);
        holder.textView = (TextView) view.findViewById(R.id.txv_row);
        holder.deltaTime = (TextView)view.findViewById(R.id.deltaTime);
        holder.currentState = (TextView)view.findViewById(R.id.currentState);
        return holder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.textView.setText(data.get(position).getTitle());
        holder.imageView.setImageResource(data.get(position).getImageId());
        holder.deltaTime.setText(data.get(position).getDeltaTime());
        holder.currentState.setText(data.get(position).getCurrentState());
        if (position > previousPosition){
            AnimationUtil.animate(holder,true);
        }else {
            AnimationUtil.animate(holder,false);
        }
        previousPosition = position;
        final int currentPosition = position;
        final Information infoData = data.get(position);
        holder.imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView textView;
        ImageView imageView;
        TextView deltaTime;
        TextView currentState;
        public ViewHolder(View itemView) {
            super(itemView);
        }
    }
}
