import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;

import javax.sound.sampled.Line;
import java.awt.*;
import java.util.ArrayList;
import java.util.Random;

public class Controller implements Runnable {
    @FXML
    Slider slider;
    @FXML
    Label number;
    @FXML
    Canvas canvas;
    @FXML
    Button draw;
    @FXML
    GraphicsContext gc;

    private double xMaxCanvas;
    private double yMaxCanvas;
    private ArrayList<Point> points;
    private Thread test = new Thread(this);
    private ArrayList<Point> mittelSenkrechten = new ArrayList<>();

    //FXML Handle Actions
    public void HandleDrawButton(ActionEvent event) {
        setup(); //starts animation
    }

    public void numberOnMouseReleased(MouseEvent event) { //Output Slidernumber on Label
        String numberOfSlider = String.valueOf((int) slider.getValue());
        number.setText(numberOfSlider);
    }

    //Methods
    public void drawBorder() {
        gc = canvas.getGraphicsContext2D();
        // draws Border for the Pane
        gc.setStroke(Color.BLACK);
        gc.moveTo(0, 0);
        gc.lineTo(0, 775);
        gc.lineTo(1125, 775);
        gc.lineTo(1125, 0);
        gc.lineTo(0, 0);
        gc.stroke();
        xMaxCanvas = canvas.getWidth() - 50;
        yMaxCanvas = canvas.getHeight() - 50;//-50 damit es nicht ganz am rand mit dem Pane ist
    }

    public void drawPoints() {
        points = new ArrayList<>();
        Random random = new Random();
        for (int x = 0; x < (int) slider.getValue(); x++) {
            int xPoint = ((int) (Math.random() * xMaxCanvas));
            int yPoint = ((int) (Math.random() * yMaxCanvas));
            Point point = new Point(xPoint, yPoint);
            points.add(point);
            gc.fillOval(xPoint, yPoint, 2, 2);
            sortByXCoordinates();
        }
    }

    public void drawDistance() {
        for (int i = 0; i < points.size(); i++) {
            for (int j = 0; j < points.size(); j++) {
                if (i < points.size()) {
                    gc.strokeLine(points.get(i).getX(), points.get(i).getY(), points.get(j).getX(), points.get(j).getY());
                }
            }
        }
    }

    public void mittelsenkrechte() {
        gc.setStroke(Color.RED);
        double xBisector;
        double yBisector;
        double gradient;
        double reversedGradient;
        ArrayList<Point> prependicularBisectorx = new ArrayList<>();
        for (int i = 0; i < points.size(); i++) {
            for (int j = i; j < points.size(); j++) {
                xBisector = (points.get(i).getX() + points.get(j).getX()) / 2;
                yBisector = (points.get(i).getY() + points.get(j).getY()) / 2;
                gradient = (points.get(j).getY() - points.get(i).getY()) / (points.get(j).getX() - points.get(i).getX());
                reversedGradient = -1 / gradient;
                double yAchsenAbschnitt = yBisector - (reversedGradient * xBisector);
                double xOppositeYAchsenabschnitt = (-yAchsenAbschnitt + 900) / reversedGradient;
                Point oppositeOfYAchsenAbschnitt = new Point();
                oppositeOfYAchsenAbschnitt.setLocation(xOppositeYAchsenabschnitt, 900);
                prependicularBisectorx.add(oppositeOfYAchsenAbschnitt);
                double SchnittpunktZwischen = -yAchsenAbschnitt / reversedGradient;
                if (reversedGradient > 0) {
                    gc.strokeLine(prependicularBisectorx.get(prependicularBisectorx.size() - 1).getX(), 900, 0, yAchsenAbschnitt);
                    System.out.println("positive i= " + i + " j= " + j);
                } else if (reversedGradient < 0) {
                    gc.strokeLine(SchnittpunktZwischen, 0, 0, yAchsenAbschnitt);
                    System.out.println("negative i= " + i + " j= " + j);
                } else if (gradient == 0) {
                    System.out.println("Division b y0 i=" + i + " j= " + j);
                }
            }
        }
        gc.setStroke(Color.BLACK);
    }

    public void sortByXCoordinates() {
        this.points.sort(new XCoordinatesComparator());
    }

    public void sortByYCoordinates() {
        this.points.sort(new YCoordinatesComparator());
    }

    public void outterCircle() {

        for (int h = 0; h < canvas.getWidth(); h++) {
            try {
                for (int i = 0; i < points.size(); i++) {
                    gc.fillOval(points.get(i).getX() - (h / 2), points.get(i).getY() - (h / 2), h, h);
                    Point temp = points.get(i).getLocation();
                    //This loop Searching for every point that matches the circle coordinate in its 360 Degree Angles
                    for (Point x : mittelSenkrechten) {
                        for (double angle = 0; angle < 360; angle++) {
                            double radians = Math.toRadians(angle);
                            double tempx = (h / 2) * Math.cos(radians);
                            double tempy = (h / 2) * Math.sin(radians);
                            Point test = new Point((int) (points.get(i).getX() + tempx), (int) (points.get(i).getY() + tempy));
                            if (test.equals(x)) {     //If a point is matched the circle will be colored Red
                                gc.setFill(Color.RED);
                                gc.fillOval(points.get(i).getX() - (h / 2), points.get(i).getY() - (h / 2), h, h);
                                gc.setStroke(Color.RED);
                                gc.strokeLine(x.getX(), x.getY(), x.getX() + 2, x.getY() + 2);

                            }
                            gc.setFill(Color.BLACK);
                            gc.setStroke(Color.BLACK);
                        }
                    }
                }
                Thread.sleep(100);
                gc.clearRect(0, 0, canvas.getWidth(), canvas.getHeight());
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            mittelsenkrechte();
        }
    }

    @Override
    public void run() {
        drawBorder();
        drawPoints();
        //drawDistance();
        mittelsenkrechte();
        outterCircle();


    }

    public void setup() {
        test.stop();
        test = new Thread(this);
        test.start();

    }
}

