package com.tmaprojects.tarekkma.tedxainshamstickets;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.InputType;
import android.util.Log;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.TextView;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.getbase.floatingactionbutton.FloatingActionButton;
import com.getbase.floatingactionbutton.FloatingActionsMenu;
import com.tmaprojects.tarekkma.tedxainshamstickets.Utils.ColorsUtil;
import com.tmaprojects.tarekkma.tedxainshamstickets.Utils.DialogHelper;
import com.tmaprojects.tarekkma.tedxainshamstickets.Utils.IntentHelper;
import com.tmaprojects.tarekkma.tedxainshamstickets.model.Attender;
import com.tmaprojects.tarekkma.tedxainshamstickets.qr.IntentIntegrator;
import com.tmaprojects.tarekkma.tedxainshamstickets.qr.IntentResult;

import org.apache.commons.lang3.StringUtils;

import java.util.List;

public class MainActivity extends AppCompatActivity
        implements
        NavigationView.OnNavigationItemSelectedListener,
        MainPresenter.View {

    private static final String TAG = "MainActivity";

    RecyclerView recyclerView;
    View emptyLayout;
    AttendersAdapter adapter;
    MaterialDialog dialog;

    MainPresenter presenter;

    FloatingActionsMenu fabM;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        dialog = new MaterialDialog.Builder(this)
                .title("Loading ...")
                .progress(true, 0)
                .cancelable(false)
                .build();


        emptyLayout =  findViewById(R.id.empty_layout);
        fabM = (FloatingActionsMenu)findViewById(R.id.fab_menu);
        FloatingActionButton fabscan = (FloatingActionButton) findViewById(R.id.scan_qr_code);
        FloatingActionButton fabmanual = (FloatingActionButton) findViewById(R.id.add_manual);

        fabscan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                fabM.collapse();
                IntentIntegrator integrator = new IntentIntegrator(MainActivity.this);
                integrator.initiateScan();
            }
        });

        fabmanual.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fabM.collapse();
                new MaterialDialog.Builder(MainActivity.this)
                        .title("Add Ticket by ID :")
                        .inputType(InputType.TYPE_CLASS_NUMBER)
                        .input("Ticket ID", null, false, new MaterialDialog.InputCallback() {
                            @Override
                            public void onInput(@NonNull MaterialDialog dialog, CharSequence input) {
                                presenter.getAttenderById(Integer.parseInt(input.toString()));
                            }
                        }).show();
            }
        });

        recyclerView = (RecyclerView)findViewById(R.id.list);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));


        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        presenter = new MainPresenter(this,getSharedPreferences("MY_PREFS_EL_5ATERA", MODE_PRIVATE));
        adapter =  new AttendersAdapter(presenter,this);
        recyclerView.setAdapter(adapter);
    }

    public void onActivityResult(final int requestCode, int resultCode, Intent intent) {
        IntentResult scanResult = IntentIntegrator.parseActivityResult(requestCode, resultCode, intent);
        if (scanResult != null) {
            if(StringUtils.isNumeric(scanResult.getContents())){
               presenter.getAttenderById(Integer.parseInt(scanResult.getContents()));
            }else{
                Log.e(TAG, "onActivityResult: '"+scanResult.getContents()+"' isn't valid id.");
                DialogHelper.showWrongFormat(this);
                showErrorDialog("Wrong Format","The scanned QR code isn't valid id.");
            }
        }
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if(drawer.isDrawerOpen(GravityCompat.START)){
            drawer.closeDrawer(GravityCompat.START);
        }
        else if (fabM.isExpanded()) {
            fabM.collapse();
        } else {
            super.onBackPressed();
        }
    }


    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_code) {
            IntentHelper.openGitHubRebo(this);
        } else if (id == R.id.nav_about) {
            DialogHelper.showAbout(this);
        }
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void toggleLoading(boolean visible) {
        if(visible)dialog.show();
        else dialog.dismiss();
    }

    @Override
    public void toggleEmptyList(boolean visible) {
        if(visible)emptyLayout.setVisibility(View.VISIBLE);
        else emptyLayout.setVisibility(View.GONE);
    }

    @Override
    public void displayAttenderDialog(final Attender attender) {
        MaterialDialog.Builder builder = new MaterialDialog.Builder(this)
                .customView(R.layout.attender_layout, true);

        if(attender.getAttended().equals("1")){
            builder.title("Already Attended !")
                    .positiveText("Dismiss")
                    .iconRes(R.drawable.warning_48);
        }else{
            builder .title("Add attender ?")
                    .positiveText("Confirm")
                    .negativeText("Cancel")
                    .onPositive(new MaterialDialog.SingleButtonCallback() {
                        @Override
                        public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                            presenter.addAttender(attender);
                        }
                    });
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

        qrTV.setText(attender.getTicketNumber());
        nameTV.setText(attender.getFirstName()+" "+attender.getMiddleName()+" "+attender.getLastName());
        typeTV.setText(attender.getType());
        phoneTV.setText(attender.getMobileNumber());
        emailTV.setText(attender.getEmail());
        natidTV.setText(attender.getNatId());
        colorTV.setText(attender.getFavColor());
        int color = getResources().getColor(ColorsUtil.getColor(attender.getFavColor().toLowerCase()));
        colorTV.setTextColor(color);

        dialog.show();
    }

    @Override
    public void addAttenders(List<Attender> attenders) {
        emptyLayout.setVisibility(View.GONE);
        adapter.addAll(attenders);
    }

    @Override
    public void deleteAttender(Attender attender) {
        adapter.delete(attender);
        if(getAttenders().size()==0){
            emptyLayout.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public List<Attender> getAttenders() {
        return adapter.getAttenders();
    }

    @Override
    public void showErrorDialog(String title, String msg) {
        new MaterialDialog.Builder(this)
                .title(title)
                .content(msg)
                .positiveText("Dismiss")
                .iconRes(R.drawable.error_48)
                .show();
    }

    @Override
    public void showWarningDialog(String title, String msg) {
        new MaterialDialog.Builder(this)
                .title(title)
                .content(msg)
                .positiveText("Dismiss")
                .iconRes(R.drawable.warning_48)
                .show();
    }


}
