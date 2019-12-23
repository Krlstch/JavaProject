package Visualization;

import World.Animal;

import javax.swing.*;

public class AnimalPanel extends JPanel
{
    public AnimalPanel(int tileSize, Animal animal)
    {
        setSize(tileSize, tileSize);
        setLocation(animal.getPosition().x * tileSize, animal.getPosition().y * tileSize);
        setToolTipText(animal.getGenes().toString());
    }

}
