package randori.plugin.xml.falcon;

import com.intellij.util.xml.DomElement;

import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: Roland
 * Date: 3/12/13
 * Time: 3:54 PM
 * To change this template use File | Settings | File Templates.
 */
public interface IFalconConfig extends DomElement {
    List<IPluginJar> getPluginJars();
    List<IRunConfig> getRunConfigs();
}