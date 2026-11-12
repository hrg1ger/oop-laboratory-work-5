package transport;

import java.io.*;
import java.lang.reflect.Constructor;
import java.util.Scanner;

import exceptions.DuplicateModelNameException;
import exceptions.NoSuchModelNameException;

public class VehicleUtils {
    public static double getAveragePrice(Vehicle vehicle) {
        if (vehicle == null) {
            return 0.0;
        }
        double[] prices = vehicle.getModelPrices();

        if (prices.length == 0) {
            return 0.0;
        }
        double sum = 0.0;

        for (double price : prices) {
            sum += price;
        }
        return sum / prices.length;
    }

    public static double getAveragePrice(Vehicle... vehicles) throws NoSuchModelNameException {
        if (vehicles == null || vehicles.length == 0) {
            return Double.NaN;
        }
        double sum = 0;
        int count = 0;

        for (Vehicle v : vehicles) {
            if (v == null) {
                continue;
            }

            for (String name : v.getModelNames()) {
                sum += v.getPriceByName(name);
                count++;
            }
        }
        return count == 0 ? 0 : sum / count;
    }

    public static void printAllNames(Vehicle vehicle) {
        if (vehicle == null) {
            System.out.println("Транспортное средство не задано");
            return;
        }
        System.out.println("Модели марки " + vehicle.getMark() + ":");
        String[] names = vehicle.getModelNames();

        if (names.length == 0) {
            System.out.println("[Список пуст]");
            return;
        }

        for (String name : names) {
            System.out.println("- " + name);
        }
    }

    public static void printAllPrices(Vehicle vehicle) {
        if (vehicle == null) {
            System.out.println("Транспортное средство не задано");
            return;
        }
        System.out.println("Цены моделей марки " + vehicle.getMark() + ":");
        double[] prices = vehicle.getModelPrices();

        if (prices.length == 0) {
            System.out.println("[Список пуст]");
            return;
        }

        for (double price : prices) {
            System.out.println("- " + price + " руб");
        }
    }

    public static void outputVehicle(Vehicle v, OutputStream out) throws IOException {
        DataOutputStream dos = new DataOutputStream(out);

        byte[] typeBytes = v.getClass().getSimpleName().getBytes();
        dos.writeInt(typeBytes.length);
        dos.write(typeBytes);

        byte[] markBytes = v.getMark().getBytes();
        dos.writeInt(markBytes.length);
        dos.write(markBytes);

        String[] names  = v.getModelNames();
        double[] prices = v.getModelPrices();
        dos.writeInt(names.length);

        for (int i = 0; i < names.length; i++) {
            byte[] nameBytes = names[i].getBytes();
            dos.writeInt(nameBytes.length);
            dos.write(nameBytes);
            dos.writeDouble(prices[i]);
        }
        dos.flush();
    }

    public static Vehicle inputVehicle(InputStream in) throws IOException {
        DataInputStream dis = new DataInputStream(in);

        String type = readString(dis);
        String mark = readString(dis);
        int count = dis.readInt();
        Vehicle v;

        if ("Car".equals(type)) {
            v = new Car(mark, 0);
        }
        else if("Motorcycle".equals(type)) {
            v = new Motorcycle(mark, 0);
        }
        else {
            throw new IOException("Неизвестный тип ТС");
        }

        for (int i = 0; i < count; i++) {
            String name  = readString(dis);
            double price = dis.readDouble();

            try {
                v.addModel(name, price);
            } catch (DuplicateModelNameException e) {
                throw new IOException("Дубликат модели при чтении: " + name, e);
            }
        }
        return v;
    }

    public static void writeVehicle(Vehicle v, Writer out) {
        PrintWriter pw = new PrintWriter(out);

        pw.printf("%s%n", v.getClass().getSimpleName());
        pw.printf("%s%n", v.getMark());

        String[] names  = v.getModelNames();
        double[] prices = v.getModelPrices();

        pw.printf("%d%n", names.length);

        for (int i = 0; i < names.length; i++) {
            pw.printf("%s %.2f%n", names[i], prices[i]);
        }
        pw.flush();
    }

    public static Vehicle readVehicle(Reader in) throws IOException {
        Scanner scanner = new Scanner(in);

        if (!scanner.hasNextLine()) {
            throw new IOException("Неожиданный конец потока: нет типа ТС");
        }
        String type = scanner.nextLine().trim();

        if (!scanner.hasNextLine()) {
            throw new IOException("Неожиданный конец потока: нет марки");
        }
        String mark = scanner.nextLine().trim();

        if (!scanner.hasNextLine()) {
            throw new IOException("Неожиданный конец потока: нет количества моделей");
        }
        int count;

        try {
            count = Integer.parseInt(scanner.nextLine().trim());
        } catch (NumberFormatException e) {
            throw new IOException("Некорректное количество моделей", e);
        }
        Vehicle v;

        if ("Car".equals(type)) {
            v = new Car(mark, 0);
        } else if ("Motorcycle".equals(type)) {
            v = new Motorcycle(mark, 0);
        } else {
            throw new IOException("Неизвестный тип ТС");
        }

        for (int i = 0; i < count; i++) {
            if (!scanner.hasNextLine()) {
                throw new IOException("Неожиданный конец потока при чтении модели " + i);
            }
            String line = scanner.nextLine().trim();
            int space = line.lastIndexOf(' ');

            if (space == -1) {
                throw new IOException("Некорректная строка модели: " + line);
            }
            String name  = line.substring(0, space);
            double price;

            try {
                price = Double.parseDouble(line.substring(space + 1).trim());
            } catch (NumberFormatException e) {
                throw new IOException("Некорректная цена модели: " + line, e);
            }

            try {
                v.addModel(name, price);
            } catch (DuplicateModelNameException e) {
                throw new IOException("Дубликат модели при чтении: " + name, e);
            }
        }
        return v;
    }

    private static String readString(DataInputStream dis) throws IOException {
        int len = dis.readInt();
        byte[] buf = new byte[len];
        dis.readFully(buf);

        return new String(buf);
    }

    public static Vehicle createVehicle(String mark, int size, Vehicle sample) {
        Class<?> clazz = sample.getClass();
        Constructor<?> constructor;

        try {
            constructor = clazz.getConstructor(String.class, int.class);
        } catch (NoSuchMethodException e) {
            return null;
        }
        Vehicle vehicle;

        try {
            vehicle = (Vehicle) constructor.newInstance(mark, size);
        } catch (InstantiationException
                 | IllegalAccessException
                 | IllegalArgumentException
                 | java.lang.reflect.InvocationTargetException e) {
            return null;
        }

        return vehicle;
    }
}