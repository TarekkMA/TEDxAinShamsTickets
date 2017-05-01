package com.tmaprojects.tarekkma.tedxainshamstickets;

import android.content.SharedPreferences;
import android.util.Log;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.tmaprojects.tarekkma.tedxainshamstickets.model.Attender;
import com.tmaprojects.tarekkma.tedxainshamstickets.model.Status;

import java.io.IOException;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by tarekkma on 4/24/17.
 */

public class MainPresenter {

    private View view;
    private SharedPreferences preferences;
    private static final String KEY_SAVED_DATA = "SAVED_DATA";

    public MainPresenter(final View view, SharedPreferences preferences) {
        this.view = view;
        this.preferences = preferences;

        checkSavedData();
    }

    public interface View {
        void toggleLoading(boolean visible);

        void toggleEmptyList(boolean visible);

        void displayAttenderDialog(Attender attender);

        void showErrorDialog(String title, String msg);

        void showWarningDialog(String title, String msg);

        void addAttenders(List<Attender> attenders);

        void deleteAttender(Attender attender);

        List<Attender> getAttenders();
    }

    public interface Listener{
        void onListen();
    }

    public void addAttender(final Attender attender) {
        view.toggleLoading(true);
        WebAPI.createService().markTicket(Integer.parseInt(attender.getTicketNumber()))
                .enqueue(new Callback<Status>() {
                    @Override
                    public void onResponse(Call<Status> call, Response<Status> response) {
                        view.toggleLoading(false);
                        if (response.body().getStatus()) {
                            view.addAttenders(Collections.singletonList(attender));
                            saveData();
                        } else {
                            view.showErrorDialog("Error Request !", response.body().getErr());
                        }
                    }

                    @Override
                    public void onFailure(Call<Status> call, Throwable t) {
                        view.toggleLoading(false);
                        view.showErrorDialog("Network Error !", t.getMessage());
                    }
                });
    }


    public void removeAttender(final Attender attender){
        WebAPI.createService().unmarkTicket(Integer.parseInt(attender.getTicketNumber())).enqueue(new Callback<Status>() {
            @Override
            public void onResponse(Call<Status> call, Response<Status> response) {
                if (response.body().getStatus()) {
                    view.deleteAttender(attender);
                } else {
                    view.showErrorDialog("Error","Can't delete this attender !\n\nserver error");
                }
            }

            @Override
            public void onFailure(Call<Status> call, Throwable t) {
                view.showErrorDialog("Error","Can't delete this attender !\n\nrequest failed");
            }
        });
    }

    public void getAttenderById(int id) {
        view.toggleLoading(true);
        WebAPI.createService().getTicket(id)
                .enqueue(new Callback<Attender>() {
                    @Override
                    public void onResponse(Call<Attender> call, final Response<Attender> response) {
                        view.toggleLoading(false);
                        if (response.body() != null) {
                            view.displayAttenderDialog(response.body());
                        } else {
                            try {
                                view.showWarningDialog("404 No Attender Data !", "Server Response :\n" + response.errorBody().string());
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<Attender> call, Throwable t) {
                        view.toggleLoading(false);
                        view.showErrorDialog("Network Error !", t.getMessage());
                    }
                });
    }

    private void checkSavedData(){
        List<Attender> savedAtternders = getSavedData();
        if (!savedAtternders.isEmpty()) {
            view.toggleLoading(true);
            int[] ids = new int[savedAtternders.size()];
            for (int i = 0; i < savedAtternders.size(); i++) {
                ids[i] = Integer.parseInt(savedAtternders.get(i).getTicketNumber());
            }
            WebAPI.createService().checkTickets(ids).enqueue(new Callback<List<Attender>>() {
                @Override
                public void onResponse(Call<List<Attender>> call, Response<List<Attender>> response) {
                    view.toggleLoading(false);
                    if (response.body() != null) {
                        List<Attender> list = response.body();
                        Iterator<Attender> i = list.iterator();
                        while (i.hasNext()){
                            Attender a = i.next();
                            if (a.getAttended().equals("0")){
                                i.remove();
                            }
                        }
                        if(!list.isEmpty()) {
                            view.toggleEmptyList(false);
                            view.addAttenders(list);
                            saveData();
                        }
                    } else {
                        try {
                            view.showWarningDialog("Can't verify current attender data !", "Server Response :\n" + response.errorBody().string());
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }

                @Override
                public void onFailure(Call<List<Attender>> call, Throwable t) {
                    view.toggleLoading(false);
                    view.showErrorDialog("Error !","Something went wrong while checking the database !\n\n"+t.getMessage());
                }
            });
        }
    }

    private void saveData() {
        SharedPreferences.Editor editor = preferences.edit();
        String s = new Gson().toJson(view.getAttenders());
        editor.putString(KEY_SAVED_DATA, s);
        editor.apply();
    }

    private List<Attender> getSavedData() {
        List<Attender> attenders = new Gson().fromJson(preferences.getString(KEY_SAVED_DATA, "[]"), new TypeToken<List<Attender>>() {
        }.getType());
        return attenders;
    }

}
