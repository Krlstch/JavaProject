package Visualization;

import World.Simulation;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class Visualization implements ActionListener {
    private JFrame frame;
    private Simulation simulation;
    private Panel panel;
    private PlotPanel plotPanel;
    private boolean running = true;
    public static final int width = 1400;
    public static final int height = 738;

    public Visualization(Simulation simulation)
    {
        this.simulation = simulation;
        this.panel = new Panel(this, simulation.getMap());
        frame = new Frame(this);
        frame.setSize(new Dimension(width, height));
        frame.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
        frame.setLocationRelativeTo(null);

        panel = new Panel(this, simulation.getMap());
        panel.setSize(new Dimension(1, 1));

        plotPanel = new PlotPanel(this, simulation.getMap());
        plotPanel.setSize(new Dimension(1, 1));

        JButton stop = new Button(this);

        frame.add(panel);
        frame.add(plotPanel);
        plotPanel.add(stop);
        frame.setVisible(true);
    }

    public void visualize()
    {
        panel.repaint();
        plotPanel.repaint();
    }

    public JFrame getFrame()
    {
        return frame;
    }

    public Simulation getSimulation() {
        return simulation;
    }

    @Override
    public void actionPerformed(ActionEvent e)
    {
        running = simulation.pause();
        panel.setRunning(running);
    }
}
