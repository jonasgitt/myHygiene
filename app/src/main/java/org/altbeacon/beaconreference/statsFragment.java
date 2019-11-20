package org.altbeacon.beaconreference;

import android.content.ComponentName;
import android.content.Context;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.Chart;
import com.github.mikephil.charting.charts.CombinedChart;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.CombinedData;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;
import com.github.mikephil.charting.formatter.PercentFormatter;
import com.github.mikephil.charting.formatter.ValueFormatter;
import com.github.mikephil.charting.interfaces.datasets.IBarDataSet;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.github.mikephil.charting.utils.ViewPortHandler;

import java.text.DecimalFormat;
import java.util.ArrayList;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link statsFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link statsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class statsFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    public statsFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment statsFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static statsFragment newInstance(String param1, String param2) {
        statsFragment fragment = new statsFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_stats, container, false);

        CombinedChart chart = (CombinedChart) view.findViewById(R.id.progress_bar_chart);
        createProgressChart(chart);

        BarChart barchart = (BarChart) view.findViewById(R.id.comparison_barchart);

        createComparisonChart(barchart);

        return view;
    }

    private void createComparisonChart(BarChart chart){

        chart.getXAxis().setPosition(XAxis.XAxisPosition.BOTTOM);
        chart.getAxisRight().setEnabled(false);
        YAxis yAxis = chart.getAxisLeft();
        yAxis.setValueFormatter(new customPercentFormatter());

        chart.getLegend().setEnabled(false);
        chart.setData(generateComparisonData());

        chart.highlightValue(4.4f,0, false);
        chart.setHighlightPerTapEnabled(false);
        chart.setHighlightPerDragEnabled(false);
        chart.getXAxis();
        chart.invalidate();
    }

    private BarData generateComparisonData(){

        ArrayList<BarEntry> entries1 = new ArrayList<>();

        entries1.add(new BarEntry(0.4f,2.3f));
        entries1.add(new BarEntry(0.8f,4.3f));
        entries1.add(new BarEntry(1.2f,6.1f));
        entries1.add(new BarEntry(1.6f,7.5f));
        entries1.add(new BarEntry(2f,8f));
        entries1.add(new BarEntry(2.4f,8f));
        entries1.add(new BarEntry(2.8f,7.8f));
        entries1.add(new BarEntry(3.2f,7.2f));
        entries1.add(new BarEntry(3.6f,6.2f));
        entries1.add(new BarEntry(4f,5.4f));
        entries1.add(new BarEntry(4.4f,4.6f));
        entries1.add(new BarEntry(4.8f,3.9f));
        entries1.add(new BarEntry(5.2f,3.2f));
        entries1.add(new BarEntry(5.6f,2.5f));
        entries1.add(new BarEntry(6f,2f));
        entries1.add(new BarEntry(6.4f,1.6f));
        entries1.add(new BarEntry(6.8f,1.25f));
        entries1.add(new BarEntry(7.2f,1f));
        entries1.add(new BarEntry(7.6f,0.95f));
        entries1.add(new BarEntry(8f,0.9f));
        entries1.add(new BarEntry(8.4f,0.85f));
        entries1.add(new BarEntry(8.8f,0.82f));
        entries1.add(new BarEntry(9.2f,0.81f));
        entries1.add(new BarEntry(9.6f,0.81f));
        entries1.add(new BarEntry(10f,0.81f));


        BarDataSet set1 = new BarDataSet(entries1, "Room Entries");
        set1.setColor(Color.rgb(187,134,252));

        set1.setValueTextSize(0f);
        set1.setAxisDependency(YAxis.AxisDependency.LEFT);

        set1.setHighLightColor(Color.RED);

        //set1.setGradientColor(Color.rgb(98, 0, 238),Color.rgb(55, 0, 179) );


        float barWidth = 0.3f;

        BarData d = new BarData(set1);
        d.setBarWidth(barWidth);

        return d;
    }


    private void createProgressChart(CombinedChart chart){

        chart.getXAxis().setPosition(XAxis.XAxisPosition.BOTTOM);
        chart.getAxisRight().setEnabled(false);
        chart.getAxisLeft().setEnabled(false);

        Legend l = chart.getLegend();
        l.setVerticalAlignment(Legend.LegendVerticalAlignment.TOP);
        l.setHorizontalAlignment(Legend.LegendHorizontalAlignment.CENTER);
        l.setOrientation(Legend.LegendOrientation.HORIZONTAL);
        l.setDrawInside(true);

        CombinedData data = new CombinedData();

        data.setData(generateLineData());
        data.setData(generateBarData());

        XAxis xAxis = chart.getXAxis();
        final String[] weekdays = {"Jan", "Feb", "Mar", "Apr", "May", "Jun", "Jul", "Aug", "Sep", "Oct", "Nov", "Dec"}; // Your List / array with String Values For X-axis Labels
        xAxis.setValueFormatter(new IndexAxisValueFormatter(weekdays));


        chart.setData(data);
        chart.invalidate();

    }


    private LineData generateLineData() {

        LineData d = new LineData();

        ArrayList<Entry> entries1 = new ArrayList<>();


        entries1.add(new Entry(1, 260));
        entries1.add(new Entry(2, 255));
        entries1.add(new Entry(3, 250));
        entries1.add(new Entry(4, 260));
        entries1.add(new Entry(5, 278));
        entries1.add(new Entry(6, 290));
        entries1.add(new Entry(7, 278));
        entries1.add(new Entry(8, 299));
        entries1.add(new Entry(9, 297));
        entries1.add(new Entry(10, 310));
        entries1.add(new Entry(11, 305));
        entries1.add(new Entry(12, 288));

        LineDataSet set = new LineDataSet(entries1, "Disinfections");
        set.setColor(Color.rgb(55, 0, 179));

        set.setLineWidth(2.5f);
        set.setColor(Color.rgb(55, 0, 179));

        set.setCircleRadius(5f);
        set.setCircleColor(Color.rgb(55, 0, 179));
        set.setMode(LineDataSet.Mode.CUBIC_BEZIER);
        set.setDrawValues(true);
        set.setValueTextSize(10f);
        set.setColor(Color.rgb(55, 0, 179));

        set.setAxisDependency(YAxis.AxisDependency.LEFT);
        d.addDataSet(set);



        return d;
    }

    private BarData generateBarData() {

        ArrayList<BarEntry> entries1 = new ArrayList<>();

        entries1.add(new BarEntry(1, 350));
        entries1.add(new BarEntry(2, 330));
        entries1.add(new BarEntry(3, 345));
        entries1.add(new BarEntry(4, 321));
        entries1.add(new BarEntry(5, 342));
        entries1.add(new BarEntry(6, 333));
        entries1.add(new BarEntry(7, 352));
        entries1.add(new BarEntry(8, 324));
        entries1.add(new BarEntry(9, 301));
        entries1.add(new BarEntry(10, 355));
        entries1.add(new BarEntry(11, 325));
        entries1.add(new BarEntry(12, 352));

        BarDataSet set1 = new BarDataSet(entries1, "Room Entries");

        set1.setColor(Color.rgb(187,134,252));

        set1.setValueTextSize(10f);
        set1.setAxisDependency(YAxis.AxisDependency.LEFT);

        float barWidth = 0.6f;

        BarData d = new BarData(set1);
        d.setBarWidth(barWidth);

        return d;
    }





    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
//            throw new RuntimeException(context.toString()
//                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
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
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }


    public class customPercentFormatter extends ValueFormatter {

        public DecimalFormat mFormat;

        public customPercentFormatter() {
            mFormat = new DecimalFormat("###,###,###");
        }

        @Override
        public String getFormattedValue(float value) {
            return mFormat.format(value) + "%";
        }
    }
}
