package World;

import java.util.*;

public class WorldMap
{
    private static final Random random = new Random();
    private Map<Position, List<Animal>> mapOfAnimals = new HashMap<>();
    private Set<Position> setOfGrassPositions = new HashSet<>();
    private List<Animal> animals = new LinkedList<>();
    private List<Grass> grasses = new LinkedList<>();
    private Position upperRight;
    private Position lowerLeft = new Position(0, 0);
    private int animalEnergy;
    private Vector<Integer> animalsGenes;
    private int numOfChildren = 0;
    private int numOfDeadAnimals = 0;
    private int deadAnimalsDays = 0;
    private int jungleLeft;
    private int jungleRight;
    private int jungleUp;
    private int jungleDown;
    private int startEnergy;
    private int moveEnergy;
    private int plantEnergy;


    WorldMap(int width, int height, int startEnergy, int moveEnergy, int plantEnergy, double jungleRatio)
    {
        animalEnergy = 0;
        this.upperRight = new Position(width - 1, height - 1);
        this.startEnergy = startEnergy;
        this.moveEnergy = moveEnergy;
        this.plantEnergy = plantEnergy;
        animalsGenes = new Vector<>(Genes.genesTypesCount);
        for(int i = 0; i < Genes.genesTypesCount; i++)
            animalsGenes.add(0);
        jungleLeft = (int) (width * (1 - jungleRatio) / 2);
        jungleRight = (int) (width * (1 + jungleRatio) / 2 - 1);
        jungleUp = (int) (height * (1 + jungleRatio) / 2 - 1);
        jungleDown = (int) (height * (1 - jungleRatio) / 2);
    }

    public void place(Animal animal)
    {
        if (!animal.getPosition().follows(lowerLeft) || !animal.getPosition().precedes(upperRight))
            throw new IllegalArgumentException("Out of map");
        animals.add(animal);
        animal.setMap(this);
        mapOfAnimals.computeIfAbsent(animal.getPosition(), k -> new LinkedList<>()); //adds list if there is no element on this position
        mapOfAnimals.get(animal.getPosition()).add(animal);
        animalEnergy += animal.getEnergy();
        for(int i : animal.getGenes().getGenes())
        {
            animalsGenes.set(i, animalsGenes.get(i) + 1);
        }
    }

    public void place(Grass grass)
    {
        if (!grass.getPosition().follows(lowerLeft) || !grass.getPosition().precedes(upperRight))
            throw new IllegalArgumentException("Out of map");
        setOfGrassPositions.add(grass.getPosition());
        grasses.add(grass);
    }

    public List<Animal> getAnimals()
    {
        return animals;
    }

    public List<Grass> getGrasses()
    {
        return grasses;
    }

    public int getStartEnergy()
    {
        return startEnergy;
    }

    public int getMoveEnergy()
    {
        return moveEnergy;
    }

    public Position getUpperRight()
    {
        return new Position(upperRight);
    }

    public void elementMoved(Animal animal, Position oldPosition)
    {
        mapOfAnimals.get(oldPosition).remove(animal);
        if(mapOfAnimals.get(oldPosition).isEmpty())
            mapOfAnimals.remove(oldPosition);
        mapOfAnimals.computeIfAbsent(animal.getPosition(), k -> new LinkedList<>());
        mapOfAnimals.get(animal.getPosition()).add(animal);
    }

    void simulateDay()
    {
        removeDead();
        moveAnimals();
        eatGrass();
        reproduce();
        newGrass();
    }

    public void moveAnimals()
    {
        for(Animal animal : animals)
        {
            animal.rotate();
            animal.move();
        }
    }

    public void removeDead()
    {
        for(ListIterator<Animal> iterator = animals.listIterator(); iterator.hasNext();)
        {
            Animal currentAnimal = iterator.next();
            if(currentAnimal.getEnergy() <= 0)
            {
                mapOfAnimals.get(currentAnimal.getPosition()).remove(currentAnimal);
                if(mapOfAnimals.get(currentAnimal.getPosition()).isEmpty())
                    mapOfAnimals.remove(currentAnimal.getPosition());
                iterator.remove();
                numOfChildren -=  currentAnimal.getNumOfChildren();
                numOfDeadAnimals++;
                deadAnimalsDays += currentAnimal.getDays();
                for(int i : currentAnimal.getGenes().getGenes())
                {
                    animalsGenes.set(i, animalsGenes.get(i) - 1);
                }
            }

        }
    }

    public void eatGrass()
    {
        for(ListIterator<Grass> iterator = grasses.listIterator(); iterator.hasNext();)
        {
            Position currentPosition = iterator.next().getPosition();
            List<Animal> tileAnimals = mapOfAnimals.get(currentPosition);
            if(tileAnimals != null)
            {
                List<Animal> strongestAnimals = new LinkedList<>();
                for (Animal tileAnimal : tileAnimals)
                {
                    if (strongestAnimals.isEmpty())
                        strongestAnimals.add(tileAnimal);
                    else if (tileAnimal.getEnergy() > strongestAnimals.get(0).getEnergy())
                    {
                        strongestAnimals.clear();
                        strongestAnimals.add(tileAnimal);
                    }
                    else if (tileAnimal.getEnergy() == strongestAnimals.get(0).getEnergy())
                        strongestAnimals.add(tileAnimal);
                }
                int foodShare = plantEnergy / strongestAnimals.size();
                for (Animal strongestAnimal : strongestAnimals)
                    strongestAnimal.feed(foodShare);
                setOfGrassPositions.remove(currentPosition);
                iterator.remove();
            }
        }
    }

    public void reproduce()
    {
        List<Animal> newAnimals = new LinkedList<>();
        for(Map.Entry<Position, List<Animal>> mapEntry : mapOfAnimals.entrySet())
        {
            List<Animal> tileAnimals = mapEntry.getValue();
            Position position = mapEntry.getKey();
            if(tileAnimals.size() > 1)
            {
                List<Animal> strongestAnimals = new LinkedList<>();
                List<Animal> secondStrongestAnimals = new LinkedList<>();
                for (Animal animal : tileAnimals)
                {
                    if(strongestAnimals.isEmpty())
                        strongestAnimals.add(animal);
                    else if(animal.getEnergy() > strongestAnimals.get(0).getEnergy())
                    {
                        secondStrongestAnimals = strongestAnimals;
                        strongestAnimals = new LinkedList<>();
                        strongestAnimals.add(animal);
                    }
                    else if(animal.getEnergy() == strongestAnimals.get(0).getEnergy())
                    {
                        strongestAnimals.add(animal);
                    }
                    else if(secondStrongestAnimals.isEmpty())
                    {
                        secondStrongestAnimals.add(animal);
                    }
                    else if(animal.getEnergy() > secondStrongestAnimals.get(0).getEnergy())
                    {
                        secondStrongestAnimals.clear();
                        secondStrongestAnimals.add(animal);
                    }
                    else if(animal.getEnergy() == secondStrongestAnimals.get(0).getEnergy())
                    {
                        secondStrongestAnimals.add(animal);
                    }
                }
                Animal father = strongestAnimals.remove(random.nextInt(strongestAnimals.size()));
                Animal mother;
                if(!strongestAnimals.isEmpty())
                    mother = strongestAnimals.remove(random.nextInt(strongestAnimals.size()));
                else
                    mother = secondStrongestAnimals.remove(random.nextInt(secondStrongestAnimals.size()));
                if(father.getEnergy() > startEnergy / 2 && mother.getEnergy() > startEnergy / 2)
                {
                    newAnimals.add(new Animal(father, mother, randomNeighbourPosition(position)));
                    father.feed(-father.getEnergy() / 4);
                    mother.feed(-mother.getEnergy() / 4);
                    father.addChild();
                    mother.addChild();
                    numOfChildren += 2;
                }
            }
        }
        for(Animal newAnimal : newAnimals)
            this.place(newAnimal);
    }

    public void newGrass()
    {
        newJungleGrass();
        newSavannaGrass();
    }

    public void newSavannaGrass()
    {
        int zone = random.nextInt(4);
        int zoneBound = zone == 0 ? 3 : zone - 1;
        for(int k = zone; k != zoneBound; k = (k+1) % 4)
        {
            int leftBound;
            int rightBound;
            int upperBound;
            int lowerBound;
            switch (k)
            {
                case 0:
                    leftBound = 0;
                    rightBound = jungleLeft - 1;
                    upperBound = upperRight.y;
                    lowerBound = jungleDown;
                    break;
                case 1:
                    leftBound = jungleLeft;
                    rightBound = upperRight.x;
                    upperBound = upperRight.y;
                    lowerBound = jungleUp + 1;
                    break;
                case 2:
                    leftBound = jungleRight + 1;
                    rightBound = upperRight.x;
                    upperBound = jungleUp;
                    lowerBound = 0;
                    break;
                case 3:
                    leftBound = 0;
                    rightBound = jungleRight;
                    upperBound = jungleDown - 1;
                    lowerBound = 0;
                    break;
                default:
                    throw new IllegalStateException("Unexpected value: " + zone);
            }
            int x = leftBound + random.nextInt(rightBound - leftBound + 1);
            int y = lowerBound + random.nextInt(upperBound - lowerBound + 1);
            Position newPosition;
            int rightLimit = x == leftBound ? rightBound : x - 1;
            int lowerLimit = y == lowerBound ? upperBound: y - 1;
            for (int i = x; i != rightLimit; i = i == rightBound ? i = leftBound : i + 1)
                for (int j = y; j != lowerLimit; j = j == upperBound ? j = lowerBound : j + 1)
                {
                    newPosition = new Position(i, j);
                    if (!mapOfAnimals.containsKey(newPosition) && !setOfGrassPositions.contains(newPosition)) {
                        place(new Grass(newPosition));
                        return;
                    }
                }
        }
    }

    public void newJungleGrass()
    {
        int x = jungleLeft + random.nextInt(jungleRight - jungleLeft + 1);
        int y = jungleDown + random.nextInt(jungleUp - jungleDown + 1);
        Position newPosition;
        int rightLimit = x == jungleLeft ? jungleRight : x - 1;
        int lowerLimit = y == jungleDown ? jungleUp: y - 1;
        for(int i = x; i != rightLimit; i = i == jungleRight ? i = jungleLeft : i + 1)
            for(int j = y; j != lowerLimit; j = j == jungleUp ? j = jungleDown : j + 1)
            {
                newPosition = new Position(i, j);
                if(!mapOfAnimals.containsKey(newPosition) && !setOfGrassPositions.contains(newPosition))
                {
                    place(new Grass(newPosition));
                    return;
                }
            }
    }

    private Position randomNeighbourPosition(Position position)
    {
        ArrayList<Position> emptyNeighbouringPositions = new ArrayList<>();
        Position neighbouringPosition;
        for(int i = -1; i <= 1; i++)
            for(int j = -1; j <= 1; j++)
            {
                neighbouringPosition = new Position(mod(position.x + i, upperRight.x), mod(position.y + j, upperRight.y));
                if(!mapOfAnimals.containsKey(neighbouringPosition) && !setOfGrassPositions.contains(neighbouringPosition))
                    emptyNeighbouringPositions.add(new Position(neighbouringPosition));
            }
        if(emptyNeighbouringPositions.isEmpty()) //if there are no empty neighbouring positions choose neighbouring position
            for(int i = -1; i <= 1; i++)
                for(int j = -1; j <= 1; j++)
                    if(i != 0 || j != 0)
                    {
                        neighbouringPosition = new Position(position.x + i, position.y + j);
                        emptyNeighbouringPositions.add(new Position(neighbouringPosition));
                    }
        return emptyNeighbouringPositions.get(random.nextInt(emptyNeighbouringPositions.size()));
    }

    private int mod(int a, int b)
    {
        if(a > 0)
            return a % b;
        else
            return (a % b + b) % b;
    }

    public Vector<Integer> getAnimalsGenes()
    {
        return (Vector<Integer>) animalsGenes.clone();
    }

    public int getJungleLeft()
    {
        return jungleLeft;
    }

    public int getJungleRight()
    {
        return jungleRight;
    }

    public int getJungleUp()
    {
        return jungleUp;
    }

    public int getJungleDown()
    {
        return jungleDown;
    }

    public double getAverageNumOfChildren()
    {
        return (double) numOfChildren / animals.size();
    }

    public int getAverageLifeSpan()
    {
        if(numOfDeadAnimals == 0)
            return 0;
        return deadAnimalsDays / numOfDeadAnimals;
    }

    public int getAvgAnimalEnergy()
    {
        if(animals.size() == 0)
            return 0;
        return animalEnergy / animals.size();
    }

    public void energyChanged(int energyDiff)
    {
        animalEnergy += energyDiff;
    }
}
