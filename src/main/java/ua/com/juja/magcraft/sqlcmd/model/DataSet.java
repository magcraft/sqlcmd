package ua.com.juja.magcraft.sqlcmd.model;

import java.util.Arrays;

public class DataSet {

    static class Data {
        private String name;

        private Object value;
        public Data(String name, Object value) {
            this.name = name;
            this.value = value;
        }

        public String getName() {
            return name;
        }

        public Object getValue() {
            return value;
        }
    }

    public Data[] data = new Data[100]; //Todo remove magic number
    public int freeIndex = 0;

    public void put(String name, Object value) {
        for (int index = 0; index < freeIndex; index++) {
            if (data[index].getName().equals(name)) {
                data[index].value = value;
                return;
            }
        }
        data[freeIndex++] = new Data(name, value);
    }

    public Object[] getValues() {
        Object[] result = new Object[freeIndex];
        for (int i = 0; i < freeIndex; i++) {
            result[i] = data[i].getValue();
        }
        return result;
    }

    public String[] getNames() {
        String[] result = new String[freeIndex];
        for (int i = 0; i < freeIndex; i++) {
            result[i] = data[i].getName();
        }
        return result;
    }

    public Object get(String name) {
        for (int i = 0; i < freeIndex; i++) {
            if (data[i].getName().equals(name)) {
                return data[i].getValue();
            }
        }
        return null;
    }

    // TODO: 24/05/2017 выяснить как мы тут нарушили инкапсуляцию? 
    public void updateFrom(DataSet input) {
        for (int index = 0; index < input.freeIndex; index++) {
            Data data = input.data[index];
            this.put(data.name, data.value);
        }
    }

//    //более ресорсоемкая реализация метода для примера
//    public void updateFrom(DataSet input) {
//        String[] names = input.getNames();
//        for (int index = 0; index < names.length; index++) {
//            String name = names[index];
//            Object value = input.get(name);
//            this.put(name, value);
//        }
//    }
//
//    //более ресорсоемкая реализация метода второй вариант для примера
//    public void updateFrom(DataSet input) {
//        String[] names = input.getNames();
//        Object[] values = input.getValues();
//        for (int index = 0; index < names.length; index++) {
//            String name = names[index];
//            Object value = values[index];
//            this.put(name, value);
//        }
//    }

    @Override
    public String toString() {
        return "{" +
                "names: " + Arrays.toString(getNames()) + ", " +
                "values: " + Arrays.toString(getValues()) +
        "}";
    }
}