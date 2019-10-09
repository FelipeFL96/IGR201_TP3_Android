package fr.telecom_paris.fdelima.cambio;

enum Currency {
    NONE(0, "NONE", "None"),
    EUR(1, "EUR", "Euro"),
    USD(2, "USD", "Dollar"),
    GBP(3, "GBP", "Livre"),
    BRL(4, "BRL", "Real");

    private final int value;
    private final String code;
    private final String name;

    private Currency(int value, String text, String name) {
        this.value = value;
        this.code = text;
        this.name = name;
    }
    public final int value() { return this.value; }
    public final String code() { return this.code; }
    public final String getName() { return this.name; }

    public static Currency convertFromValue(int value) {
        switch (value) {
            case 1:
                return EUR;
            case 2:
                return USD;
            case 3:
                return GBP;
            case 4:
                return BRL;
            default:
                return NONE;
        }
    }
}