package ru.tehkode.mualauncher.gui;

import java.awt.Color;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.SwingConstants;
import ru.tehkode.mualauncher.widgets.ProgressBar;

/**
 *
 * @author t3hk0d3
 */
public class DownaloadWindow extends JFrame {

    JLabel progress;
    private long maximum = 0;
    private long current = 0;
    private String maximumSize;
    private ProgressBar progressBar = new ProgressBar();

    public DownaloadWindow() {
        super("Downloading...");

        this.setUndecorated(true);

        this.setBackground(new Color(0, 0, 0, 0));

        this.setSize(500, 80);

        this.setContentPane(progressBar);

        this.initComponents();

        this.setLocationRelativeTo(null);
    }

    private void initComponents() {
        progress = new JLabel("Downloading...", SwingConstants.CENTER);

        progress.setLocation(0, 50);
        progress.setSize(this.getWidth(), 50);
        progress.setForeground(Color.white);

        progress.setBounds(0, 0, this.getWidth(), this.getHeight());

        this.add(progress);
    }

    public void setTotal(long total) {
        this.maximum = total;
        this.maximumSize = formatSize(total);
        this.progressBar.setMaximum(maximum);
    }

    public void setDownloadProgress(long current) {
        this.current = current;

        float percent = 1.0f * current / maximum * 100.0f;

        progress.setText(String.format("Downloading... %.2f%% - %s/%s", percent, formatSize(current), this.maximumSize));

        this.progressBar.setProgress(current);

        this.repaint();
    }

    public void setUnpackingProgress(long current) {
        this.current = current;

        float percent = 1.0f * current / maximum * 100.0f;

        progress.setText(String.format("Unpacking... %.2f%% - %s/%s", percent, current, this.maximumSize));

        this.progressBar.setProgress(current);

        this.repaint();
    }

    public String formatSize(long size) {
        float currentSize = size;

        String[] sizes = new String[]{"B", "KB", "MB", "GB"};

        for (String label : sizes) {
            if (currentSize < 1024) {
                return String.format("%.2f %s", currentSize, label);
            }

            currentSize /= 1024;
        }

        return size + " bytes";
    }
}
