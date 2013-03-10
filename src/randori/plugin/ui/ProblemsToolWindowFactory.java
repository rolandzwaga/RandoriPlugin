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

package randori.plugin.ui;

import com.intellij.openapi.project.Project;
import com.intellij.openapi.wm.ToolWindow;
import com.intellij.openapi.wm.ToolWindowFactory;

/**
 * @author Michael Schmalle
 */
public class ProblemsToolWindowFactory implements ToolWindowFactory
{
    public static final String WINDOW_ID = "Problems";

    private Project project;

    private ProblemsToolWindow window;

    @Override
    public void createToolWindowContent(Project project, ToolWindow toolWindow)
    {
        this.project = project;
        this.window = new ProblemsToolWindow(toolWindow);
        //window.refresh(null);
    }

}


/*

DefaultActionGroup group = new DefaultActionGroup();

group.add(action1);
group.add(action2);
group.add(action3);

JComponent toolbar = ActionManager.getInstance().createActionToolbar(ActionPlaces.UNKNOWN, group, false).getComponent();


*/