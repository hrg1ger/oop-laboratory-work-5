package transport;

import exceptions.DuplicateModelNameException;
import exceptions.NoSuchModelNameException;

public interface Vehicle extends java.io.Serializable, Cloneable {
    String getMark();
    void setMark(String mark);

    int getModelsSize();
    String[] getModelNames();
    double[] getModelPrices();

    double getPriceByName(String name) throws NoSuchModelNameException;
    void setPriceByName(String name, double newPrice) throws NoSuchModelNameException;

    void updateModelName(String oldName, String newName) throws NoSuchModelNameException, DuplicateModelNameException;

    void addModel(String name, double price) throws DuplicateModelNameException;
    void deleteModel(String name) throws NoSuchModelNameException;
}