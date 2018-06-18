package com.aey.theapp;

import android.app.Dialog;
import android.app.DialogFragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class FullScreenDialog extends DialogFragment {

    public static final String TAG = "FullScreenDialog";

    private String jsonResponse;

    @BindView(R.id.tv_date)
    TextView dateTv;
    @BindView(R.id.tv_origin)
    TextView originTv;
    @BindView(R.id.tv_destination)
    TextView destinationTv;

    @BindView(R.id.tv_time)
    TextView timeTv;
    @BindView(R.id.tv_distance)
    TextView distanceTv;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(DialogFragment.STYLE_NORMAL, R.style.FullScreenDialogStyle);
    }

    @Override
    public void onStart() {
        super.onStart();

        Dialog dialog = getDialog();
        if (dialog != null) {
            int width = ViewGroup.LayoutParams.MATCH_PARENT;
            int height = ViewGroup.LayoutParams.MATCH_PARENT;
            dialog.getWindow().setLayout(width, height);
        }

        viewData();
    }

    private void viewData() {
        if (jsonResponse != null && !jsonResponse.isEmpty()) {
            try {
                JSONObject root = new JSONObject(jsonResponse);
                JSONObject trip = root.getJSONObject("trip");
                String date = trip.getString("date");
                String origin = trip.getString("origin");
                String destination = trip.getString("destination");

                JSONObject time = root.getJSONObject("time");
                String hours = time.getString("hours");
                String mins = time.getString("mins");

                JSONObject distance = root.getJSONObject("distance");
                String km = distance.getString("km");

                dateTv.setText(date);
                originTv.setText(origin);
                destinationTv.setText(destination);

                timeTv.setText(hours + "h ," + mins + "m.");
                distanceTv.setText(km + "km");
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup parent, Bundle state) {
        super.onCreateView(inflater, parent, state);
        View view = getActivity().getLayoutInflater().inflate(R.layout.dialog_layout, parent, false);
        ButterKnife.bind(this, view);
        return view;
    }

    @OnClick(R.id.btn_close)
    public void closeDialog() {
        this.dismiss();
    }

    public void setJsonResponse(String jsonResponse) {
        this.jsonResponse = jsonResponse;
    }
}