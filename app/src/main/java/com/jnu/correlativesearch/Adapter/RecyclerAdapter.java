package com.jnu.correlativesearch.Adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.jnu.correlativesearch.Adapter.ViewHolder.RecyclerHeaderViewHolder;
import com.jnu.correlativesearch.Adapter.ViewHolder.RecyclerItemViewHolder;
import com.jnu.correlativesearch.R;
import com.jnu.correlativesearch.beans.RelatedRecord;

import java.util.List;

/**
 * Created by Leo on 2016/3/22.
 */
public class RecyclerAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> implements View.OnClickListener{

    private static final int TYPE_HEADER = 2;
    private static final int TYPE_ITEM = 1;

    private OnRecyclerViewItemClickListener onRecyclerViewItemClickListener = null;

    private List<RelatedRecord> mItemList_relatedrecord;
    private Context context;
    private RelatedRecord relatedRecord;

    public RecyclerAdapter(Context context,List<RelatedRecord> itemList) {
        this.context= context;
        this.mItemList_relatedrecord = itemList;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        if(viewType == TYPE_ITEM){
            final View view = LayoutInflater.from(context).inflate(R.layout.list_item,parent,false);
            view.setOnClickListener(this);
            return RecyclerItemViewHolder.newInstance(view);
        }else if(viewType == TYPE_HEADER){
            final View view = LayoutInflater.from(context).inflate(R.layout.recycler_header, parent, false);
            return new RecyclerHeaderViewHolder(view);
        }
        throw new RuntimeException("There is no type that matches the type " + viewType + " + make sure your using types correctly");
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, int position) {
          if(!isPositionHeader(position)){
              RecyclerItemViewHolder holder = (RecyclerItemViewHolder) viewHolder;
              relatedRecord = mItemList_relatedrecord.get(position - 2 );
              String itemText1 = mItemList_relatedrecord.get(position - 2 ).getName(); // header
              Long millis = mItemList_relatedrecord.get(position - 2 ).getStart_time();
              String itemMime = mItemList_relatedrecord.get(position - 2).getMime();
              holder.getRecord(mItemList_relatedrecord.get(position - 2));
              holder.setTag(position - 2);
              holder.setItemImage(itemMime);
              holder.setItemText(itemText1);
              holder.setItemTimeText(millis);
//              holder.setItemOnclick(context);

          }
    }

    public int getBasicItemCount() {
        return mItemList_relatedrecord == null ? 0 : mItemList_relatedrecord.size();
    }

    public int getItemViewType(int position) {
        if (isPositionHeader(position)) {
            return TYPE_HEADER;
        }

        return TYPE_ITEM;
    }
    @Override
    public int getItemCount() {
        return  getBasicItemCount() + 2; // header
    }

    private boolean isPositionHeader(int position) {
        return position == 0|| position == 1;
    }

    @Override
    public void onClick(View v) {

        if(onRecyclerViewItemClickListener!=null){
            onRecyclerViewItemClickListener.onItemClick(v);
        }
    }


    public void setOnRecyclerViewItemClickListener(OnRecyclerViewItemClickListener onRecyclerViewItemClickListener){
        this.onRecyclerViewItemClickListener = onRecyclerViewItemClickListener;
    }

    public static interface OnRecyclerViewItemClickListener {
        void onItemClick(View view);
    }
}
