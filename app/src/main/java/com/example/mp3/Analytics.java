package com.example.mp3;

import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.eazegraph.lib.charts.ValueLineChart;
import org.eazegraph.lib.models.BarModel;
import org.eazegraph.lib.models.PieModel;
import org.eazegraph.lib.models.ValueLinePoint;
import org.eazegraph.lib.models.ValueLineSeries;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

public class Analytics extends AppCompatActivity {

    private DatabaseReference databaseRef;
    ArrayList<Social> socials;
    Map<String, Integer> userToNumPosts;
    BarChart chart;
    PieChart piechart;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_analytics);

        socials = new ArrayList<>();

        databaseRef = FirebaseDatabase.getInstance().getReference().child("socials");

        userToNumPosts = new HashMap<>();


        databaseRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // This method is called once with the initial value and again
                // whenever data at this location is updated.

                socials.clear();

                // fetching new snapshot and convert it to a social object
                for (DataSnapshot dataSnapshot2: dataSnapshot.getChildren()) {
                    socials.add(0, FirebaseUtils.getSocialFromFirebase(dataSnapshot2));
                }

                /*
                * Creates bar chart that shows how many posts each user has
                * */
                // get number of posts per user
                userToNumPosts.clear();
                for (Social s : socials) {
                    if (!userToNumPosts.containsKey(s.getPoster())) {
                        userToNumPosts.put(s.getPoster(), 1);
                    } else {
                        userToNumPosts.put(s.getPoster(), userToNumPosts.get(s.getPoster()) + 1);
                    }
                }


                chart = findViewById(R.id.barchart);
                ArrayList<BarEntry> barEntries = new ArrayList<>();
                ArrayList<String> labels = new ArrayList<>();
                int i = 0;
                for (String key : userToNumPosts.keySet()) {
                    barEntries.add(new BarEntry((float) userToNumPosts.get(key), i));
                    String username = key.substring(0, key.indexOf("@"));
                    labels.add(username);
                    i += 1;
                }

                BarDataSet barDataSet = new BarDataSet(barEntries, "Usernames");

                BarData theData = new BarData(labels, barDataSet);
                chart.setData(theData);
                chart.setTouchEnabled(true);
                chart.setDragEnabled(true);
                chart.setScaleEnabled(true);

                /*
                * Creates pie chart showing the most popular events
                * */



                Map<String, Integer> eventNameToCount = new HashMap<>();
                for (Social s : socials) {
                    int count = Integer.parseInt(s.getInterested());
                    if (count != 0) {
                        eventNameToCount.put(s.getEventName(), count);
                    }

                }

                piechart = findViewById(R.id.piechart);

                piechart.setUsePercentValues(true);
                piechart.setExtraOffsets(5, 10, 5, 5);
                piechart.setDragDecelerationFrictionCoef(0.95f);
                piechart.setDrawHoleEnabled(true);
                piechart.setHoleColor(Color.WHITE);
                piechart.setTransparentCircleRadius(61f);




                List<Entry> pieEntries = new ArrayList<>();
                ArrayList<String> pieLabels = new ArrayList<>();
                int j = 0;
                for (String key : eventNameToCount.keySet()) {
                    pieEntries.add(new Entry((float) eventNameToCount.get(key), j));
                    pieLabels.add(key);
                    j += 1;
                }

                PieDataSet dataSet = new PieDataSet(pieEntries, "Event Names");
                dataSet.setColors(ColorTemplate.JOYFUL_COLORS);
                dataSet.setSliceSpace(3f);

                PieData piedata = new PieData(pieLabels, dataSet);
                piechart.setData(piedata);

            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                Log.w("FAILURE", "Failed to read value.", error.toException());
            }
        });



    }
}
