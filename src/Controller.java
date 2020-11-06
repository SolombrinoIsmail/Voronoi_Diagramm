import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;

import java.awt.*;
import java.net.URL;
import java.util.ArrayList;
import java.util.Random;
import java.util.ResourceBundle;

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

    //FXML Handle Actions

    public void HandleDrawButton(ActionEvent event) {
        drawBorder();
        drawPoints();
        setup();
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


    @Override
    public void run() {
        for (int h = 0; h < canvas.getWidth(); h++) {
            try {
                for (Point p : points) {
                    gc.strokeOval(p.getX() - (h / 2), p.getY() - (h / 2), h, h);
                }
                Thread.sleep(30);
                gc.clearRect(0, 0, canvas.getWidth(), canvas.getHeight());
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

        }
    }

    public void setup() {
        test.stop();
        test = new Thread(this);
        test.start();
        gc.clearRect(0, 0, canvas.getWidth(), canvas.getHeight());
    }
}

