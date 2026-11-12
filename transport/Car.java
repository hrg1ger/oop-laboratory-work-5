package transport;

import java.util.Arrays;
import java.util.Random;

import exceptions.NoSuchModelNameException;
import exceptions.DuplicateModelNameException;
import exceptions.ModelPriceOutOfBoundsException;

public class Car implements Vehicle {
    private String _mark;
    private Model[] _models;

    public Car(String mark, int size) {
        _mark = mark;
        _models = new Model[size];

        for (int i = 0; i < size; i++) {
            _models[i] = new Model("Model_" + (i + 1), 100_000 + new Random().nextInt(9_900_000));
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
    public void updateModelName(String oldName, String newName) throws NoSuchModelNameException, DuplicateModelNameException {
        if (oldName.equals(newName)) {
            return;
        }
        Model targetModel = null;

        for (Model model : _models) {
            String currentName = model.getName();

            if (currentName.equals(newName)) {
                throw new DuplicateModelNameException(newName);
            }

            if (currentName.equals(oldName)) {
                targetModel = model;
            }
        }

        if (targetModel == null) {
            throw new NoSuchModelNameException(oldName);
        }
        targetModel.setName(newName);
    }

    @Override
    public String[] getModelNames() {
        String[] names = new String[_models.length];

        for (int i = 0; i < _models.length; i++) {
            names[i] = _models[i].getName();
        }
        return names;
    }

    @Override
    public double getPriceByName(String name) throws NoSuchModelNameException {
        for (Model model : _models) {
            if (model.getName().equals(name)) {
                return model.getPrice();
            }
        }
        throw new NoSuchModelNameException(name);
    }

    @Override
    public void setPriceByName(String name, double newPrice) throws NoSuchModelNameException {
        if (newPrice <= 0) {
            throw new ModelPriceOutOfBoundsException(newPrice);
        }

        for (Model model : _models) {
            if (model.getName().equals(name)) {
                model.setPrice(newPrice);
                return;
            }
        }
        throw new NoSuchModelNameException(name);
    }

    @Override
    public double[] getModelPrices() {
        double[] prices = new double[_models.length];

        for (int i = 0; i < _models.length; i++) {
            prices[i] = _models[i].getPrice();
        }
        return prices;
    }

    @Override
    public void addModel(String name, double price) throws DuplicateModelNameException {
        if (price <= 0) {
            throw new ModelPriceOutOfBoundsException(price);
        }

        if (isModelExist(name)) {
            throw new DuplicateModelNameException(name);
        }
        _models = Arrays.copyOf(_models, _models.length + 1);
        _models[_models.length - 1] = new Model(name, price);
    }

    @Override
    public void deleteModel(String name) throws NoSuchModelNameException {
        int index = -1;

        for (int i = 0; i < _models.length; i++) {
            if (_models[i].getName().equals(name)) {
                index = i;
                break;
            }
        }

        if (index == -1) {
            throw new NoSuchModelNameException(name);
        }

        if (index < _models.length - 1) {
            System.arraycopy(_models, index + 1, _models, index, _models.length - (index + 1));
        }
        _models = Arrays.copyOf(_models, _models.length - 1);
    }

    @Override
    public int getModelsSize() {
        return _models.length;
    }

    private boolean isModelExist(String name) {
        for (Model model : _models) {
            if (model.getName().equals(name)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public String toString() {
        StringBuffer sb = new StringBuffer();

        sb.append("Car{mark='").append(_mark).append("', size=").append(_models.length).append(", models=[");

        for (int i = 0; i < _models.length; i++) {
            if (i != 0) {
                sb.append(", ");
            }
            sb.append("{name='").append(_models[i].getName()).append("', price=").append(_models[i].getPrice()).append("}");
        }
        sb.append("]}");

        return sb.toString();
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }

        if (!(obj instanceof Car)) {
            return false;
        }
        Car other = (Car) obj;

        if (!_mark.equals(other._mark)) {
            return false;
        }

        if (_models.length != other._models.length) {
            return false;
        }

        for (int i = 0; i < _models.length; i++) {
            if (!_models[i].getName().equals(other._models[i].getName())) {
                return false;
            }

            if (Double.compare(_models[i].getPrice(), other._models[i].getPrice()) != 0) {
                return false;
            }
        }
        return true;
    }

    @Override
    public int hashCode() {
        int result = _mark.hashCode();

        for (Model model : _models) {
            result = 31 * result + model.getName().hashCode();
            result = 31 * result + Double.hashCode(model.getPrice());
        }
        return result;
    }

    @Override
    public Car clone() {
        try {
            Car cloned = (Car) super.clone();
            cloned._models = new Model[_models.length];

            for (int i = 0; i < _models.length; i++) {
                cloned._models[i] = new Model(_models[i].getName(), _models[i].getPrice());
            }
            return cloned;
        } catch (CloneNotSupportedException e) {
            throw new AssertionError(e);
        }
    }

    private static class Model implements java.io.Serializable {
        private String _name;
        private double _price;

        public Model(String name, double price) {
            _name = name;
            _price = price;
        }

        public String getName() {
            return _name;
        }

        public void setName(String name) {
            _name = name;
        }

        public double getPrice() {
            return _price;
        }

        public void setPrice(double price) {
            _price = price;
        }
    }
}