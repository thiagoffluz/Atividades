import java.awt.*;
import java.awt.event.*;
import java.awt.geom.AffineTransform;
import java.awt.geom.GeneralPath;
import javax.swing.Timer;

/**
 * An example for the use of a GeneralPath to draw a car.
 *
 * @author Frank Klawonn
 * Last change 07.01.2005
 */
public class GeneralPathCar extends Frame implements KeyListener {
    // Constructor
    GeneralPathCar() {
        // Enables the closing of the window.
        addWindowListener(new MyFinishWindow());
        // Add KeyListener to handle key events
        addKeyListener(this);
        setFocusable(true);
        // Start the plane movement
        startPlaneMovement();
    }

    // Coordinates of the boat
    private int boatX = 150;
    private int boatY = 650;

    // Coordinates of the plane
    private int planeX = 190;
    private int planeY = 80;
    private int planeDirection = 1; // 1 for right, -1 for left

    // Methods to move the boat
    private void moveLeft() {
        boatX -= 10;
        repaint();
    }

    private void moveRight() {
        boatX += 10;
        repaint();
    }

    // Methods to move the plane
    private void movePlane() {
        // Move the plane horizontally
        planeX += planeDirection * 10;
        // Change direction when reaching the specified quadrants
        if (planeX >= 800 || planeX <= 100) {
            planeDirection *= -1;
        }
        repaint();
    }

    // Start the movement of the plane
    private void startPlaneMovement() {
        // Create a timer to move the plane every 100 milliseconds
        Timer timer = new Timer(100, new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                movePlane();
            }
        });
        timer.start();
    }

    // Handle key events
    public void keyPressed(KeyEvent e) {
        int key = e.getKeyCode();
        if (key == KeyEvent.VK_LEFT) {
            moveLeft();
        } else if (key == KeyEvent.VK_RIGHT) {
            moveRight();
        }
    }

    public void keyReleased(KeyEvent e) {
    }

    public void keyTyped(KeyEvent e) {
    }

    public void paint(Graphics g) {
        Graphics2D g2d = (Graphics2D) g;

        // Use antialiasing to have nicer lines.
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        // The lines should have a thickness of 3.0 instead of 1.0.
        BasicStroke bs = new BasicStroke(3.0f);
        g2d.setStroke(bs);

        // The GeneralPath to describe the car.
        GeneralPath gp = new GeneralPath();
        GeneralPath gp3 = new GeneralPath();

        
          //BARCO
        gp.moveTo(boatX, boatY);
        gp.lineTo(boatX + 15, boatY + 60);
        gp.lineTo(boatX + 300, boatY + 60);
        gp.quadTo(boatX + 330, boatY + 60, boatX + 350, boatY);
        gp.quadTo(boatX + 170, boatY + 10, boatX, boatY);
        gp.moveTo(boatX + 240, boatY + 3);
        gp.lineTo(boatX + 230, boatY - 30);
        gp.lineTo(boatX + 220, boatY - 40);
        gp.lineTo(boatX + 70, boatY - 40);
        gp.quadTo(boatX + 40, boatY - 40, boatX + 40, boatY + 2);
        gp.moveTo(boatX + 160, boatY - 40);
        gp.lineTo(boatX + 150, boatY - 110);
        gp.lineTo(boatX + 120, boatY - 110);
        gp.lineTo(boatX + 120, boatY - 40);

        //AVIAO
        gp3.moveTo(planeX, planeY);
        gp3.lineTo(planeX + 30, planeY + 70);
        gp3.lineTo(planeX + 35, planeY + 100);
        gp3.quadTo(planeX - 40, planeY + 100, planeX - 80, planeY + 110);
        gp3.lineTo(planeX - 110, planeY + 80);
        gp3.lineTo(planeX - 120, planeY + 80);
        gp3.lineTo(planeX - 105, planeY + 120);
        gp3.lineTo(planeX - 120, planeY + 160);
        gp3.lineTo(planeX - 110, planeY + 160);
        gp3.lineTo(planeX - 80, planeY + 130);
        gp3.quadTo(planeX - 5, planeY + 140, planeX + 35, planeY + 140);
        gp3.lineTo(planeX + 30, planeY + 170);
        gp3.lineTo(planeX, planeY + 230);
        gp3.lineTo(planeX + 15, planeY + 230);
        gp3.lineTo(planeX + 80, planeY + 130);
        gp3.quadTo(planeX + 250, planeY + 120, planeX + 80, planeY + 100);
        gp3.lineTo(planeX + 15, planeY);
        gp3.lineTo(planeX, planeY);


 
        // If the plane is moving to the left, flip it horizontally
        if (planeDirection == -1) {
            AffineTransform flip = AffineTransform.getScaleInstance(-1, 1);
            flip.translate(-2 * planeX - 100, 0); // Shift to maintain position
            gp3.transform(flip);
        }

        // gp.transform(rotation);
       
        g2d.draw(gp);
        g2d.draw(gp3);
      
        
        g2d.setStroke(new BasicStroke(1.0f));
        // Draw a coordinate system.
        drawSimpleCoordinateSystem(1000, 1000, g2d);

    }

    /**
     * Draws a coordinate system (according to the window coordinates).
     *
     * @param xmax x-coordinate to which the x-axis should extend.
     * @param ymax y-coordinate to which the y-axis should extend.
     * @param g2d  Graphics2D object for drawing.
     */
    public static void drawSimpleCoordinateSystem(int xmax, int ymax,
                                                  Graphics2D g2d) {
        int xOffset = 30;
        int yOffset = 50;
        int step = 20;
        String s;
        // Remember the actual font.
        Font fo = g2d.getFont();
        // Use a small font.
        g2d.setFont(new Font("sansserif", Font.PLAIN, 9));
        // x-axis.
        g2d.drawLine(xOffset, yOffset, xmax, yOffset);
        // Marks and labels for the x-axis.
        for (int i = xOffset + step; i <= xmax; i = i + step) {
            g2d.drawLine(i, yOffset - 2, i, yOffset + 2);
            g2d.drawString(String.valueOf(i), i - 7, yOffset - 7);
        }

        // y-axis.
        g2d.drawLine(xOffset, yOffset, xOffset, ymax);

        // Marks and labels for the y-axis.
        s = "  "; // for indention of numbers < 100
        for (int i = yOffset + step; i <= ymax; i = i + step) {
            g2d.drawLine(xOffset - 2, i, xOffset + 2, i);
            if (i > 99) {
                s = "";
            }
            g2d.drawString(s + String.valueOf(i), xOffset - 25, i + 5);
        }

        // Reset to the original font.
        g2d.setFont(fo);
    }

    public static void main(String[] argv) {
        GeneralPathCar f = new GeneralPathCar();
        f.setTitle("GeneralPath example");
        f.setSize(1000, 1000);
        f.setVisible(true);
    }
}