package com.tmaprojects.tarekkma.tedxainshamstickets;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by tarekkma on 4/22/17.
 */

public class AttendersAdapter extends RecyclerView.Adapter<AttendersAdapter.VH>{
    private List<Attender> attenders=  new ArrayList<>();

    public void add(Attender a){
        int pos = attenders.size();
        attenders.add(a);
        notifyItemInserted(pos);
    }

    public void addAll(List<Attender> a){
        attenders.addAll(a);
        notifyDataSetChanged();
    }

    @Override
    public VH onCreateViewHolder(ViewGroup parent, int viewType) {
        return new VH(LayoutInflater.from(parent.getContext()).inflate(R.layout.attender_list_item,parent,false));
    }

    @Override
    public void onBindViewHolder(VH holder, int position) {
        holder.bind(attenders.get(position));
    }

    @Override
    public int getItemCount() {
        return attenders.size();
    }

    public class VH extends RecyclerView.ViewHolder{
        public TextView qrTV,nameTV, typeTV, phoneTV, emailTV, natidTV, colorTV;
        public VH(View v) {
            super(v);
            qrTV   = (TextView) v.findViewById(R.id.attender_qr_code);
            nameTV = (TextView) v.findViewById(R.id.attender_name);
            typeTV = (TextView) v.findViewById(R.id.attender_type);
            phoneTV = (TextView) v.findViewById(R.id.attender_phonenumber);
            emailTV = (TextView) v.findViewById(R.id.attender_email);
            natidTV = (TextView) v.findViewById(R.id.attender_nat_id);
            colorTV = (TextView) v.findViewById(R.id.attender_fav_color);
        }
        public void bind(Attender a){
            qrTV.setText(a.getId()+"");
            nameTV.setText(a.getName());
            typeTV.setText(a.getType());
            phoneTV.setText(a.getPhoneNumber());
            emailTV.setText(a.getEmail());
            natidTV.setText(a.getNat_id());
            colorTV.setText(a.getFavColor());
            int color = itemView.getContext().getResources().getColor(ColorsUtil.getColor(a.getFavColor()));
            colorTV.setTextColor(color);
        }
    }
}
