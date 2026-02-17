package utils.containers;

import java.util.Objects;

public class Pair<T, K>
{
    private T first;
    private K second;

    public Pair(T first, K second)
    {
        this.first = first;
        this.second = second;
    }

    @Override
    public boolean equals(Object o)
    {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Pair<?, ?> pair = (Pair<?, ?>) o;
        return Objects.equals(first, pair.first) && Objects.equals(second, pair.second);
    }

    @Override
    public int hashCode()
    {
        return Objects.hash(first, second);
    }

    public T getFirst()
    {
        return first;
    }

    public void setFirst(T first)
    {
        this.first = first;
    }

    public K getSecond()
    {
        return second;
    }

    public void setSecond(K second)
    {
        this.second = second;
    }

    @Override
    public String toString()
    {
        final StringBuilder sb = new StringBuilder("Pair{");
        sb.append("first=").append(first);
        sb.append(", second=").append(second);
        sb.append('}');
        return sb.toString();
    }
}
