package transport;

import java.util.LinkedList;
import java.util.Random;

import exceptions.NoSuchModelNameException;
import exceptions.DuplicateModelNameException;
import exceptions.ModelPriceOutOfBoundsException;

public class Moped implements Vehicle {
    private String _mark;
    private LinkedList<Model> _models;

    public Moped(String mark, int size) {
        _mark = mark;
        _models = new LinkedList<>();
        Random random = new Random();

        for (int i = 0; i < size; i++) {
            String name = "Model_" + (i + 1);
            double price = 100_000 + random.nextInt(9_900_000);

            _models.add(new Model(name, price));
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
        String[] names = new String[_models.size()];
        int i = 0;

        for (Model model : _models) {
            names[i++] = model.getName();
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
        double[] prices = new double[_models.size()];
        int i = 0;

        for (Model model : _models) {
            prices[i++] = model.getPrice();
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
        _models.add(new Model(name, price));
    }

    @Override
    public void deleteModel(String name) throws NoSuchModelNameException {
        Model targetModel = null;

        for (Model model : _models) {
            if (model.getName().equals(name)) {
                targetModel = model;
                break;
            }
        }

        if (targetModel == null) {
            throw new NoSuchModelNameException(name);
        }
        _models.remove(targetModel);
    }

    @Override
    public int getModelsSize() {
        return _models.size();
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

        sb.append("Moped{mark='")
                .append(_mark)
                .append("', size=")
                .append(_models.size())
                .append(", models=[");

        boolean first = true;

        for (Model model : _models) {
            if (!first) {
                sb.append(", ");
            }
            first = false;

            sb.append("{name='")
                    .append(model.getName())
                    .append("', price=")
                    .append(model.getPrice())
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

        if (!(obj instanceof Moped)) {
            return false;
        }
        Moped other = (Moped) obj;

        if (!_mark.equals(other._mark)) {
            return false;
        }

        if (_models.size() != other._models.size()) {
            return false;
        }

        for (int i = 0; i < _models.size(); i++) {
            Model a = _models.get(i);
            Model b = other._models.get(i);

            if (!a.getName().equals(b.getName())) {
                return false;
            }

            if (Double.compare(a.getPrice(), b.getPrice()) != 0) {
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
    public Moped clone() {
        try {
            Moped cloned = (Moped) super.clone();
            cloned._models = new LinkedList<>();

            for (Model model : _models) {
                cloned._models.add(new Model(model.getName(), model.getPrice()));
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

        public String getName() { return _name; }

        public void setName(String name) { _name = name; }

        public double getPrice() { return _price; }

        public void setPrice(double price) { _price = price; }
    }
}