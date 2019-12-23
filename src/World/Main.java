package World;

import java.io.FileNotFoundException;

public class Main
{
    public static final int directionCount = 8;

    public static void main(String[] args)
    {
        try
        {
            PropertiesLoader prop = PropertiesLoader.loadPropFromFile();
            WorldMap map = new WorldMap(prop.getWidth(), prop.getHeight(), prop.getStartEnergy(), prop.getMoveEnergy(), prop.getPlantEnergy(), prop.getJungleRatio());
            Simulation simulation = new Simulation(map);
            simulation.simulate();
        }
        catch (FileNotFoundException | IllegalArgumentException e)
        {
            e.printStackTrace();
        }
    }
}
