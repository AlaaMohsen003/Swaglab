package utils;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
public class DataDriven {

    public static JsonObject jsonReader() {

        try (Reader reader = new FileReader(
                "src/test/resources/testData.json")) {

            return JsonParser.parseReader(reader)
                    .getAsJsonObject();

        } catch (IOException e) {
            throw new RuntimeException(
                    "Could not read JSON file", e);
        }
    }
}
