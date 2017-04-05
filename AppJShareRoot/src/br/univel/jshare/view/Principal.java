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

import br.univel.jshare.comum.Arquivo;
import br.univel.jshare.comum.Cliente;
import br.univel.jshare.comum.IServer;
import br.univel.jshare.comum.TipoFiltro;

import java.awt.event.ActionListener;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.awt.event.ActionEvent;
import javax.swing.JTable;
import java.awt.Font;
import javax.swing.table.DefaultTableModel;
import javax.swing.JCheckBox;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class Principal extends JFrame implements IServer {

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
	private SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy H:mm:ss:SSS");
	private Map<String, Cliente> mapClienteServer = new HashMap<>();
	private Map<Cliente, List<Arquivo>> ListArqServer = new HashMap<>();
	private Registry registry;
	private int porta;
	private String ip;
	private JTextArea taLog;
	private JPanel panel_2;
	private JScrollPane scrollPane;
	private JTable table;
	private String meunome;
	private JCheckBox chckbxServidor;

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
		setTitle("RMI Chat Client");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 745, 526);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		GridBagLayout gbl_contentPane = new GridBagLayout();
		gbl_contentPane.columnWidths = new int[]{719, 0};
		gbl_contentPane.rowHeights = new int[]{48, 313, 116, 0};
		gbl_contentPane.columnWeights = new double[]{1.0, Double.MIN_VALUE};
		gbl_contentPane.rowWeights = new double[]{0.0, 1.0, 0.0, Double.MIN_VALUE};
		contentPane.setLayout(gbl_contentPane);
				
						JPanel panel_1 = new JPanel();
						GridBagConstraints gbc_panel_1 = new GridBagConstraints();
						gbc_panel_1.anchor = GridBagConstraints.NORTH;
						gbc_panel_1.fill = GridBagConstraints.HORIZONTAL;
						gbc_panel_1.insets = new Insets(0, 0, 5, 0);
						gbc_panel_1.gridx = 0;
						gbc_panel_1.gridy = 0;
						contentPane.add(panel_1, gbc_panel_1);
						GridBagLayout gbl_panel_1 = new GridBagLayout();
						gbl_panel_1.columnWidths = new int[] { 0, 148, 0, 0, 0, 0, 0 };
						gbl_panel_1.rowHeights = new int[] { 0, 0, 0 };
						gbl_panel_1.columnWeights = new double[] { 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, Double.MIN_VALUE };
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
										GridBagConstraints gbc_txfMeuNome = new GridBagConstraints();
										gbc_txfMeuNome.gridwidth = 3;
										gbc_txfMeuNome.insets = new Insets(0, 0, 5, 5);
										gbc_txfMeuNome.fill = GridBagConstraints.HORIZONTAL;
										gbc_txfMeuNome.gridx = 1;
										gbc_txfMeuNome.gridy = 0;
										panel_1.add(txfMeuNome, gbc_txfMeuNome);
										txfMeuNome.setColumns(10);
												
												chckbxServidor = new JCheckBox("Servidor");
												chckbxServidor.addMouseListener(new MouseAdapter() {
													
													@SuppressWarnings("deprecation")
													@Override
													public void mouseClicked(MouseEvent arg0) {
														
														if(txfMeuNome.isEnabled() && txfIp.isEnabled()){
															txfMeuNome.setText("");
															txfMeuNome.enable(false);
															txfIp.setText("");
															txfIp.enable(false);
														}else{
															txfMeuNome.enable(true);
															txfIp.enable(true);
														}
														
													}
													
												});
												GridBagConstraints gbc_chckbxServidor = new GridBagConstraints();
												gbc_chckbxServidor.insets = new Insets(0, 0, 5, 5);
												gbc_chckbxServidor.gridx = 4;
												gbc_chckbxServidor.gridy = 0;
												panel_1.add(chckbxServidor, gbc_chckbxServidor);
										
												JLabel lblNewLabel = new JLabel("Endereço IP");
												GridBagConstraints gbc_lblNewLabel = new GridBagConstraints();
												gbc_lblNewLabel.insets = new Insets(0, 0, 0, 5);
												gbc_lblNewLabel.anchor = GridBagConstraints.EAST;
												gbc_lblNewLabel.gridx = 0;
												gbc_lblNewLabel.gridy = 1;
												panel_1.add(lblNewLabel, gbc_lblNewLabel);
												
														txfIp = new JTextField();
														txfIp.setText("127.0.0.1");
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
																						
																						if(chckbxServidor.isSelected()){
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
																						buttonDesconectar.setEnabled(false);
																						GridBagConstraints gbc_buttonDesconectar = new GridBagConstraints();
																						gbc_buttonDesconectar.gridx = 5;
																						gbc_buttonDesconectar.gridy = 1;
																						panel_1.add(buttonDesconectar, gbc_buttonDesconectar);
				
				panel_2 = new JPanel();
				GridBagConstraints gbc_panel_2 = new GridBagConstraints();
				gbc_panel_2.insets = new Insets(0, 0, 5, 0);
				gbc_panel_2.fill = GridBagConstraints.BOTH;
				gbc_panel_2.gridx = 0;
				gbc_panel_2.gridy = 1;
				contentPane.add(panel_2, gbc_panel_2);
				GridBagLayout gbl_panel_2 = new GridBagLayout();
				gbl_panel_2.columnWidths = new int[]{358, 2, 0};
				gbl_panel_2.rowHeights = new int[]{2, 0, 0};
				gbl_panel_2.columnWeights = new double[]{1.0, 0.0, Double.MIN_VALUE};
				gbl_panel_2.rowWeights = new double[]{0.0, 1.0, Double.MIN_VALUE};
				panel_2.setLayout(gbl_panel_2);
				
				scrollPane = new JScrollPane();
				GridBagConstraints gbc_scrollPane = new GridBagConstraints();
				gbc_scrollPane.insets = new Insets(0, 0, 5, 0);
				gbc_scrollPane.anchor = GridBagConstraints.NORTHWEST;
				gbc_scrollPane.gridx = 1;
				gbc_scrollPane.gridy = 0;
				panel_2.add(scrollPane, gbc_scrollPane);
				
				table = new JTable();
				table.setModel(new DefaultTableModel(
					new Object[][] {
					},
					new String[] {
						"Nome", "IP", "Porta", "Arquivo", "Tamanho"
					}
				));
				GridBagConstraints gbc_table = new GridBagConstraints();
				gbc_table.insets = new Insets(0, 0, 0, 5);
				gbc_table.fill = GridBagConstraints.BOTH;
				gbc_table.gridx = 0;
				gbc_table.gridy = 1;
				panel_2.add(table, gbc_table);
		
				JPanel panel = new JPanel();
				GridBagConstraints gbc_panel = new GridBagConstraints();
				gbc_panel.fill = GridBagConstraints.HORIZONTAL;
				gbc_panel.anchor = GridBagConstraints.NORTH;
				gbc_panel.gridx = 0;
				gbc_panel.gridy = 2;
				contentPane.add(panel, gbc_panel);
				GridBagLayout gbl_panel = new GridBagLayout();
				gbl_panel.columnWidths = new int[]{356, 6, 0};
				gbl_panel.rowHeights = new int[]{116, 0, 0};
				gbl_panel.columnWeights = new double[]{1.0, 0.0, Double.MIN_VALUE};
				gbl_panel.rowWeights = new double[]{0.0, 1.0, Double.MIN_VALUE};
				panel.setLayout(gbl_panel);
				
				scrollPane_2 = new JScrollPane();
				GridBagConstraints gbc_scrollPane_2 = new GridBagConstraints();
				gbc_scrollPane_2.insets = new Insets(0, 0, 5, 0);
				gbc_scrollPane_2.anchor = GridBagConstraints.NORTHWEST;
				gbc_scrollPane_2.gridx = 1;
				gbc_scrollPane_2.gridy = 0;
				panel.add(scrollPane_2, gbc_scrollPane_2);
				
				taLog = new JTextArea();
				//scrollPane_2.setViewportView(taLog);
				taLog.setFont(new Font("Courier New", Font.PLAIN, 12));
				GridBagConstraints gbc_taLog = new GridBagConstraints();
				gbc_taLog.gridheight = 2;
				gbc_taLog.insets = new Insets(0, 0, 0, 5);
				gbc_taLog.fill = GridBagConstraints.BOTH;
				gbc_taLog.gridx = 0;
				gbc_taLog.gridy = 0;
				panel.add(taLog, gbc_taLog);
	}

	@Override
	public void registrarCliente(Cliente c) throws RemoteException {
		
	}

	@Override
	public void publicarListaArquivos(Cliente c, List<Arquivo> lista) throws RemoteException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public Map<Cliente, List<Arquivo>> procurarArquivo(String query, TipoFiltro tipoFiltro, String filtro)
			throws RemoteException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public byte[] baixarArquivo(Cliente cli, Arquivo arq) throws RemoteException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void desconectar(Cliente c) throws RemoteException {
		// TODO Auto-generated method stub
		
	}
	
	public void log(String string) {
		taLog.append("[ " + dateFormat.format(new Date()) + " ]");
		taLog.append(" -> ");
		taLog.append(string);
		taLog.append("\n");
	}
	
	protected void startServer() {

		String strPorta = txfPorta.getText().trim();

		if (!strPorta.matches("[0-9]+") || strPorta.length() > 5) {
			JOptionPane.showMessageDialog(this, "A porta deve ser um valor numérico de no máximo 5 dígitos!");
			return;
		}
		int intPorta = Integer.parseInt(strPorta);
		if (intPorta < 1024 || intPorta > 65535) {
			JOptionPane.showMessageDialog(this, "A porta deve estar entre 1024 e 65535");
			return;
		}

		try {
			this.servidor = (IServer) UnicastRemoteObject.exportObject(this, 0);
			registry = LocateRegistry.createRegistry(this.porta);
			registry.rebind(IServer.NOME_SERVICO, servidor);

			log("Servidor Iniciado!");

			txfMeuNome.setEnabled(false);
			txfIp.setEnabled(false);
			txfPorta.setEnabled(false);
			buttonConectar.setEnabled(false);
			buttonDesconectar.setEnabled(true);
			
		} catch (RemoteException e) {
			log("Erro ao iniciar serviço, verifique se a porta está sendo usada.");
			e.printStackTrace();
		}

	}
	
	protected void conectar(){
		
		meunome = txfMeuNome.getText().trim();
		
		if (meunome.length() == 0) {
			JOptionPane.showMessageDialog(this, "Você precisa digitar um nome!");
			return;
		}

		// Endereço IP
		String host = txfIp.getText().trim();
		if (!host.matches("[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}")) {
			JOptionPane.showMessageDialog(this, "O endereço ip parece inválido!");
			return;
		}

		// Porta
		String strPorta = txfPorta.getText().trim();
		if (!strPorta.matches("[0-9]+") || strPorta.length() > 5) {
			JOptionPane.showMessageDialog(this, "A porta deve ser um valor numérico de no máximo 5 dígitos!");
			return;
		}
		
		int intPorta = Integer.parseInt(strPorta);


		// Iniciando objetos para conexão.
		try {
			
			registry = LocateRegistry.getRegistry(host, intPorta);
			servidor = (IServer) registry.lookup(IServer.NOME_SERVICO);
			cliente = (Cliente) UnicastRemoteObject.exportObject(this, 0);

			// Avisando o servidor que está entrando no Chat.
			//servidor.entrarNoChat(meunome, cliente);
	
			log("Criando instancia de cliente");
			
			cliente = new Cliente();
			
			cliente.setId(1);
			cliente.setIp(host);
			cliente.setPorta(intPorta);
			cliente.setNome(meunome);
			
			servidor.registrarCliente(cliente);	
			
			buttonDesconectar.setEnabled(true);

			buttonConectar.setEnabled(false);
			txfMeuNome.setEnabled(false);
			txfIp.setEnabled(false);
			txfPorta.setEnabled(false);

			buttonConectar.setEnabled(false);

		} catch (RemoteException e) {
			e.printStackTrace();
			log("Erro na conexão com o servidor!");
		} catch (NotBoundException e) {
			e.printStackTrace();
		}

	}


}
