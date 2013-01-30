package ru.tehkode.mualauncher.widgets;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import javax.swing.JComponent;

/**
 *
 * @author t3hk0d3
 */
public class ProgressBar extends JComponent {
    
    private long progress = 0;
    
    private long maximum = 1;

    public ProgressBar() {
        
        this.setBackground(Color.GRAY);
        this.setForeground(Color.BLACK);
    }

    public long getProgress() {
        return progress;
    }

    public void setProgress(long progress) {
        this.progress = progress;
    }

    public long getMaximum() {
        return maximum;
    }

    public void setMaximum(long maximum) {
        this.maximum = maximum;
    }
    
    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g;
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        
        float value = ((float)progress/(float)maximum); // 0.0f - 1.0f
        
        g2.setColor(this.getBackground());        
        g2.fillRoundRect(0, 0, this.getWidth() - 1, this.getHeight() - 1, 15, 15);
        g2.clipRect(0, 0, (int)((this.getWidth()-1) * value), this.getHeight() - 1);
        
        g2.setColor(this.getForeground());
        g2.fillRoundRect(0, 0, this.getWidth() - 1, this.getHeight() - 1, 15, 15);     
        
        g2.setClip(null);
        
        super.paintComponent(g);
    }
}
