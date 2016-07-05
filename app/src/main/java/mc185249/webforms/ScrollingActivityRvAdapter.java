package mc185249.webforms;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import models.DevolucionPartes;
import models.ScrollingActivityModel;

/**
 * Created by jn185090 on 6/14/2016.
 * Adapter para Recycler View de Scrolling Activity ( cotent_scrolling ).
 *
 */
public class ScrollingActivityRvAdapter extends RecyclerView.Adapter<ScrollingActivityRvAdapter.ViewHolder> {

    ScrollingActivityModel model;
    Context mContext;
    public ScrollingActivityRvAdapter(Context context) {

        this.mContext = context;
        this.model = new ScrollingActivityModel(mContext);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context mContext = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(mContext);
        View view = inflater.inflate(R.layout.scrolling_activity_customview_row,parent,false);
        ViewHolder holder = new ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        holder.textView.setText(model.getText(position));
        holder.imageView.setImageResource(model.getResource(position));
        holder.textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (position){
                    case 0:
                        Intent i = new Intent(mContext, InventoryActivity.class);
                        mContext.startActivity(i);
                        break;
                    case 1:
                        Intent intent = new Intent(mContext,EnvironmentalSiteActivity.class);
                        mContext.startActivity(intent);
                        break;
                    case 2:
                        Intent intent1 = new Intent(mContext,LogisticsSurveyActivity.class);
                        mContext.startActivity(intent1);
                        break;
                    case 3:
                        Intent mantenimientoIntent = new Intent(mContext, MantenimientoSurveyActivity.class);
                        mContext.startActivity(mantenimientoIntent);
                        break;
                    case 4:
                        Intent in = new Intent(mContext, memoriaFiscalActivity.class);
                        mContext.startActivity(in);
                        break;
                    case 5:
                        Intent intentt1 = new Intent(mContext,cambioPidPad.class);
                        mContext.startActivity(intentt1);
                        break;
                    case 6:
                        Intent intent2 = new Intent(mContext,VisitaTecnica.class);
                        mContext.startActivity(intent2);
                        break;
                    case 7:
                        Intent intent3 = new Intent(mContext,TecladoEncryptorActivity.class);
                        mContext.startActivity(intent3);
                        break;
                    case 8:
                        Intent intent4 = new Intent(mContext, DevolucionPartesActivity.class);
                        mContext.startActivity(intent4);
                        break;
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return model.count();
    }

    /**
     * Provide a direct reference to each of the views within a data item
     Used to cache the views within the item layout for fast access
      */
     public static class ViewHolder extends RecyclerView.ViewHolder {

        /**
         * // Your holder should contain a member variable
         // for any view that will be set as you render a row
         */
        public TextView textView;
        public ImageView imageView;

        /**
         * // We also create a constructor that accepts the entire item row
         // and does the view lookups to find each subview
         * @param itemView
         */
         public ViewHolder(View itemView) {
            // Stores the itemView in a public final member variable that can be used
            // to access the context from any ViewHolder instance.
            super(itemView);
            textView =(TextView) itemView.findViewById(R.id.textView);
             imageView = (ImageView) itemView.findViewById(R.id.imageView);
        }
    }
}
