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

package randori.plugin.runner;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.eclipse.jetty.server.Handler;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.handler.DefaultHandler;
import org.eclipse.jetty.server.handler.HandlerList;
import org.eclipse.jetty.server.handler.ResourceHandler;
import org.eclipse.jetty.server.nio.NetworkTrafficSelectChannelConnector;

import randori.plugin.components.RandoriProjectComponent;

import com.intellij.openapi.components.ProjectComponent;
import com.intellij.openapi.project.Project;
import randori.plugin.components.RandoriProjectModel;

/**
 * @author Michael Schmalle
 */
public class RandoriServerComponent implements ProjectComponent
{

    private static final String DEFAULT_INDEX_HTML = "index.html";

    private Server server;

    private ExecutorService execService;

    private Project project;

    private RandoriProjectComponent component;

    public RandoriServerComponent(Project project,
            RandoriProjectComponent component)
    {
        this.project = project;
        this.component = component;
    }

    @Override
    public void initComponent()
    {

    }

    public void startServer(RandoriProjectModel model)
    {
        server = new Server();
        NetworkTrafficSelectChannelConnector connector = new NetworkTrafficSelectChannelConnector(
                server);
        connector.setPort(model.getPort());
        server.addConnector(connector);

        ResourceHandler resourceHandler = new ResourceHandler();
        resourceHandler.setDirectoriesListed(true);
        resourceHandler.setWelcomeFiles(new String[] { DEFAULT_INDEX_HTML });
        // TODO dosn't look like the RandoriProjectComponent has been restored yet, so this is the worn place

        String root = model.getWebRoot();
        if (root == null || root.equals(""))
            root = project.getBasePath();

        resourceHandler.setResourceBase(root);

        HandlerList handlers = new HandlerList();
        handlers.setHandlers(new Handler[] { resourceHandler,
                new DefaultHandler() });

        server.setHandler(handlers);

        execService = Executors.newFixedThreadPool(1);
        execService.submit(new Runnable() {
            @Override
            public void run()
            {
                try
                {
                    server.start();
                    server.join();
                }
                catch (Exception e)
                {
                    e.printStackTrace();
                }
            }
        });
    }

    @Override
    public void disposeComponent()
    {
        try
        {
            server.stop();
            execService.shutdown();
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    @Override
    public void projectOpened()
    {
        startServer(component.getState());
    }

    @Override
    public void projectClosed()
    {
    }

    @Override
    public String getComponentName()
    {
        return "RandoriServerComponent";
    }

}
