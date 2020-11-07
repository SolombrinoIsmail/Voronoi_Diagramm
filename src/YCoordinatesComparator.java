import java.awt.*;
import java.util.Comparator;

public class YCoordinatesComparator implements Comparator<Point> {

    @Override
    public int compare(Point p1, Point p2) {
        return Integer.compare(p1.y, p2.y);
    }
}
