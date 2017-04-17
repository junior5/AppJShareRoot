package br.univel.jshare.view;

import java.awt.EventQueue;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;
import javax.swing.text.DefaultCaret;
import br.univel.jshare.comum.Arquivo;
import br.univel.jshare.comum.Cliente;
import br.univel.jshare.comum.IServer;
import br.univel.jshare.comum.TipoFiltro;
import br.univel.jshare.modelos.ModeloArquivos;
import br.univel.jshare.util.Tools;
import java.awt.event.ActionListener;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.regex.Pattern;
import java.awt.event.ActionEvent;
import javax.swing.JTable;
import java.awt.Font;
import javax.swing.JCheckBox;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import javax.swing.JComboBox;

public class Principal extends JFrame implements IServer {

	private static final long serialVersionUID = 1L;
	private JPanel contentPane;
	private JTextField txfIp;
	private JTextField txfPorta;
	private JButton buttonDesconectar;
	private JButton buttonConectar;
	private JLabel lblNome;
	private JTextField txfMeuNome;
	private IServer servidor;
	private Cliente cliente;
	private JScrollPane scrollPane_2;
	private SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy H:mm:ss");
	private Map<String, Cliente> mapaClientes = new HashMap<>();
	private Map<Cliente, List<Arquivo>> mapaArquivos = new HashMap<Cliente, List<Arquivo>>();
	private ModeloArquivos modeloArquivos = new ModeloArquivos(mapaArquivos);
	private Registry registry;
	private JTextArea taLog;
	private JPanel panel_2;
	private JScrollPane scrollPane;
	private JTable table;
	private JCheckBox cbServidor;
	private JPanel panel_3;
	private JLabel lblDiretrio;
	private JTextField txfNomeArquivo;
	private JLabel lblFiltro;
	private JComboBox<?> cbFiltro;
	private JButton buttonBaixar;
	private JTextField txfValorFiltro;
	private int idArquivo;
	private JButton buttonPublicar;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					Principal frame = new Principal();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the frame.
	 */
	public Principal() {

		setResizable(false);
		setTitle("RMI Chat - JShareRoot");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 1151, 584);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		GridBagLayout gbl_contentPane = new GridBagLayout();
		gbl_contentPane.columnWidths = new int[]{719, 0};
		gbl_contentPane.rowHeights = new int[]{48, 0, 313, 116, 0};
		gbl_contentPane.columnWeights = new double[]{1.0, Double.MIN_VALUE};
		gbl_contentPane.rowWeights = new double[]{0.0, 0.0, 1.0, 0.0, Double.MIN_VALUE};
		contentPane.setLayout(gbl_contentPane);

		JPanel panel_1 = new JPanel();
		GridBagConstraints gbc_panel_1 = new GridBagConstraints();
		gbc_panel_1.fill = GridBagConstraints.BOTH;
		gbc_panel_1.insets = new Insets(0, 0, 5, 0);
		gbc_panel_1.gridx = 0;
		gbc_panel_1.gridy = 0;
		contentPane.add(panel_1, gbc_panel_1);
		GridBagLayout gbl_panel_1 = new GridBagLayout();
		gbl_panel_1.columnWidths = new int[] { 0, 148, 0, 0, 0, 0, 0, 0 };
		gbl_panel_1.rowHeights = new int[] { 0, 0, 0 };
		gbl_panel_1.columnWeights = new double[] { 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, Double.MIN_VALUE };
		gbl_panel_1.rowWeights = new double[] { 0.0, 0.0, Double.MIN_VALUE };
		panel_1.setLayout(gbl_panel_1);

		lblNome = new JLabel("Nome");
		GridBagConstraints gbc_lblNome = new GridBagConstraints();
		gbc_lblNome.anchor = GridBagConstraints.EAST;
		gbc_lblNome.insets = new Insets(0, 0, 5, 5);
		gbc_lblNome.gridx = 0;
		gbc_lblNome.gridy = 0;
		panel_1.add(lblNome, gbc_lblNome);

		txfMeuNome = new JTextField();
		txfMeuNome.setText("Junior");
		GridBagConstraints gbc_txfMeuNome = new GridBagConstraints();
		gbc_txfMeuNome.gridwidth = 3;
		gbc_txfMeuNome.insets = new Insets(0, 0, 5, 5);
		gbc_txfMeuNome.fill = GridBagConstraints.HORIZONTAL;
		gbc_txfMeuNome.gridx = 1;
		gbc_txfMeuNome.gridy = 0;
		panel_1.add(txfMeuNome, gbc_txfMeuNome);
		txfMeuNome.setColumns(10);

		cbServidor = new JCheckBox("Servidor");
		cbServidor.addMouseListener(new MouseAdapter() {

			public void mouseClicked(MouseEvent arg0) {
				if(txfMeuNome.isEnabled() && txfIp.isEnabled()){
					txfMeuNome.setText("");
					txfMeuNome.setEnabled(false);
					txfIp.setText("");
					txfIp.setEnabled(false);
				}else{
					txfMeuNome.setEnabled(true);
					txfIp.setEnabled(true);
				}
			}

		});
		GridBagConstraints gbc_chckbxServidor = new GridBagConstraints();
		gbc_chckbxServidor.insets = new Insets(0, 0, 5, 5);
		gbc_chckbxServidor.gridx = 4;
		gbc_chckbxServidor.gridy = 0;
		panel_1.add(cbServidor, gbc_chckbxServidor);

		JLabel lblNewLabel = new JLabel("Endereço IP");
		GridBagConstraints gbc_lblNewLabel = new GridBagConstraints();
		gbc_lblNewLabel.insets = new Insets(0, 0, 0, 5);
		gbc_lblNewLabel.anchor = GridBagConstraints.EAST;
		gbc_lblNewLabel.gridx = 0;
		gbc_lblNewLabel.gridy = 1;
		panel_1.add(lblNewLabel, gbc_lblNewLabel);

		txfIp = new JTextField();
		txfIp.setText(getIp());

		GridBagConstraints gbc_txfIp = new GridBagConstraints();
		gbc_txfIp.insets = new Insets(0, 0, 0, 5);
		gbc_txfIp.fill = GridBagConstraints.HORIZONTAL;
		gbc_txfIp.gridx = 1;
		gbc_txfIp.gridy = 1;
		panel_1.add(txfIp, gbc_txfIp);
		txfIp.setColumns(10);

		JLabel lblNewLabel_1 = new JLabel("Porta");
		GridBagConstraints gbc_lblNewLabel_1 = new GridBagConstraints();
		gbc_lblNewLabel_1.anchor = GridBagConstraints.EAST;
		gbc_lblNewLabel_1.insets = new Insets(0, 0, 0, 5);
		gbc_lblNewLabel_1.gridx = 2;
		gbc_lblNewLabel_1.gridy = 1;
		panel_1.add(lblNewLabel_1, gbc_lblNewLabel_1);

		txfPorta = new JTextField();
		txfPorta.setText("1818");
		GridBagConstraints gbc_txfPorta = new GridBagConstraints();
		gbc_txfPorta.insets = new Insets(0, 0, 0, 5);
		gbc_txfPorta.fill = GridBagConstraints.HORIZONTAL;
		gbc_txfPorta.gridx = 3;
		gbc_txfPorta.gridy = 1;
		panel_1.add(txfPorta, gbc_txfPorta);
		txfPorta.setColumns(10);

		buttonConectar = new JButton("Conectar");
		buttonConectar.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {

				if(cbServidor.isSelected()){
					startServer();
				}else{
					conectar();																						
				}

			}
		});
		GridBagConstraints gbc_buttonConectar = new GridBagConstraints();
		gbc_buttonConectar.insets = new Insets(0, 0, 0, 5);
		gbc_buttonConectar.gridx = 4;
		gbc_buttonConectar.gridy = 1;
		panel_1.add(buttonConectar, gbc_buttonConectar);

		buttonDesconectar = new JButton("Desconectar");
		buttonDesconectar.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				if(cbServidor.isSelected()){
					stopServer();
				}else{
					sair();																					
				}
			}
		});
		buttonDesconectar.setEnabled(false);
		GridBagConstraints gbc_buttonDesconectar = new GridBagConstraints();
		gbc_buttonDesconectar.insets = new Insets(0, 0, 0, 5);
		gbc_buttonDesconectar.gridx = 5;
		gbc_buttonDesconectar.gridy = 1;
		panel_1.add(buttonDesconectar, gbc_buttonDesconectar);
		
		buttonPublicar = new JButton("Publicar");
		buttonPublicar.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent arg0) {
				try {
					servidor.publicarListaArquivos(cliente, meusArquivos());
				} catch (RemoteException e) {
					log("Erro ao publicar lista de arquivos!");
					e.printStackTrace();
				}
			}
		});
		buttonPublicar.setEnabled(false);
		GridBagConstraints gbc_buttonPublicar = new GridBagConstraints();
		gbc_buttonPublicar.gridx = 6;
		gbc_buttonPublicar.gridy = 1;
		panel_1.add(buttonPublicar, gbc_buttonPublicar);

		panel_3 = new JPanel();
		GridBagConstraints gbc_panel_3 = new GridBagConstraints();
		gbc_panel_3.insets = new Insets(0, 0, 5, 0);
		gbc_panel_3.fill = GridBagConstraints.BOTH;
		gbc_panel_3.gridx = 0;
		gbc_panel_3.gridy = 1;
		contentPane.add(panel_3, gbc_panel_3);
		GridBagLayout gbl_panel_3 = new GridBagLayout();
		gbl_panel_3.columnWidths = new int[]{74, 301, 67, 394, 155, 0, 0};
		gbl_panel_3.rowHeights = new int[]{0, 0};
		gbl_panel_3.columnWeights = new double[]{0.0, 0.0, 0.0, 0.0, 0.0, 1.0, Double.MIN_VALUE};
		gbl_panel_3.rowWeights = new double[]{0.0, Double.MIN_VALUE};
		panel_3.setLayout(gbl_panel_3);

		lblDiretrio = new JLabel("Arquivos");
		GridBagConstraints gbc_lblDiretrio = new GridBagConstraints();
		gbc_lblDiretrio.insets = new Insets(0, 0, 0, 5);
		gbc_lblDiretrio.anchor = GridBagConstraints.EAST;
		gbc_lblDiretrio.gridx = 0;
		gbc_lblDiretrio.gridy = 0;
		panel_3.add(lblDiretrio, gbc_lblDiretrio);

		txfNomeArquivo = new JTextField();
		txfNomeArquivo.setEnabled(false);
		GridBagConstraints gbc_txfNomeArquivo = new GridBagConstraints();
		gbc_txfNomeArquivo.fill = GridBagConstraints.HORIZONTAL;
		gbc_txfNomeArquivo.insets = new Insets(0, 0, 0, 5);
		gbc_txfNomeArquivo.gridx = 1;
		gbc_txfNomeArquivo.gridy = 0;
		panel_3.add(txfNomeArquivo, gbc_txfNomeArquivo);
		txfNomeArquivo.setColumns(10);

		buttonBaixar = new JButton("Buscar");
		buttonBaixar.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				buscaArquivo();
			}
		});
		buttonBaixar.setEnabled(false);
		GridBagConstraints gbc_buttonBaixar = new GridBagConstraints();
		gbc_buttonBaixar.insets = new Insets(0, 0, 0, 5);
		gbc_buttonBaixar.gridx = 2;
		gbc_buttonBaixar.gridy = 0;
		panel_3.add(buttonBaixar, gbc_buttonBaixar);

		lblFiltro = new JLabel("Filtro");
		GridBagConstraints gbc_lblFiltro = new GridBagConstraints();
		gbc_lblFiltro.anchor = GridBagConstraints.EAST;
		gbc_lblFiltro.insets = new Insets(0, 0, 0, 5);
		gbc_lblFiltro.gridx = 3;
		gbc_lblFiltro.gridy = 0;
		panel_3.add(lblFiltro, gbc_lblFiltro);
		

		cbFiltro = new JComboBox(TipoFiltro.values());
		cbFiltro.setEnabled(false);
		GridBagConstraints gbc_cbFiltro = new GridBagConstraints();
		gbc_cbFiltro.fill = GridBagConstraints.HORIZONTAL;
		gbc_cbFiltro.insets = new Insets(0, 0, 0, 5);
		gbc_cbFiltro.gridx = 4;
		gbc_cbFiltro.gridy = 0;
		panel_3.add(cbFiltro, gbc_cbFiltro);

		txfValorFiltro = new JTextField();
		
		GridBagConstraints gbc_txfValorFiltro = new GridBagConstraints();
		gbc_txfValorFiltro.fill = GridBagConstraints.HORIZONTAL;
		gbc_txfValorFiltro.gridx = 5;
		gbc_txfValorFiltro.gridy = 0;
		panel_3.add(txfValorFiltro, gbc_txfValorFiltro);
		txfValorFiltro.setColumns(10);

		panel_2 = new JPanel();
		GridBagConstraints gbc_panel_2 = new GridBagConstraints();
		gbc_panel_2.fill = GridBagConstraints.BOTH;
		gbc_panel_2.insets = new Insets(0, 0, 5, 0);
		gbc_panel_2.gridx = 0;
		gbc_panel_2.gridy = 2;

		contentPane.add(panel_2, gbc_panel_2);
		GridBagLayout gbl_panel_2 = new GridBagLayout();
		gbl_panel_2.columnWidths = new int[]{479, 0};
		gbl_panel_2.rowHeights = new int[]{27, 0};
		gbl_panel_2.columnWeights = new double[]{1.0, Double.MIN_VALUE};
		gbl_panel_2.rowWeights = new double[]{1.0, Double.MIN_VALUE};
		panel_2.setLayout(gbl_panel_2);

		scrollPane = new JScrollPane();
		GridBagConstraints gbc_scrollPane = new GridBagConstraints();
		gbc_scrollPane.fill = GridBagConstraints.BOTH;
		gbc_scrollPane.gridx = 0;
		gbc_scrollPane.gridy = 0;
		panel_2.add(scrollPane, gbc_scrollPane);

		table = new JTable();
		table.getTableHeader().setReorderingAllowed(false);

		table.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent arg0) {
				if (arg0.getClickCount() > 1) {  
					downloadArquivo();
				}  
			}
		});
		
		
		scrollPane.setViewportView(table);
		table.setModel(modeloArquivos);

		JPanel panel = new JPanel();
		GridBagConstraints gbc_panel = new GridBagConstraints();
		gbc_panel.fill = GridBagConstraints.BOTH;
		gbc_panel.gridx = 0;
		gbc_panel.gridy = 3;
		contentPane.add(panel, gbc_panel);
		GridBagLayout gbl_panel = new GridBagLayout();
		gbl_panel.columnWidths = new int[]{356, 6, 0};
		gbl_panel.rowHeights = new int[]{116, 0, 0};
		gbl_panel.columnWeights = new double[]{1.0, 0.0, Double.MIN_VALUE};
		gbl_panel.rowWeights = new double[]{1.0, 1.0, Double.MIN_VALUE};
		panel.setLayout(gbl_panel);

		scrollPane_2 = new JScrollPane();
		GridBagConstraints gbc_scrollPane_2 = new GridBagConstraints();
		gbc_scrollPane_2.insets = new Insets(0, 0, 5, 0);
		gbc_scrollPane_2.anchor = GridBagConstraints.NORTHWEST;
		gbc_scrollPane_2.gridx = 1;
		gbc_scrollPane_2.gridy = 0;

		taLog = new JTextArea();
		taLog.setEditable(false);
		taLog.setFont(new Font("Courier New", Font.PLAIN, 12));
		scrollPane_2.setViewportView(taLog);
		
		DefaultCaret caret = (DefaultCaret) taLog.getCaret();
		caret.setUpdatePolicy(DefaultCaret.ALWAYS_UPDATE);

		GridBagConstraints gbc_taLog = new GridBagConstraints();
		gbc_taLog.gridheight = 2;
		gbc_taLog.insets = new Insets(0, 0, 0, 5);
		gbc_taLog.fill = GridBagConstraints.BOTH;
		gbc_taLog.gridx = 0;
		gbc_taLog.gridy = 0;
		panel.add(scrollPane_2, gbc_taLog);
	}

	@Override
	public void registrarCliente(Cliente c) throws RemoteException {

		mapaClientes.get(c.getNome());

		if (mapaClientes.get(c.getNome()) != null) {
			log("O cliente: \"" + c.getNome() + "\" já está registrado no sistema, por favor escolha outro nome.");
			throw new RemoteException("Alguém já está usando o nome: " + c.getNome());
		}

		mapaClientes.put(c.getNome(), c);
		log(c.getNome() + " se conectou!");
	}

	@Override
	public void publicarListaArquivos(Cliente c, List<Arquivo> lista) throws RemoteException {
		mapaArquivos.put(c, lista);
		modeloArquivos.setMap(mapaArquivos);
		log(c.getNome() + " publicou sua lista de arquivos!");		
	}

	@Override
	public Map<Cliente, List<Arquivo>> procurarArquivo(String query, TipoFiltro tipoFiltro, String filtro) throws RemoteException {

		List<Arquivo> arquivosEncontrados = new ArrayList<>();

		HashMap<Cliente, List<Arquivo>> result = new HashMap<>();

		Pattern patern = Pattern.compile(".*" + query + ".*");

		for (Map.Entry<Cliente, List<Arquivo>> e : mapaArquivos.entrySet()) {

			Cliente client = buscarClienteMap(e);

			for (Arquivo arq : e.getValue()) {

				switch (tipoFiltro) {
					case NOME:
						if (arq.getNome().contains(query)) {
							arquivosEncontrados.add(arq);
						}
						break;
					case TAMANHO_MIN:
						if (arq.getTamanho() >= Integer.parseInt(filtro) && filtro != "") {
							if (arq.getNome().contains(query)) {
								arquivosEncontrados.add(arq);
							}
						}
						break;
					case TAMANHO_MAX:
						if (arq.getTamanho() <= Integer.parseInt(filtro) && filtro != "") {
							if (arq.getNome().contains(query)) {
								arquivosEncontrados.add(arq);
							}
						}
						break;
					case EXTENSAO:
						if (arq.getExtensao().equals(filtro)) {
							arquivosEncontrados.add(arq);
						}
						break;
					default:
						log("Erro ao buscar arquivo!");
						break;
					}

			}

			result.put(client, arquivosEncontrados);
		}

		return result;
	}

	@Override
	public byte[] baixarArquivo(Cliente cli, Arquivo arq) throws RemoteException {

		log("O cliente " + cli.getNome() + " fez o download do arquivo " + '"' + arq.getNome()  +"." + arq.getExtensao() + '"');
		
		byte[] arquivo = null;
		Path path = Paths.get(arq.getPath());
		try {
			arquivo = Files.readAllBytes(path);
		} catch (IOException e) {
			log("Erro ao ler arquivo!");
			e.printStackTrace();
		}
		return arquivo;

	}

	@Override
	public void desconectar(Cliente c) throws RemoteException {

		mapaClientes.remove(c.getNome());

		for(Iterator<Entry<Cliente, List<Arquivo>>> it = mapaArquivos.entrySet().iterator(); it.hasNext(); ) {	      
			Entry<Cliente, List<Arquivo>> entry = it.next();			
			if(entry.getKey().getNome().equals(c.getNome())) {
				it.remove();
			}
		}

		modeloArquivos.setMap(mapaArquivos);

		log(c.getNome() + " saiu..."); 

	}

	public void sair(){
		try {
			desconectar(cliente);
			servidor.desconectar(cliente);
			configurarFields(false);
		} catch (RemoteException e) {
			e.printStackTrace();
		}	
	}

	public void log(String string) {
		taLog.append("[ " + dateFormat.format(new Date()) + " ]");
		taLog.append(" -> ");
		taLog.append(string);
		taLog.append("\n");
	}

	public void startServer() {

		String strPorta = txfPorta.getText().trim();

		if (!strPorta.matches("[0-9]+") || strPorta.length() > 5) {
			JOptionPane.showMessageDialog(this, "A porta deve ser um valor numérico de no máximo 4 dígitos!");
			return;
		}

		int intPorta = Integer.parseInt(strPorta);

		if (intPorta < 1024 || intPorta > 65535) {
			JOptionPane.showMessageDialog(this, "A porta deve estar entre 1024 e 65535");
			return;
		}

		try {
			this.servidor = (IServer) UnicastRemoteObject.exportObject(this, 0);
			registry = LocateRegistry.createRegistry(intPorta);
			registry.rebind(IServer.NOME_SERVICO, servidor);
			log("Servidor Iniciado!");
			configurarFields(true);
		} catch (RemoteException e) {
			log("Erro ao iniciar servidor, verifique se a porta está sendo usada!");
			e.printStackTrace();
		}

	}

	public void stopServer(){

		log("Parando servidor...");

		try {
			UnicastRemoteObject.unexportObject(this, true);
			UnicastRemoteObject.unexportObject(registry, true);
			configurarFields(false);
			log("Servidor desconectado! ");
		} catch (RemoteException e) {
			e.printStackTrace();
			log("Erro ao desconectar servidor!");
		}
	}

	public void conectar(){

		String nomeCliente = txfMeuNome.getText().trim();
		String ipCliente = getIp();
		
		if (nomeCliente.length() == 0) {
			JOptionPane.showMessageDialog(this, "Você precisa digitar um nome!");
			txfMeuNome.requestFocus();
			return;
		}

		String host = txfIp.getText().trim();

		if (!host.matches("[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}")) {
			JOptionPane.showMessageDialog(this, "O endereço ip parece inválido!");
			txfIp.requestFocus();
			return;
		}

		String strPorta = txfPorta.getText().trim();

		if (!strPorta.matches("[0-9]+") || strPorta.length() > 5) {
			JOptionPane.showMessageDialog(this, "A porta deve ser um valor numérico de no máximo 5 dígitos!");
			txfPorta.requestFocus();
			return;
		}

		int intPorta = Integer.parseInt(strPorta);
		

		this.cliente = new Cliente();
		this.cliente.setId(9);
		this.cliente.setIp(ipCliente);
		this.cliente.setPorta(intPorta);
		this.cliente.setNome(nomeCliente);

		log("Tentando conectar...");

		try {
			Registry registry = LocateRegistry.getRegistry(host, intPorta);
			servidor = (IServer) registry.lookup(IServer.NOME_SERVICO);
			servidor.registrarCliente(cliente); 
			log("Conectado com sucesso!");
			configurarFields(true);
			createFolders();
			servidor.publicarListaArquivos(cliente, meusArquivos());
		} catch (RemoteException e) {
			log("Problemas ao conectar!");
			e.printStackTrace();
		} catch (NotBoundException e) {
			log("Problemas ao conectar!");
			e.printStackTrace();
		}
	}

	public String getIp() {
		InetAddress ip;
		String ipLocal = "127.0.0.1";
		try {
			ip = InetAddress.getLocalHost();
			ipLocal = ip.getHostAddress();
		} catch (UnknownHostException e) {
			e.printStackTrace();
		}
		return ipLocal;
	}

	public void configurarFields(Boolean var){
		if(var){
			txfIp.setEnabled(false);
			txfMeuNome.setEnabled(false);
			txfPorta.setEnabled(false);
			txfNomeArquivo.setEnabled(true);
			buttonBaixar.setEnabled(true);
			buttonConectar.setEnabled(false);
			buttonDesconectar.setEnabled(true);
			buttonPublicar.setEnabled(true);
			cbFiltro.setEnabled(true);
			cbServidor.setEnabled(false);
		}else{
			txfIp.setEnabled(true);
			txfMeuNome.setEnabled(true);
			txfPorta.setEnabled(true);
			txfNomeArquivo.setEnabled(false);
			txfValorFiltro.setEnabled(false);
			buttonBaixar.setEnabled(false);
			buttonConectar.setEnabled(true);
			buttonDesconectar.setEnabled(false);
			buttonPublicar.setEnabled(false);
			cbFiltro.setEnabled(false);
			cbServidor.setEnabled(true);
			mapaArquivos.clear();
			modeloArquivos.setMap(mapaArquivos);
			modeloArquivos.refresh();
			table.setModel(modeloArquivos);
		}
	}
	
	public void createFolders(){

		String currentFolder = new File("").getAbsolutePath();
		File dirUploads = new File(currentFolder + "\\Share\\Uploads");
		File dirDownloads = new File(currentFolder + "\\Share\\Downloads");
		
		if (!dirUploads.exists()) {
			log("Diretorio criado em "+ currentFolder + "\\Share\\Uploads");
			dirUploads.mkdirs(); 
		}
		
		if (!dirDownloads.exists()) {
			log("Diretorio criado em "+ currentFolder + "\\Share\\Downloads");
			dirDownloads.mkdirs(); 
		}
		
	}

	public List<Arquivo> meusArquivos() {

		File dir = new File(new File("").getAbsolutePath() + "\\Share\\Uploads");
		
		List<Arquivo> listaArquivos = new ArrayList<>();

		for (File file : dir.listFiles()) {
			if (file.isFile()) {
				Arquivo arq = new Arquivo();
				arq.setId(new Long(idArquivo++));
				arq.setNome(new Tools().getNomeArquivo(file.getName()));
				arq.setExtensao(new Tools().getExtension(file.getName()));
				arq.setTamanho(file.length());
				arq.setDataHoraModificacao(new Date(file.lastModified()));
				arq.setPath(file.getPath());
				arq.setMd5(new Tools().getMD5Checksum(arq.getPath()));
				arq.setTamanho(file.length());
				listaArquivos.add(arq);
			}
		}

		if(listaArquivos.isEmpty()){
			log("Nenhum arquivo encontrado para publicar!");
		}else{
			log("Lista de arquivos publicados!");
		}
		return listaArquivos;
	}

	public Cliente buscarClienteMap(Map.Entry<Cliente, List<Arquivo>> e) {
		Cliente cliente = new Cliente();
		cliente.setId(e.getKey().getId());
		cliente.setIp(e.getKey().getIp());
		cliente.setNome(e.getKey().getNome());
		cliente.setPorta(e.getKey().getPorta());
		return cliente;
	}

	public void buscaArquivo(){
		HashMap<Cliente, List<Arquivo>> resultadoBusca = new HashMap<>();
		try {
			resultadoBusca = (HashMap<Cliente, List<Arquivo>>) servidor.procurarArquivo(txfNomeArquivo.getText(), (TipoFiltro) cbFiltro.getSelectedItem(), txfValorFiltro.getText());
			if (!resultadoBusca.isEmpty()) {
				ModeloArquivos modelo = new ModeloArquivos(resultadoBusca);
				table.setModel(modelo);
			}
		} catch (RemoteException e1) {
			e1.printStackTrace();
		}
	}
	
	public void downloadArquivo(){
		
		Arquivo arquivo = new Arquivo();
		String ip = (table.getValueAt(table.getSelectedRow(), 1).toString());
		int porta = (Integer.parseInt(table.getValueAt(table.getSelectedRow(), 2).toString()));
		arquivo.setNome(table.getValueAt(table.getSelectedRow(), 3).toString());
		arquivo.setExtensao(table.getValueAt(table.getSelectedRow(), 4).toString());
		arquivo.setTamanho(Long.parseLong(table.getValueAt(table.getSelectedRow(), 5).toString()));
		arquivo.setPath(table.getValueAt(table.getSelectedRow(), 6).toString());
		arquivo.setMd5(table.getValueAt(table.getSelectedRow(), 7).toString());
		String diretorioArquivo = "\\Share\\Downloads\\Cópia de " + arquivo.getNome() + "." + arquivo.getExtensao();
		
		Thread t1 = new Thread(new Runnable() {
			public void run() {
				try {
					
					Registry registryCliente = LocateRegistry.getRegistry(ip, porta);
					
					IServer server = (IServer) registryCliente.lookup(IServer.NOME_SERVICO);
					
					File file = new File(new File("").getAbsolutePath() + diretorioArquivo);
					
					FileOutputStream in = new FileOutputStream(file);	
					
					log("Realizando o download do arquivo: " + arquivo.getNome() + "." + arquivo.getExtensao());

					in.write(server.baixarArquivo(cliente, arquivo));	
					
					in.close();		 								
					
					String md5 = new Tools().getMD5Checksum(file.getPath());
					
					if(md5.equals(arquivo.getMd5())){
						log("A integridade do arquivo está correta!");
					}else{
						log("Arquivo corrompido!");
					}
					
				} catch (RemoteException e) {
					log("Erro ao iniciar download do arquivo.");
					e.printStackTrace();
				} catch (NotBoundException e) {
					log("Erro ao iniciar download do arquivo.");
					e.printStackTrace();
				} catch (FileNotFoundException e) {
					log("Erro: o arquivo não foi encontrado.");
					e.printStackTrace();
				} catch (IOException e) {
					log("Erro ao escrever o arquivo.");
					e.printStackTrace();
				}
			}
		});  
		t1.start();		
	}
}
