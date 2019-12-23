package World;

public abstract class Element
{
    Position position;

    public Position getPosition()
    {
        return new Position(this.position);
    }
}
