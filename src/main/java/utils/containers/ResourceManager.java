package utils.containers;

import java.util.HashMap;
import java.util.Map;

public class ResourceManager
{
    private final Map<String, Object> resources = new HashMap<>();

    public <T> void addResource(String key, T value)
    {
        resources.put(key, value);
    }

    public boolean containsResource(String key)
    {
        return resources.containsKey(key);
    }

    public <T> T getResource(String key, Class<T> clazz)
    {
        Object value = resources.get(key);
        if (value == null)
        {
            throw new IllegalArgumentException("Resource not found for key " + key);
        }
        if (clazz.isInstance(value))
        {
            return clazz.cast(value);
        } else
        {
            throw new IllegalArgumentException("Invalid resource type for key " + key);
        }
    }
}

