package com.example.morro.FastBuyApp.UI.Buyer;


import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.morro.FastBuyApp.R;

/**
 * Provides a Fragment permitting the user to Checkout
 */
public class CheckoutFragment extends Fragment implements View.OnClickListener {

    private CheckoutListener listener;
    private EditText addressET;
    private EditText cardNumberET;
    private Spinner paymentSpinner;

    /** Catch the listener which implemented the interface */
    @Override
    public void onAttach(Context context){
        super.onAttach(context);
        listener = (CheckoutListener) context;
        updateWindowTitle();
    }

    private void updateWindowTitle() {
        ((AppCompatActivity)getActivity()).getSupportActionBar().setTitle("Checkout");
        ((AppCompatActivity)getActivity()).getSupportActionBar().setSubtitle("Grand Total: "+listener.onGetTotal());
    }

    /** Inflate the view */
    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_user_checkout, container, false);

        addressET = view.findViewById(R.id.shipping_address);
        addressET.setHint("Your Address");
        cardNumberET = view.findViewById(R.id.card_number);
        cardNumberET.setHint("Card Number");
        paymentSpinner = view.findViewById(R.id.payment_method);

        ArrayAdapter<CharSequence> paymentAdapter = ArrayAdapter.createFromResource(
                this.getContext(),R.array.payment_methods,android.R.layout.simple_spinner_item);
        paymentAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        paymentSpinner.setAdapter(paymentAdapter);

        view.findViewById(R.id.payButton).setOnClickListener(this);

        return view;
    }

    /**
     * Factory method for passing a serializable object to the current Fragment during its creation.
     * Since the default constructor for a Fragment must be empty, newInstance will take care of
     * receiving the serializable object.
     */
    public static CheckoutFragment newInstance() {
        CheckoutFragment fragment = new CheckoutFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    /**
     * Called when the user clicks on "PAY" button
     * @param view the button object
     */
    @Override
    public void onClick(View view) {
        if(addressET.getText().toString().equals("") || cardNumberET.getText().toString().equals("")){
            Toast.makeText(getActivity(), "Please fill in all the fields!", Toast.LENGTH_LONG).show();
            return;
        }
        listener.onCheckout();
        updateWindowTitle();
    }

    /**
     * Interface for back an forth communication with the Activity
     */
    public interface CheckoutListener{
        void onCheckout();
        double onGetTotal();
    }
}
