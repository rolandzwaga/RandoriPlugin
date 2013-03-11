package randori.plugin.forms;

import com.intellij.openapi.options.ConfigurationException;
import com.intellij.openapi.options.SettingsEditor;
import org.jetbrains.annotations.NotNull;
import randori.plugin.components.RandoriProjectComponent;

import javax.swing.*;

/**
 * @author Michael Schmalle
 */
public class RandoriProjectConfigurationForm extends
        SettingsEditor<RandoriProjectConfigurationForm>
{

    private JPanel panel;

    private JTextField basePath;

    private JTextField libraryPath;

    private JCheckBox exportAsFiles;

    public void getData(RandoriProjectComponent data)
    {
        data.setBasePath(basePath.getText());
        data.setLibraryPath(libraryPath.getText());
        data.setClassesAsFile(exportAsFiles.isSelected());
    }

    public void setData(RandoriProjectComponent data)
    {
        basePath.setText(data.getBasePath());
        libraryPath.setText(data.getLibraryPath());
        exportAsFiles.setSelected(data.isClassesAsFile());
    }

    @Override
    protected void resetEditorFrom(RandoriProjectConfigurationForm model)
    {
        // apply the saved model to the components
        //        basePath.setText(model.getBasePath());
        //        libraryPath.setText(model.getLibraryPath());
        //        exportAsFiles.setSelected(model.isClassesAsFile());
    }

    @Override
    protected void applyEditorTo(RandoriProjectConfigurationForm s)
            throws ConfigurationException
    {
    }

    @NotNull
    @Override
    protected JComponent createEditor()
    {
        return panel;
    }

    @Override
    protected void disposeEditor()
    {
    }

    public boolean isModified(RandoriProjectComponent data)
    {
        if (isOpposite(exportAsFiles, data.isClassesAsFile()))
            return true;
        return isModified(basePath, data.getBasePath())
                || isModified(libraryPath, data.getLibraryPath());

    }

    private boolean isOpposite(JCheckBox exportAsFiles, boolean selected)
    {
        return exportAsFiles.isSelected() != selected;
    }

    private boolean isModified(JTextField field, String value)
    {
        return field.getText() != null ? !field.getText().equals(value)
                : value != null;
    }
}
