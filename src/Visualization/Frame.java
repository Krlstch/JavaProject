package Visualization;

import javax.swing.*;

public class Frame extends JFrame
{
    private Visualization vis;

    public Frame(Visualization vis)
    {
        super("World");
        this.vis = vis;
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setVisible(true);

    }
}
