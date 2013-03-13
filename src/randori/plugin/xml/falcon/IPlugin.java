package randori.plugin.xml.falcon;

import com.intellij.util.xml.DomElement;
import com.intellij.util.xml.GenericAttributeValue;

/**
 * Created with IntelliJ IDEA.
 * User: Roland
 * Date: 3/12/13
 * Time: 6:25 PM
 * To change this template use File | Settings | File Templates.
 */
public interface IPlugin extends DomElement {
    GenericAttributeValue<String> getRef();
    void setRef(GenericAttributeValue<String> value);
}
