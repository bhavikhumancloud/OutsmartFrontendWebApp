package utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CsvUtils {

    public static List<Map<String,String>> readCsvFromClasspath(String resourcePath) throws IOException {
        List<Map<String,String>> rows = new ArrayList<>();
        try (InputStream in = CsvUtils.class.getClassLoader().getResourceAsStream(resourcePath)) {
            if (in == null) {
                throw new IOException("CSV resource not found: " + resourcePath);
            }
            try (BufferedReader br = new BufferedReader(new InputStreamReader(in))) {
                String headerLine = br.readLine();
                if (headerLine == null) return rows;
                String[] headers = headerLine.split(",");
                String line;
                while ((line = br.readLine()) != null) {
                    String[] cols = line.split(",", -1);
                    Map<String,String> map = new HashMap<>();
                    for (int i = 0; i < headers.length && i < cols.length; i++) {
                        map.put(headers[i].trim(), cols[i].trim());
                    }
                    rows.add(map);
                }
            }
        }
        return rows;
    }
}

