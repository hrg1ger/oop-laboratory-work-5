package transport;

import java.util.Date;
import java.util.Random;

import exceptions.NoSuchModelNameException;
import exceptions.DuplicateModelNameException;
import exceptions.ModelPriceOutOfBoundsException;

public class Motorcycle implements Vehicle {
    private String _mark;
    private int _size;
    private Model _head;
    private transient long _lastModified;

    {
        _lastModified = System.currentTimeMillis();
    }

    public Motorcycle(String mark, int size) {
        _mark = mark;
        _size = size;
        _head = new Model();
        _head._prev = _head;
        _head._next = _head;

        for (int i = 0; i < size; i++) {
            addModelInternal("Model_" + (i + 1), 100_000 + new Random().nextInt(9_900_000));
        }
        updateLastModified();
    }

    @Override
    public String getMark() {
        return _mark;
    }

    @Override
    public void setMark(String mark) {
        _mark = mark;
        updateLastModified();
    }

    public Date getLastModifiedDate() {
        return new Date(_lastModified);
    }

    @Override
    public void updateModelName(String oldName, String newName) throws NoSuchModelNameException, DuplicateModelNameException {
        if (oldName.equals(newName)) {
            return;
        }
        Model targetNode = null;
        Model node = _head._next;

        while (node != _head) {
            if (newName.equals(node._name)) {
                throw new DuplicateModelNameException(newName);
            }

            if (oldName.equals(node._name)) {
                targetNode = node;
            }
            node = node._next;
        }
        if (targetNode == null) {
            throw new NoSuchModelNameException(oldName);
        }
        targetNode._name = newName;
        updateLastModified();
    }

    @Override
    public String[] getModelNames() {
        String[] names = new String[_size];
        Model node = _head._next;
        int i = 0;

        while (node != _head) {
            names[i++] = node._name;
            node = node._next;
        }
        return names;
    }

    @Override
    public double getPriceByName(String name) throws NoSuchModelNameException {
        Model node = _head._next;

        while (node != _head) {
            if (name.equals(node._name)) {
                return node._price;
            }
            node = node._next;
        }
        throw new NoSuchModelNameException(name);
    }

    @Override
    public void setPriceByName(String name, double newPrice) throws NoSuchModelNameException {
        if (newPrice <= 0) {
            throw new ModelPriceOutOfBoundsException(newPrice);
        }
        Model node = _head._next;

        while (node != _head) {
            if (name.equals(node._name)) {
                node._price = newPrice;
                updateLastModified();
                return;
            }
            node = node._next;
        }
        throw new NoSuchModelNameException(name);
    }

    @Override
    public double[] getModelPrices() {
        double[] prices = new double[_size];
        Model node = _head._next;
        int i = 0;

        while (node != _head) {
            prices[i++] = node._price;
            node = node._next;
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
        addModelInternal(name, price);
        _size++;
        updateLastModified();
    }

    @Override
    public void deleteModel(String name) throws NoSuchModelNameException {
        Model node = _head._next;

        while (node != _head) {
            if (name.equals(node._name)) {
                node._prev._next = node._next;
                node._next._prev = node._prev;

                _size--;
                updateLastModified();
                return;
            }
            node = node._next;
        }
        throw new NoSuchModelNameException(name);
    }

    @Override
    public int getModelsSize() {
        return _size;
    }

    private void updateLastModified() {
        _lastModified = System.currentTimeMillis();
    }

    private boolean isModelExist(String name) {
        Model node = _head._next;

        while (node != _head) {
            if (name.equals(node._name)) {
                return true;
            }
            node = node._next;
        }
        return false;
    }

    @Override
    public String toString() {
        StringBuffer sb = new StringBuffer();

        sb.append("Motorcycle{mark='").append(_mark).append("', lastModified=").append(_lastModified).append(", size=").append(_size).append(", models=[");

        Model curr = _head._next;

        while (curr != _head) {
            if (curr._prev != _head) {
                sb.append(", ");
            }
            sb.append("{name='").append(curr.getName()).append("', price=").append(curr.getPrice()).append("}");
            curr = curr._next;
        }
        sb.append("]}");

        return sb.toString();
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }

        if (!(obj instanceof Motorcycle)) {
            return false;
        }
        Motorcycle other = (Motorcycle) obj;

        if (!_mark.equals(other._mark)) {
            return false;
        }

        if (_size != other._size) {
            return false;
        }
        Model curr = _head._next;
        Model currOther = other._head._next;

        while (curr != _head) {
            if (!curr.getName().equals(currOther.getName())) {
                return false;
            }

            if (Double.compare(curr.getPrice(), currOther.getPrice()) != 0) {
                return false;
            }
            curr = curr._next;
            currOther = currOther._next;
        }
        return true;
    }

    @Override
    public int hashCode() {
        int result = _mark.hashCode();
        Model curr = _head._next;

        while (curr != _head) {
            result = 31 * result + curr.getName().hashCode();
            result = 31 * result + Double.hashCode(curr.getPrice());

            curr = curr._next;
        }
        return result;
    }

    @Override
    public Motorcycle clone() {
        try {
            Motorcycle cloned = (Motorcycle) super.clone();

            cloned._head = new Model();
            cloned._head._prev = cloned._head;
            cloned._head._next = cloned._head;

            Model curr = _head._next;

            while (curr != _head) {
                cloned.addModelInternal(curr.getName(), curr.getPrice());
                curr = curr._next;
            }
            return cloned;
        } catch (CloneNotSupportedException e) {
            throw new AssertionError(e);
        }
    }

    private void addModelInternal(String name, double price) {
        Model newModel = new Model(name, price);

        newModel._next = _head;
        newModel._prev = _head._prev;

        _head._prev._next = newModel;
        _head._prev = newModel;
    }

    private static class Model implements java.io.Serializable {
        String _name;
        double _price;
        Model _prev;
        Model _next;

        public Model() { }

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