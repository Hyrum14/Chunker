package utils.containers;

import java.util.Objects;

public class Triplet<A, B, C>
{
    private A first;
    private B second;
    private C third;

    public Triplet(A first, B second, C third)
    {
        this.first = first;
        this.second = second;
        this.third = third;
    }

    public A getFirst()
    {
        return first;
    }

    public void setFirst(A first)
    {
        this.first = first;
    }

    public B getSecond()
    {
        return second;
    }

    public void setSecond(B second)
    {
        this.second = second;
    }

    public C getThird()
    {
        return third;
    }

    public void setThird(C third)
    {
        this.third = third;
    }

    @Override
    public boolean equals(Object o)
    {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Triplet<?, ?, ?> triplet = (Triplet<?, ?, ?>) o;
        return Objects.equals(first, triplet.first) && Objects.equals(second, triplet.second) && Objects.equals(third, triplet.third);
    }

    @Override
    public int hashCode()
    {
        return Objects.hash(first, second, third);
    }

    @Override
    public String toString()
    {
        final StringBuilder sb = new StringBuilder("Triplet{");
        sb.append("first=").append(first);
        sb.append(", second=").append(second);
        sb.append(", third=").append(third);
        sb.append('}');
        return sb.toString();
    }
}

