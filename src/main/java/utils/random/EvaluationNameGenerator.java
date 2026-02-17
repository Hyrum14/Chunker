package utils.random;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

public class EvaluationNameGenerator
{
    private static final Random random = new Random();
    private static List<List<String>> options = new ArrayList<>();

    public static String generate()
    {
        if (options.isEmpty())
            fill();

        StringBuilder output = new StringBuilder();

        output.append(getRandomPart(1)).append("_");
        output.append(getRandomPart(0)).append("_");
        output.append(getRandomPart(2));
        return output.toString();
    }

    private static String getRandomPart(int categoryIndex)
    {
        return options.get(random.nextInt(options.size())).get(categoryIndex);
    }

    private static void fill()
    {
        options.add(Arrays.asList("Red", "Happy", "Dog"));
        options.add(Arrays.asList("Green", "Angry", "Cat"));
        options.add(Arrays.asList("Blue", "Sad", "Elephant"));
        options.add(Arrays.asList("Yellow", "Calm", "Giraffe"));
        options.add(Arrays.asList("Pink", "Frustrated", "Lion"));
        options.add(Arrays.asList("Orange", "Elated", "Gorilla"));
        options.add(Arrays.asList("Purple", "Anxious", "Tiger"));
        options.add(Arrays.asList("Gray", "Cheerful", "Zebra"));
        options.add(Arrays.asList("Brown", "Content", "Hippopotamus"));
        options.add(Arrays.asList("Black", "Excited", "Rhinoceros"));
        options.add(Arrays.asList("White", "Grateful", "Monkey"));
        options.add(Arrays.asList("Beige", "Hopeful", "Cheetah"));
        options.add(Arrays.asList("Cyan", "Amused", "Kangaroo"));
        options.add(Arrays.asList("Magenta", "Confident", "Crocodile"));
        options.add(Arrays.asList("Olive", "Enchanted", "Penguin"));
        options.add(Arrays.asList("Maroon", "Joyful", "Panda"));
        options.add(Arrays.asList("Navy", "Peaceful", "Flamingo"));
        options.add(Arrays.asList("Teal", "Refreshed", "Koala"));
        options.add(Arrays.asList("Coral", "Proud", "Bear"));
        options.add(Arrays.asList("Lavender", "Satisfied", "Owl"));
        options.add(Arrays.asList("Gold", "Surprised", "Wolf"));
        options.add(Arrays.asList("Silver", "Thankful", "Fox"));
        options.add(Arrays.asList("Turquoise", "Amused", "Kangaroo"));
        options.add(Arrays.asList("Mauve", "Grateful", "Lemur"));
        options.add(Arrays.asList("Crimson", "Hopeful", "Jaguar"));
        options.add(Arrays.asList("Emerald", "Anxious", "Sloth"));
        options.add(Arrays.asList("Teal", "Content", "Meerkat"));
        options.add(Arrays.asList("Indigo", "Excited", "Raccoon"));
        options.add(Arrays.asList("Azure", "Enchanted", "Platypus"));
        options.add(Arrays.asList("Fuchsia", "Joyful", "Otter"));
        options.add(Arrays.asList("Sepia", "Peaceful", "Ostrich"));
        options.add(Arrays.asList("Sapphire", "Refreshed", "Panda"));
        options.add(Arrays.asList("Amber", "Proud", "Parrot"));
        options.add(Arrays.asList("Periwinkle", "Satisfied", "Seal"));
        options.add(Arrays.asList("Lavender", "Surprised", "Squirrel"));
        options.add(Arrays.asList("Scarlet", "Thankful", "Toucan"));
        options.add(Arrays.asList("Ochre", "Amused", "Turtle"));
        options.add(Arrays.asList("Burgundy", "Confident", "Vulture"));
        options.add(Arrays.asList("Chartreuse", "Enchanted", "Weasel"));
        options.add(Arrays.asList("Sienna", "Satisfied", "Walrus"));
        options.add(Arrays.asList("Cerulean", "Peaceful", "Yak"));
        options.add(Arrays.asList("Rose", "Proud", "Zebra"));
        options.add(Arrays.asList("Maroon", "Hopeful", "Armadillo"));
        options.add(Arrays.asList("Ivory", "Anxious", "Badger"));
        options.add(Arrays.asList("Mustard", "Content", "Bison"));
        options.add(Arrays.asList("Salmon", "Excited", "Chinchilla"));
        options.add(Arrays.asList("Tan", "Enchanted", "Coyote"));
        options.add(Arrays.asList("Olive", "Joyful", "Deer"));
        options.add(Arrays.asList("Mauve", "Peaceful", "Echidna"));
        options.add(Arrays.asList("Teal", "Refreshed", "Fox"));
        options.add(Arrays.asList("Auburn", "Satisfied", "Gazelle"));
        options.add(Arrays.asList("Crimson", "Surprised", "Hamster"));
        options.add(Arrays.asList("Turquoise", "Thankful", "Hyena"));
        options.add(Arrays.asList("Vermilion", "Amused", "Kangaroo"));
        options.add(Arrays.asList("Lavender", "Confident", "Lemur"));
        options.add(Arrays.asList("Amber", "Enchanted", "Lynx"));
        options.add(Arrays.asList("Sienna", "Satisfied", "Mongoose"));
        options.add(Arrays.asList("Navy", "Peaceful", "Opossum"));
        options.add(Arrays.asList("Coral", "Proud", "Panther"));
        options.add(Arrays.asList("Beige", "Surprised", "Porcupine"));
        options.add(Arrays.asList("Sepia", "Thankful", "Quokka"));
        options.add(Arrays.asList("Sapphire", "Amused", "Raccoon"));
    }

    public static void main(String[] args)
    {
        for (int i = 0; i < 15; ++i)
            System.out.println(generate());
    }
}
