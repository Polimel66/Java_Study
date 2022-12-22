import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GradientPaint;
import java.awt.Point;
import java.util.ArrayList;
import javax.swing.JPanel;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PiePlot;
import org.jfree.chart.title.TextTitle;
import org.jfree.data.general.DefaultPieDataset;
import org.jfree.data.general.PieDataset;
import org.jfree.chart.ui.ApplicationFrame;
import org.jfree.chart.ui.RectangleInsets;

public class Chart extends ApplicationFrame {
    public Chart(String title, ArrayList<Integer> args) {
        super(title);
        setContentPane(createDemoPanel(args));
    }

    private PieDataset<String> createDataset(ArrayList<Integer> args) {
        DefaultPieDataset<String> dataset = new DefaultPieDataset<String>();
        dataset.setValue("75.0 - 80.0", args.get(0));
        dataset.setValue("80.0 - 85.0", args.get(1));
        dataset.setValue("85.0 - 90.0", args.get(2));
        dataset.setValue("90.0 - 95.0", args.get(3));
        dataset.setValue("95.0 - 100.0", args.get(4));
        return dataset;
    }

    private JFreeChart createChart(PieDataset<String> dataset) {
        JFreeChart chart = ChartFactory.createPieChart(
                "Распределение баллов среди студентов",
                dataset,
                false,
                true,
                false
        );

        // Определение фона графического изображения
        chart.setBackgroundPaint(new GradientPaint(new Point(0, 0), new Color(100, 0, 255),
                new Point(400, 200), Color.darkGray));
        // Определение заголовка
        TextTitle t = chart.getTitle();
        t.setPaint(new Color(240, 240, 240));
        t.setFont(new Font("Arial", Font.BOLD, 26));

        // Настройка фона диаграммы
        var plot = (PiePlot) chart.getPlot();
        plot.setBackgroundPaint(null);
        plot.setInteriorGap(0.04);
        plot.setOutlineVisible(false);

        // Определение секций круговой диаграммы
        plot.setDefaultSectionOutlinePaint(Color.WHITE);
        plot.setSectionOutlinesVisible(true);
        plot.setDefaultSectionOutlineStroke(new BasicStroke(2.0f));

        // Настройка меток названий секций
        plot.setLabelFont(new Font("Courier New", Font.BOLD, 20));
        plot.setLabelLinkPaint(Color.WHITE);
        plot.setLabelLinkStroke(new BasicStroke(2.0f));
        plot.setLabelOutlineStroke(null);
        plot.setLabelPaint(Color.WHITE);
        plot.setLabelBackgroundPaint(null);
        return chart;
    }

    public JPanel createDemoPanel(ArrayList<Integer> args) {
        JFreeChart chart = createChart(createDataset(args));
        chart.setPadding(new RectangleInsets(4, 8, 2, 2));
        ChartPanel panel = new ChartPanel(chart);
        panel.setFillZoomRectangle(true);
        panel.setMouseWheelEnabled(true);
        panel.setPreferredSize(new Dimension(600, 300));
        return panel;
    }

    public static void main(String[] args) {

    }

    public static void makeChart(ArrayList<Integer> args) {
        Chart demo = new Chart("JFreeChart: PieChart Demo", args);
        demo.pack();
        demo.setVisible(true);
    }
}

