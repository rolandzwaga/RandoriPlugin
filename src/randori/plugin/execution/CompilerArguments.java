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

import com.intellij.openapi.project.Project;
import randori.plugin.components.RandoriProjectComponent;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Michael Schmalle
 */
public class CompilerArguments
{

    private List<String> libraries = new ArrayList<String>();

    private List<String> sources = new ArrayList<String>();

    private String output;

    private String jsLibraryPath;

    private String jsBasePath;

    private boolean jsOutputAsFiles;

    public void addLibraryPath(String path)
    {
        libraries.add(path);
    }

    public void addSourcepath(String path)
    {
        sources.add(path);
    }

    public String getOutput()
    {
        return output;
    }

    public void setOutput(String path)
    {
        output = path;
    }

    public String getJsLibraryPath()
    {
        return jsLibraryPath;
    }

    public void setJsLibraryPath(String jsLibraryPath)
    {
        this.jsLibraryPath = jsLibraryPath;
    }

    public String getJsBasePath()
    {
        return jsBasePath;
    }

    public void setJsBasePath(String jsBasePath)
    {
        this.jsBasePath = jsBasePath;
    }

    public boolean isJsOutputAsFiles()
    {
        return jsOutputAsFiles;
    }

    public void setJsOutputAsFiles(boolean jsOutputAsFiles)
    {
        this.jsOutputAsFiles = jsOutputAsFiles;
    }

    public void clear()
    {
        jsBasePath = "";
        jsLibraryPath = "";
        jsOutputAsFiles = false;
        output = "";
        clearLibraries();
        clearSourcePaths();
    }

    public void clearSourcePaths()
    {
        sources = new ArrayList<String>();
    }

    public void clearLibraries()
    {
        libraries = new ArrayList<String>();
    }

    public String[] toArguments()
    {
        List<String> result = new ArrayList<String>();

        // libs
        for (String arg : libraries)
        {
            result.add("-library-path=" + arg);
        }
        // sources
        for (String arg : sources)
        {
            result.add("-sp=" + arg);
        }
        //

        result.add("-js-base-path=" + getJsBasePath());
        result.add("-js-library-path=" + getJsLibraryPath());
        //result.add("-js-classes-as-files=" + (isJsOutputAsFiles() ? "true" : "false"));
        result.add("-js-classes-as-files=true");
        result.add("-output=" + getOutput());

        return result.toArray(new String[]{});
    }

    public void configure(Project project, RandoriProjectComponent component)
    {
        setJsBasePath(component.getBasePath());
        setJsLibraryPath(component.getLibraryPath());
        setJsOutputAsFiles(component.isClassesAsFile());
        setOutput(project.getBasePath());
    }

    public static class CompilerArgument
    {
        private String name;

        private String value;

        CompilerArgument(String name, String value)
        {
            this.name = name;
            this.value = value;
        }

        public String getValue()
        {
            return value;
        }

        public void setValue(String value)
        {
            this.value = value;
        }

        public String getName()
        {
            return name;
        }

        public void setName(String name)
        {
            this.name = name;
        }

        public static CompilerArgument create(String name, String value)
        {
            return new CompilerArgument(name, value);
        }
    }
}
