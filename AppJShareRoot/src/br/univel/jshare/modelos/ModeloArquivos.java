package br.univel.jshare.modelos;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.TableModel;
import br.univel.jshare.comum.Arquivo;
import br.univel.jshare.comum.Cliente;

public class ModeloArquivos extends AbstractTableModel implements TableModel {
	
	private static final long serialVersionUID = 1L;
	Map<Cliente, List<Arquivo>> mapaArquivos = new HashMap<Cliente, List<Arquivo>>();
	private Object[][] matriz;
	private int linhas;
	
	public ModeloArquivos(Map<Cliente, List<Arquivo>> mapa) {
		setMap(mapa);
	}

	public int getColumnCount() {
		return 8;
	}

	public int getRowCount() {
		return linhas;
	}

	@Override
	public Object getValueAt(int rowIndex, int columnIndex) {
		return matriz[rowIndex][columnIndex];
	}
	
	 @Override
	 public String getColumnName (int col) {
        switch (col) {
        case 0:
            return "Nome";
        case 1:
            return "Ip";
        case 2:
        	return "Porta";
        case 3:
        	return "Arquivo";
        case 4:
        	return "Extensão";
        case 5:
        	return "Bytes";
        case 6:
        	return "Path";
        case 7:
        	return "MD5";
        default:
            return "Erro";
        }
    }

	public void refresh(){
		super.fireTableDataChanged();
	}
	
	public void removeAll(){
		this.mapaArquivos.clear();
	}
	
	public void setMap(Map<Cliente, List<Arquivo>> mapa) {
		
		mapaArquivos = mapa;
		
		 linhas = 0;
		 for(Entry<Cliente, List<Arquivo>> e: mapa.entrySet()){
			linhas+= e.getValue().size();
		 }
			
		matriz = new Object[linhas][8];
		
		int linha = 0;
		
		for (Entry<Cliente, List<Arquivo>> e : mapa.entrySet()) {
			for (Arquivo arq : e.getValue()) {
				matriz[linha][0] = e.getKey().getNome();
				matriz[linha][1] = e.getKey().getIp();
				matriz[linha][2] = e.getKey().getPorta();
				matriz[linha][3] = arq.getNome();
				matriz[linha][4] = arq.getExtensao();
				matriz[linha][5] = arq.getTamanho();
				matriz[linha][6] = arq.getPath();
				matriz[linha][7] = arq.getMd5();
				linha++;
			}
		}
			
		refresh();
	}	
}
