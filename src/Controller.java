import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;

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
        drawBorder();
        drawPoints();
        mittelsenkrechte();
        setup(); //starts animation
    }

    public void numberOnMouseReleased(MouseEvent event) { //Output Slidernumber on Label
        String numberOfSlider = String.valueOf(Math.round(slider.getValue()));
        number.setText(numberOfSlider);
    }

    //Methods
    public void drawBorder() {
        System.out.println("Drawing Border");
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
        System.out.println("Drawing Points");
        gc.clearRect(0, 0, canvas.getWidth(), canvas.getHeight());
        points = new ArrayList<>();
        Random random = new Random();
        for (int x = 0; x < slider.getValue(); x++) {
            int xPoint = ((int) (Math.random() * xMaxCanvas));
            int yPoint = ((int) (Math.random() * yMaxCanvas));
            Point point = new Point(xPoint, yPoint);
            System.out.println(xPoint + " " + yPoint);
            points.add(point);
            gc.fillOval(xPoint, yPoint, 5, 5);
        }
    }

    public void mittelsenkrechte() {
        double xLine;
        double yLine;
        sortByXCoordinates();
        //System.out.println("Drawing Separation Lines based of distance of points");
        for (int j = 1; j < 4; j++) {
            for (int i = 0; i < points.size(); i++) {
                //System.out.println("Location: X= " + points.get(i).getX() + " Y= " + points.get(i).getY());
                if (i < points.size() - j) {
                    xLine = (points.get(i).getX() + points.get(i + j).getX()) / 2;
                    yLine = (points.get(i).getY() + points.get(i + j).getY()) / 2;
                    //System.out.println("X Line= " + xLine + " Y Line: " + yLine);
                    Point mitte = new Point();
                    mitte.setLocation(xLine, yLine);
                    mittelSenkrechten.add(mitte);
                    gc.strokeLine(xLine, yLine, xLine + 2, yLine + 2);
                }
            }

        }
    }

    public void sortByXCoordinates() {
        this.points.sort(new XCoordinatesComparator());
    }

    public void sortByYCoordinates() {
        this.points.sort(new YCoordinatesComparator());
    }

    @Override
    public void run() {
        sortByXCoordinates();
        sortByYCoordinates();
        for (int h = 0; h < canvas.getWidth(); h++) {
            try {
                for (int i = 0; i < points.size(); i++) {
                    gc.strokeOval(points.get(i).getX() - (h / 2), points.get(i).getY() - (h / 2), h, h);
                    //
                    //System.out.println("Circle Durchmesser " + h);
                }
                Thread.sleep(30);
                gc.clearRect(0, 0, canvas.getWidth(), canvas.getHeight());
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            mittelsenkrechte();
        }
    }

    public void setup() {
        test.stop();
        test = new Thread(this);
        test.start();
        gc.clearRect(0, 0, canvas.getWidth(), canvas.getHeight());
    }
}

