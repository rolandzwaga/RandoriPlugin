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

/**
 * @author Michael Schmalle
 */
public class RandoriProjectModel
{

    //--------------------------------------------------------------------------
    //
    //--------------------------------------------------------------------------

    //----------------------------------
    // webRoot
    //----------------------------------

    private String webRoot = null;

    /**
     * For the time being, the default webroot will be the project root.
     */
    public String getWebRoot()
    {
        return webRoot;
    }

    public void setWebRoot(String value)
    {
        webRoot = value;
    }

    //----------------------------------
    // port
    //----------------------------------

    private int port = 8080;

    public int getPort()
    {
        return port;
    }

    public void setPort(int value)
    {
        port = value;
    }

    //----------------------------------
    // basePath
    //----------------------------------

    private String basePath = "generated";

    public String getBasePath()
    {
        return basePath;
    }

    public void setBasePath(String value)
    {
        basePath = value;
    }

    //----------------------------------
    // libraryPath
    //----------------------------------

    private String libraryPath = "generated/lib";

    public String getLibraryPath()
    {
        return libraryPath;
    }

    public void setLibraryPath(String value)
    {
        libraryPath = value;
    }

    //----------------------------------
    // classesAsFile
    //----------------------------------

    private boolean classesAsFile = true;

    public boolean isClassesAsFile()
    {
        return classesAsFile;
    }

    public void setClassesAsFile(boolean value)
    {
        classesAsFile = value;
    }

    public RandoriProjectModel()
    {
    }

}
