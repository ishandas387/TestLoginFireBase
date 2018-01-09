package com.ishan387.testlogin.model;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.ishan387.testlogin.R;
import com.ishan387.testlogin.onClickInterface;

/**
 * Created by ishan on 27-12-2017.
 */

public class OfferZoneViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener  {

    public TextView offerznname, offerzndesc, offerznprice,offerznid;
    public  Button delete, addtocart;


    public void setItemClickListener(onClickInterface itemClickListener) {
        this.itemClickListener = itemClickListener;
    }

    private onClickInterface itemClickListener;
    public OfferZoneViewHolder(View itemView) {
        super(itemView);
        offerznname = (TextView) itemView.findViewById(R.id.offerzoneoffername);
        offerzndesc = (TextView) itemView.findViewById(R.id.offerzoneofferdesc);
        offerznprice = (TextView) itemView.findViewById(R.id.offerznprice);
        offerznid = (TextView) itemView.findViewById(R.id.offerznid);
        delete = (Button) itemView.findViewById(R.id.offerzndelete);
        addtocart = (Button) itemView.findViewById(R.id.offerznaddtocart);
    }

    @Override
    public void onClick(View v) {
        itemClickListener.onClick(v,getAdapterPosition(),false);

    }
}
