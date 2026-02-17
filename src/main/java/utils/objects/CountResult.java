package utils.objects;

import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.TreeMap;

public class CountResult<T extends Comparable<T>>
{
    private final Map<T, Integer> data = new TreeMap<>();

    Integer total = 0;

    public synchronized void add(T root)
    {
        data.put(root, data.getOrDefault(root, 0) + 1);
        total += 1;
    }

    public synchronized void add(T root, int i)
    {
        data.put(root, data.getOrDefault(root, 0) + i);
        total += i;
    }

    public synchronized void addAllResults(CountResult<T> result)
    {
        for (T word : result.getKeys())
        {
            add(word, result.get(word));
//            total += result.get(word);
        }
    }

    @Override
    public boolean equals(Object o)
    {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CountResult<T> that = (CountResult<T>) o;
        return Objects.equals(data, that.data);
    }

    @Override
    public int hashCode()
    {
        return Objects.hash(data);
    }

    @Override
    public String toString()
    {
        final StringBuilder sb = new StringBuilder();
        int offset = 0;
        for (T key : data.keySet())
        {
            if (offset % 5 == 0)
            {
                sb.append("\n\t");
            }
            sb.append(key).append("=").append(data.get(key)).append(", ");
            offset++;
        }
        sb.delete(sb.length() - 2, sb.length() - 1);
        return sb.toString();
    }

    public synchronized Set<T> getKeys()
    {
        return data.keySet();
    }

    public Integer getTotal()
    {
        return total;
    }

    public synchronized int get(T root)
    {
        return data.getOrDefault(root, 0);
    }
}
