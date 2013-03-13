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

package randori.plugin.components;

import javax.swing.JComponent;

import org.apache.flex.compiler.problems.ICompilerProblem;
import org.jetbrains.annotations.Nls;
import org.jetbrains.annotations.NotNull;

import randori.plugin.execution.CompilerArguments;
import randori.plugin.forms.RandoriProjectConfigurationForm;
import randori.plugin.utils.ProjectUtils;
import randori.plugin.utils.VFileUtils;

import com.intellij.openapi.components.PersistentStateComponent;
import com.intellij.openapi.components.ProjectComponent;
import com.intellij.openapi.components.State;
import com.intellij.openapi.components.Storage;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.editor.LogicalPosition;
import com.intellij.openapi.fileEditor.FileEditorManager;
import com.intellij.openapi.fileEditor.OpenFileDescriptor;
import com.intellij.openapi.module.Module;
import com.intellij.openapi.module.ModuleManager;
import com.intellij.openapi.options.Configurable;
import com.intellij.openapi.options.ConfigurationException;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.roots.ModuleRootManager;
import com.intellij.openapi.vfs.VirtualFile;

/**
 * Autopackage module adds new module tab and holds plugin configuration.
 */
@State(name = RandoriProjectComponent.COMPONENT_NAME, storages = { @Storage(id = "randoriproject", file = "$PROJECT_FILE$") })
// TODO Need to get the state out of this component and into a Model class
/**
 * @author Michael Schmalle
 */
public class RandoriProjectComponent implements ProjectComponent, Configurable,
        PersistentStateComponent<RandoriProjectModel>
{
    public static final String COMPONENT_NAME = "RandoriProject";

    private Project project;

    private RandoriProjectConfigurationForm form;

    private RandoriProjectModel model;

    public RandoriProjectModel getModel()
    {
        return model;
    }

    public Project getProject()
    {
        return project;
    }

    public RandoriProjectComponent(Project project)
    {
        this.project = project;
        this.model = new RandoriProjectModel();
    }

    public void reparse(VirtualFile file)
    {
        // parses a file and its dependencies
        // TEMP
        parse();
    }

    public void parse()
    {
        RandoriApplicationComponent applicationComponent = ProjectUtils
                .getApplicationComponent();
        RandoriProjectComponent projectComponent = ProjectUtils
                .getProjectComponent(project);

        CompilerArguments arguments = applicationComponent
                .getCompilerArguments();
        projectComponent.configureDependencies(project, arguments);

        applicationComponent.getBuildSourceCommand().parse(project, arguments);
    }

    /**
     * Builds the current Project by doing a full parse and output render.
     * 
     * @param doClean
     */
    public void build(boolean doClean)
    {
        RandoriApplicationComponent applicationComponent = ProjectUtils
                .getApplicationComponent();
        RandoriProjectComponent projectComponent = ProjectUtils
                .getProjectComponent(project);

        CompilerArguments arguments = applicationComponent
                .getCompilerArguments();
        projectComponent.configureDependencies(project, arguments);

        applicationComponent.getBuildSourceCommand().build(project, doClean,
                arguments);
    }

    /**
     * Opens a ICompilerProblem in a new editor, or opens the editor and places
     * the caret a the specific proplem.
     * 
     * @param problem The ICompilerProblem to focus.
     */
    public void openFileForProblem(ICompilerProblem problem)
    {
        VirtualFile virtualFile = VFileUtils.getFile(problem.getSourcePath());
        OpenFileDescriptor descriptor = new OpenFileDescriptor(project,
                virtualFile);
        Editor editor = FileEditorManager.getInstance(project).openTextEditor(
                descriptor, true);
        LogicalPosition position = new LogicalPosition(problem.getLine(),
                problem.getColumn());
        editor.getCaretModel().moveToLogicalPosition(position);
    }

    @Override
    public void projectOpened()
    {
        //ProjectRootManager projectRootManager = ProjectRootManager
        //        .getInstance(project);
        //VirtualFile[] sourceRoots = projectRootManager.getContentSourceRoots();
    }

    @Override
    public void projectClosed()
    {
    }

    @Override
    public void initComponent()
    {
    }

    @Override
    public void disposeComponent()
    {
    }

    @NotNull
    @Override
    public String getComponentName()
    {
        return COMPONENT_NAME;
    }

    @Nls
    @Override
    public String getDisplayName()
    {
        return "Randori";
    }

    @Override
    public String getHelpTopic()
    {
        return null;
    }

    @Override
    public RandoriProjectModel getState()
    {
        return model;
    }

    @Override
    public void loadState(RandoriProjectModel state)
    {
        //setBasePath(state.getBasePath());
        //setLibraryPath(state.getLibraryPath());
        //setClassesAsFile(state.isClassesAsFile());
    }

    @Override
    public JComponent createComponent()
    {
        if (form == null)
        {
            form = new RandoriProjectConfigurationForm();
        }
        return form.getComponent();
    }

    @Override
    public boolean isModified()
    {
        return form.isModified(getState());
    }

    @Override
    public void apply() throws ConfigurationException
    {
        if (form != null)
        {
            form.getData(getState());
        }
    }

    //------------------------------------------------------
    // TODO get the below in a Model

    @Override
    public void reset()
    {
        if (form != null)
        {
            form.setData(getState());
        }
    }

    @Override
    public void disposeUIResources()
    {
    }

    public void configureDependencies(Project project,
            CompilerArguments arguments)
    {
        arguments.clear();

        arguments.configure(project, getModel());

        if (ProjectUtils.isSDKInstalled(project))
        {
            arguments.addLibraryPath(ProjectUtils.getPlayerGloablPath(project));
        }
        else
        {
            // TODO throw Exception()
        }

        for (String library : ProjectUtils.getAllProjectSWCs(project))
        {
            arguments.addLibraryPath(library);
        }

        for (String library : ProjectUtils.getAllProjectSourcePaths(project))
        {
            arguments.addSourcepath(library);
        }

        Module[] modules = ModuleManager.getInstance(project).getModules();
        for (Module module : modules)
        {
            // RandoriFlash/src
            for (VirtualFile virtualFile : ModuleRootManager
                    .getInstance(module).getSourceRoots())
            {
                arguments.addSourcepath(virtualFile.getPath());
            }
        }
    }

}
