package components;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

@SuppressWarnings("serial")
public class Field extends JPanel {

    private boolean paused;
    private ArrayList<BouncingBall> balls = new ArrayList<>(10);
    private ArrayList<Brick> bricks = new ArrayList<>();
    private Timer repaintTimer = new Timer(10, new ActionListener() {
        public void actionPerformed(ActionEvent ev) {
            // перерисовка окна
            repaint();
        }
    });

    public Field() {
        setBackground(Color.WHITE);
        repaintTimer.start();
    }

    // Унаследованный от JPanel метод перерисовки компонента
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D canvas = (Graphics2D) g;
        for (BouncingBall ball: balls) {
            ball.paint(canvas);
        }
        Graphics2D canvas2 = (Graphics2D) g;
        for(Brick brick : bricks)
        {
            brick.paint(canvas2);
        }
    }

    public void addBall() {
        balls.add(new BouncingBall(this));
    }

    public void addBrick()
    {
        bricks.add(new Brick(this));
    }

    public void deleteBrick(Brick brick)
    {
        bricks.removeIf(item -> item.equals(brick));
    }

    public ArrayList<Brick> getBrick()
    {
        return bricks;
    }

    public synchronized void pause() {
        paused = true;
    }

    public synchronized void resume() {
        paused = false;
        notifyAll();
    }

    public synchronized void canMove(BouncingBall ball) throws InterruptedException {
        if (paused) {
            wait();
        }
    }
}