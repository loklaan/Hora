package hora;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Calendar;
import java.util.HashMap;

public class Application {

    private static final String ASSETS = "res/";

    private static final long MILLIS_IN_SECOND = 1000;

    private static final long MILLIS_IN_MINUTE = 60 * MILLIS_IN_SECOND;

    private static final long MILLIS_IN_HOUR = 60 * MILLIS_IN_MINUTE;

    private HashMap<String, BufferedImage> assets = new HashMap<>();

    private ModeWindow mode;

    private boolean paused = false;

    public static void main(String[] args) {
        new Application();
    }

    public Application() {
        if (!SystemTray.isSupported()) {
            System.out.println("System tray is not supported");
            System.exit(0);
        }
        final SystemTray systemTray = SystemTray.getSystemTray();
        
        loadAsset("icon.png");
        loadAsset("back.png");
        loadAsset("slide.png");
        loadAsset("texts.png");
        mode = new ModeWindow(assets);
        while (mode.isOpen())
            Thread.yield();
        final TrayIcon trayIcon = new TrayIcon(assets.get("icon.png"), "Hora");
        trayIcon.setImageAutoSize(true);
        final PopupMenu popupMenu = new PopupMenu();
        final MenuItem pauseItem = new MenuItem("Pause");
        pauseItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                paused = !paused;
                if (pauseItem.getLabel().equals("Pause"))
                    pauseItem.setLabel("Resume");
                else
                    pauseItem.setLabel("Pause");
            }
        });
        popupMenu.add(pauseItem);
        popupMenu.addSeparator();
        final MenuItem exitItem = new MenuItem("Exit");
        exitItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                systemTray.remove(trayIcon);
                System.exit(0);
            }
        });
        popupMenu.add(exitItem);
        trayIcon.setPopupMenu(popupMenu);
        try {
            systemTray.add(trayIcon);
        } catch (AWTException exception) {
            exception.printStackTrace();
        }
        start();
    }

    public void loadAsset(String name) {
        BufferedImage image = null;
        try {
            image = ImageIO.read(new File(ASSETS + name));
        } catch (IOException ignored) {}
        if (image == null) {
            try {
                image = ImageIO.read(getClass().getResource("/" + ASSETS + name));
            } catch (IOException ignored) {}
        }
        if (image != null)
            assets.put(name, image);
    }

    private void start() {
        for (;;) {
            switch (mode.getMode()) {
                case ON_THE_HOUR:
                    try {
                        int secondsOffset = Calendar.getInstance().get(Calendar.SECOND);
                        if (secondsOffset != 0)
                            Thread.sleep(MILLIS_IN_SECOND * (60 - secondsOffset));
                        while (Calendar.getInstance().get(Calendar.MINUTE) != 0)
                            Thread.sleep(MILLIS_IN_MINUTE);
                        if (!paused)
                            new Thread(new AlertWindow()).start();
                        Thread.sleep(MILLIS_IN_MINUTE * 58);
                    } catch (InterruptedException exception) {
                        exception.printStackTrace();
                    }
                    break;
                case EVERY_HOUR:
                    try {
                        if (!paused)
                            new Thread(new AlertWindow()).start();
                        Thread.sleep(MILLIS_IN_HOUR);
                    } catch (InterruptedException exception) {
                        exception.printStackTrace();
                    }
                    break;
            }
        }
    }

}
