package ua.epam.spring.hometask.domain;

/**
 * @author Yuriy_Tkach
 */
public enum EventRating {

    LOW(0.8),

    MID(1d),

    HIGH(1.2);
    private double priceMultiplier;

    EventRating(Double priceMultiplier) {
        this.priceMultiplier = priceMultiplier;
    }

    public double getPriceMultiplier() {
        return priceMultiplier;
    }
}
