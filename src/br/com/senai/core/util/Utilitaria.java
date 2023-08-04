package br.com.senai.core.util;

import java.awt.Component;
import java.awt.Container;
import java.util.Collection;
import java.util.List;

import javax.swing.JComboBox;
import javax.swing.JFormattedTextField;
import javax.swing.JTextField;

public class Utilitaria {
	
	 public static void limparCampos(Container container) {
	        limparCampos(container, null);
	  }

    public static void limparCampos(Container container, List<Component> camposManter) {
        for (Component component : container.getComponents()) {
            if (camposManter == null || !camposManter.contains(component)) {
                if (component instanceof JTextField) {
                    JTextField textField = (JTextField) component;
                    textField.setText("");
                } else if (component instanceof JFormattedTextField) {
                    JFormattedTextField formattedTextField = (JFormattedTextField) component;
                    formattedTextField.setValue(null);
                } else if (component instanceof JComboBox) {
                    @SuppressWarnings("rawtypes")
                    JComboBox comboBox = (JComboBox) component;
                    comboBox.setSelectedIndex(0);
                }  else if (component instanceof Container) {
                    Container childContainer = (Container) component;
                    limparCampos(childContainer, camposManter);
                }
            }
        }
    }

    public static <T> void preencherCombo(JComboBox<T> comboBox, Collection<T> list, T placeholder) {
        comboBox.removeAllItems();
        if (placeholder != null) {
            comboBox.addItem(placeholder);
        }
        for (T item : list) {
            comboBox.addItem(item);
        }
    }

    public static <T> void preencherCombo(JComboBox<T> comboBox, Collection<T> list) {
        preencherCombo(comboBox, list, null);
    }

}