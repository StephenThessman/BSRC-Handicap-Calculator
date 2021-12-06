package com.thessman.stephent.bsrchandicapcalculator;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import java.io.IOException;
import java.io.InputStream;
import java.util.Scanner;

public class FirstFragment extends Fragment {

    private double[][] handicaps = new double[41][31];

    private int score, average;

    TextView scoreValue;
    TextView averageValue;

    @Override
    public View onCreateView(
            LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View fragmentFirstLayout = inflater.inflate(R.layout.fragment_first, container, false);
        scoreValue = fragmentFirstLayout.findViewById(R.id.score_input);
        averageValue = fragmentFirstLayout.findViewById(R.id.average_input);
        getHandicapData();

        return fragmentFirstLayout;
    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        view.findViewById(R.id.handicap_calculate).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String scoreVal = scoreValue.getText().toString();
                if(!scoreVal.equals("")){
                    try {
                        score = Integer.parseInt(scoreVal);
                    } catch (Exception e) {
                        score = 0;
                    }
                }
                String averageVal = averageValue.getText().toString();
                if(!averageVal.equals("")) {
                    try {
                        average = Integer.parseInt(averageVal);
                    } catch (Exception e) {
                        average = 0;
                    }
                }

                TextView handicap = view.getRootView().findViewById(R.id.handicap_result);
                String value = "Handicap:\n";
                if(getHandicapValue(score, average) == -1){
                    value = "Score out of range. Sorry";
                } else if (getHandicapValue(score, average) == -2){
                    value = "Handicap out of range. Sorry";
                } else {

                    StringBuilder displayText = new StringBuilder("" + getHandicapValue(score, average));
                    if(displayText.toString().startsWith("100.")){
                        while(displayText.length() < 7){
                            displayText.append("0");
                        }
                    } else if (displayText.toString().startsWith("99.")){
                        while(displayText.length() < 6){
                            displayText.append("0");
                        }
                    }
                    value += displayText;
                }
                handicap.setText(value);
            }
        });
    }

    /**
     * Import the handicap values from the asset text file.
     * Provided by BSRC.
     */
    private void getHandicapData(){
        try{
            if(getActivity() != null) {
                InputStream is = this.getResources().getAssets().open("Handicap Reference.txt");
                System.out.println(is);
                int size = is.available();
                byte[] buffer = new byte[size];
                is.read(buffer);
                Scanner allValues = new Scanner(new String(buffer));
                for (int row = 0; row < 41; row++) {
                    for (int col = 0; col < 31; col++) {
                        handicaps[row][col] = allValues.nextDouble();
                    }
                }

            }

        } catch(IOException e){e.printStackTrace();}
    }

    /**
     * Get the handicap value from the score and average given
     * @param score - Score the shooter shot
     * @param average - Their average for the current series
     * @return the found Handicap value for the given handicap value and score
     */
    private double getHandicapValue(int score, int average){
        int row = score - 60;
        int col = average  - 70;
        if(row >= 41 || row < 0){return -1;}
        if(col >= 31 || col < 0){return -2;}
        return handicaps[row][col];
    }
}