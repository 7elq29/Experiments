package personal.experiments.loadfactor;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.NumberTickUnit;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.plot.XYPlot;
import org.jfree.data.category.DefaultCategoryDataset;
import org.jfree.data.xy.DefaultXYDataset;
import org.jfree.data.xy.XYDataset;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;
import org.jfree.ui.ApplicationFrame;
import org.jfree.ui.RefineryUtilities;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.List;
import org.jfree.chart.axis.NumberAxis;

/**
 * Created by Ken on 8/28/17.
 */
public class LineChart extends ApplicationFrame {

    XYSeriesCollection dataset = new XYSeriesCollection( );
    String chartTitle;
    String categoryAxisLable;
    String valuAxisLabel;
    int bucketNum;

    public LineChart( String applicationTitle , String chartTitle, String categoryAxisLable, String valuAxisLabel, int bucketNum) {
        super(applicationTitle);
        this.chartTitle=chartTitle;
        this.bucketNum=bucketNum;
        this.categoryAxisLable=categoryAxisLable;
        this.valuAxisLabel=valuAxisLabel;
    }

    public void setData(List<Double[]> data, String rowKey){
        NumberFormat formatter = new DecimalFormat("#0.00");
        final XYSeries series = new XYSeries(rowKey);
        for(Double[] d:data){
            series.add(d[0],d[1]);
        }
        dataset.addSeries(series);
    }

    public void build(){
        JFreeChart lineChart = ChartFactory.createXYLineChart(
                chartTitle+"["+this.bucketNum+"]",
                categoryAxisLable,
                valuAxisLabel,
                dataset,
                PlotOrientation.VERTICAL,
                true,true,false);

        ChartPanel chartPanel = new ChartPanel( lineChart );
        chartPanel.setPreferredSize( new java.awt.Dimension( 1000 , 600 ) );
        setContentPane( chartPanel );
        this.pack();
        RefineryUtilities.centerFrameOnScreen( this );
        final XYPlot plot = lineChart.getXYPlot();
        final NumberAxis rangeAxis = (NumberAxis) plot.getRangeAxis();
        rangeAxis.setTickUnit(new NumberTickUnit(0.05));
        this.setVisible( true );

    }
}
