package randori.plugin.xml;

import com.intellij.util.xml.DomFileDescription;
import randori.plugin.xml.falcon.IFalconConfig;

/**
 * Created with IntelliJ IDEA.
 * User: Roland
 * Date: 3/12/13
 * Time: 3:38 PM
 * To change this template use File | Settings | File Templates.
 */
public class FalconConfigDomFileDescription extends DomFileDescription {

    public FalconConfigDomFileDescription() {
        super(IFalconConfig.class, "falcon-config");//"http://flex.apache.org/falcon"
    }
}