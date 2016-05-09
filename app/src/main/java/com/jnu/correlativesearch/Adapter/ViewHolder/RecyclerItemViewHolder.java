package com.jnu.correlativesearch.Adapter.ViewHolder;

import android.support.v7.widget.RecyclerView;

import android.view.View;
import android.widget.TextView;


import com.jnu.correlativesearch.R;
import com.jnu.correlativesearch.activities.SearchMainActivity;
import com.jnu.correlativesearch.beans.RelatedRecord;
import com.jnu.correlativesearch.utils.TimeTextUtils;


/**
 * Created by Leo on 2016/3/22.
 */
public class RecyclerItemViewHolder extends RecyclerView.ViewHolder {
    private TextView mItemTextView1;
    private TextView mItemTextView2;
    private TextView mImageView;
    private RelatedRecord relatedRecord;




    public RecyclerItemViewHolder(View itemView, TextView mItemTextView1, TextView mItemTextView2, TextView mImageView) {
        super(itemView);
        this.mItemTextView1 = mItemTextView1;
        this.mItemTextView2 = mItemTextView2;
        this.mImageView = mImageView;
/*        itemView.findViewById(R.id.item_container).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });*/
    }
    public static RecyclerItemViewHolder newInstance(View parent){
        TextView itemTextView1 = (TextView) parent.findViewById(R.id.itemTextView1);
        TextView itemTextView2 = (TextView) parent.findViewById(R.id.itemTextView2);
        TextView itemImage = (TextView)parent.findViewById(R.id.itemImage);
        return new RecyclerItemViewHolder(parent,itemTextView1,itemTextView2,itemImage);
    }
    public void setItemText(CharSequence text1) {
        mItemTextView1.setText(text1);
    }

     public void setItemTimeText(long millis){
         mItemTextView2.setText(TimeTextUtils.millisToLifeString(millis));
     }



    public void setItemImage(String mime){
        mImageView.setTypeface(SearchMainActivity.getIconfont());
        switch (mime){
            case "application/pdf":
            case "application/msword":
            case "application/vnd.ms-excel":
            case "application/vnd.ms-powerpoint":
            case "text/plain":
//                mImageView.setImageResource(R.drawable.text);
                mImageView.setText(R.string.icon_text);
                break;
            case "text/html":
//                mImageView.setImageResource(R.drawable.html);
                mImageView.setText(R.string.icon_web);
                break;
            case "audio/mpeg":
//                mImageView.setImageResource(R.drawable.mpeg);
                mImageView.setText(R.string.icon_music);
                break;
            case "application/app":
//                mImageView.setImageResource(R.drawable.app);
                mImageView.setText(R.string.icon_app);
                break;
            case "application/sms":
//                mImageView.setImageResource(R.drawable.sms);
                mImageView.setText(R.string.icon_message);
                break;
            case "application/call":
//                mImageView.setImageResource(R.drawable.call);
                mImageView.setText(R.string.icon_phone);
                break;
            case "application/calendar":
//                mImageView.setImageResource(R.drawable.calendar);
                mImageView.setText(R.string.icon_calendar);
                break;
            case "application/email":
//                mImageView.setImageResource(R.drawable.email);
                mImageView.setText(R.string.icon_email);
                break;

        }
    }

    public String getItemText(){
        return mItemTextView1.getText().toString();
    }
    public void getRecord(RelatedRecord relatedRecord){
        this.relatedRecord = relatedRecord;
    }
    public void setTag(int tag){
        itemView.setTag(tag);
    }


 /*   public void setItemOnclick(final Context context){
        itemView.findViewById(R.id.item_container).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(context,String.valueOf(getPosition()),Toast.LENGTH_SHORT).show();
                Dialog dialog = new AlertDialog.Builder(context).setIcon(R.drawable.search_image)
                        .setTitle("选择操作").setMessage(getItemText())
                        .setPositiveButton("查找", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                SearchMainActivity.setStartTime(relatedRecord.getStart_time());
                                SearchMainActivity.setEndTime(relatedRecord.getEnd_time());
                                SearchMainActivity.setStartDateAndTime(relatedRecord.getStart_time());
                                SearchMainActivity.setEndDateAndTime(relatedRecord.getEnd_time());
                                SearchMainActivity.setMime("");
                                SearchMainActivity.notify("");
                                dialog.dismiss();
                            }
                        }).setNeutralButton("打开", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Toast.makeText(context,getItemText(),Toast.LENGTH_SHORT).show();
                            }
                        }).setNegativeButton("取消", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        }).create();
                dialog.show();

    }
});
    }*/
}
