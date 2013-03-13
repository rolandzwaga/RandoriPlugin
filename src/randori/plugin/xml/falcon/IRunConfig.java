package randori.plugin.xml.falcon;

import com.intellij.util.xml.DomElement;
import com.intellij.util.xml.GenericAttributeValue;
import com.intellij.util.xml.GenericDomValue;
import com.intellij.util.xml.TagValue;

import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: Roland
 * Date: 3/12/13
 * Time: 3:57 PM
 * To change this template use File | Settings | File Templates.
 */
public interface IRunConfig extends DomElement {
    GenericAttributeValue<String> getId();
    void setId(GenericAttributeValue<String> value);
    GenericDomValue<String> getDescription();
    void setDescription(GenericDomValue<String> value);
    List<IPlugin> getPlugins();
}

