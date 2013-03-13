package randori.plugin.runner;

import com.intellij.execution.configurations.RunConfigurationModule;
import com.intellij.openapi.project.Project;

/**
 * @author Michael Schmalle
 */
public class RandoriApplicationModuleBasedConfiguration extends
        RunConfigurationModule
{

    public RandoriApplicationModuleBasedConfiguration(Project project)
    {
        super(project);
    }
}
