package com.gattaca.bitalinoecgchart;

import android.graphics.Color;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;

public class RealTimeChart {

    private LineChart mChart;
    MonitorActivity mActivity;

    private static final int VISIBLE_NUM = 100;

    public RealTimeChart(MonitorActivity monitorActivity) {
        mActivity = monitorActivity;
    }

    public void init() {

        mChart = (LineChart)mActivity.findViewById(R.id.chart);
        //mChart.setOnChartValueSelectedListener(this);

        // no description text
        mChart.setDescription("");
        mChart.setNoDataTextDescription("You need to provide data for the chart.");

        // enable touch gestures
        mChart.setTouchEnabled(true);

        // enable scaling and dragging
        mChart.setDragEnabled(true);
        mChart.setScaleEnabled(true);
        mChart.setDrawGridBackground(false);

        // if disabled, scaling can be done on x- and y-axis separately
        mChart.setPinchZoom(true);

        // set an alternative background color
        mChart.setBackgroundColor(Color.BLACK);

        LineData data = new LineData();
        data.setValueTextColor(Color.WHITE);

        // add empty data
        mChart.setData(data);

        // get the legend (only possible after setting data)
        Legend l = mChart.getLegend();

        // modify the legend ...
        // l.setPosition(LegendPosition.LEFT_OF_CHART);
        l.setForm(Legend.LegendForm.LINE);
        //l.setTypeface(mTfLight);
        l.setTextColor(Color.WHITE);

        XAxis xl = mChart.getXAxis();
        //xl.setTypeface(mTfLight);
        xl.setTextColor(Color.WHITE);
        xl.setDrawGridLines(false);
        xl.setAvoidFirstLastClipping(true);
        xl.setEnabled(true);

        YAxis leftAxis = mChart.getAxisLeft();
        //leftAxis.setTypeface(mTfLight);
        leftAxis.setTextColor(Color.WHITE);
        leftAxis.setAxisMaxValue(1.5f);
        leftAxis.setAxisMinValue(-1.5f);
        leftAxis.setDrawGridLines(false);

        YAxis rightAxis = mChart.getAxisRight();
        rightAxis.setEnabled(false);

    }

    public void addData(float value) {
        LineData data = mChart.getData();
        if (data != null) {
            LineDataSet set = (LineDataSet)data.getDataSetByIndex(0);

            if (set == null) {
                set = createSet();
                data.addDataSet(set);
            }

            if(set.getEntryCount() == VISIBLE_NUM) {
                set.removeFirst();

                for (Entry entry : set.getValues()) {
                    entry.setX(entry.getX() - 1);
                }
            }
            data.addEntry(new Entry(set.getEntryCount(), value), 0);

            mChart.notifyDataSetChanged();
            mChart.postInvalidate();

            //mChart.setVisibleXRange(VISIBLE_NUM-1);
            //mChart.moveViewToX(data.getXValCount() - VISIBLE_NUM);
            //Toast.makeText(mActivity, Float.toString(value), Toast.LENGTH_SHORT).show();
        }
    }

    private LineDataSet createSet() {
        LineDataSet set = new LineDataSet(null, "DataSet");
        //set.enableDashedLine(10f, 5f, 0f);
        set.setColor(Color.BLUE);
        set.setCircleColor(Color.RED);
        set.setLineWidth(1f);
        set.setDrawCircleHole(true);
        set.setValueTextSize(9f);
        //set.setFillAlpha(65);
        set.setFillColor(Color.GREEN);
        return set;
    }


}
