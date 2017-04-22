package com.tmaprojects.tarekkma.tedxainshamstickets;

import android.content.Context;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.afollestad.materialdialogs.MaterialDialog;

import org.apache.commons.lang3.StringUtils;

/**
 * Created by tarekkma on 4/22/17.
 */

public class DialogHelper {
    public static void showAbout(final Context c){
        MaterialDialog dialog = new MaterialDialog.Builder(c)
                .customView(R.layout.about_layout, false)
                .build();
        View view = dialog.getCustomView();
        Button linkinBTN = (Button) view.findViewById(R.id.linkedinBTN);
        Button facebookBTN = (Button) view.findViewById(R.id.facebookBTN);
        linkinBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                IntentHelper.openLinkin(c);
            }
        });
        facebookBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                IntentHelper.openFacebook(c);
            }
        });
        dialog.show();
    }
    public static void showWrongFormat(final Context c){
        new MaterialDialog.Builder(c)
                .title("Wrong Format")
                .content("The scanned QR code isn't valid id.")
                .positiveText("Ok")
                .iconRes(R.drawable.error_48)
                .show();
    }
    public static void showAttenderInfo(final Context c, Attender attender, MaterialDialog.SingleButtonCallback listener) {
        MaterialDialog.Builder builder = new MaterialDialog.Builder(c)
                .customView(R.layout.attender_layout, true);

        if(attender.isAttended()){
            builder.title("Already Attended !")
                    .positiveText("Dismiss")
                    .iconRes(R.drawable.warning_48);
        }else{
            builder .title("Add attender ?")
                    .positiveText("Confirm")
                    .negativeText("Cancel")
                    .onPositive(listener);
        }

        MaterialDialog dialog = builder.build();

        View v = dialog.getCustomView();
        TextView qrTV = (TextView) v.findViewById(R.id.attender_qr_code);
        TextView nameTV = (TextView) v.findViewById(R.id.attender_name);
        TextView typeTV = (TextView) v.findViewById(R.id.attender_type);
        TextView phoneTV = (TextView) v.findViewById(R.id.attender_phonenumber);
        TextView emailTV = (TextView) v.findViewById(R.id.attender_email);
        TextView natidTV = (TextView) v.findViewById(R.id.attender_nat_id);
        TextView colorTV = (TextView) v.findViewById(R.id.attender_fav_color);

        qrTV.setText(attender.getId()+"");
        nameTV.setText(attender.getName());
        typeTV.setText(attender.getType());
        phoneTV.setText(attender.getPhoneNumber());
        emailTV.setText(attender.getEmail());
        natidTV.setText(attender.getNat_id());
        colorTV.setText(attender.getFavColor());
        int color = c.getResources().getColor(ColorsUtil.getColor(attender.getFavColor()));
        colorTV.setTextColor(color);

        dialog.show();
    }
}
