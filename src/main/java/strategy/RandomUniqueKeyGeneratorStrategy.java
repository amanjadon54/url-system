package strategy;

import java.util.Random;

public class RandomUniqueKeyGeneratorStrategy implements UniqueKeyStrategy {

    private final int leftLimit = 97; // letter 'a'
    private final int rightLimit = 122; // letter 'z'
    private final int targetStringLength = 10;

    @Override
    public String generate() {

        Random random = new Random();
        StringBuilder buffer = new StringBuilder(targetStringLength);
        for (int i = 0; i < targetStringLength; i++) {
            int randomLimitedInt = leftLimit + (int)
                    (random.nextFloat() * (rightLimit - leftLimit + 1));
            buffer.append((char) randomLimitedInt);
        }
        String generatedString = buffer.toString();

        return generatedString;
    }
}
