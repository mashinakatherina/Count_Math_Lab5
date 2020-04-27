import java.util.ArrayList;
import java.util.List;

public class EulerMethod {

    /**
     *
     * @param x0 - x-координата начальной точки
     * @param y0 - y-координата начальной точки
     * @param lastX - x-координата последней точки (xn)
     * @param precision - точность
     * @return - массив точек, который дальше будет отрисовываться
     */
    public static List<Point> calculateByEulerMethod(double x0, double y0, double lastX, double precision) {
        List<Point> points = new ArrayList<>();

        int amountOfNodes = (int) ((lastX - x0)/precision);

        double[] X = new double[amountOfNodes];
        double[] Y = new double[amountOfNodes];

        X[0] = x0;
        Y[0] = y0;
        points.add(new Point(X[0],Y[0]));
        for (int i = 1; i < amountOfNodes; i ++) {
            X[i] = x0 + i*precision;
            Y[i] = Y[i-1]+precision * calculateFunction(X[i - 1], Y[i - 1]);
            points.add(new Point(X[i],Y[i]));
        }
        return points;
    }

    static double calculateFunction(double x, double y) {
        switch (MainFrame.tmpCounter)
        {
            case 1: return -2*x;
            case 2: return x - y;
            case 3: return Math.log(x * x + 1);
            case 4: return Math.sin(x) - y;
        }
        return 0;
    }
}