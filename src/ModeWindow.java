package hora;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.image.BufferedImage;
import java.util.HashMap;

public class ModeWindow extends Frame {

    private static final int FPS = 30;

    private static final int ANIM_TIME = 250;

    private Mode mode = Mode.ON_THE_HOUR;

    private HashMap<String, BufferedImage> assets;

    private Canvas canvas;

    private boolean open = true;

    public ModeWindow(HashMap<String, BufferedImage> assets) {
        this.assets = assets;
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                setVisible(false);
                dispose();
                open = false;
            }
        });
        setType(Type.UTILITY);
        setResizable(false);
        setTitle("Choose mode and close window");
        canvas = new Canvas();
        canvas.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                animate();
                swapMode();
                render(mode == Mode.ON_THE_HOUR ? 0 : slideDifference());
            }
        });
        canvas.setIgnoreRepaint(true);
        BufferedImage reference = assets.get("back.png");
        Dimension backSize = new Dimension(reference.getWidth(), reference.getHeight());
        canvas.setSize(backSize);
        add(canvas);
        pack();
        setLocationRelativeTo(null);
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                setVisible(true);
                canvas.createBufferStrategy(2);
                render(0);
            }
        });
    }

    private void render(int slide) {
        Graphics graphics = canvas.getBufferStrategy().getDrawGraphics();
        graphics.setColor(Color.BLACK);
        graphics.fillRect(0, 0, canvas.getWidth(), canvas.getHeight());
        graphics.drawImage(assets.get("back.png"), 0, 0, canvas);
        graphics.drawImage(assets.get("slide.png"), slide, 0, canvas);
        graphics.drawImage(assets.get("texts.png"), 0, 0, canvas);
        graphics.dispose();
        canvas.getBufferStrategy().show();
    }

    private int slideDifference() {
        return assets.get("back.png").getWidth() - assets.get("slide.png").getWidth();
    }

    private void animate() {
        Thread animator = new Thread(new Runnable() {
            @Override
            public void run() {
                long elapsed, start = System.currentTimeMillis();
                while ((elapsed = System.currentTimeMillis() - start) < ANIM_TIME) {
                    long begin = System.currentTimeMillis();
                    float progress = (float) elapsed / ANIM_TIME;
                    int slide = 0;
                    switch (mode) {
                        case ON_THE_HOUR:
                            slide = (int) (progress * slideDifference());
                            break;
                        case EVERY_HOUR:
                            slide = (int) ((1 - progress) * slideDifference());
                            break;
                    }
                    render(slide);
                    long sleepTime = 1000 / FPS - (System.currentTimeMillis() - begin);
                    try {
                        if (sleepTime > 0)
                            Thread.sleep(sleepTime);
                    } catch (InterruptedException exception) {
                        exception.printStackTrace();
                    }
                }
            }
        });
        animator.start();
        try {
            animator.join();
        } catch (InterruptedException exception) {
            exception.printStackTrace();
        }
    }

    private void swapMode() {
        switch (mode) {
            case ON_THE_HOUR:
                mode = Mode.EVERY_HOUR;
                break;
            case EVERY_HOUR:
                mode = Mode.ON_THE_HOUR;
                break;
        }
    }

    public Mode getMode() {
        return mode;
    }

    public boolean isOpen() {
        return open;
    }

}
