package Visualization;

import javax.swing.*;

public class Button extends JButton
{
    Visualization vis;
    Button(Visualization vis)
    {
        super();
        this.vis = vis;
        setVisible(true);
        setSize(150, 25);
        setLocation(10, 650);
        addActionListener(vis);
        setText("PAUSE/RESUME");
    }


}
