package br.com.senai.core.util;

import java.awt.Component;
import java.awt.Container;
import java.awt.Window;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import java.util.function.Consumer;

import javax.swing.JComboBox;
import javax.swing.JFormattedTextField;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.SwingWorker;
import javax.swing.table.TableColumn;
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
					if (model instanceof TableModelLimpavel) {
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
				} else if (component instanceof Container) {
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

	public static void configurarTabela(JTable tabela, Map<String, Integer> largurasColunas) {
		tabela.getTableHeader().setReorderingAllowed(false);
		tabela.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

		for (Map.Entry<String, Integer> entrada : largurasColunas.entrySet()) {
			configurarColuna(tabela, entrada.getKey(), entrada.getValue());
		}
	}

	public static void configurarColuna(JTable tabela, String nomeColuna, int largura) {
		TableColumn coluna = tabela.getColumn(nomeColuna);
		coluna.setResizable(false);
		coluna.setPreferredWidth(largura);
	}

	public static <T> void executarAmbos(Window owner, Callable<T> searcher, Consumer<? super T> action) {
		SwingWorker<T, Void> worker = new SwingWorker<T, Void>() {
			@Override
			protected T doInBackground() throws Exception {
				ExecutorService executor = Executors.newSingleThreadExecutor();
				Future<T> future = executor.submit(searcher);

				try {
					return future.get(15, TimeUnit.SECONDS);
				} catch (TimeoutException e) {
					future.cancel(true);
					throw new RuntimeException("Ocorreu um erro de conexão. Verifique a internet.");
				} finally {
					executor.shutdown();
				}
			}

			@Override
			protected void done() {
				try {
					action.accept(get());
				} catch (InterruptedException | ExecutionException e) {
					throw new RuntimeException(e);
				}
			}
		};
		worker.execute();
	}

}