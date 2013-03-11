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

package randori.plugin.execution;

import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.progress.ProgressIndicator;
import com.intellij.openapi.progress.ProgressManager;
import com.intellij.openapi.progress.Task;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.projectRoots.Sdk;
import com.intellij.openapi.roots.ProjectRootManager;
import com.intellij.openapi.util.io.FileUtil;
import com.intellij.openapi.vfs.VirtualFile;
import org.apache.flex.compiler.driver.IBackend;
import org.apache.flex.compiler.problems.ICompilerProblem;
import org.jetbrains.annotations.NotNull;
import randori.compiler.clients.Randori;
import randori.compiler.internal.driver.RandoriBackend;
import randori.plugin.components.RandoriProjectComponent;
import randori.plugin.ui.ProblemsToolWindow;
import randori.plugin.utils.NotificationUtils;
import randori.plugin.utils.ProjectUtils;
import randori.plugin.utils.SdkUtils;

import java.io.File;
import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

/**
 * @author Michael Schmalle
 */
public class BuildSourceCommand
{

    public void parse(final Project project, final CompilerArguments arguments)
    {
        //final String name = project.getName();

        ProgressManager.getInstance().run(
                new Task.Backgroundable(project,
                        "Randori compiler building project", true, null) {
                    @Override
                    public void run(@NotNull ProgressIndicator indicator)
                    {
                        final Set<ICompilerProblem> problems = new HashSet<ICompilerProblem>();
                        RandoriBackend backend = new RandoriBackend();
                        backend.parseOnly(true);
                        final Randori randori = new Randori(backend);

                        // need to only parse not generate
                        final int code = randori.mainNoExit(
                                arguments.toArguments(), problems);

                        if (code == 0)
                        {
                            // for now the chance we have SWC warnings
                            problems.clear();
                        }

                        ApplicationManager.getApplication().invokeLater(
                                new Runnable() {
                                    @Override
                                    public void run()
                                    {
                                        ProblemsToolWindow.refresh(problems);
                                    }
                                });
                    }
                });
    }

    public void build(final Project project, boolean doClean,
            final CompilerArguments arguments)
    {
        final String name = project.getName();
        //final VirtualFile file = project.getBaseDir();

        if (doClean)
            clean(project);

        ProgressManager.getInstance().run(
                new Task.Backgroundable(project,
                        "Randori compiler building project", true, null) {
                    @Override
                    public void run(@NotNull ProgressIndicator indicator)
                    {
                        final Set<ICompilerProblem> problems = new HashSet<ICompilerProblem>();
                        IBackend backend = new RandoriBackend();
                        final Randori randori = new Randori(backend);
                        final int code = randori.mainNoExit(
                                arguments.toArguments(), problems);

                        if (code == 0)
                        {
                            NotificationUtils.sendRandoriInformation("Success",
                                    "Successfully compiled and built project '"
                                            + name + "'");
                        }
                        else
                        {
                            ApplicationManager.getApplication().invokeLater(
                                    new Runnable() {
                                        @Override
                                        public void run()
                                        {
                                            ProblemsToolWindow
                                                    .refresh(problems);
                                        }
                                    });

                            if (ProblemsToolWindow.hasErrors())
                            {
                                NotificationUtils.sendRandoriInformation(
                                        "Error",
                                        "Error in project, Check Problems view for more information '"
                                                + toErrorCode(code) + "'");
                            }
                            else
                            {
                                // XXX This is temp until I get the ProblemQuery yanked out of the compiler
                                // this would hit here if there are still Warnings but the build passed
                                NotificationUtils.sendRandoriInformation(
                                        "Success",
                                        "Successfully compiled and built project with warnings '"
                                                + name + "'");
                            }
                        }

                        copySdkLibraries(project);
                    }
                });
    }

    private void clean(Project project)
    {
        final VirtualFile file = project.getBaseDir();
        final RandoriProjectComponent component = ProjectUtils
                .getProjectComponent(project);

        ApplicationManager.getApplication().runWriteAction(new Runnable() {
            @Override
            public void run()
            {
                // wipe the generated directory
                VirtualFile virtualFile = file.findFileByRelativePath(component
                        .getBasePath());
                try
                {
                    if (virtualFile != null && virtualFile.exists())
                    {
                        virtualFile.delete(this);
                    }
                }
                catch (IOException e)
                {
                    e.printStackTrace();
                }
            }
        });
    }

    private void copySdkLibraries(Project project)
    {
        // only copy the SDK js libraries if the SWCs exist in the SDK

        final VirtualFile baseDir = project.getBaseDir();

        final Sdk sdk = ProjectRootManager.getInstance(project).getProjectSdk();
        VirtualFile sdkRoot = sdk.getHomeDirectory();

        //        final VirtualFile randoriSWC = sdkRoot
        //                .findFileByRelativePath("bin/Randori.swc");
        //        final VirtualFile randoriGuiceSWC = sdkRoot
        //                .findFileByRelativePath("bin/RandoriGuice.swc");

        if (SdkUtils.libraryExists(project, "Randori")
                && SdkUtils.libraryExists(project, "RandoriGuiceJS"))
        {
            String libPath = project
                    .getComponent(RandoriProjectComponent.class)
                    .getLibraryPath();

            VirtualFile libraryDir = baseDir.findFileByRelativePath(libPath);
            if (libraryDir == null)
            {
                new File(baseDir.getPath(), libPath).mkdirs();
                libraryDir = baseDir.findFileByRelativePath(libPath);
            }

            // copy the files to generated
            VirtualFile randoriJS = sdkRoot
                    .findFileByRelativePath("src/Randori.js");
            VirtualFile guiceJS = sdkRoot
                    .findFileByRelativePath("src/RandoriGuiceJS.js");

            try
            {
                FileUtil.copy(new File(randoriJS.getPath()),
                        new File(baseDir.getPath(), libPath + "/Randori.js"));
                FileUtil.copy(new File(guiceJS.getPath()),
                        new File(baseDir.getPath(), libPath
                                + "/RandoriGuiceJS.js"));
            }
            catch (IOException e)
            {
                e.printStackTrace(); //To change body of catch statement use File | Settings | File Templates.
            }

        }

        // TODO figure out the correct way to refresh the generated dir
        // says this should only be called in a writeAction
        baseDir.refresh(true, true);
    }

    private String toErrorCode(int code)
    {
        switch (code)
        {
        case 1:
            return "Unknown";
        case 2:
            return "Compiler problems";
        case 3:
            return "Compiler Exceptions";
        case 4:
            return "Configuration Problems";
        }

        return "Unkown error code";
    }

}
