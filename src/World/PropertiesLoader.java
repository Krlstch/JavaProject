package World;

import com.google.gson.Gson;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;

public class PropertiesLoader
{
    private int width;
    private int height;

    public int getWidth()
    {
        return width;
    }

    public int getHeight()
    {
        return height;
    }

    public int getStartEnergy()
    {
        return startEnergy;
    }

    public int getMoveEnergy()
    {
        return moveEnergy;
    }

    public int getPlantEnergy()
    {
        return plantEnergy;
    }

    public double getJungleRatio()
    {
        return jungleRatio;
    }

    private int startEnergy;
    private int moveEnergy;
    private int plantEnergy;
    private double jungleRatio;

    static public PropertiesLoader loadPropFromFile() throws FileNotFoundException,IllegalArgumentException
    {
        Gson gson = new Gson();
        File f = new File("");
        System.out.println(f.getAbsolutePath());
        PropertiesLoader properties =  (PropertiesLoader)gson.fromJson(new FileReader("src\\parameters.json"), PropertiesLoader.class);
        properties.check();
        return properties;
    }

    public void check() throws IllegalArgumentException
    {
        if(this.width <= 0)
        {
            throw new IllegalArgumentException("Invalid map width");
        }
        if(this.height <= 0)
        {
            throw new IllegalArgumentException("Invalid map height");
        }
        if(this.startEnergy < 0)
        {
            throw new IllegalArgumentException("Invalid startEnergy");
        }
        if(this.moveEnergy< 0)
        {
            throw new IllegalArgumentException("Invalid moveEnergy");
        }
        if(this.plantEnergy < 0)
        {
            throw new IllegalArgumentException("Invalid plantEnergy");
        }
        if (0 > this.jungleRatio || this.jungleRatio > 1)
        {
            throw new IllegalArgumentException("Invalid jungleRatio");
        }

    }
}
