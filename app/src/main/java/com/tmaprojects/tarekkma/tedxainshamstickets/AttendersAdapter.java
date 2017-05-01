package com.tmaprojects.tarekkma.tedxainshamstickets;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.tmaprojects.tarekkma.tedxainshamstickets.Utils.ColorsUtil;
import com.tmaprojects.tarekkma.tedxainshamstickets.model.Attender;
import com.tmaprojects.tarekkma.tedxainshamstickets.model.Status;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by tarekkma on 4/22/17.
 */

public class AttendersAdapter extends RecyclerView.Adapter<AttendersAdapter.VH>{
    private List<Attender> attenders=  new ArrayList<>();
    private MainPresenter presenter;
    private MainPresenter.View view;

    public AttendersAdapter(MainPresenter presenter, MainPresenter.View view) {
        this.presenter = presenter;
        this.view = view;
    }

    public List<Attender> getAttenders() {
        return attenders;
    }

    public void add(Attender a){
        int pos = attenders.size();
        attenders.add(a);
        notifyItemInserted(pos);
    }

    public void addAll(List<Attender> a){
        int startPos = getItemCount();
        attenders.addAll(a);
        int endPos = getItemCount()-1;
        notifyItemRangeInserted(startPos,endPos);
        notifyDataSetChanged();
    }

    public void delete(Attender a){
        int pos = attenders.indexOf(a);
        attenders.remove(a);
        //notifyItemInserted(pos);
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
        public Button removeBTN;
        public VH(View v) {
            super(v);
            qrTV   = (TextView) v.findViewById(R.id.attender_qr_code);
            nameTV = (TextView) v.findViewById(R.id.attender_name);
            typeTV = (TextView) v.findViewById(R.id.attender_type);
            phoneTV = (TextView) v.findViewById(R.id.attender_phonenumber);
            emailTV = (TextView) v.findViewById(R.id.attender_email);
            natidTV = (TextView) v.findViewById(R.id.attender_nat_id);
            colorTV = (TextView) v.findViewById(R.id.attender_fav_color);
            removeBTN = (Button) v.findViewById(R.id.remove_btn);
        }
        public void bind(final Attender a){
            qrTV.setText(a.getTicketNumber());
            nameTV.setText(a.getFirstName()+" "+a.getMiddleName()+" "+a.getLastName());
            typeTV.setText(a.getType());
            phoneTV.setText(a.getMobileNumber());
            emailTV.setText(a.getEmail());
            natidTV.setText(a.getNatId());
            colorTV.setText(a.getFavColor());
            int color = itemView.getContext().getResources().getColor(ColorsUtil.getColor(a.getFavColor()));
            colorTV.setTextColor(color);
            removeBTN.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(final View v) {
                    new MaterialDialog.Builder(v.getContext())
                            .title("Are you sure to delete this ticket ?")
                            .positiveText("Yes")
                            .negativeText("No")
                            .onPositive(new MaterialDialog.SingleButtonCallback() {
                                @Override
                                public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                                    presenter.removeAttender(a);
                                }
                            })
                            .show();
                }
            });
        }
    }
}
