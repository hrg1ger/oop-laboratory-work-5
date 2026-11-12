import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.Reader;
import java.lang.reflect.Method;

import transport.*;

public class Main {

    public static void main(String[] args) throws Exception {
        System.out.println("=================================================");
        System.out.println("=================== ЗАДАНИЕ 1 ===================");
        System.out.println("=================================================");

        String className = args[0];
        String methodName = args[1];
        String modelName = args[2];
        double newPrice = Double.parseDouble(args[3]);

        Class<?> clazz = Class.forName(className);

        Vehicle vehicle = (Vehicle) clazz
                .getConstructor(String.class, int.class)
                .newInstance("Toyota", 1);

        System.out.println("\nКласс: " + clazz.getName());

        System.out.println("\nДо вызова метода:");
        System.out.println("    " + vehicle);

        Method method = clazz.getMethod(methodName, String.class, double.class);
        method.invoke(vehicle, modelName, newPrice);

        System.out.println("\nПосле вызова " + methodName + "(\"" + modelName + "\", " + newPrice + "):");
        System.out.println("    " + vehicle);

        System.out.println("\n=================================================");
        System.out.println("=================== ЗАДАНИЕ 2 ===================");
        System.out.println("=================================================");

        Car car = new Car("Toyota", 3);
        Vehicle createdCar  = VehicleUtils.createVehicle("Lexus", 2, car);

        System.out.println("\nКласс: transport.Car");
        System.out.println("Исходный:");
        System.out.println("    " + car);
        System.out.println("Созданный:");
        System.out.println("    " + createdCar);

        Motorcycle moto = new Motorcycle("Yamaha", 3);
        Vehicle createdMoto = VehicleUtils.createVehicle("Honda", 2, moto);

        System.out.println("\nКласс: transport.Motorcycle");
        System.out.println("Исходный:");
        System.out.println("    " + moto);
        System.out.println("Созданный:");
        System.out.println("    " + createdMoto);

        System.out.println("\n=================================================");
        System.out.println("================== ЗАДАНИЕ 3-5 ==================");
        System.out.println("=================================================");

        Scooter scooter = new Scooter("Honda", 3);

        System.out.println("\nКласс: transport.Scooter:");
        System.out.println("    " + scooter);

        Quadrocyle quadrocyle = new Quadrocyle("Yamaha", 3);

        System.out.println("\nКласс: transport.Quadrocyle:");
        System.out.println("    " + quadrocyle);

        Moped moped = new Moped("Suzuki", 3);

        System.out.println("\nКласс: transport.Moped:");
        System.out.println("    " + moped);

        System.out.println("\n=================================================");
        System.out.println("=================== ЗАДАНИЕ 6 ===================");
        System.out.println("=================================================");

        System.out.println("\ncar:");
        System.out.println("    " + car);

        System.out.println("\nmoto:");
        System.out.println("    " + moto);

        System.out.println("\nscooter:");
        System.out.println("    " + scooter);

        System.out.println("\nquadrocyle:");
        System.out.println("    " + quadrocyle);

        System.out.println("\nmoped:");
        System.out.println("    " + moped);

        System.out.println("\nVehicleUtils.getAveragePrice(car, moto, scooter) = " + VehicleUtils.getAveragePrice(car, moto, scooter));
        System.out.println("VehicleUtils.getAveragePrice(new Vehicle[] { car, moto, scooter }) = " + VehicleUtils.getAveragePrice(new Vehicle[] { car, moto, scooter }));
        System.out.println("VehicleUtils.getAveragePrice() = " + VehicleUtils.getAveragePrice());

        System.out.println("\n=================================================");
        System.out.println("=================== ЗАДАНИЕ 7 ===================");
        System.out.println("=================================================");

        System.out.println("\nВведите транспортное средство:\n");

        try {
            Reader reader = new InputStreamReader(System.in);
            Vehicle v = VehicleUtils.readVehicle(reader);

            VehicleUtils.writeVehicle(v, new OutputStreamWriter(System.out));
        } catch (IOException e) {
            System.err.println(e.getMessage());
        }
    }
}