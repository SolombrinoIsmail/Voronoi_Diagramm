import java.awt.*;
import java.util.Comparator;

//This comparator compares the given point by it's X Coordinate Distance
public class XCoordinatesComparator implements Comparator<Point> {

    @Override
    public int compare(Point p1, Point p2) {
        return Integer.compare(p1.x, p2.x);
    }

}
