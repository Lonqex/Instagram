package com.f22labs.instalikefragmenttransaction.Adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.f22labs.instalikefragmenttransaction.R;

import java.util.List;


public class Recyclerview_Adapter extends RecyclerView.Adapter<Recyclerview_Adapter.MyHolder>{

    private Context context;
    private List<Item_Chat> liste;
    private String id;

    public Recyclerview_Adapter(Context context, List<Item_Chat> liste,String id)
    {
        this.context = context;
        this.liste = liste;
        this.id = id;
    }

    @Override
    public MyHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view;
        if(viewType == 1)
        {
            view = LayoutInflater.from(context).inflate(R.layout.item_chat,parent,false);
            return new MyHolder(view);
        }
        else if(viewType == 2)
        {
            view = LayoutInflater.from(context).inflate(R.layout.item_chat2,parent,false);
            return new MyHolder(view);
        }

        return null;
    }

    @Override
    public void onBindViewHolder(MyHolder holder, int position) {
            Item_Chat itemchat = liste.get(position);

            holder.mesaj.setText(itemchat.getMesaj());
    }

    @Override
    public int getItemCount() {
        return liste.size();
    }

    @Override
    public int getItemViewType(int position) {

         Item_Chat item_chat = liste.get(position);

         if(item_chat.getUid().equals(id))
         {
             return 1;
         }
         else
         {
             return 2;
         }

    }

    public class MyHolder extends RecyclerView.ViewHolder {

        TextView mesaj;
        public MyHolder(View itemView) {
            super(itemView);

            mesaj = (TextView) itemView.findViewById(R.id.mesaj);
        }
    }
}
