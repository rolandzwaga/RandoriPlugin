package randori.plugin.runner;

import org.jetbrains.annotations.Nullable;

import com.intellij.execution.ExecutionException;
import com.intellij.execution.ExecutionResult;
import com.intellij.execution.Executor;
import com.intellij.execution.configurations.ConfigurationFactory;
import com.intellij.execution.configurations.ConfigurationInfoProvider;
import com.intellij.execution.configurations.ConfigurationPerRunnerSettings;
import com.intellij.execution.configurations.ModuleRunConfiguration;
import com.intellij.execution.configurations.RunConfiguration;
import com.intellij.execution.configurations.RunConfigurationBase;
import com.intellij.execution.configurations.RunProfileState;
import com.intellij.execution.configurations.RunnerSettings;
import com.intellij.execution.configurations.RuntimeConfigurationException;
import com.intellij.execution.runners.ExecutionEnvironment;
import com.intellij.execution.runners.ProgramRunner;
import com.intellij.ide.BrowserUtil;
import com.intellij.openapi.module.Module;
import com.intellij.openapi.module.ModuleManager;
import com.intellij.openapi.options.SettingsEditor;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.util.JDOMExternalizable;

@SuppressWarnings({ "deprecation", "rawtypes" })
public class RandoriRunConfiguration extends RunConfigurationBase implements
        ModuleRunConfiguration
{

    private ExecutionEnvironment myEnvironment;

    private Module myModule;

    private String myModuleName;

    public RandoriRunConfiguration(Project project,
            ConfigurationFactory factory, String name)
    {
        super(project, factory, name);
    }

    @Override
    public SettingsEditor<? extends RunConfiguration> getConfigurationEditor()
    {
        // called when clicking on a default run config for Randori App
        // the default form just contains the "Make" options
        return new RandoriRunConfigurationEditor(this);
    }

    @Override
    public JDOMExternalizable createRunnerSettings(
            ConfigurationInfoProvider arg0)
    {
        return null;
    }

    @Override
    public SettingsEditor<JDOMExternalizable> getRunnerSettingsEditor(
            ProgramRunner arg0)
    {
        return null;
    }

    @Override
    public RunProfileState getState(Executor arg0,
            ExecutionEnvironment environment) throws ExecutionException
    {
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

    @Override
    public void checkConfiguration() throws RuntimeConfigurationException
    {
        // TODO Auto-generated method stub

    }

    @Override
    public Module[] getModules()
    {
        final Module module = getModule();
        return module != null ? new Module[] { module } : Module.EMPTY_ARRAY;
    }

    @Nullable
    public Module getModule()
    {
        if (myModule == null && myModuleName != null)
        {
            myModule = ModuleManager.getInstance(getProject())
                    .findModuleByName(myModuleName);
        }
        if (myModule != null && myModule.isDisposed())
        {
            myModule = null;
        }

        return myModule;
    }

    public void setModule(Module module)
    {
        myModule = module;
    }

    public class MyRunProfileState implements RunProfileState
    {
        @Override
        public ExecutionResult execute(Executor arg0, ProgramRunner arg1)
                throws ExecutionException
        {
            // temp, will hook up properly, can create a config that says
            // something like preview in browser checkbox
            BrowserUtil.launchBrowser("http://localhost:8080/");

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

}
