package randori.plugin.runner;

import com.intellij.execution.ExecutionException;
import com.intellij.execution.Executor;
import com.intellij.execution.configurations.ConfigurationFactory;
import com.intellij.execution.configurations.ConfigurationInfoProvider;
import com.intellij.execution.configurations.ModuleRunConfiguration;
import com.intellij.execution.configurations.RunConfiguration;
import com.intellij.execution.configurations.RunConfigurationBase;
import com.intellij.execution.configurations.RunProfileState;
import com.intellij.execution.configurations.RuntimeConfigurationException;
import com.intellij.execution.runners.ExecutionEnvironment;
import com.intellij.execution.runners.ProgramRunner;
import com.intellij.openapi.module.Module;
import com.intellij.openapi.options.SettingsEditor;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.util.JDOMExternalizable;

public class RandoriRunConfiguration extends RunConfigurationBase implements
        ModuleRunConfiguration
{

    public RandoriRunConfiguration(Project project,
            ConfigurationFactory factory, String name)
    {
        super(project, factory, name);
    }

    @Override
    public JDOMExternalizable createRunnerSettings(
            ConfigurationInfoProvider arg0)
    {
        return null;
    }

    @Override
    public SettingsEditor<? extends RunConfiguration> getConfigurationEditor()
    {
        // called when clicking on a default run config for Randori App
        // the default form just contains the "Make" options
        return new RandoriRunConfigurationEditor(this); 
    }

    @Override
    public SettingsEditor<JDOMExternalizable> getRunnerSettingsEditor(
            ProgramRunner arg0)
    {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public void checkConfiguration() throws RuntimeConfigurationException
    {
        // TODO Auto-generated method stub

    }

    @Override
    public RunProfileState getState(Executor arg0, ExecutionEnvironment arg1)
            throws ExecutionException
    {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public Module[] getModules()
    {
        // TODO Auto-generated method stub
        return null;
    }

    
}
