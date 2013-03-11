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

import com.intellij.openapi.components.ApplicationComponent;
import com.intellij.openapi.components.PersistentStateComponent;
import com.intellij.openapi.components.State;
import com.intellij.openapi.components.Storage;
import com.intellij.openapi.options.Configurable;
import com.intellij.openapi.options.ConfigurationException;
import org.eclipse.jetty.server.Handler;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.handler.DefaultHandler;
import org.eclipse.jetty.server.handler.HandlerList;
import org.eclipse.jetty.server.handler.ResourceHandler;
import org.eclipse.jetty.server.nio.NetworkTrafficSelectChannelConnector;

import javax.swing.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@State(name = "MyRunnerSettings", storages = { @Storage(id = "jettyrunner", file = "$APP_CONFIG$/jettyrunner.xml") })
/**
 * @author Michael Schmalle
 */
public class RandoriServerComponent implements ApplicationComponent,
        Configurable, PersistentStateComponent<RandoriServerComponent>
{

    private Server server;
    private ExecutorService execService;

    @Override
    public void initComponent()
    {
        server = new Server();
        NetworkTrafficSelectChannelConnector connector = new NetworkTrafficSelectChannelConnector(
                server);
        connector.setPort(8080);
        server.addConnector(connector);

        ResourceHandler resource_handler = new ResourceHandler();
        resource_handler.setDirectoriesListed(true);
        resource_handler.setWelcomeFiles(new String[] { "index.html" });

        resource_handler.setResourceBase("C:\\webroot");

        HandlerList handlers = new HandlerList();
        handlers.setHandlers(new Handler[] { resource_handler,
                new DefaultHandler() });

        server.setHandler(handlers);

        execService = Executors.newFixedThreadPool(1);
        execService.submit(new Runnable() {
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
    public String getComponentName()
    {
        return "RandoriServerComponent";
    }

    @Override
    public JComponent createComponent()
    {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public boolean isModified()
    {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public void apply() throws ConfigurationException
    {
        // TODO Auto-generated method stub

    }

    @Override
    public void reset()
    {
        // TODO Auto-generated method stub

    }

    @Override
    public void disposeUIResources()
    {
        // TODO Auto-generated method stub

    }

    @Override
    public RandoriServerComponent getState()
    {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public void loadState(RandoriServerComponent state)
    {
        // TODO Auto-generated method stub

    }

    @Override
    public String getDisplayName()
    {
        return "Randori Runner";
    }

    @Override
    public String getHelpTopic()
    {
        return null;
    }

    //--------------------------------------------------------------------------
    //
    //--------------------------------------------------------------------------

    private String webRoot = "index.html";

    private int port = 8080;

    public String getWebRoot()
    {
        return webRoot;
    }

    public void setWebRoot(String value)
    {
        webRoot = value;
    }

    public int getPort()
    {
        return port;
    }

    public void setPort(int value)
    {
        port = value;
    }
}
