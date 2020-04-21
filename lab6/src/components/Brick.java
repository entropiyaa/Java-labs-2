package components;

import java.awt.*;
import java.awt.geom.Rectangle2D;

public class Brick {

    private double sizeX;
    private double sizeY;
    private Integer countOfStrokes;
    private Field field;
    private Color color;
    private double x;
    private double y;
    private static boolean[][] m = new boolean[11][14];
    private int deltX;
    private int deltY;

    public Brick(Field field)
    {
        this.field = field;
        sizeX = field.getSize().getWidth() * 0.09;
        sizeY = sizeX / 2;
        countOfStrokes = (int)(Math.random() * 2) + 1;
        color = new Color((float)Math.random(), (float)Math.random(), (float)Math.random());
        searchPlace();
    }

    private void searchPlace()
    {
        deltX = (int)(Math.random()*11);
        deltY = (int)(Math.random()*14);
        if(!m[deltX][deltY])
        {
            x = sizeX * deltX;
            y = sizeY * deltY;
            m[deltX][deltY] = true;
        } else {
            searchPlace();
        }
    }

    public void freeSpace()
    {
        m[deltX][deltY] = false;
    }

    public void paint(Graphics2D canvas) {
        canvas.setColor(color);
        canvas.setPaint(color);
        Rectangle2D.Double brick = new Rectangle2D.Double(x, y, sizeX, sizeY);
        canvas.draw(brick);
        canvas.fill(brick);
        canvas.drawString(countOfStrokes.toString(),(int)x,(int)y);
    }

    public double getSizeX() {
        return sizeX;
    }

    public double getSizeY() {
        return sizeY;
    }

    public int getCountOfStrokes() {
        return countOfStrokes;
    }

    public void changeCountOfStrokes()
    {
        countOfStrokes--;
    }

    public Field getField() {
        return field;
    }

    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }
}