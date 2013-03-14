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

import java.io.IOException;
import java.net.ServerSocket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.eclipse.jetty.server.Handler;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.handler.DefaultHandler;
import org.eclipse.jetty.server.handler.HandlerList;
import org.eclipse.jetty.server.handler.ResourceHandler;
import org.eclipse.jetty.server.nio.NetworkTrafficSelectChannelConnector;

import randori.plugin.components.RandoriProjectComponent;

import com.intellij.ide.BrowserUtil;
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
        server = new Server();
        NetworkTrafficSelectChannelConnector connector = new NetworkTrafficSelectChannelConnector(
                server);
        int portNr = component.getState().getPort();
        if (portNr < 0) {
            portNr = findFreePort();
            if (portNr < 0) {
                //For now do nothing, but if this fails something
                //is terribly wrong, we need some kind of global
                //state that indicates that the entire system is
                //unstable and that the user cannot proceed further. (Or something)
                return;
            }
            component.getState().setPort(portNr);
        }
        connector.setPort(portNr);
        server.addConnector(connector);

        ResourceHandler resourceHandler = new ResourceHandler();
        resourceHandler.setDirectoriesListed(true);
        resourceHandler.setWelcomeFiles(new String[] { DEFAULT_INDEX_HTML });

        String root = component.getState().getWebRoot();
        if (root == null || root.equals(""))
            root = project.getBasePath();

        resourceHandler.setResourceBase(root);

        HandlerList handlers = new HandlerList();
        handlers.setHandlers(new Handler[] { resourceHandler,
                new DefaultHandler() });

        server.setHandler(handlers);
    }

    public static int findFreePort() {
        int port = -1;
        try {
            ServerSocket server =
                    new ServerSocket(0);
            port = server.getLocalPort();
            server.close();
        } catch(Exception e){
            e.printStackTrace();
        }
        return port;
    }

    @Override
    public void disposeComponent()
    {
        server = null;
        execService = null;
    }

    void startServer(RandoriProjectModel model)
    {
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
    public void projectOpened()
    {
        startServer(component.getState());
    }

    @Override
    public void projectClosed()
    {
        stopServer();
    }

    void stopServer()
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
    public String getComponentName()
    {
        return "RandoriServerComponent";
    }

    public void openURL(String reletiveURL)
    {
        // still new to this, figuring out if this is the right way to do this.
        String url = getURL(reletiveURL);
        // temp, will hook up properly, can create a config that says
        // something like preview in browser checkbox
        BrowserUtil.launchBrowser(url);
    }

    public String getURL(String index)
    {
        int port = component.getState().getPort();
        String url = "http://localhost:" + port + "/" + index;
        return url;
    }
}
