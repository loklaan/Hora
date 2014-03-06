package hora;

import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

public class AlertWindow extends Window implements Runnable {

    private static final Color HUE = Color.decode("#FBA16C");

    private static final int FLASHES = 2;

    private static final int FPS = 30;

    private static final int ANIM_TIME = 300;

    private static final int PEAK_TIME = ANIM_TIME / 2;

    private static final float BRIGHTNESS = 0.2f;

    public AlertWindow() {
        super(null);
        addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ESCAPE)
                    System.exit(0);
            }
        });
        setAlwaysOnTop(true);
        GraphicsEnvironment environment = GraphicsEnvironment.getLocalGraphicsEnvironment();
        GraphicsDevice device = environment.getDefaultScreenDevice();
        DisplayMode display = device.getDisplayMode();
        setBounds(0, 0, display.getWidth(), display.getHeight());
    }

    @Override
    public void run() {
        setBackground(new Color(HUE.getRed(), HUE.getGreen(), HUE.getBlue(), 0));
        setVisible(true);
        for (int i = 0; i < FLASHES; i++) {
            long elapsed, start = System.currentTimeMillis();
            while ((elapsed = System.currentTimeMillis() - start) < ANIM_TIME) {
                long begin = System.currentTimeMillis();
                float opacity = (float) (elapsed % PEAK_TIME) / PEAK_TIME;
                if (elapsed > PEAK_TIME)
                    opacity = 1 - opacity;
                opacity *= BRIGHTNESS;
                setBackground(new Color(HUE.getRed(), HUE.getGreen(), HUE.getBlue(), (int) (opacity * 255)));
                long sleepTime = 1000 / FPS - (System.currentTimeMillis() - begin);
                try {
                    if (sleepTime > 0)
                        Thread.sleep(sleepTime);
                } catch (InterruptedException exception) {
                    exception.printStackTrace();
                }
            }
            setBackground(new Color(HUE.getRed(), HUE.getGreen(), HUE.getBlue(), 0));
        }
        setVisible(false);
        dispose();
    }

}
