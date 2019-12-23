package World;

import Visualization.Visualization;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Random;

public class Simulation implements ActionListener
{
    private int delay;
    private WorldMap map;
    private Visualization vis;
    public Timer timer;
    Simulation(WorldMap map)
    {
        delay = 10;
        timer = new Timer(delay, this);
        Random random = new Random();
        this.map = map;
        vis = new Visualization(this);

        map.place(new Animal(new Position(map.getJungleLeft() + random.nextInt(map.getJungleRight() - map.getJungleLeft() + 1),
                                          map.getJungleLeft() + random.nextInt(map.getJungleRight() - map.getJungleLeft() + 1)),
                            map.getStartEnergy()));
        map.place(new Animal(new Position(map.getJungleLeft() + random.nextInt(map.getJungleRight() - map.getJungleLeft() + 1),
                                          map.getJungleLeft() + random.nextInt(map.getJungleRight() - map.getJungleLeft() + 1)),
                             map.getStartEnergy()));
        for(int i = 0; i < map.getUpperRight().x * map.getUpperRight().y / 4; i++)
            map.newGrass();
    }

    public void simulate()
    {
        timer.start();
    }

    public WorldMap getMap()
    {
        return map;
    }

    @Override
    public void actionPerformed(ActionEvent e)
    {
        vis.visualize();
        map.simulateDay();
    }

    public void changeDelay(int newDelay)
    {
        delay = newDelay;
        timer.setDelay(newDelay);
    }

    public boolean pause()
    {
        if(timer.isRunning())
        {
            timer.stop();
            return false;
        }
        timer.start();
        return true;
    }
}
