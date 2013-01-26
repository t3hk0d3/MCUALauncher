package ru.tehkode.mualauncher.widgets;

import java.awt.Component;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;

/**
 *
 * @author t3hk0d3
 */
public class MouseDragger implements MouseListener, MouseMotionListener {
    
    private final Component component;
    
    private int componentX;
    private int componentY;

    public MouseDragger(Component component) {
        this.component = component;
        
        component.addMouseListener(this);
        component.addMouseMotionListener(this);
    }

    public void mouseClicked(MouseEvent e) {
        
    }

    public void mousePressed(MouseEvent e) {
        // remember relative position
        this.componentX = e.getX();
        this.componentY = e.getY();
    }

    public void mouseReleased(MouseEvent e) {
        
    }

    public void mouseEntered(MouseEvent e) {
        
    }

    public void mouseExited(MouseEvent e) {
        
    }

    public void mouseDragged(MouseEvent e) {
        this.component.setLocation(e.getXOnScreen() - this.componentX, e.getYOnScreen() - this.componentY);
    }

    public void mouseMoved(MouseEvent e) {
        
    }
 
}
