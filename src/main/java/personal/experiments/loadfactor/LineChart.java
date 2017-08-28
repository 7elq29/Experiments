package personal.experiments.loadfactor;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.category.DefaultCategoryDataset;
import org.jfree.ui.ApplicationFrame;
import org.jfree.ui.RefineryUtilities;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.List;

/**
 * Created by Ken on 8/28/17.
 */
public class LineChart extends ApplicationFrame {

    DefaultCategoryDataset dataset = new DefaultCategoryDataset( );
    String chartTitle;
    int bucketNum;

    public LineChart( String applicationTitle , String chartTitle, int bucketNum) {
        super(applicationTitle);
        this.chartTitle=chartTitle;
        this.bucketNum=bucketNum;
    }

    public void setConflicts(List<Double[]> data){
        NumberFormat formatter = new DecimalFormat("#0.00");
        for(Double[] d:data){
            dataset.addValue(d[1],"conflict ratio", formatter.format(d[0]));
        }
    }

    public void build(){
        JFreeChart lineChart = ChartFactory.createLineChart(
                chartTitle+"["+this.bucketNum+"]",
                "Data size",
                "Conflict number",
                dataset,
                PlotOrientation.VERTICAL,
                true,true,false);

        ChartPanel chartPanel = new ChartPanel( lineChart );
        chartPanel.setPreferredSize( new java.awt.Dimension( 560 , 367 ) );
        setContentPane( chartPanel );
        this.pack();
        RefineryUtilities.centerFrameOnScreen( this );
        this.setVisible( true );

    }
}
