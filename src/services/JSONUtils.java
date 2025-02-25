package services;

import com.google.gson.Gson;
//import com.google.gson.reflect.TypeToken;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.List;

public class JSONUtils {
    private static final Gson gson = new Gson();

    public static <T> List<T> readFromFile(String filePath, Type typeOfT) throws IOException {
        try (FileReader reader = new FileReader(filePath)) {
            return gson.fromJson(reader, typeOfT);
        }
    }

    public static <T> void writeToFile(String filePath, List<T> data) throws IOException {
        try (FileWriter writer = new FileWriter(filePath)) {
            gson.toJson(data, writer);
        }
    }
}
