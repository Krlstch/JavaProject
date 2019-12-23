package Visualization;

import World.WorldMap;

import javax.swing.*;
import java.awt.*;
import java.util.*;

import static java.lang.Math.*;

public class PlotPanel extends JPanel
{
    private WorldMap map;
    private int day;
    Visualization visualization;
    private LinkedList<Integer> animalPopulation = new LinkedList<>();
    private LinkedList<Integer> grassPopulation = new LinkedList<>();
    private LinkedList<Integer> avgAnimalEnergy = new LinkedList<>();
    private LinkedList<Integer> genes = new LinkedList<>();
    private LinkedList<Integer> avgLifeSpan = new LinkedList<>();
    private LinkedList<Double> avgChildCount = new LinkedList<>();

    public PlotPanel(Visualization visualization, WorldMap map)
    {
        setLayout(null);
        this.visualization = visualization;
        this.map = map;
        day = 0;
    }

    @Override
    protected void paintComponent(Graphics g)
    {
        super.paintComponent(g);
        this.setSize((int) (Visualization.width * 0.5), Visualization.height - 38);
        this.setLocation(0, 0);
        int width = this.getWidth();
        int height = this.getHeight();
        day++;
        g.drawString("Day: " + day, 10,  20);
        if(day > 500)
        {
            animalPopulation.removeFirst();
            grassPopulation.removeFirst();
            avgAnimalEnergy.removeFirst();
            genes.removeFirst();
            avgLifeSpan.removeFirst();
            avgChildCount.removeFirst();

        }
        animalPopulation.add(map.getAnimals().size());
        grassPopulation.add(map.getGrasses().size());
        avgAnimalEnergy.add(map.getAvgAnimalEnergy());
        Vector<Integer> vecOfGenes = map.getAnimalsGenes();
        genes.add(vecOfGenes.indexOf(Collections.max(vecOfGenes)));
        avgLifeSpan.add(map.getAverageLifeSpan());
        avgChildCount.add((double) map.getAverageNumOfChildren());

        Plot p1 = new Plot(Visualization.width/2 - 100, Visualization.height / 7 - 10, 90, 120);
        Plot p2 = new Plot(Visualization.width/2 - 100, Visualization.height / 7 - 10, 90, 220);
        Plot p3 = new Plot(Visualization.width/2 - 100, Visualization.height / 7 - 10, 90, 320);
        Plot p4 = new Plot(Visualization.width/2 - 100, Visualization.height / 7 - 10, 90, 420);
        Plot p5 = new Plot(Visualization.width/2 - 100, Visualization.height / 7 - 10, 90, 520);
        Plot p6 = new Plot(Visualization.width/2 - 100, Visualization.height / 7 - 10, 90, 620);
        Plot[] plots = new Plot[]{p1, p2, p3, p4, p5, p6};
        g.setColor(new Color(0, 0, 0));
        for(Plot p : plots)
        {
            drawHorizontalArrow(g, p.x, p.y, p.x + p.width, p.y, day);
        }


        int maxAnimalPopulation = Collections.max(animalPopulation);
        int maxGrassPopulation = Collections.max(grassPopulation);
        int maxEnergy = Collections.max(avgAnimalEnergy);
        int maxLifeSpan = Collections.max(avgLifeSpan);
        double maxChildCount = Collections.max(avgChildCount);

        drawVerticalArrow(g, p1.x, p1.y, p1.x, p1.y - p1.height, maxAnimalPopulation);
        drawVerticalArrow(g, p2.x, p2.y, p2.x, p2.y - p2.height, maxGrassPopulation);
        drawVerticalArrow(g, p3.x, p3.y, p3.x, p3.y - p3.height, maxAnimalPopulation);
        drawVerticalArrow(g, p4.x, p4.y, p4.x, p4.y - p4.height, 8);
        drawVerticalArrow(g, p5.x, p5.y, p5.x, p5.y - p5.height, maxEnergy);
        drawVerticalArrow(g, p6.x, p6.y, p6.x, p6.y - p6.height, maxChildCount);

        g.setColor(new Color(255, 67, 41));

        ArrayList<Integer> animalPopulationAvg = new ArrayList<>();
        ArrayList<Integer> grassPopulationAvg = new ArrayList<>();
        ArrayList<Integer> energyAvg = new ArrayList<>();
        ArrayList<Integer> lifeSpanAvg = new ArrayList<>();
        ArrayList<Double> childCountAvg = new ArrayList<>();



        for (int i = 0; i < animalPopulation.size(); i++)
        {
            if(maxAnimalPopulation == 0)
                animalPopulationAvg.add(0);
            else
                animalPopulationAvg.add(p1.height * animalPopulation.get(i) / maxAnimalPopulation);

            if(maxGrassPopulation == 0)
                grassPopulationAvg.add(0);
            else
                grassPopulationAvg.add(p2.height * grassPopulation.get(i) / maxGrassPopulation);

            if(maxEnergy == 0)
                energyAvg.add(0);
            else
                energyAvg.add(p4.height * avgAnimalEnergy.get(i) / maxEnergy);

            if(maxLifeSpan == 0)
                lifeSpanAvg.add(0);
            else
                lifeSpanAvg.add(p5.height * avgLifeSpan.get(i) / maxLifeSpan);

            if(maxChildCount == 0)
                childCountAvg.add(0.0);
            else
                childCountAvg.add(p6.height * avgChildCount.get(i) / maxChildCount);
        }


        //drawing animals
        for (int i = 0; i < animalPopulationAvg.size(); i++)
        {
            g.drawRect(i + p1.x, p1.y - animalPopulationAvg.get(i), 2, 2);
            //g.drawLine(i + p1.x + 1, p1.y - animalPopulationAvg.get(i), i + p1.x + 1, p1.y - animalPopulationAvg.get(i));
        }

        //drawing grasses
        for (int i = 0; i < grassPopulationAvg.size(); i++)
        {
            g.drawRect(i + p2.x, p2.y - grassPopulationAvg.get(i), 2, 2);
            //g.drawLine(i + p2.x + 1, p2.y - grassPopulationAvg.get(i), i + p2.x + 1, p2.y - grassPopulationAvg.get(i));
        }

        //drawing genes
        for (int i = 0; i < genes.size(); i++)
        {
            g.drawRect(i + p3.x, p3.y - 90 * genes.get(i) / 8, 2, 2);
            //g.drawLine(p3.x + i, p3.y - genes.get(i) / 8 * 90, p3.x + i, p3.y - genes.get(i) / 8 * 90);
        }

        //drawing energy
        for (int i = 0; i < energyAvg.size(); i++)
        {
            g.drawRect(i + p4.x, p4.y - energyAvg.get(i), 2, 2);
            //g.drawLine(i + p4.x + 1, p4.y - energyAvg.get(i), i + p4.x + 1, p4.y - energyAvg.get(i));
        }

        //drawing lifespan
        for (int i = 0; i < lifeSpanAvg.size(); i++)
        {
            g.drawRect(i + p5.x, p5.y - lifeSpanAvg.get(i), 2, 2);
            //g.drawLine(i + p5.x + 1, p5.y - lifeSpanAvg.get(i), i + p5.x + 1, p5.y - lifeSpanAvg.get(i));
        }

        //drawing child
        for (int i = 0; i < childCountAvg.size(); i++)
        {
            g.drawRect(i + p6.x, (int) (p6.y - childCountAvg.get(i)), 2, 2);
            //g.drawLine(i + p6.x + 1, p6.y - childCountAvg.get(i), i + p6.x + 1, p6.y - childCountAvg.get(i));
        }



        //Legend

        g.setColor(Color.BLACK);
        g.drawString("Number Of Animals " + animalPopulation.getLast(), p1.x + p1.width/2, p1.y - p1.height/2);
        g.drawString("Number Of Grasses " + grassPopulation.getLast(), p2.x + p2.width/2, p2.y - p2.height/2);
        g.drawString("Dominant Gene " + genes.getLast(),  p3.x + p3.width/2, p3.y - p3.height/2);
        g.drawString("Average Energy " + avgAnimalEnergy.getLast(), p4.x + p4.width/2, p4.y - p4.height/2);
        g.drawString("Average Life Span " + avgLifeSpan.getLast(), p5.x + p5.width/2, p5.y - p5.height/2);
        g.drawString("Average Amount of Children " + avgChildCount.getLast(), p6.x + p6.width/2, p6.y - p6.height/2);

        Plot p = new Plot(540, 60, 160, 640);

        g.setColor(new Color(253, 239, 71));
        g.fillRect(p.x + 10, p.y + 5, 20, 20);
        g.setColor(Color.BLACK);
        g.drawString(" Savanna", p.x + 30, p.y + 25);

        g.setColor(new Color(102, 253, 89));
        g.fillRect(p.x + 10, p.y + 35, 20, 20);
        g.setColor(Color.BLACK);
        g.drawString(" Jungle", p.x + 30, p.y + 55);

        g.setColor(new Color(0, 120, 0));
        g.fillRect(p.x + 80, p.y + 5, 20, 20);
        g.setColor(Color.BLACK);
        g.drawString(" Grass", p.x + 100, p.y + 25);

        g.setColor(new Color(0, 0, 120));
        g.fillRect(p.x + 80, p.y + 35, 20, 20);
        g.setColor(new Color(0, 0, 255));
        g.fillRect(p.x + 100, p.y + 35, 20, 20);
        g.setColor(new Color(120, 81, 92));
        g.fillRect(p.x + 120, p.y + 35, 20, 20);
        g.setColor(new Color(255, 0, 0));
        g.fillRect(p.x + 140, p.y + 35, 20, 20);
        g.setColor(Color.BLACK);
        g.drawString(" Animal", p.x + 160, p.y + 55);
        /*
        g.setColor(new Color(0, 160, 7));
        g.fillRect(p1.x, p1.y + height / 20, width / 25, height / 50);
        g.drawString(" - Grass population plot", p1.x + width / 25, p1.y + height / 15);

        g.setColor(new Color(0, 0, 0));
        g.drawString("Legend: ", p1.x, p1.y + height / 10);
        int yp = p1.y + height / 10;
        int a = height / 22;
        g.drawString("Steppe Field ", p1.x + width / 10, yp + a);
        g.drawString("Jungle Field ", p1.x + width / 10, yp + 2 * a);
        g.drawString("Animal (Changes color to darker when has more energy)", p1.x + width / 10, yp + 3 * a);
        g.drawString("Grass ", p1.x + width / 10, yp + 4 * a);

        g.setColor(new Color(170, 224, 103));
        g.fillRect(p1.x, yp + a - 10, width / 20, height / 40);

        g.setColor(new Color(0, 160, 7));
        g.fillRect(p1.x, yp + 2 * a - 10, width / 20, height / 40);

        g.setColor(new Color(146, 82, 73));
        g.fillOval(p1.x, yp + 3 * a - 10, width / 20, height / 40);

        g.setColor(new Color(67, 222, 31));
        g.fillRect(p1.x, yp + 4 * a - 10, width / 20, height / 40);



         */
    }

    private void drawVerticalArrow(Graphics g, int x, int y, int x2, int y2, double max)
    {
        g.drawLine(x, y, x2, y2);
        g.drawLine(x2, y2, x2 - 5, y2 + 5);
        g.drawLine(x2, y2, x2 + 5, y2 + 5);

        g.drawLine(x - 3, (y2 - y)/4 + y, x + 3, (y2 - y)/4 + y);
        g.drawString(String.format("%.2f", max/4.0), (int) (x - 10 * ceil(log10(max))) - 20, (y2 - y)/4 + y);

        g.drawLine(x - 3, (y2 - y)/2 + y, x + 3, (y2 - y)/2 + y);
        g.drawString(String.format("%.2f", max/2.0), (int) (x - 10 * ceil(log10(max))) - 20, (y2 - y)/2 + y);

        g.drawLine(x - 3, (y2 - y)* 3/4 + y, x + 3, (y2 - y)* 3/4 + y);
        g.drawString(String.format("%.2f", max * 3.0/4), (int) (x - 10 * ceil(log10(max))) - 20, (y2 - y)*3/4 + y);
    }

    private void drawHorizontalArrow(Graphics g, int x, int y, int x2, int y2, Integer max)
    {
        g.drawLine(x, y, x2, y2);
        g.drawLine(x2, y2, x2 - 5, y2 + 5);
        g.drawLine(x2, y2, x2 - 5, y2 - 5);

        g.drawLine(x2 / 2, y2 + 3, x2 / 2, y2 - 3);
        g.drawString(Integer.toString(max / 2), x2 / 2 - 20, y + 20);

        g.drawLine(x2 / 4, y2 + 3, x2 / 4, y2 - 3);
        g.drawString(Integer.toString(max / 4), x2 / 4 - 20, y + 20);

        g.drawLine(x2 * 3 / 4, y2 + 3, x2 * 3 / 4, y2 - 3);
        g.drawString(Integer.toString(max * 3 / 4), x2 * 3 / 4 - 20, y + 20);

    }
}
