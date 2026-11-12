package exceptions;

public class ModelPriceOutOfBoundsException extends RuntimeException {
    public ModelPriceOutOfBoundsException(double price) {
        super("Цена модели должна быть больше 0. Передано значение: " + price);
    }
}