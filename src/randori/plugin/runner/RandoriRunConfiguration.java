package randori.plugin.runner;

import java.util.Arrays;
import java.util.Collection;

import org.jdom.Element;
import org.jetbrains.annotations.NotNull;

import randori.plugin.components.RandoriProjectComponent;

import com.intellij.execution.ExecutionException;
import com.intellij.execution.ExecutionResult;
import com.intellij.execution.Executor;
import com.intellij.execution.configurations.ConfigurationPerRunnerSettings;
import com.intellij.execution.configurations.ModuleBasedConfiguration;
import com.intellij.execution.configurations.RunConfiguration;
import com.intellij.execution.configurations.RunProfileState;
import com.intellij.execution.configurations.RunnerSettings;
import com.intellij.execution.configurations.RuntimeConfigurationException;
import com.intellij.execution.runners.ExecutionEnvironment;
import com.intellij.execution.runners.ProgramRunner;
import com.intellij.ide.BrowserUtil;
import com.intellij.openapi.components.PathMacroManager;
import com.intellij.openapi.module.Module;
import com.intellij.openapi.module.ModuleManager;
import com.intellij.openapi.options.SettingsEditor;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.util.InvalidDataException;
import com.intellij.openapi.util.WriteExternalException;
import com.intellij.util.xmlb.XmlSerializer;
import com.intellij.util.xmlb.annotations.Transient;

@SuppressWarnings({ "rawtypes" })
public class RandoriRunConfiguration extends
        ModuleBasedConfiguration<RandoriApplicationModuleBasedConfiguration>
{

    public String indexRoot;

    private ExecutionEnvironment myEnvironment;

    public RandoriRunConfiguration(String name, Project project,
            RandoriRunnerConfigurationType configurationType)
    {
        super(name, new RandoriApplicationModuleBasedConfiguration(project),
                configurationType.getConfigurationFactories()[0]);
    }

    @Override
    public Collection<Module> getValidModules()
    {
        Module[] modules = ModuleManager.getInstance(getProject()).getModules();
        return Arrays.asList(modules);
    }

    @Override
    protected ModuleBasedConfiguration createInstance()
    {
        return new RandoriRunConfiguration(getName(), getProject(),
                RandoriRunnerConfigurationType.getInstance());
    }

    @Override
    @Transient
    public void setModule(Module module)
    {
        super.setModule(module);
    }

    @Override
    public void checkConfiguration() throws RuntimeConfigurationException
    {
        super.checkConfiguration();

        if (indexRoot == null)
            throw new RuntimeConfigurationException("An index is required");

        if (getModule() == null)
            throw new RuntimeConfigurationException("A module is required");
    }

    public SettingsEditor<? extends RunConfiguration> getConfigurationEditor()
    {
        return new RandoriRunConfigurationEditor(getProject());
    }

    public void readExternal(final Element element) throws InvalidDataException
    {
        PathMacroManager.getInstance(getProject()).expandPaths(element);
        XmlSerializer.deserializeInto(this, element);
        readModule(element);
    }

    public void writeExternal(final Element element)
            throws WriteExternalException
    {
        super.writeExternal(element);
        XmlSerializer.serializeInto(this, element);
        writeModule(element);
        PathMacroManager.getInstance(getProject()).collapsePathsRecursively(
                element);
    }

    public RunProfileState getState(@NotNull Executor executor,
            @NotNull ExecutionEnvironment environment)
            throws ExecutionException
    {
        //        return new RandoriCommandLineState(new RandoriConsoleProperties(this,
        //                executor), env);
        myEnvironment = environment;

        RunProfileState state = new MyRunProfileState();
        //        final JavaCommandLineState state = new JavaCommandLineState(environment) {
        //            @Override
        //            protected JavaParameters createJavaParameters() throws ExecutionException
        //            {
        //                return null;
        //            }
        //            @Override
        //            public ExecutionResult execute(Executor executor,
        //                    ProgramRunner runner) throws ExecutionException
        //            {
        //                // TODO Auto-generated method stub
        //                return super.execute(executor, runner);
        //            }
        //        };
        //        state.setConsoleBuilder(TextConsoleBuilderFactory.getInstance().createBuilder(getProject()));
        return state;
    }

    public Module getModule()
    {
        return getConfigurationModule().getModule();
    }

    @Override
    public boolean isGeneratedName()
    {
        return true;
    }

    @Override
    public String suggestedName()
    {
        String name = getName();
        int pos = name.lastIndexOf('.');
        if (pos == -1)
        {
            return name;
        }
        return name.substring(pos + 1);
    }

    //--------------------------------------------------------------------------

    public class MyRunProfileState implements RunProfileState
    {
        @Override
        public ExecutionResult execute(Executor arg0, ProgramRunner arg1)
                throws ExecutionException
        {
            String url = getURL();
            // temp, will hook up properly, can create a config that says
            // something like preview in browser checkbox
            BrowserUtil.launchBrowser(url);

            //            ExecutionResult result = new ExecutionResult() {
            //
            //                @Override
            //                public ProcessHandler getProcessHandler()
            //                {
            //                    // TODO Auto-generated method stub
            //                    return null;
            //                }
            //
            //                @Override
            //                public ExecutionConsole getExecutionConsole()
            //                {
            //                    // TODO Auto-generated method stub
            //                    return null;
            //                }
            //
            //                @Override
            //                public AnAction[] getActions()
            //                {
            //                    // TODO Auto-generated method stub
            //                    return null;
            //                }
            //            };
            return null;
        }

        @Override
        public ConfigurationPerRunnerSettings getConfigurationSettings()
        {
            return myEnvironment.getConfigurationSettings();
        }

        @Override
        public RunnerSettings getRunnerSettings()
        {
            return myEnvironment.getRunnerSettings();
        }
    }

    public String getURL()
    {
        RandoriProjectComponent component = getProject().getComponent(
                RandoriProjectComponent.class);
        int port = component.getState().getPort();
        String url = "http://localhost:" + port + "/" + indexRoot;
        return url;
    }
}
