package World;

import java.awt.*;
import java.util.Random;

public class Animal extends Element
{
    private static final Random random = new Random();
    private Direction direction;
    private int energy;
    private Genes genes;
    private WorldMap worldMap;
    private int numOfChildren = 0;
    private int days = 0;

    Animal(Position position, int energy)
    {
        this.energy = energy;
        this.position = new Position(position);
        this.direction = new Direction(random.nextInt(Main.directionCount));
        genes = new Genes();
    }

    Animal(Position position, Direction direction, int energy)
    {
        this.energy = energy;
        this.position = new Position(position);
        this.direction = direction;
        genes = new Genes();
    }

    Animal(Animal parent1, Animal parent2, Position position)
    {
        energy = parent1.getEnergy() / 4 + parent2.getEnergy() / 4;
        this.position = new Position(position);
        direction = new Direction(random.nextInt(Main.directionCount));
        genes = new Genes(parent1.getGenes().getGenes(), parent2.getGenes().getGenes());
        setMap(parent1.getWorldMap());
    }

    public WorldMap getWorldMap()
    {
        return worldMap;
    }

    public Genes getGenes()
    {
        return genes;
    }

    @Override
    public String toString()
    {
        return "A:" + this.position.toString() + " " + energy;
    }

    public void rotate()
    {
        direction.rotate(genes.direction());
    }

    public void move()
    {
        Position oldPosition = new Position(position);
        position = position.add(direction.toPosition(), worldMap.getUpperRight());
        worldMap.elementMoved(this, oldPosition);
        worldMap.energyChanged(Math.min(-worldMap.getMoveEnergy(), this.energy));
        energy -= worldMap.getMoveEnergy();
        days += 1;
    }

    public void setMap(WorldMap map)
    {
        this.worldMap = map;
    }

    public int getEnergy()
    {
        return energy;
    }

    public void feed(int energy)
    {
        worldMap.energyChanged(Math.min(this.energy + energy, worldMap.getStartEnergy()) - this.energy);
        this.energy = Math.min(this.energy + energy, worldMap.getStartEnergy());
    }

    public void addChild()
    {
        numOfChildren++;
    }

    public int getNumOfChildren()
    {
        return numOfChildren;
    }

    public int getDays()
    {
        return days;
    }

    public Color getColor()
    {
        if(energy > 0.66 * worldMap.getStartEnergy())
            return new Color(0, 0, 120);
        else if (energy > 0.33 * worldMap.getStartEnergy())
            return new Color(0, 0, 255);
        else if (energy > 0.10 * worldMap.getStartEnergy())
            return new Color(120, 81, 92);
        return new Color(255, 0, 0);
    }
}
