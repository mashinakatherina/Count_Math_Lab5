import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.XYLineAndShapeRenderer;
import org.jfree.data.xy.XYDataset;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;
import org.jfree.ui.RefineryUtilities;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.geom.Ellipse2D;
import java.util.ArrayList;
import java.util.List;

public class MainFrame extends JFrame {

    private final List<NamingDoubleBiFunction> functions;
    private JPanel chartPanel;
    private List<Point> points;
    private final XYSeriesCollection dataset;
    private final XYSeries pointList = new XYSeries("Искомая функция");
    private JComboBox<String> functionNames;
    private final IntegerTextField x0;
    private final IntegerTextField y0;
    private final IntegerTextField xn;
    private final IntegerTextField precision;
    private JButton actionButton;
    public static int tmpCounter;


    {
        setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
        addOnExitListener();

        functions = new ArrayList<>();
        points = new ArrayList<>();
        dataset = new XYSeriesCollection();
        x0 = new IntegerTextField();
        y0 = new IntegerTextField();
        xn = new IntegerTextField();
        precision = new IntegerTextField();
        fillFunctions();
        initGraph();
        JLabel functionLabel = new JLabel("Уравнение: ");
        initFunctionAliases();

        JPanel main = new JPanel();
        main.setLayout(new BoxLayout(main, BoxLayout.PAGE_AXIS));
        main.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        main.setPreferredSize(new Dimension(600, 600));
        main.add(chartPanel);

        JPanel funcPanel = new JPanel();
        funcPanel.add(functionLabel);
        funcPanel.add(functionNames);
        main.add(funcPanel);

        JPanel inputs = new JPanel(new GridLayout(0, 4, 35, 5));
        inputs.add(new JLabel("x0"));
        inputs.add(new JLabel("y0"));
        inputs.add(new JLabel("xn"));
        inputs.add(new JLabel("Точность"));
        inputs.add(x0);
        inputs.add(y0);
        inputs.add(xn);
        inputs.add(precision);
        main.add(inputs);

        initButtons();
        JPanel buttonPanel = new JPanel();
        buttonPanel.add(actionButton);
        main.add(buttonPanel);

        setContentPane(main);
    }

    public MainFrame(String header) {
        super(header);
    }

    private void initButtons() {
        actionButton = new JButton("Вычислить");
        actionButton.addActionListener(event -> {
            int numberFunction = functionNames.getSelectedIndex();
            double x0Arg, y0Arg, xnArg, precisionArg;
            JFrame frame = this;
            try {
                x0Arg = Double.parseDouble(x0.getText());
            } catch (Exception e) {
                JOptionPane.showMessageDialog(frame, "Неверная координата x0!", "Ошибка!", JOptionPane.ERROR_MESSAGE);
                return;
            }
            try {
                y0Arg = Double.parseDouble(y0.getText());
            } catch (Exception e) {
                JOptionPane.showMessageDialog(frame, "Неверная координата y0!", "Ошибка!", JOptionPane.ERROR_MESSAGE);
                return;
            }
            try {
                xnArg = Double.parseDouble(xn.getText());
            } catch (Exception e) {
                JOptionPane.showMessageDialog(frame, "Неверная координата xn!", "Ошибка!", JOptionPane.ERROR_MESSAGE);
                return;
            }
            try {
                precisionArg = Double.parseDouble(precision.getText());
                if (precisionArg <= 0 || precisionArg > 1) {
                    precisionArg = 0.001;
                    JOptionPane.showMessageDialog(frame, "Будет использовано значение по умолчанию равное 0.001", "Неверное значение точности!", JOptionPane.ERROR_MESSAGE);
                }
            } catch (Exception e) {
                JOptionPane.showMessageDialog(frame, "Неверное значение точности!", "Ошибка!", JOptionPane.ERROR_MESSAGE);
                return;
            }

            tmpCounter = functions.get(numberFunction).getFunctionNumber();
            points = EulerMethod.calculate(x0Arg, y0Arg, xnArg, precisionArg);
            initDataset();
        });
        actionButton.setAlignmentX(Component.CENTER_ALIGNMENT);
    }

    private void addOnExitListener() {
        final MainFrame mainFrame = this;
        this.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                if (JOptionPane.showConfirmDialog(mainFrame, "Вы действительно хотите выйти?", "Закрытие программы", JOptionPane.OK_CANCEL_OPTION, JOptionPane.WARNING_MESSAGE) == 0)
                    System.exit(0);
            }
        });
    }

    private void initFunctionAliases() {
        String[] funcNames = functions.stream().map(NamingDoubleBiFunction::getAlias).toArray(String[]::new);
        for (int i = 0; i < funcNames.length; ++i)
            funcNames[i] = "y' = " + funcNames[i];
        functionNames =
                new JComboBox<>(funcNames);
    }

    private void fillFunctions() {
        functions.add(new NamingDoubleBiFunction((x, y) -> -2 * x, "-2x", 1));
        functions.add(new NamingDoubleBiFunction((x, y) -> x - y, "x - y", 2));
        functions.add(new NamingDoubleBiFunction((x, y) -> Math.log(x * x + 1), "ln(x^2+1)", 3));
        functions.add(new NamingDoubleBiFunction((x, y) -> Math.sin(x) - y, "sin(x) - y", 4));
    }

    private void initGraph() {
        final JFreeChart chart = initChart(initDataset());
        chartPanel = new ChartPanel(chart);
        chartPanel.setPreferredSize(new Dimension(500, 500));
    }

    private JFreeChart initChart(final XYDataset dataset) {

        final JFreeChart chart = ChartFactory.createXYLineChart(
                null,                          // chart title
                "X",                      // x axis label
                "Y",                      // y axis label
                dataset,                            // data
                PlotOrientation.VERTICAL,           // orientation
                true,                        // include legend
                false,                      // tooltips
                false                          // urls
        );


        chart.setBackgroundPaint(Color.lightGray);

        final XYPlot plot = chart.getXYPlot();
        plot.setBackgroundPaint(Color.white);
        plot.setDomainGridlinePaint(Color.gray);
        plot.setRangeGridlinePaint(Color.gray);

        final XYLineAndShapeRenderer renderer = new XYLineAndShapeRenderer();
        renderer.setSeriesShapesVisible(0, false);
        final double ellipseRadius = 5;
        renderer.setSeriesShape(0, new Ellipse2D.Double(-ellipseRadius / 2, -ellipseRadius / 2, ellipseRadius, ellipseRadius));
        renderer.setSeriesShape(1, new Ellipse2D.Double(-ellipseRadius / 2, -ellipseRadius / 2, ellipseRadius, ellipseRadius));
        plot.setRenderer(renderer);


        final NumberAxis rangeAxis = (NumberAxis) plot.getRangeAxis();
        rangeAxis.setStandardTickUnits(NumberAxis.createIntegerTickUnits());

        return chart;

    }

    private XYDataset initDataset() {

        dataset.removeAllSeries();
        pointList.clear();
        points.forEach(e -> pointList.add(e.getX(), e.getY()));
        dataset.addSeries(pointList);
        return dataset;

    }

    public static void main(String... args) {
        MainFrame mainFrame = new MainFrame("Лабораторная работа №5");
        mainFrame.pack();
        mainFrame.setMinimumSize(mainFrame.getSize());
        RefineryUtilities.centerFrameOnScreen(mainFrame);
        mainFrame.setVisible(true);
    }
}