package randori.plugin.xml.falcon;

import com.intellij.util.xml.DomElement;
import com.intellij.util.xml.GenericAttributeValue;

/**
 * Created with IntelliJ IDEA.
 * User: Roland
 * Date: 3/12/13
 * Time: 3:56 PM
 * To change this template use File | Settings | File Templates.
 */
public interface IPluginJar extends DomElement {
    GenericAttributeValue<String> getPath();
    void setPath(GenericAttributeValue<String> value);
    GenericAttributeValue<String> getId();
    void setId(GenericAttributeValue<String> value);
}

