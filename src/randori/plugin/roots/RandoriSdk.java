/***
 * Copyright 2013 Teoti Graphix, LLC.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 *
 * @author Michael Schmalle <mschmalle@teotigraphix.com>
 */

package randori.plugin.roots;

import com.intellij.openapi.projectRoots.*;
import com.intellij.openapi.roots.JavadocOrderRootType;
import com.intellij.openapi.roots.OrderRootType;
import com.intellij.openapi.vfs.JarFileSystem;
import com.intellij.openapi.vfs.VfsUtil;
import com.intellij.openapi.vfs.VirtualFile;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;
import randori.plugin.module.RandoriModuleType;

import javax.swing.*;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.File;
import java.io.IOException;

/**
 * @author Michael Schmalle
 */
public class RandoriSdk extends SdkType
{
    public RandoriSdk()
    {
        super("Randori SDK");
    }

    public static SdkType getInstance()
    {
        return findInstance(RandoriSdk.class);
    }

    // called by SdkType.setupSdkPaths()
    @Override
    public void setupSdkPaths(Sdk sdk)
    {
        // right now this is a ProjectJdkImpl, we must need to use a provicder for our own impl?
        // or is this a generic project sdk class?
        // sdkHomedirectory is what the user selected in the file chooser, the root
        VirtualFile sdkRoot = sdk.getHomeDirectory();

        SdkModificator modificator = sdk.getSdkModificator();

        VirtualFile global = sdkRoot.findFileByRelativePath("bin/builtin.swc");
        VirtualFile randori = sdkRoot.findFileByRelativePath("bin/Randori.swc");
        VirtualFile guice = sdkRoot
                .findFileByRelativePath("bin/RandoriGuiceJS.swc");

        addSWC(modificator, global);
        addSWC(modificator, randori);
        addSWC(modificator, guice);

        modificator.commitChanges();

        @SuppressWarnings("unused")
        String sdkVersion = getVersion(sdkRoot);
        if (sdkRoot != null && sdkRoot.isValid())
            return;

    }

    private void addSWC(SdkModificator modificator, VirtualFile swc)
    {
        final VirtualFile jarRoot = JarFileSystem.getInstance()
                .getJarRootForLocalFile(swc);
        if (jarRoot != null)
        {
            modificator.addRoot(jarRoot, OrderRootType.CLASSES);
        }
    }

    private String getVersion(VirtualFile sdkRoot)
    {
        VirtualFile versionFile = sdkRoot.findFileByRelativePath("sdk.xml");
        if (versionFile.exists())
        {
            File fXmlFile = new File(versionFile.getPath());
            DocumentBuilderFactory dbFactory = DocumentBuilderFactory
                    .newInstance();

            try
            {
                DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
                Document doc = dBuilder.parse(fXmlFile);
                Element element = doc.getDocumentElement();
                NodeList version = element.getElementsByTagName("version");
                String versionId = version.item(0).getTextContent();
                return versionId;
            }
            catch (SAXException e)
            {
                e.printStackTrace();
            }
            catch (IOException e)
            {
                e.printStackTrace();
            }
            catch (ParserConfigurationException e)
            {
                e.printStackTrace();
            }

        }
        return "0.0.0";
    }

    @Override
    public String suggestHomePath()
    {
        return null;
    }

    // called when the directory is selected when creating a new SDK
    // 'path' is the selected directory from the file chooser
    @Override
    public boolean isValidSdkHome(String path)
    {
        return getVersionString(path) != null;
    }

    @Override
    public String suggestSdkName(String currentSdkName, String sdkHome)
    {
        return "Randori SDK " + getVersionString(sdkHome);
    }

    @Override
    public AdditionalDataConfigurable createAdditionalDataConfigurable(
            SdkModel sdkModel, SdkModificator sdkModificator)
    {
        return null;
    }

    @Override
    public void saveAdditionalData(SdkAdditionalData additionalData,
            org.jdom.Element additional)
    {
    }

    @Override
    public Icon getIconForAddAction()
    {
        return RandoriModuleType.RANDORI_ICON_SMALL;
    }

    @Override
    public Icon getIcon()
    {
        return RandoriModuleType.RANDORI_ICON_SMALL;
    }

    @Override
    public String getVersionString(String sdkHome)
    {
        // need to get the version from the XML file at the root of the SDK
        VirtualFile sdkRoot = sdkHome != null ? VfsUtil.findRelativeFile(
                sdkHome, null) : null;
        return getVersion(sdkRoot);
    }

    @Override
    public String getPresentableName()
    {
        return "Randori SDK";
    }

    @Override
    public boolean isRootTypeApplicable(OrderRootType type)
    {
        // called after an sdk has been choosen and when a project is starting up OR the IDE is starting with ProjectStructure
        return type == OrderRootType.CLASSES || type == OrderRootType.SOURCES
                || type == JavadocOrderRootType.getInstance();
    }

}
