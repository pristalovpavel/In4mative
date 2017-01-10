package com.pristalovpavel.in4mative;

import android.content.Context;
import android.location.Location;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;
import com.pristalovpavel.in4mative.rest.interfaces.OpenWeatherMapAPI;
import com.pristalovpavel.in4mative.rest.model.OpenWeatherMapData;

import java.text.DateFormat;
import java.util.Calendar;

import retrofit.Callback;
import retrofit.GsonConverterFactory;
import retrofit.Response;
import retrofit.Call;
import retrofit.Retrofit;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link OnDateTimeFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link DateTimeFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class DateTimeFragment
        extends Fragment
        implements GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener,
        Callback<OpenWeatherMapData>
{
    private OnDateTimeFragmentInteractionListener mListener;

    // TODO move to presenter
    private GoogleApiClient googleApiClient;
    private Location mLastLocation;

    public DateTimeFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment DateTimeFragment.
     */
    public static DateTimeFragment newInstance() {
        DateTimeFragment fragment = new DateTimeFragment();
        /*Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);*/
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            //mParam1 = getArguments().getString(ARG_PARAM1);
            //mParam2 = getArguments().getString(ARG_PARAM2);
        }

        // Create an instance of GoogleAPIClient.
        if (googleApiClient == null) {
            googleApiClient = new GoogleApiClient.Builder(getContext())
                    .addConnectionCallbacks(this)
                    .addOnConnectionFailedListener(this)
                    .addApi(LocationServices.API)
                    .build();
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_date_time, container, false);
    }

    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnDateTimeFragmentInteractionListener) {
            mListener = (OnDateTimeFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnDateTimeFragmentInteractionListener");
        }

    }

    public void onStart() {
        googleApiClient.connect();
        super.onStart();
    }

    @Override
    public void onResume() {
        super.onResume();
        //TODO test me on 14..16 API!!!
        if (Build.VERSION.SDK_INT < 17) {
            //TODO use data binding library instead
            if (getView() != null) {
                View dateView = getView().findViewById(R.id.dateView);
                if (dateView != null && dateView instanceof TextView) {
                    ((TextView) dateView).setText(DateFormat.getDateInstance(
                            DateFormat.SHORT).format(Calendar.getInstance().getTime()));
                }
            }
        }
    }


    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    public void onStop() {
        googleApiClient.disconnect();
        super.onStop();
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        //TODO make runtime check and request permission like in checkPermission() method
        try {
            mLastLocation = LocationServices.FusedLocationApi.getLastLocation(googleApiClient);
        }
        catch (SecurityException e) {
            //TODO write handler
        }

        if (mLastLocation != null) {
            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl("http://api.openweathermap.org")
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();

            // prepare call in Retrofit 2.0
            OpenWeatherMapAPI stackOverflowAPI = retrofit.create(OpenWeatherMapAPI.class);

            Call<OpenWeatherMapData> call =
                    stackOverflowAPI.loadQuestions(mLastLocation.getLatitude(),
                                                    mLastLocation.getLongitude());
            //asynchronous call
            call.enqueue(DateTimeFragment.this);
            //mLatitudeText.setText(String.valueOf(mLastLocation.getLatitude()));
            //mLongitudeText.setText(String.valueOf(mLastLocation.getLongitude()));
        }
    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        Toast.makeText(getContext(), "Connection failed!", Toast.LENGTH_LONG).show();
    }

    @Override
    public void onResponse(Response<OpenWeatherMapData> response, Retrofit retrofit) {
        Toast.makeText(getContext(), "Success!!!", Toast.LENGTH_LONG).show();
    }

    @Override
    public void onFailure(Throwable t) {

    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnDateTimeFragmentInteractionListener {
        void onFragmentInteraction(Uri uri);
    }
    /*
    //TODO write permission handler. see https://developer.android.com/training/permissions/requesting.html
    private boolean checkPermission()
    {
        // Here, thisActivity is the current activity
        if (ContextCompat.checkSelfPermission(thisActivity,
                Manifest.permission.READ_CONTACTS)
                != PackageManager.PERMISSION_GRANTED) {

            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(thisActivity,
                    Manifest.permission.READ_CONTACTS)) {

                // Show an explanation to the user *asynchronously* -- don't block
                // this thread waiting for the user's response! After the user
                // sees the explanation, try again to request the permission.

            } else {

                // No explanation needed, we can request the permission.

                ActivityCompat.requestPermissions(thisActivity,
                        new String[]{Manifest.permission.READ_CONTACTS},
                        MY_PERMISSIONS_REQUEST_READ_CONTACTS);

                // MY_PERMISSIONS_REQUEST_READ_CONTACTS is an
                // app-defined int constant. The callback method gets the
                // result of the request.
            }
        }
    }*/
}
