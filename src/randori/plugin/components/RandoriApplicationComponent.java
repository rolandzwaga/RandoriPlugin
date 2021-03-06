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

import com.intellij.openapi.components.ApplicationComponent;
import com.intellij.openapi.vfs.VirtualFileManager;
import org.jetbrains.annotations.NotNull;
import randori.plugin.builder.FileChangeBuilder;
import randori.plugin.execution.BuildSourceCommand;
import randori.plugin.execution.CompilerArguments;

/**
 * @author Michael Schmalle
 */
public class RandoriApplicationComponent implements ApplicationComponent
{

    private CompilerArguments compilerArguments;

    private BuildSourceCommand buildSourceCommand;

    public BuildSourceCommand getBuildSourceCommand()
    {
        return buildSourceCommand;
    }

    public CompilerArguments getCompilerArguments()
    {
        return compilerArguments;
    }

    @Override
    public void initComponent()
    {
        VirtualFileManager.getInstance().addVirtualFileListener(new FileChangeBuilder());
        //startupApplication();

        compilerArguments = new CompilerArguments();
        buildSourceCommand = new BuildSourceCommand();
    }

    @Override
    public void disposeComponent()
    {
    }

    @NotNull
    @Override
    public String getComponentName()
    {
        return "RandoriApplicationComponent";
    }

}
