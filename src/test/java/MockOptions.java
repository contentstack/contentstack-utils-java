import com.contentstack.utils.embedded.StyleType;
import com.contentstack.utils.render.Options;
import org.json.JSONObject;

public class MockOptions implements Options {

    @Override
    public String renderOptions(StyleType type, JSONObject entryObject) {
        return null;
    }

}
