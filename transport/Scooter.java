package transport;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import exceptions.NoSuchModelNameException;
import exceptions.DuplicateModelNameException;
import exceptions.ModelPriceOutOfBoundsException;

public class Scooter implements Vehicle {
    private String _mark;
    private HashMap<String, Double> _models;

    public Scooter(String mark, int size) {
        _mark = mark;
        _models = new HashMap<>();
        Random random = new Random();

        for (int i = 0; i < size; i++) {
            String name = "Model_" + (i + 1);
            double price = 100_000 + random.nextInt(9_900_000);

            _models.put(name, price);
        }
    }

    @Override
    public String getMark() {
        return _mark;
    }

    @Override
    public void setMark(String mark) {
        _mark = mark;
    }

    @Override
    public void updateModelName(String oldName, String newName)
            throws NoSuchModelNameException, DuplicateModelNameException {
        if (oldName.equals(newName)) {
            return;
        }

        if (!_models.containsKey(oldName)) {
            throw new NoSuchModelNameException(oldName);
        }

        if (_models.containsKey(newName)) {
            throw new DuplicateModelNameException(newName);
        }
        Double price = _models.remove(oldName);
        _models.put(newName, price);
    }

    @Override
    public String[] getModelNames() {
        return _models.keySet().toArray(new String[0]);
    }

    @Override
    public double getPriceByName(String name) throws NoSuchModelNameException {
        Double price = _models.get(name);

        if (price == null) {
            throw new NoSuchModelNameException(name);
        }
        return price;
    }

    @Override
    public void setPriceByName(String name, double newPrice) throws NoSuchModelNameException {
        if (newPrice <= 0) {
            throw new ModelPriceOutOfBoundsException(newPrice);
        }

        if (!_models.containsKey(name)) {
            throw new NoSuchModelNameException(name);
        }
        _models.put(name, newPrice);
    }

    @Override
    public double[] getModelPrices() {
        double[] prices = new double[_models.size()];
        int i = 0;

        for (Double price : _models.values()) {
            prices[i++] = price;
        }
        return prices;
    }

    @Override
    public void addModel(String name, double price) throws DuplicateModelNameException {
        if (price <= 0) {
            throw new ModelPriceOutOfBoundsException(price);
        }

        if (_models.containsKey(name)) {
            throw new DuplicateModelNameException(name);
        }
        _models.put(name, price);
    }

    @Override
    public void deleteModel(String name) throws NoSuchModelNameException {
        if (_models.remove(name) == null) {
            throw new NoSuchModelNameException(name);
        }
    }

    @Override
    public int getModelsSize() {
        return _models.size();
    }

    @Override
    public String toString() {
        StringBuffer sb = new StringBuffer();

        sb.append("Scooter{mark='")
                .append(_mark)
                .append("', size=")
                .append(_models.size())
                .append(", models=[");

        boolean first = true;

        for (Map.Entry<String, Double> entry : _models.entrySet()) {
            if (!first) {
                sb.append(", ");
            }
            first = false;

            sb.append("{name='")
                    .append(entry.getKey())
                    .append("', price=")
                    .append(entry.getValue())
                    .append("}");
        }
        sb.append("]}");

        return sb.toString();
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }

        if (!(obj instanceof Scooter)) {
            return false;
        }
        Scooter other = (Scooter) obj;

        if (!_mark.equals(other._mark)) {
            return false;
        }

        if (_models.size() != other._models.size()) {
            return false;
        }

        for (Map.Entry<String, Double> entry : _models.entrySet()) {
            Double otherPrice = other._models.get(entry.getKey());

            if (otherPrice == null) {
                return false;
            }

            if (Double.compare(entry.getValue(), otherPrice) != 0) {
                return false;
            }
        }
        return true;
    }

    @Override
    public int hashCode() {
        int result = _mark.hashCode();

        for (Map.Entry<String, Double> entry : _models.entrySet()) {
            result = 31 * result + entry.getKey().hashCode();
            result = 31 * result + Double.hashCode(entry.getValue());
        }
        return result;
    }

    @Override
    public Scooter clone() {
        try {
            Scooter cloned = (Scooter) super.clone();
            cloned._models = new HashMap<>(_models);

            return cloned;
        } catch (CloneNotSupportedException e) {
            throw new AssertionError(e);
        }
    }
}