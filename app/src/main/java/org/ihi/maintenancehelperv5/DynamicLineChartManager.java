package org.ihi.maintenancehelperv5;

//import com.github.mikephil.charting.charts.LineChart;

import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.LimitLine;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

public class DynamicLineChartManager {

    private CustomLineChart lineChart;
    private YAxis leftAxis;
    private YAxis rightAxis;
    private XAxis xAxis;
    private LineData lineData;
    private LineDataSet lineDataSet;
    private List<ILineDataSet> lineDataSets = new ArrayList<ILineDataSet>();
    //private SimpleDateFormat df = new SimpleDateFormat("HH:mm:ss");//设置日期格式
    //private List<String> timeList = new ArrayList<>(); //存储x轴的时间

    //一条曲线
    /*public DynamicLineChartManager(CustomLineChart mLineChart, String name, int color) {
        this.lineChart = mLineChart;
        leftAxis = lineChart.getAxisLeft();
        rightAxis = lineChart.getAxisRight();
        xAxis = lineChart.getXAxis();
        initLineChart();
        initLineDataSet(name, color);
    }*/

    //多条曲线
    public DynamicLineChartManager(CustomLineChart mLineChart, List<String> names, List<Integer> colors) {
        this.lineChart = mLineChart;
        leftAxis = lineChart.getAxisLeft();
        rightAxis = lineChart.getAxisRight();
        xAxis = lineChart.getXAxis();
        initLineChart();
        initLineDataSet(names, colors);
    }

    /**
     * 初始化LineChar
     */
    private void initLineChart() {
        lineChart.getDescription().setEnabled(false);
        //lineChart.setDrawGridBackground(false);
        //显示边界
        lineChart.setDrawBorders(true);

        /*//折线图例 标签 设置
        Legend legend = lineChart.getLegend();
        legend.setForm(Legend.LegendForm.LINE);
        legend.setTextSize(11f);
        //显示位置
        legend.setVerticalAlignment(Legend.LegendVerticalAlignment.BOTTOM);
        legend.setHorizontalAlignment(Legend.LegendHorizontalAlignment.LEFT);
        legend.setOrientation(Legend.LegendOrientation.HORIZONTAL);
        legend.setDrawInside(false);*/

        /*//X轴设置显示位置在底部
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setGranularity(1f);
        xAxis.setLabelCount(10);*/

        ////自定义x轴显示
        ////获取此图表的x轴
        XAxis xAxis = lineChart.getXAxis();
        xAxis.setEnabled(false);//设置轴启用或禁用 如果禁用以下的设置全部不生效
        xAxis.setDrawAxisLine(false);//是否绘制轴线
        xAxis.setDrawGridLines(false);//设置x轴上每个点对应的线
        xAxis.setDrawLabels(false);//绘制标签  指x轴上的对应数值
        xAxis.setPosition(XAxis.XAxisPosition.TOP);//设置x轴的显示位置
        //xAxis.setTextSize(20f);//设置字体
        //xAxis.setTextColor(Color.BLACK);//设置字体颜色
        ////设置竖线的显示样式为虚线
        //lineLength控制虚线段的长度
        //spaceLength控制线之间的空间
        //xAxis.enableGridDashedLine(10, 10, 0);
        xAxis.setAxisMinimum(0);//设置x轴的最小值
        xAxis.setAxisMaximum(10);//设置最大值
        xAxis.setAvoidFirstLastClipping(true);//图表将避免第一个和最后一个标签条目被减掉在图表或屏幕的边缘
        //xAxis.setLabelRotationAngle(10);//设置x轴标签的旋转角度
        ////设置x轴显示标签数量 还有一个重载方法第二个参数为布尔值强制设置数量 如果启用会导致绘制点出现偏差
        //xAxis.setLabelCount(10);
        //xAxis.setTextColor(Color.BLUE);//设置轴标签的颜色
        //xAxis.setTextSize(24f);//设置轴标签的大小
        //xAxis.setGridLineWidth(10f);//设置竖线大小
        //xAxis.setGridColor(Color.RED);//设置竖线颜色
        //xAxis.setAxisLineColor(Color.GREEN);//设置x轴线颜色
        //xAxis.setAxisLineWidth(5f);//设置x轴线宽度
        //xAxis.setValueFormatter();//格式化x轴标签显示字符
        YAxis mLeftYAxis = lineChart.getAxis(YAxis.AxisDependency.LEFT);
        YAxis mRightYAxis = lineChart.getAxis(YAxis.AxisDependency.RIGHT);
        //mLeftYAxis.setAxisMinimum(-125);
        //mLeftYAxis.setAxisMaximum(-55);
        //mLeftYAxis.setLabelCount(8, true);
        //mRightYAxis.setAxisMinimum(-125);
        //mRightYAxis.setAxisMaximum(-55);
        //mRightYAxis.setLabelCount(8, true);

        /*xAxis.setValueFormatter(new IAxisValueFormatter() {
            @Override
            public String getFormattedValue(float value, AxisBase axis) {
                return timeList.get((int) value % timeList.size());
            }
        });*/

        //保证Y轴从0开始，不然会上移一点
        leftAxis.setAxisMinimum(0f);
        rightAxis.setAxisMinimum(0f);
    }

    /**
     * 初始化折线(一条线)
     *
     * @param name
     * @param color
     */
    /*private void initLineDataSet(String name, int color) {

        lineDataSet = new LineDataSet(null, name);
        lineDataSet.setLineWidth(1.0f);
        lineDataSet.setCircleRadius(1.0f);
        lineDataSet.setColor(color);
        lineDataSet.setCircleColor(color);
        lineDataSet.setHighLightColor(color);
        //设置曲线填充
        lineDataSet.setDrawFilled(false);
        lineDataSet.setAxisDependency(YAxis.AxisDependency.LEFT);
        lineDataSet.setValueTextSize(10f);
        lineDataSet.setMode(LineDataSet.Mode.CUBIC_BEZIER);
        //添加一个空的 LineData
        lineData = new LineData();
        lineChart.setData(lineData);
        lineChart.invalidate();
    }*/

    /**
     * 初始化折线（多条线）
     *
     * @param names
     * @param colors
     */
    private void initLineDataSet(List<String> names, List<Integer> colors) {

        for (int i = 0; i < names.size(); i++) {
            List<Entry> entrylist = new ArrayList<Entry>();
            entrylist.add(new Entry(0, -140));
            lineDataSet = new LineDataSet(entrylist, names.get(i));
            //lineDataSet = new LineDataSet(null, names.get(i));
            lineDataSet.setColor(colors.get(i));
            lineDataSet.setLineWidth(1.0f);
            lineDataSet.setCircleRadius(1.0f);
            lineDataSet.setColor(colors.get(i));

            lineDataSet.setDrawFilled(false);
            lineDataSet.setCircleColor(colors.get(i));
            lineDataSet.setHighLightColor(colors.get(i));
            lineDataSet.setMode(LineDataSet.Mode.CUBIC_BEZIER);
            lineDataSet.setAxisDependency(YAxis.AxisDependency.LEFT);
            lineDataSet.setValueTextSize(10f);
            lineDataSets.add(lineDataSet);
        }
        //添加一个空的 LineData
        /*lineData = new LineData();
        lineChart.setData(lineData);*/

        lineData = new LineData(lineDataSets);

        if (lineData == null) {
            lineChart.clear();
        } else {
            lineChart.setData(lineData);
        }

        lineChart.invalidate();
    }

    /**
     * 动态添加数据（一条折线图）
     *
     * @param number
     */
    public void addEntry(int number) {

        //最开始的时候才添加 lineDataSet（一个lineDataSet 代表一条线）
        if (lineDataSet.getEntryCount() == 0) {
            lineData.addDataSet(lineDataSet);
        }
        lineChart.setData(lineData);

        //避免集合数据过多，及时清空（做这样的处理，并不知道有没有用，但还是这样做了）
        /*if (timeList.size() > 11) {
            timeList.clear();
        }

        timeList.add(df.format(System.currentTimeMillis()));*/

        Entry entry = new Entry(lineDataSet.getEntryCount(), number);
        lineData.addEntry(entry, 0);
        //通知数据已经改变
        lineData.notifyDataChanged();
        lineChart.notifyDataSetChanged();
        //设置在曲线图中显示的最大数量
        lineChart.setVisibleXRangeMaximum(10);
        //移到某个位置
        lineChart.moveViewToX(lineData.getEntryCount() - 5);
    }

    /**
     * 动态添加数据（多条折线图）
     *
     * @param numbers
     */
    public void addEntry(List<Integer> numbers) {

        /*if (lineDataSets.get(0).getEntryCount() == 0) {
            lineData = new LineData(lineDataSets);
            lineChart.setData(lineData);
        }*/

        /*if (timeList.size() > 11) {
            timeList.clear();
        }
        timeList.add(df.format(System.currentTimeMillis()));*/

        for (int i = 0; i < numbers.size(); i++) {
            Entry entry = new Entry(lineDataSet.getEntryCount(), numbers.get(i));
            lineData.addEntry(entry, i);
            lineData.notifyDataChanged();
            lineChart.notifyDataSetChanged();
            lineChart.setVisibleXRangeMaximum(10);
            lineChart.moveViewToX(lineData.getEntryCount() - 5);
        }
    }

    /**
     * 设置Y轴值
     *
     * @param max
     * @param min
     * @param labelCount
     */
    public void setYAxis(float max, float min, int labelCount) {
        if (max < min) {
            return;
        }
        /*mLeftYAxis.setAxisMinimum(-125);
        mLeftYAxis.setAxisMaximum(-55);
        mLeftYAxis.setLabelCount(8, true);


        mRightYAxis.setAxisMinimum(-125);
        mRightYAxis.setAxisMaximum(-55);
        mRightYAxis.setLabelCount(8, true);*/

        leftAxis.setAxisMaximum(max);
        leftAxis.setAxisMinimum(min);
        leftAxis.setLabelCount(labelCount, true);

        rightAxis.setAxisMaximum(max);
        rightAxis.setAxisMinimum(min);
        rightAxis.setLabelCount(labelCount, true);
        lineChart.invalidate();
    }

    /**
     * 设置高限制线
     *
     * @param high
     * @param name
     */
    public void setHightLimitLine(float high, String name, int color) {
        if (name == null) {
            name = "高限制线";
        }
        LimitLine hightLimit = new LimitLine(high, name);
        hightLimit.setLineWidth(4f);
        hightLimit.setTextSize(10f);
        hightLimit.setLineColor(color);
        hightLimit.setTextColor(color);
        leftAxis.addLimitLine(hightLimit);
        lineChart.invalidate();
    }

    /**
     * 设置低限制线
     *
     * @param low
     * @param name
     */
    public void setLowLimitLine(int low, String name) {
        if (name == null) {
            name = "低限制线";
        }
        LimitLine hightLimit = new LimitLine(low, name);
        hightLimit.setLineWidth(4f);
        hightLimit.setTextSize(10f);
        leftAxis.addLimitLine(hightLimit);
        lineChart.invalidate();
    }

    /**
     * 设置描述信息
     *
     * @param str
     */
    public void setDescription(String str) {
        Description description = new Description();
        description.setText(str);
        lineChart.setDescription(description);
        lineChart.invalidate();
    }
}
