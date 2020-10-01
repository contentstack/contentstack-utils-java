import org.json.JSONArray;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

public class ResourceFile {

    public JSONArray readJson(String filename) throws IOException {
        File file = new File(filename);
        String absolutePath = file.getAbsolutePath();
        System.out.println(absolutePath);
        BufferedReader reader = new BufferedReader(new FileReader(file));
        StringBuilder stringBuilder = new StringBuilder();
        char[] buffer = new char[10];
        while (reader.read(buffer) != -1) {
            stringBuilder.append(new String(buffer));
            buffer = new char[10];
        }
        reader.close();
        String response = stringBuilder.toString();
        return new JSONArray(response);
    }

}
