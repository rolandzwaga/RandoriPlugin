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

import javax.swing.Icon;

import org.jetbrains.annotations.NotNull;

import com.intellij.execution.configurations.ConfigurationFactory;
import com.intellij.execution.configurations.ConfigurationType;
import com.intellij.execution.configurations.RunConfiguration;
import com.intellij.openapi.components.ApplicationComponent;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.util.IconLoader;

/**
 * @author Michael Schmalle
 */
public class RandoriRunnerConfigurationType implements ConfigurationType,
        ApplicationComponent
{

    private ConfigurationFactory myFactory;

    public RandoriRunnerConfigurationType()
    {
        myFactory = new ConfigurationFactory(this) {

            @Override
            public RunConfiguration createTemplateConfiguration(Project project)
            {
                final RandoriRunConfiguration configuration = new RandoriRunConfiguration(
                        project, this, "");
                return configuration;
            }

            @Override
            public RunConfiguration createConfiguration(String name,
                    RunConfiguration template)
            {
                final RandoriRunConfiguration pluginRunConfiguration = (RandoriRunConfiguration) template;
                // check settings ?
                return super.createConfiguration(name, template);
            }
        };
    }

    public String getDisplayName()
    {
        return "Randori Runner";
    }

    public String getConfigurationTypeDescription()
    {
        return "Runs a randori application using the Jetty server";
    }

    public Icon getIcon()
    {
        return IconLoader.getIcon("icons/randori.png");
    }

    @NotNull
    public String getId()
    {
        return "#randori.plugin.runner.RandoriRunnerConfigurationType";
    }

    @Override
    public ConfigurationFactory[] getConfigurationFactories()
    {
        return new ConfigurationFactory[] { myFactory };
    }

    @Override
    public void initComponent()
    {
    }

    @Override
    public void disposeComponent()
    {
    }

    @Override
    public String getComponentName()
    {
        return "RandoriRunnerConfigurationType";
    }
}
