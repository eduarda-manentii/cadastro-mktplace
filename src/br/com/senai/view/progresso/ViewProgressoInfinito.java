package br.com.senai.view.progresso;

import java.awt.Window;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import java.util.function.Consumer;

import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JProgressBar;
import javax.swing.SwingUtilities;
import javax.swing.SwingWorker;

public class ViewProgressoInfinito {

    private static JDialog loadingFrame;

    public static void mostraProgresso(Window owner) {

        SwingUtilities.invokeLater(() -> {
            loadingFrame = new JDialog(owner);
            loadingFrame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
            loadingFrame.setSize(200, 100);
            loadingFrame.setAlwaysOnTop(true); 

            JProgressBar progressBar = new JProgressBar();
            progressBar.setIndeterminate(true);
            progressBar.setString("Carregando...");
            progressBar.setStringPainted(true);
            loadingFrame.add(progressBar);

            loadingFrame.setLocationRelativeTo(null);
            loadingFrame.setUndecorated(true);
            loadingFrame.setVisible(true);
        });
    }

    public static void fechaProgresso() {
    	loadingFrame.setVisible(false);
    	loadingFrame.dispose();
    }
    
    
    public static <T> void chamaMetodoComProgresso(Window owner, Callable<T> searcher, Consumer<? super T> action) {
    	ViewProgressoInfinito.mostraProgresso(owner);
         SwingWorker<T, Void> worker = new SwingWorker<T, Void>() {
            @Override
            protected T doInBackground() throws Exception {
                ExecutorService executor = Executors.newSingleThreadExecutor();
                Future<T> future = executor.submit(searcher);

                try {
                    return future.get(15, TimeUnit.SECONDS);
                } catch (TimeoutException e) {
                    future.cancel(true); 
                    throw new RuntimeException("Ocorreu um erro de conexão."
                    		+ " Verifique a internet.");
                } finally {
                    executor.shutdown();
                }
            }
            

            @Override
            protected void done() {
                try {
                	ViewProgressoInfinito.fechaProgresso();
                	action.accept(get());
                } catch (InterruptedException | ExecutionException e) {
                	throw new RuntimeException(e);
				}
            }
        };
        worker.execute();
    }
}
