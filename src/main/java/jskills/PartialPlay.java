package jskills;

public class PartialPlay {

    public static double getPartialPlayPercentage(Object player) {
        // If the player doesn't support the interface, assume 1.0 == 100%
        if (player instanceof SupportPartialPlay) {
            double partialPlayPercentage = ((SupportPartialPlay) player)
                    .getPartialPlayPercentage();

            // HACK to get around bug near 0
            final double smallestPercentage = 0.0001;
            if (partialPlayPercentage < smallestPercentage) {
                partialPlayPercentage = smallestPercentage;
            }

            return partialPlayPercentage;
        }
        return 1.0;
    }
}