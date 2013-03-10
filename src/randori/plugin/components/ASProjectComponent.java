package randori.plugin.components;

import com.intellij.openapi.components.ProjectComponent;
import com.intellij.openapi.project.Project;
import org.apache.flex.compiler.internal.projects.FlexProject;
import org.jetbrains.annotations.NotNull;

/**
 * @author Michael Schmalle
 */
public class ASProjectComponent implements ProjectComponent
{
    private final WorkspaceApplicationComponent workspace;

    private final Project project;

    private FlexProject asProject;

    public ASProjectComponent(Project project, WorkspaceApplicationComponent workspace)
    {
        this.project = project;
        this.workspace = workspace;
    }

    public FlexProject getASProject()
    {
        return asProject;
    }

    public void initComponent()
    {
        //asProject = new FlexProject(workspace.getWorkspace());
    }

    public void disposeComponent()
    {
        //workspace.getWorkspace().deleteProject(asProject);
    }

    @NotNull
    public String getComponentName()
    {
        return "ASProjectComponent";
    }

    public void projectOpened()
    {
        // called when project is opened
    }

    public void projectClosed()
    {
        // called when project is being closed
    }
}
