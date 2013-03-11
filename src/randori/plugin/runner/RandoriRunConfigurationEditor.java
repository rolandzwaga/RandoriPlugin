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

package randori.plugin.runner;

import javax.swing.JComponent;
import javax.swing.JPanel;

import com.intellij.openapi.options.ConfigurationException;
import com.intellij.openapi.options.SettingsEditor;

/**
 * @author Michael Schmalle
 */
public class RandoriRunConfigurationEditor extends
        SettingsEditor<RandoriRunConfiguration>
{
    private JPanel panel;

    private final RandoriRunConfiguration configuration;

    public RandoriRunConfigurationEditor(RandoriRunConfiguration configuration)
    {
        this.configuration = configuration;
        // add ui listeners

        // TODO what is implements PanelWithAnchor ?
        // setAnchor(myModuleLabel); ???
    }

    public void setData(RandoriServerComponent data)
    {
        // set ui properties
    }

    public void getData(RandoriServerComponent data)
    {
        //data.setBaseURL(baseURL.getText());
    }

    public JComponent getRootComponent()
    {
        return panel;
    }

    public boolean isModified(RandoriServerComponent data)
    {
        // test the ui components based off the data
        return false;
    }

    @Override
    protected void applyEditorTo(RandoriRunConfiguration configuration)
            throws ConfigurationException
    {
        // apply the ui component values to the configuration

    }

    @Override
    protected JComponent createEditor()
    {
        if (panel == null)
            panel = new JPanel();
        return panel;
    }

    @Override
    protected void disposeEditor()
    {
    }

    @Override
    protected void resetEditorFrom(RandoriRunConfiguration arg0)
    {
        // TODO Auto-generated method stub
        // reset ui components with config data
    }
}
