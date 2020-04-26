import java.util.ArrayList;
import java.util.List;

public class EulerMethod {

    /**
     *
     * @param func - функция, меняется в MainFrame.java
     * @param x0 - x-координата начальной точки
     * @param y0 - y-координата начальной точки
     * @param lastX - x-координата последней точки (xn)
     * @param precision - точность (здесь она же будет шагом - опытным путем
     *                доказано, что это оптимальный шаг)
     * @return - массив точек, который дальше будет отрисовываться
     */
    public static List<Point> calculate(DoubleBiFunction func, double x0, double y0, double lastX, double precision) {
        double dx = precision;
        List<Point> points = new ArrayList<>();

        int amountOfNodes = (int) ((lastX - x0)/precision);

        double X[] = new double[amountOfNodes];
        double Y[] = new double[amountOfNodes];

        X[0] = x0;
        Y[0] = y0;
        points.add(new Point(X[0],Y[0]));
        for (int i = 1; i < amountOfNodes; i ++) {
            X[i] = x0 + i*precision;
            Y[i] = Y[i-1]+precision * calculateF(X[i - 1], Y[i - 1]);
            points.add(new Point(X[i],Y[i]));
        }
        return points;
    }
    //todo оно не работает так
    static double calculateF(double x, double y) {
        return 0;
    }
}
