package pers.chris.common.plugin;

import java.util.List;
import java.util.Map;

public interface ResponseParsePluginable extends Pluginable {

    List<Map<String, String>> run (String response);

}
