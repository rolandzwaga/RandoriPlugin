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

import com.intellij.openapi.wm.ToolWindow;
import com.intellij.ui.components.JBScrollPane;
import com.intellij.ui.content.Content;
import com.intellij.ui.content.ContentFactory;
import com.intellij.ui.content.ContentManager;
import com.intellij.ui.table.JBTable;
import com.intellij.util.containers.ArrayListSet;
import org.apache.flex.compiler.problems.ICompilerProblem;
import org.apache.flex.compiler.problems.NoDefinitionForSWCDependencyProblem;
import org.apache.flex.compiler.problems.annotations.DefaultSeverity;
import randori.plugin.components.RandoriProjectComponent;
import randori.plugin.utils.ProjectUtils;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.DefaultTableCellRenderer;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * @author Michael Schmalle
 */
public class ProblemsToolWindow
{
    private static final String[] COLUMN_TITLES = new String[] { "Description",
            "Resource", "Path", "Location", "Type" };

    private static ProblemsToolWindow instance;

    ContentManager contentManager;

    @SuppressWarnings("unused")
    private ToolWindow window;

    private JBTable table;

    private JPanel jPanel;

    private List<ICompilerProblem> problems;

    public ProblemsToolWindow(ToolWindow window)
    {
        instance = this;
        this.window = window;
        contentManager = window.getContentManager();
        create();
        reset(null);
    }

    public static ProblemsToolWindow getInstance()
    {
        return instance;
    }

    private void create()
    {
        contentManager.removeAllContents(true);

        String tableName = "Project";

        jPanel = new JPanel();
        jPanel.setLayout(new BorderLayout());

        // create a table with columns and rows
        table = new JBTable();
        table.setCellSelectionEnabled(false);
        table.setAutoCreateRowSorter(false);

        // the scrollpane holds the table which is layed out FULL and will
        // not fit inside the parent Panel without scroll
        JBScrollPane jbScrollPane = new JBScrollPane(table);
        jbScrollPane.getVerticalScrollBar().setUnitIncrement(16);

        jPanel.add(jbScrollPane, BorderLayout.CENTER);

        Content content = ContentFactory.SERVICE.getInstance().createContent(
                jPanel, tableName, false);
        contentManager.addContent(content);

        ListSelectionModel listMod = table.getSelectionModel();
        listMod.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        listMod.addListSelectionListener(new TableMouseListener());

        table.setEnableAntialiasing(true);
        //table.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
        table.setCellSelectionEnabled(false);
        table.setRowSelectionAllowed(true);

        table.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e)
            {
                if (e.getClickCount() == 2)
                {
                    handleDoubleClick(e);
                }
            }
        });
    }

    private void reset(Set<ICompilerProblem> problems)
    {
        if (problems == null)
            problems = new ArrayListSet<ICompilerProblem>();

        List<ICompilerProblem> result = new ArrayList<ICompilerProblem>();
        for (ICompilerProblem p : problems)
        {
            if (!(p instanceof NoDefinitionForSWCDependencyProblem))
                result.add(p);
        }
        this.problems = result;
        AbstractTableModel model = new ProblemsTableModel(result);
        table.setModel(model);
    }

    private void handleDoubleClick(MouseEvent e)
    {
        int row = table.rowAtPoint(e.getPoint());
        ProblemsTableModel model = (ProblemsTableModel) table.getModel();
        ICompilerProblem problem = model.getProblemAt(row);
        if (!isValid(problem))
            return;

        RandoriProjectComponent component = ProjectUtils.findProjectComponent(
                jPanel, RandoriProjectComponent.class);
        component.openFileForProblem(problem);
    }

    private boolean isValid(ICompilerProblem problem)
    {
        return problem.getLine() != -1;
    }

    // TODO this is a hack until I figure out if this is a Service or not
    public static void refresh(Set<ICompilerProblem> problems)
    {
        if (instance == null)
            return;
        instance.reset(problems);
    }

    public static boolean hasErrors()
    {
        if (instance == null || instance.problems == null)
            return false;
        return instance.problems.size() > 0;
    }

    @SuppressWarnings("serial")
    static class IconRenderer extends DefaultTableCellRenderer
    {
        public IconRenderer()
        {
            super();
        }

        @Override
        public void setValue(Object value)
        {
            if (value == null)
            {
                setText("");
            }
            else
            {
                setIcon((Icon) value);
            }
        }
    }

    @SuppressWarnings("serial")
    public static class ProblemsTableModel extends AbstractTableModel
    {
        private List<ICompilerProblem> problems;

        //        @Override
        //        public Class<?> getColumnClass(int columnIndex)
        //        {
        //            if (columnIndex == 4)
        //                return ImageIcon.class;
        //            return super.getColumnClass(columnIndex);
        //        }

        public ProblemsTableModel(List<ICompilerProblem> problems)
        {
            this.problems = problems;
        }

        @Override
        public int getRowCount()
        {
            if (problems == null)
                return 0;
            return problems.size();
        }

        @Override
        public String getColumnName(int column)
        {
            return COLUMN_TITLES[column];
        }

        @Override
        public int getColumnCount()
        {
            return COLUMN_TITLES.length;
        }

        @Override
        public Object getValueAt(int rowIndex, int columnIndex)
        {
            return getProblemValue(columnIndex, problems.get(rowIndex));
        }

        private String getProblemValue(int index, ICompilerProblem problem)
        {
            switch (index)
            {
            case 0:
                return problem.toString();
            case 1:
                return getName(problem);
            case 2:
                return getPath(problem);
            case 3:
                return Integer.toString(problem.getLine());
            case 4:
                return getSeverty(problem);
            }
            return "Not found";
        }

        private String getSeverty(ICompilerProblem problem)
        {
            //return "icons/randori13x13.png";
            DefaultSeverity defaultSeverity = problem.getClass().getAnnotation(
                    DefaultSeverity.class);
            return defaultSeverity.value().toString();
        }

        private String getPath(ICompilerProblem problem)
        {
            File file = new File(problem.getSourcePath());
            String result = file.getParent();
            return result;
        }

        private String getName(ICompilerProblem problem)
        {
            File file = new File(problem.getSourcePath());
            String result = file.getName();
            return result;
        }

        public ICompilerProblem getProblemAt(int row)
        {
            return problems.get(row);
        }
    }

    public class TableMouseListener implements ListSelectionListener
    {

        @Override
        public void valueChanged(ListSelectionEvent e)
        {

        }
    }
}
