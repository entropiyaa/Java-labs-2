package components;

import java.awt.*;
import java.awt.geom.Ellipse2D;
import java.util.Iterator;

public class BouncingBall implements Runnable {

    private static final int MAX_RADIUS = 40;
    private static final int MIN_RADIUS = 3;
    private static final int MAX_SPEED = 15;
    private Field field;
    private int radius;
    private Color color;
    private double x;
    private double y;
    private int speed;
    private double speedX;
    private double speedY;

    public BouncingBall(Field field) {
        this.field = field;
        radius = new Double(Math.random()*(MAX_RADIUS - MIN_RADIUS)).intValue() + MIN_RADIUS;
        speed = new Double(Math.round(5*MAX_SPEED / radius)).intValue();
        if (speed > MAX_SPEED) {
            speed = MAX_SPEED;
        }
        double angle = Math.random() * 2 * Math.PI;
        speedX = 3 * Math.cos(angle);
        speedY = 3 * Math.sin(angle);
        color = new Color((float)Math.random(), (float)Math.random(), (float)Math.random());
        x = Math.random()*(field.getSize().getWidth()-2*radius) + radius;
        y = Math.random()*(field.getSize().getHeight()-2*radius) + radius;
        // Создаѐм новый экземпляр потока, передавая аргументом
        // ссылку на класс, реализующий Runnable (т.е. на себя)
        Thread thisThread = new Thread(this);
        thisThread.start();
    }

    public void run() {
        try {
            while(true) {

                field.canMove(this);

                if(!field.getBrick().isEmpty()) {
                    for(Brick brick : field.getBrick())
                    {
                        if((x+speedX >= brick.getX()) && (x+speedX <= brick.getSizeX()+brick.getX()) &&
                                (y+speedY <= brick.getY()))
                        {
                            if(y + speedY >= brick.getY()-radius) {
                                // Достигли верхней плоскости прямоугольника, отскакиваем вверх
                                speedY = -speedY;
                                y = brick.getY()-radius;
                                if(brick.getCountOfStrokes() > 0)
                                {
                                    brick.changeCountOfStrokes();
                                }
                            }
                        }
                        if((y + speedY >= brick.getY()) && (y+ speedY <= brick.getSizeY()+brick.getY()) &&
                                (x+speedX <= brick.getX()))
                        {
                            if((x + speedX >= brick.getX()-radius)){
                                // Достигли правой плоскости прямоугольника, отскакиваем влево
                                speedX = -speedX;
                                x = brick.getX()-radius;
                                if(brick.getCountOfStrokes() > 0)
                                {
                                    brick.changeCountOfStrokes();
                                }
                            }
                        }
                        if((x+speedX >= brick.getX()) && (x+speedX <= brick.getSizeX()+brick.getX()) &&
                                (y+speedY >= brick.getY()+brick.getSizeY()))
                        {
                            if((y + speedY<= brick.getY()+brick.getSizeY()+radius)){
                                // Достигли нижней плоскости прямоугольника, отскакиваем вниз
                                speedY = -speedY;
                                y = brick.getY()+brick.getSizeY()+radius;
                                if(brick.getCountOfStrokes() > 0)
                                {
                                    brick.changeCountOfStrokes();
                                }
                            }
                        }
                        if((y + speedY >= brick.getY()) && (y + speedY <= brick.getSizeY() + brick.getY()) &&
                                x + speedX >= brick.getX()+brick.getSizeX())
                        {
                            if((x + speedX<= brick.getX()+brick.getSizeX()+radius)){
                                // Достигли левой плоскости прямоугольника, отскакиваем вправо
                                speedX = -speedX;
                                x = brick.getX()+brick.getSizeX() + radius;
                                if(brick.getCountOfStrokes() > 0)
                                {
                                    brick.changeCountOfStrokes();
                                }
                            }
                        }
                    }
                }

                Iterator<Brick> iterator = field.getBrick().iterator();
                while(iterator.hasNext()) {
                    Brick item = iterator.next();
                    if (item != null) {
                        if (item.getCountOfStrokes() == 0) {
                            item.freeSpace();
                            iterator.remove();
                        }
                    }
                }

                if (x + speedX <= radius) {
                    // Достигли левой стенки, отскакиваем право
                    speedX = -speedX;
                    x = radius;
                } else
                if (x + speedX >= field.getWidth() - radius) {
                    // Достигли правой стенки, отскакиваем влево
                    speedX = -speedX;
                    x = new Double(field.getWidth()-radius).intValue();
                } else
                if (y + speedY <= radius) {
                    // Достигли верхней стенки
                    speedY = -speedY;
                    y = radius;
                } else
                if (y + speedY >= field.getHeight() - radius) {
                    // Достигли нижней стенки
                    speedY = -speedY;
                    y = new Double(field.getHeight()-radius).intValue();
                } else {
                    // Просто смещаемся
                    x += speedX;
                    y += speedY;
                }

                Thread.sleep(16-speed);
            }
        } catch (InterruptedException ex) {
            // Если нас прервали, то ничего не делаем
            // и просто выходим (завершаемся)
        }
    }

    public void paint(Graphics2D canvas) {
        canvas.setColor(color);
        canvas.setPaint(color);
        Ellipse2D.Double ball = new Ellipse2D.Double(x-radius, y-radius, 2*radius, 2*radius);
        canvas.draw(ball);
        canvas.fill(ball);
    }
}