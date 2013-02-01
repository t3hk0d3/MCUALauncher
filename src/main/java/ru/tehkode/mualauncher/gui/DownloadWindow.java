package ru.tehkode.mualauncher.gui;

import java.awt.Color;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.SwingConstants;
import ru.tehkode.mualauncher.net.DownloadListener;
import ru.tehkode.mualauncher.utils.Logger;
import ru.tehkode.mualauncher.utils.Resources;
import ru.tehkode.mualauncher.widgets.ProgressBar;

import static ru.tehkode.mualauncher.utils.Resources.*;

/**
 *
 * @author t3hk0d3
 */
public class DownloadWindow extends JFrame implements DownloadListener {

    private final static String[] SIZES = new String[]{"B", "KB", "MB", "GB"};
    JLabel progress;
    private long maximum = 0;
    private String maximumSize;
    private ProgressBar progressBar = new ProgressBar();
    private long downloadStartedAt;

    public DownloadWindow(String title) {
        super(title);

        try {
            this.setIconImage(Resources.image("window_icon"));
        } catch (Throwable e) {
            Logger.warning("Failed to setup window icon");
        }

        this.setUndecorated(true);

        this.setBackground(new Color(0, 0, 0, 0));

        this.setSize(500, 80);

        this.setContentPane(progressBar);

        this.initComponents();

        this.setLocationRelativeTo(null);
    }

    private void initComponents() {
        progress = new JLabel(string("download_progress") + "...", SwingConstants.CENTER);

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

    public void setDownloadProgress(long current, int speed) {
        float percent = 1.0f * current / maximum * 100.0f;

        progress.setText(String.format("%.2f%% - %s/%s (%s/s)", percent, formatSize(current), this.maximumSize, formatSize(speed)));

        this.progressBar.setProgress(current);
    }

    public void setUnpackingProgress(long current) {
        float percent = 1.0f * current / maximum * 100.0f;

        this.setTitle(string("unpacking_progress"));

        progress.setText(String.format("%.2f%% - %d/%d", percent, current, this.maximum));

        this.progressBar.setProgress(current);
    }

    public String formatSize(long size) {
        float currentSize = size;

        for (String label : SIZES) {
            if (currentSize < 1024) {
                return String.format("%.2f %s", currentSize, label);
            }

            currentSize /= 1024;
        }

        return size + " bytes";
    }

    @Override
    public void onDownloadProgress(long readed, long total) {
        long now = System.currentTimeMillis();

        this.setDownloadProgress(readed, (int) (readed / (now - this.downloadStartedAt) * 1000));

        this.setVisible(true);
    }

    @Override
    public void onDownloadStarted(long total) {
        this.downloadStartedAt = System.currentTimeMillis();
        this.setTotal(total);
    }

    @Override
    public void onDownloadFinished(long readed) {
        this.setVisible(false);
        // do nothig, for now
    }
}
