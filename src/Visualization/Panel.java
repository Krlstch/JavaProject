package Visualization;

import World.Animal;
import World.Grass;
import World.WorldMap;

import javax.swing.*;
import java.awt.*;
import java.util.LinkedList;

public class Panel extends JPanel
{
    private WorldMap map;
    private Visualization vis;
    private LinkedList<JPanel> animals = new LinkedList<>();
    private boolean running;


    public Panel(Visualization vis, WorldMap map)
    {
        this.vis = vis;
        this.map = map;
    }

    public void setRunning(boolean running)
    {
        this.running = running;
        if(!running)
        {
            for(Animal animal : map.getAnimals())
            {
                int tileSize = Math.min(this.getWidth() / map.getUpperRight().x, this.getHeight() / map.getUpperRight().y);
                AnimalPanel animalBox = new AnimalPanel(tileSize, animal);
                this.add(animalBox);
                animals.add(animalBox);
            }
        }
        else
        {
            for(JPanel animal : animals)
            {
                this.remove(animal);
            }
            animals.clear();
        }
    }


    @Override
    protected void paintComponent(Graphics g)
    {
        super.paintComponent(g);
        this.setSize((int) (Visualization.width * 0.5), Visualization.height - 38);
        this.setLocation((int) (Visualization.width * 0.5), 0);

        int tileSize = Math.min(this.getWidth() / map.getUpperRight().x, this.getHeight() / map.getUpperRight().y);

        g.setColor(new Color(253, 239, 71));
        g.fillRect(0, 0, (map.getUpperRight().x  + 1) * tileSize, (map.getUpperRight().y + 1) * tileSize);

        g.setColor(new Color(102, 253, 89));
        g.fillRect(map.getJungleLeft() * tileSize,
                map.getJungleDown() * tileSize,
                (map.getJungleRight() - map.getJungleLeft() + 1)  * tileSize,
                (map.getJungleUp() - map.getJungleDown() + 1) * tileSize);

        g.setColor(new Color(0, 120, 0));
        for (Grass grass : map.getGrasses())
        {
            int x = grass.getPosition().x * tileSize;
            int y = grass.getPosition().y * tileSize;
            g.fillRect(x, y, tileSize, tileSize);
        }


        for (Animal a : map.getAnimals())
        {
            g.setColor(a.getColor());
            int x = a.getPosition().x * tileSize;
            int y = a.getPosition().y * tileSize;
            g.fillRect(x, y, tileSize, tileSize);
        }
    }


}
