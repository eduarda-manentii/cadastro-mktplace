package br.com.senai.core.util;

import java.awt.Component;
import java.awt.Container;
import java.util.Collection;
import java.util.List;

import javax.swing.JComboBox;
import javax.swing.JFormattedTextField;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.table.TableModel;

import br.com.senai.view.componentes.table.TableModelLimpavel;

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
                } else if (component instanceof JTable) {
                	JTable table = (JTable) component;
                	TableModel model = table.getModel();
                	if(model instanceof TableModelLimpavel) {
                		TableModelLimpavel limpavel = (TableModelLimpavel) model;
                		limpavel.limpar();
                		table.updateUI();
                	}
                } else if (component instanceof JTextArea) {
                	JTextArea textArea = (JTextArea) component;
                	textArea.setText("");
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
    
	public static void configurarTabela(JTable table) {
		final int COLUNA_ID = 0;
		final int COLUNA_NOME = 1;
		table.getTableHeader().setReorderingAllowed(false);
		table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		configurarColuna(table, COLUNA_ID, 90);
		configurarColuna(table, COLUNA_NOME, 250);
	}
	
    
	public static void configurarColuna(JTable table, int indice, int largura) {
		table.getColumnModel().getColumn(indice).setResizable(false);
		table.getColumnModel().getColumn(indice).setPreferredWidth(largura);
	}
	

}